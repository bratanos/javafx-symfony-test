<?php

namespace App\Controller;

use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/api')]
class AuthController extends AbstractController
{
    #[Route('/register', name: 'api_register', methods: ['POST'])]
    public function register(
        Request $request,
        EntityManagerInterface $em,
        UserPasswordHasherInterface $passwordHasher
    ): JsonResponse {
        $data = json_decode($request->getContent(), true);

        if (!isset($data['email'], $data['password'])) {
            return new JsonResponse([
                'error' => 'Email and password are required'
            ], 400);
        }

        // Check if user already exists
        $existingUser = $em->getRepository(User::class)
            ->findOneBy(['email' => $data['email']]);

        if ($existingUser) {
            return new JsonResponse([
                'error' => 'User already exists'
            ], 409);
        }

        $user = new User();
        $user->setEmail($data['email']);

        $hashedPassword = $passwordHasher->hashPassword(
            $user,
            $data['password']
        );

        $user->setPassword($hashedPassword);
        $user->setRoles(['ROLE_USER']);
        $user->setIsVerified(false);

        $em->persist($user);
        $em->flush();

        return new JsonResponse([
            'message' => 'User registered successfully'
        ], 201);
    }

    /* #[Route('/login', name: 'api_login', methods: ['POST'])]
    public function login(): void
    {
        // This will NEVER be executed
        // Symfony Security intercepts the request before this
        throw new \LogicException('This should never be reached.');
        dd($this->getUser()->getRoles()); 
    } */ //disabled for now will put back on later
    #[Route('/admin/test', methods: ['GET'])]
        public function adminTest(): JsonResponse
            {
                return $this->json([
                'message' => 'Hello admin 👑'
                    ]);
            }
}