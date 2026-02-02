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
}
