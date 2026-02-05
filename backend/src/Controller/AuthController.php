<?php

namespace App\Controller;

use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Symfony\Component\Routing\Annotation\Route;
use App\Service\EmailSender;
use App\Service\AuthService;
use App\Service\EmailVerificationService;
use App\Entity\EmailVerificationCode;


#[Route('/api')]
class AuthController extends AbstractController
{
    #[Route('/register', name: 'api_register', methods: ['POST'])]
    public function register(
        Request $request,
        EntityManagerInterface $em,
        AuthService $authService,
        EmailVerificationService $verificationService
    ): JsonResponse {

        $data = json_decode($request->getContent(), true);

        if (!isset($data['email'], $data['password'])) {
            return new JsonResponse(['error' => 'Email and password required'], 400);
        }

        $existing = $em->getRepository(User::class)
            ->findOneBy(['email' => $data['email']]);

        if ($existing) {
            return new JsonResponse(['error' => 'User already exists'], 409);
        }

        $user = $authService->createUser(
            $data['email'],
            $data['password']
        );

        $verificationService->sendVerification($user);

        return new JsonResponse([
            'message' => 'Compte créé. Vérifiez votre email.',
            'requires_verification' => true
        ], 201);


    }

    #[Route('/verify-email', name: 'api_verify_email', methods: ['POST'])]
    public function verifyEmail(
        Request $request,
        EntityManagerInterface $em
    ): JsonResponse {
        $data = json_decode($request->getContent(), true);

        if (!isset($data['email'], $data['code'])) {
            return new JsonResponse(['error' => 'Email and code required'], 400);
        }

        $user = $em->getRepository(User::class)
            ->findOneBy(['email' => $data['email']]);

        if (!$user) {
            return new JsonResponse(['error' => 'User not found'], 404);
        }

        $verification = $em->getRepository(EmailVerificationCode::class)
            ->findOneBy(['user' => $user]);

        if (!$verification) {
            return new JsonResponse(['error' => 'No verification code found'], 400);
        }

        if ($verification->getExpiresAt() < new \DateTimeImmutable()) {
            return new JsonResponse(['error' => 'Code expired'], 400);
        }

        // Increment attempts before validation
        $verification->setVerifyAttempts($verification->getVerifyAttempts() + 1);
        $em->flush();

        if ($verification->getVerifyAttempts() > 5) {
            return new JsonResponse([
                'error' => 'Too many failed attempts. Request new code.'
            ], 429);
        }

        if (!hash_equals($verification->getCode(), $data['code'])) {
            return new JsonResponse(['error' => 'Invalid code'], 400);
        }

        $user->setIsVerified(true);
        $user->setStatus('ACTIVE');

        // Reset attempts or set usedAt
        $verification->setUsedAt(new \DateTimeImmutable());
        $verification->setVerifyAttempts(0);

        $em->persist($verification);
        $em->flush();

        return new JsonResponse([
            'message' => 'Email verified successfully'
        ]);
    }

    #[Route('/login', name: 'api_login', methods: ['POST'])]
    public function login(): JsonResponse
    {
        // This code is intercepted by the login firewall
        // but the route definition is required.
        return new JsonResponse(['message' => 'Login successful'], 200);
    }

    #[Route('/resend-verification', name: 'api_resend_verification', methods: ['POST'])]
    public function resendVerification(
        Request $request,
        EntityManagerInterface $em,
        EmailSender $emailSender
    ): JsonResponse {
        $data = json_decode($request->getContent(), true);

        if (!isset($data['email'])) {
            return new JsonResponse(['error' => 'Email required'], 400);
        }

        $user = $em->getRepository(User::class)
            ->findOneBy(['email' => $data['email']]);

        if (!$user) {
            return new JsonResponse(['error' => 'User not found'], 404);
        }

        if ($user->isVerified()) {
            return new JsonResponse(['error' => 'User already verified'], 400);
        }

        $verification = $em->getRepository(EmailVerificationCode::class)
            ->findOneBy(['user' => $user]);

        if (!$verification) {
            $verification = new EmailVerificationCode();
            $verification->setUser($user);
        }

        //  Cooldown: 60 seconds
        if (
            $verification->getLastSentAt() &&
            $verification->getLastSentAt() > new \DateTimeImmutable('-60 seconds')
        ) {
            return new JsonResponse([
                'error' => 'Wait before requesting another code'
            ], 429);
        }

        //  Max resend attempts: 3 per hour
        if ($verification->getLastSentAt() && $verification->getLastSentAt() < new \DateTimeImmutable('-1 hour')) {
            $verification->setResendAttempts(0);
        }

        if (
            $verification->getResendAttempts() >= 3 &&
            $verification->getLastSentAt() > new \DateTimeImmutable('-5 minutes')
        ) {
            return new JsonResponse([
                'error' => 'Too many requests. Try later.'
            ], 429);
        }

        // Generate new OTP
        $otp = random_int(100000, 999999);

        $verification->setCode((string) $otp);
        $verification->setExpiresAt(new \DateTimeImmutable('+10 minutes'));
        $verification->setLastSentAt(new \DateTimeImmutable());
        $verification->setResendAttempts($verification->getResendAttempts() + 1);
        $verification->setVerifyAttempts(0); // Reset failed guesses for the new code

        $em->persist($verification);
        $em->flush();

        //  Send email
        $emailSender->sendVerificationEmail($user->getEmail(), (string) $otp);

        return new JsonResponse([
            'message' => 'Verification code resent'
        ]);
    }

    #[Route('/me', name: 'api_me', methods: ['GET'])]
    public function me(): JsonResponse
    {
        /** @var User $user */
        $user = $this->getUser();

        return new JsonResponse([
            'id' => $user->getId(),
            'email' => $user->getEmail(),
            'roles' => $user->getRoles(),
            'isVerified' => $user->isVerified(),
            'status' => $user->getStatus(),
        ]);
    }
}