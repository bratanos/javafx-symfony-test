<?php

namespace App\Service;

use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;

class EmailSender
{
    public function __construct(private MailerInterface $mailer) {}

    public function sendVerificationEmail(string $to, string $otp): void
    {
        $email = (new Email())
            ->from('no-reply@innertrack.tn')
            ->to($to)
            ->subject('Veuillez vérifier votre adresse e-mail')
            ->html(" 
                <p>Votre code de vérification est : </p>
                <h2>$otp</h2>
                <p>Ce code est valable pendant 10 minutes.</p>
                ");
        $this->mailer->send($email);
    }
}