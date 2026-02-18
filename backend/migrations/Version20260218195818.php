<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20260218195818 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE password_reset_codes DROP FOREIGN KEY `FK_PRC_USER`');
        $this->addSql('DROP TABLE password_reset_codes');
        $this->addSql('ALTER TABLE email_verification_code DROP FOREIGN KEY `FK_EVC_USER`');
        $this->addSql('ALTER TABLE email_verification_code CHANGE resend_attempts resend_attempts INT NOT NULL, CHANGE verify_attempts verify_attempts INT NOT NULL');
        $this->addSql('DROP INDEX fk_evc_user ON email_verification_code');
        $this->addSql('CREATE INDEX IDX_BD2ADC58A76ED395 ON email_verification_code (user_id)');
        $this->addSql('ALTER TABLE email_verification_code ADD CONSTRAINT `FK_EVC_USER` FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE user ADD first_name VARCHAR(255) NOT NULL, ADD last_name VARCHAR(255) NOT NULL, ADD profile_picture VARCHAR(255) DEFAULT NULL, CHANGE roles roles JSON NOT NULL, CHANGE is_verified is_verified TINYINT NOT NULL, CHANGE status status VARCHAR(20) NOT NULL');
        $this->addSql('DROP INDEX email ON user');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_8D93D649E7927C74 ON user (email)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE password_reset_codes (id INT AUTO_INCREMENT NOT NULL, user_id INT NOT NULL, code VARCHAR(10) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_unicode_ci`, expires_at DATETIME NOT NULL, used_at DATETIME DEFAULT NULL, INDEX FK_PRC_USER (user_id), PRIMARY KEY (id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('ALTER TABLE password_reset_codes ADD CONSTRAINT `FK_PRC_USER` FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE email_verification_code DROP FOREIGN KEY FK_BD2ADC58A76ED395');
        $this->addSql('ALTER TABLE email_verification_code CHANGE resend_attempts resend_attempts INT DEFAULT 0 NOT NULL, CHANGE verify_attempts verify_attempts INT DEFAULT 0 NOT NULL');
        $this->addSql('DROP INDEX idx_bd2adc58a76ed395 ON email_verification_code');
        $this->addSql('CREATE INDEX FK_EVC_USER ON email_verification_code (user_id)');
        $this->addSql('ALTER TABLE email_verification_code ADD CONSTRAINT FK_BD2ADC58A76ED395 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE user DROP first_name, DROP last_name, DROP profile_picture, CHANGE status status VARCHAR(255) DEFAULT \'PENDING\' NOT NULL, CHANGE roles roles LONGTEXT NOT NULL COMMENT \'(DC2Type:json)\', CHANGE is_verified is_verified TINYINT DEFAULT 0 NOT NULL');
        $this->addSql('DROP INDEX uniq_8d93d649e7927c74 ON user');
        $this->addSql('CREATE UNIQUE INDEX email ON user (email)');
    }
}
