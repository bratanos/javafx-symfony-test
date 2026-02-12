package com.innertrack.dao;

import com.innertrack.model.EmailVerificationCode;
import com.innertrack.model.User;
import com.innertrack.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

public class EmailVerificationCodeDao {
    private final Connection connection = DBConnection.getInstance().getConnection();

    public boolean create(EmailVerificationCode verification) {
        String sql = "INSERT INTO email_verification_code (user_id, code, expires_at, resend_attempts, verify_attempts, last_sent_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, verification.getUser().getId());
            stmt.setString(2, verification.getCode());
            stmt.setTimestamp(3, Timestamp.valueOf(verification.getExpiresAt()));
            stmt.setInt(4, verification.getResendAttempts());
            stmt.setInt(5, verification.getVerifyAttempts());
            stmt.setTimestamp(6, Timestamp.valueOf(verification.getLastSentAt()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        verification.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating verification code: " + e.getMessage());
        }
        return false;
    }

    public EmailVerificationCode findByUserId(int userId) {
        String sql = "SELECT * FROM email_verification_code WHERE user_id = ? ORDER BY last_sent_at DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding verification code: " + e.getMessage());
        }
        return null;
    }

    public boolean update(EmailVerificationCode verification) {
        String sql = "UPDATE email_verification_code SET code = ?, expires_at = ?, used_at = ?, resend_attempts = ?, verify_attempts = ?, last_sent_at = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, verification.getCode());
            stmt.setTimestamp(2, Timestamp.valueOf(verification.getExpiresAt()));
            stmt.setTimestamp(3, verification.getUsedAt() != null ? Timestamp.valueOf(verification.getUsedAt()) : null);
            stmt.setInt(4, verification.getResendAttempts());
            stmt.setInt(5, verification.getVerifyAttempts());
            stmt.setTimestamp(6, Timestamp.valueOf(verification.getLastSentAt()));
            stmt.setInt(7, verification.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating verification code: " + e.getMessage());
        }
        return false;
    }

    private EmailVerificationCode map(ResultSet rs) throws SQLException {
        EmailVerificationCode evc = new EmailVerificationCode();
        evc.setId(rs.getInt("id"));
        evc.setCode(rs.getString("code"));
        evc.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
        Timestamp usedAt = rs.getTimestamp("used_at");
        if (usedAt != null) {
            evc.setUsedAt(usedAt.toLocalDateTime());
        }
        evc.setResendAttempts(rs.getInt("resend_attempts"));
        evc.setVerifyAttempts(rs.getInt("verify_attempts"));
        evc.setLastSentAt(rs.getTimestamp("last_sent_at").toLocalDateTime());

        // Note: User object needs to be set by the caller or we fetch it here.
        // For simplicity, we'll let the service handle attaching the User object.
        return evc;
    }

}
