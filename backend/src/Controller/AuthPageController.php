<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

use Symfony\Component\HttpFoundation\Request;

class AuthPageController extends AbstractController
{
    #[Route('/', name: 'home')]
    public function index(): Response
    {
        return $this->render('pages/home.html.twig');
        
    }
    #[Route('/login', name: 'login')]
    public function login(): Response
    {
        return $this->render('pages/login.html.twig');
    }

    #[Route('/register', name:'register')]
    public function register(): Response
    {
        return $this->render('pages/signup.html.twig');
    }
    #[Route('/forgot-password', name:'forgot_password', methods: ['GET', 'POST'])]
    public function forgotPassword(Request $request): Response
    {
        if ($request->isMethod('POST')) {
            // TODO: Process email sending logic here
            
            return $this->redirectToRoute('check_email');
        }

        return $this->render('pages/forgotPassword.html.twig');
    }
    #[Route('/verify-email', name:'verify-email')]
    public function verifyEmail(): Response
    {
        return $this->render('pages/verifyEmail.html.twig', [
            'email' => 'user@example.com' // Placeholder until integrated with previous window
        ]);
    }

    #[Route('/check-email', name: 'check_email')]
    public function checkEmail(): Response
    {
        return $this->render('pages/checkEmail.html.twig');
    }
}   