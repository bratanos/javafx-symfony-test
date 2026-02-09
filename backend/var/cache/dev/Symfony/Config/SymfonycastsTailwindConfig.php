<?php

namespace Symfony\Config;

use Symfony\Component\Config\Loader\ParamConfigurator;
use Symfony\Component\Config\Definition\Exception\InvalidConfigurationException;

/**
 * This class is automatically generated to help in creating a config.
 */
class SymfonycastsTailwindConfig implements \Symfony\Component\Config\Builder\ConfigBuilderInterface
{
    private $inputCss;
    private $configFile;
    private $binary;
    private $binaryVersion;
    private $binaryPlatform;
    private $postcssConfigFile;
    private $strictMode;
    private $_usedProperties = [];

    /**
     * @param ParamConfigurator|list<ParamConfigurator|mixed>|mixed $value
     *
     * @return $this
     */
    public function inputCss(mixed $value): static
    {
        $this->_usedProperties['inputCss'] = true;
        $this->inputCss = $value;

        return $this;
    }

    /**
     * Path to the tailwind.config.js file
     * @default '%kernel.project_dir%/tailwind.config.js'
     * @param ParamConfigurator|mixed $value
     * @return $this
     */
    public function configFile($value): static
    {
        $this->_usedProperties['configFile'] = true;
        $this->configFile = $value;

        return $this;
    }

    /**
     * The tailwind binary to use instead of downloading a new one
     * @default null
     * @param ParamConfigurator|mixed $value
     * @return $this
     */
    public function binary($value): static
    {
        $this->_usedProperties['binary'] = true;
        $this->binary = $value;

        return $this;
    }

    /**
     * Tailwind CLI version to download - null means the latest version
     * @default null
     * @param ParamConfigurator|mixed $value
     * @return $this
     */
    public function binaryVersion($value): static
    {
        $this->_usedProperties['binaryVersion'] = true;
        $this->binaryVersion = $value;

        return $this;
    }

    /**
     * Tailwind CLI platform to download - "auto" will try to detect the platform automatically
     * @default 'auto'
     * @param ParamConfigurator|'auto'|'linux-arm64'|'linux-arm64-musl'|'linux-x64'|'linux-x64-musl'|'macos-arm64'|'macos-x64'|'windows-x64' $value
     * @return $this
     */
    public function binaryPlatform($value): static
    {
        $this->_usedProperties['binaryPlatform'] = true;
        $this->binaryPlatform = $value;

        return $this;
    }

    /**
     * Path to PostCSS config file which is passed to the Tailwind CLI
     * @default null
     * @param ParamConfigurator|mixed $value
     * @return $this
     */
    public function postcssConfigFile($value): static
    {
        $this->_usedProperties['postcssConfigFile'] = true;
        $this->postcssConfigFile = $value;

        return $this;
    }

    /**
     * When enabled, an exception will be thrown if there are no built assets (default: false in `test` env, true otherwise)
     * @default null
     * @param ParamConfigurator|bool $value
     * @return $this
     */
    public function strictMode($value): static
    {
        $this->_usedProperties['strictMode'] = true;
        $this->strictMode = $value;

        return $this;
    }

    public function getExtensionAlias(): string
    {
        return 'symfonycasts_tailwind';
    }

    public function __construct(array $value = [])
    {
        if (array_key_exists('input_css', $value)) {
            $this->_usedProperties['inputCss'] = true;
            $this->inputCss = $value['input_css'];
            unset($value['input_css']);
        }

        if (array_key_exists('config_file', $value)) {
            $this->_usedProperties['configFile'] = true;
            $this->configFile = $value['config_file'];
            unset($value['config_file']);
        }

        if (array_key_exists('binary', $value)) {
            $this->_usedProperties['binary'] = true;
            $this->binary = $value['binary'];
            unset($value['binary']);
        }

        if (array_key_exists('binary_version', $value)) {
            $this->_usedProperties['binaryVersion'] = true;
            $this->binaryVersion = $value['binary_version'];
            unset($value['binary_version']);
        }

        if (array_key_exists('binary_platform', $value)) {
            $this->_usedProperties['binaryPlatform'] = true;
            $this->binaryPlatform = $value['binary_platform'];
            unset($value['binary_platform']);
        }

        if (array_key_exists('postcss_config_file', $value)) {
            $this->_usedProperties['postcssConfigFile'] = true;
            $this->postcssConfigFile = $value['postcss_config_file'];
            unset($value['postcss_config_file']);
        }

        if (array_key_exists('strict_mode', $value)) {
            $this->_usedProperties['strictMode'] = true;
            $this->strictMode = $value['strict_mode'];
            unset($value['strict_mode']);
        }

        if ([] !== $value) {
            throw new InvalidConfigurationException(sprintf('The following keys are not supported by "%s": ', __CLASS__).implode(', ', array_keys($value)));
        }
    }

    public function toArray(): array
    {
        $output = [];
        if (isset($this->_usedProperties['inputCss'])) {
            $output['input_css'] = $this->inputCss;
        }
        if (isset($this->_usedProperties['configFile'])) {
            $output['config_file'] = $this->configFile;
        }
        if (isset($this->_usedProperties['binary'])) {
            $output['binary'] = $this->binary;
        }
        if (isset($this->_usedProperties['binaryVersion'])) {
            $output['binary_version'] = $this->binaryVersion;
        }
        if (isset($this->_usedProperties['binaryPlatform'])) {
            $output['binary_platform'] = $this->binaryPlatform;
        }
        if (isset($this->_usedProperties['postcssConfigFile'])) {
            $output['postcss_config_file'] = $this->postcssConfigFile;
        }
        if (isset($this->_usedProperties['strictMode'])) {
            $output['strict_mode'] = $this->strictMode;
        }

        return $output;
    }

}
