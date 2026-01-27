<?php

namespace App\Service;

use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;

class EmailSender
{
    public function __construct(private MailerInterface $mailer){}

    public function sendVerificationEmail(string $to): void
    {
        $email = (new Email())
            ->from('noreply@example.com')
            ->to($to)
            ->subject('Please Verify Your Email')
            ->html("
                <p>Votre code de verification est : </p>
                <h2>$otp</h2>
                <p>Ce code expire dans 5 minutes.</p>");
        
        $this->mailer->send($email);
    }
}
