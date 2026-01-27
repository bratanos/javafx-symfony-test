<?php

namespace App\Service;

use App\Entity\EmailVerificationCode;
use App\Entity\User;
use App\Security\OtpGenerator;
use Doctrine\ORM\EntityManagerInterface;

class EmailVerificationService
{
    public function __construct(
        private EntityManagerInterface $em,
        private OtpGenerator $otpGenerator,
        private EmailSender $emailSender
    ){}

    public function sendVerification(User $user): void
    {
        $otp = $this->otpGenerator->generate();
        $entity->setUser($user);
        $entity->setCode(password_hash($otp, PASSWORD_DEFAULT));
        $entity->setExpiresAt(new \DateTimeImmutable('+10 minutes'));

        $this->em->persist($entity);
        $this->em->flush();

        $this->emailSender->sendVerificationEmail($user->getEmail(), $otp);
    }
}