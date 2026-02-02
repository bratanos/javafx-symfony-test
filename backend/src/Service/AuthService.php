<?php

namespace App\Service;

use App\Entity\User;
use App\Security\PasswordHasher;
use Doctrine\ORM\EntityManagerInterface;

class AuthService
{
    public function __construct(
        private EntityManagerInterface $em,
        private PasswordHasher $passwordHasher
    ){}

    public function createUser(string $email, string $plainPassword): User
    {
        $user = new User();
        $user->setEmail($email);
        $user->setRoles(['ROLE_USER']);
        $user->setIsVerified(false);
        $user->setPassword(
            $this->passwordHasher->hashPassword($user, $plainPassword)
        );

        $this->em->persist($user);
        $this->em->flush();
        
        return $user;
    }

}