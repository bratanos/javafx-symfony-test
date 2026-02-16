package com.innertrack.service;

import com.innertrack.dao.EmailVerificationCodeDao;
import com.innertrack.dao.UserDao;
import com.innertrack.model.EmailVerificationCode;
import com.innertrack.model.User;
import com.innertrack.security.BCryptHasher;
import com.innertrack.security.JwtUtil;
import com.innertrack.session.SessionManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class AuthService {
    private final UserDao userDao = new UserDao();
    private final EmailVerificationCodeDao otpDao = new EmailVerificationCodeDao();

    public String register(String email, String password, List<String> requestedRoles) {
        // Validation: Block Admin role from registration
        if (requestedRoles.contains("ROLE_ADMIN")) {
            return "Registration for Admin is not allowed.";
        }

        if (userDao.findByEmail(email) != null) {
            return "User already exists.";
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(BCryptHasher.hash(password));
        user.setRoles(requestedRoles);
        user.setVerified(false);
        user.setStatus("PENDING");

        try {
            if (userDao.create(user)) {
                // Reload user to get generated ID
                user = userDao.findByEmail(email);
                sendNewOtp(user);
                return "SUCCESS";
            }
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
        return "Registration failed.";
    }

    public void sendNewOtp(User user) {
        String code = String.format("%06d", new Random().nextInt(1000000));
        EmailVerificationCode evc = new EmailVerificationCode();
        evc.setUser(user);
        evc.setCode(code);
        evc.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        evc.setLastSentAt(LocalDateTime.now());
        evc.setResendAttempts(1);
        evc.setVerifyAttempts(0);

        otpDao.create(evc);
        // Send actual email
        EmailService.getInstance().sendVerificationEmail(user.getEmail(), code);
    }

    public String verifyOtp(String email, String code) {
        User user = userDao.findByEmail(email);
        if (user == null)
            return "User not found.";
        if (user.isVerified())
            return "User already verified.";

        EmailVerificationCode evc = otpDao.findByUserId(user.getId());
        if (evc == null)
            return "No verification code found.";

        if (evc.getExpiresAt().isBefore(LocalDateTime.now())) {
            return "Code expired.";
        }

        if (evc.getVerifyAttempts() >= 5) {
            return "Too many failed attempts. Request a new code.";
        }

        if (!evc.getCode().equals(code)) {
            evc.setVerifyAttempts(evc.getVerifyAttempts() + 1);
            otpDao.update(evc);
            return "Invalid code.";
        }

        // Success
        userDao.updateStatus(user.getId(), "ACTIVE", true);
        evc.setUsedAt(LocalDateTime.now());
        otpDao.update(evc);

        return "SUCCESS";
    }

    public String resendOtp(String email) {
        User user = userDao.findByEmail(email);
        if (user == null)
            return "User not found.";
        if (user.isVerified())
            return "User already verified.";

        EmailVerificationCode evc = otpDao.findByUserId(user.getId());
        if (evc == null) {
            sendNewOtp(user);
            return "SUCCESS";
        }

        // Cooldown: 60 seconds
        if (evc.getLastSentAt().isAfter(LocalDateTime.now().minusSeconds(60))) {
            return "Wait before requesting another code.";
        }

        // Max resend attempts: 3 per hour (simplified for this logic)
        if (evc.getResendAttempts() >= 3 && evc.getLastSentAt().isAfter(LocalDateTime.now().minusMinutes(5))) {
            return "Too many requests. Try later.";
        }

        String code = String.format("%06d", new Random().nextInt(1000000));
        evc.setCode(code);
        evc.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        evc.setLastSentAt(LocalDateTime.now());
        evc.setResendAttempts(evc.getResendAttempts() + 1);
        evc.setVerifyAttempts(0);

        otpDao.update(evc);
        // Mock Email Sending
        System.out.println("DEBUG: Resending OTP " + code + " to " + user.getEmail());
        return "SUCCESS";
    }

    public String login(String email, String password) {
        User user = userDao.findByEmail(email);
        if (user == null)
            return "Invalid credentials.";

        if (!BCryptHasher.check(password, user.getPassword())) {
            return "Invalid credentials.";
        }

        if (!user.isVerified() || !"ACTIVE".equals(user.getStatus())) {
            return "Account not verified. Please verify your email.";
        }

        String token = JwtUtil.generateToken(user.getEmail(), user.getRoles());
        SessionManager.getInstance().setCurrentUser(user);
        SessionManager.getInstance().setJwtToken(token);

        return "SUCCESS";
    }

    public String requestPasswordReset(String email) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            return "Email non trouvé.";
        }

        String code = String.format("%06d", new java.util.Random().nextInt(999999));
        com.innertrack.dao.PasswordResetDao resetDao = new com.innertrack.dao.PasswordResetDao();

        if (resetDao.create(user.getId(), code)) {
            EmailService.getInstance().sendPasswordResetEmail(email, code);
            return "SUCCESS";
        }
        return "Erreur lors de la génération du code.";
    }

    public String resetPassword(String email, String code, String newPassword) {
        com.innertrack.dao.PasswordResetDao resetDao = new com.innertrack.dao.PasswordResetDao();
        int userId = resetDao.findUserIdByValidCode(email, code);

        if (userId == -1) {
            return "Code invalide ou expiré.";
        }

        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(newPassword, org.mindrot.jbcrypt.BCrypt.gensalt());
        if (userDao.updatePassword(userId, hashedPassword)) {
            resetDao.markAsUsed(userId, code);
            return "SUCCESS";
        }
        return "Erreur lors de la mise à jour du mot de passe.";
    }
}
