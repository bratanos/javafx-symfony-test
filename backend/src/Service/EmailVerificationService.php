<?php
namespace App\Service;

use App\Entity\EmailVerificationCode;
use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;

class EmailVerificationService
{
    public function __construct(
        private EntityManagerInterface $em,
        private EmailSender $emailSender
    ) {}

    public function sendVerification(User $user): void
    {
        $otp = random_int(100000, 999999);

        $verification = new EmailVerificationCode();
        $verification->setUser($user);
        $verification->setCode((string)$otp);
        $verification->setExpiresAt(
            new \DateTimeImmutable('+10 minutes')
        );

        $this->em->persist($verification);
        $this->em->flush();

        $this->emailSender->sendVerificationEmail(
            $user->getEmail(),
            (string)$otp
        );
    }
}
