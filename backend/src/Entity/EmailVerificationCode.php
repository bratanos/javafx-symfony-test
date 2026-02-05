<?php

namespace App\Entity;

use App\Repository\EmailVerificationCodeRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: EmailVerificationCodeRepository::class)]
class EmailVerificationCode
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\ManyToOne(targetEntity: User::class, inversedBy: 'code')]
    #[ORM\JoinColumn(nullable: false, onDelete: 'CASCADE')]
    private ?User $user = null;

    #[ORM\Column(length: 255)]
    private ?string $code = null;

    #[ORM\Column]
    private ?\DateTimeImmutable $expiresAt = null;

    #[ORM\Column(nullable: true)]
    private ?\DateTimeImmutable $usedAt = null;

    #[ORM\Column(type: 'integer')]
    private int $resendAttempts = 0;

    #[ORM\Column(type: 'integer')]
    private int $verifyAttempts = 0;

    #[ORM\Column(type: 'datetime_immutable')]
    private ?\DateTimeImmutable $lastSentAt = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getUser(): ?User
    {
        return $this->user;
    }

    public function setUser(?User $user): static
    {
        $this->user = $user;

        return $this;
    }

    public function getCode(): ?string
    {
        return $this->code;
    }

    public function setCode(string $code): static
    {
        $this->code = $code;

        return $this;
    }

    public function getExpiresAt(): ?\DateTimeImmutable
    {
        return $this->expiresAt;
    }

    public function setExpiresAt(\DateTimeImmutable $expiresAt): static
    {
        $this->expiresAt = $expiresAt;

        return $this;
    }

    public function getUsedAt(): ?\DateTimeImmutable
    {
        return $this->usedAt;
    }

    public function setUsedAt(?\DateTimeImmutable $usedAt): static
    {
        $this->usedAt = $usedAt;

        return $this;
    }
    public function getLastSentAt(): ?\DateTimeImmutable
    {
        return $this->lastSentAt;
    }

    public function setLastSentAt(?\DateTimeImmutable $lastSentAt): static
    {
        $this->lastSentAt = $lastSentAt;

        return $this;
    }

    public function getResendAttempts(): int
    {
        return $this->resendAttempts;
    }

    public function setResendAttempts(int $resendAttempts): static
    {
        $this->resendAttempts = $resendAttempts;

        return $this;
    }

    public function getVerifyAttempts(): int
    {
        return $this->verifyAttempts;
    }

    public function setVerifyAttempts(int $verifyAttempts): static
    {
        $this->verifyAttempts = $verifyAttempts;

        return $this;
    }
}
