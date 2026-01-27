<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20260127123032 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE email_verification_code (id INT AUTO_INCREMENT NOT NULL, code VARCHAR(255) NOT NULL, expires_at DATETIME NOT NULL, used_at DATETIME DEFAULT NULL, user_id INT NOT NULL, INDEX IDX_BD2ADC58A76ED395 (user_id), PRIMARY KEY (id)) DEFAULT CHARACTER SET utf8mb4');
        $this->addSql('ALTER TABLE email_verification_code ADD CONSTRAINT FK_BD2ADC58A76ED395 FOREIGN KEY (user_id) REFERENCES user (id)');
        $this->addSql('ALTER TABLE user ADD status VARCHAR(20) NOT NULL');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE email_verification_code DROP FOREIGN KEY FK_BD2ADC58A76ED395');
        $this->addSql('DROP TABLE email_verification_code');
        $this->addSql('ALTER TABLE user DROP status');
    }
}
