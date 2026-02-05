<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20260203191836 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE email_verification_code DROP FOREIGN KEY `FK_BD2ADC58A76ED395`');
        $this->addSql('ALTER TABLE email_verification_code ADD resend_attempts INT NOT NULL, ADD verify_attempts INT NOT NULL, ADD last_sent_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE email_verification_code ADD CONSTRAINT FK_BD2ADC58A76ED395 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE email_verification_code DROP FOREIGN KEY FK_BD2ADC58A76ED395');
        $this->addSql('ALTER TABLE email_verification_code DROP resend_attempts, DROP verify_attempts, DROP last_sent_at');
        $this->addSql('ALTER TABLE email_verification_code ADD CONSTRAINT `FK_BD2ADC58A76ED395` FOREIGN KEY (user_id) REFERENCES user (id)');
    }
}
