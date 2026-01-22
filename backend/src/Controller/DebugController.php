<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Routing\Annotation\Route;

class DebugController extends AbstractController
{
    #[Route('/api/debug/me', methods: ['GET'])]
    public function me()
    {
        $user = $this->getUser();

        if (!$user) {
            return $this->json(['error' => 'NO USER']);
        }

        return $this->json([
            'email' => $user->getUserIdentifier(),
            'roles' => $user->getRoles(),
            'raw_roles_property' => method_exists($user, 'getRoles') ? $user->getRoles() : null,
        ]);
    }
}
