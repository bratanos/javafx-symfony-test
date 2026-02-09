<?php

use Twig\Environment;
use Twig\Error\LoaderError;
use Twig\Error\RuntimeError;
use Twig\Extension\CoreExtension;
use Twig\Extension\SandboxExtension;
use Twig\Markup;
use Twig\Sandbox\SecurityError;
use Twig\Sandbox\SecurityNotAllowedTagError;
use Twig\Sandbox\SecurityNotAllowedFilterError;
use Twig\Sandbox\SecurityNotAllowedFunctionError;
use Twig\Source;
use Twig\Template;
use Twig\TemplateWrapper;

/* components/_flash_messages.html.twig */
class __TwigTemplate_68635dcb650c48f9fd460d9ddbda8560 extends Template
{
    private Source $source;
    /**
     * @var array<string, Template>
     */
    private array $macros = [];

    public function __construct(Environment $env)
    {
        parent::__construct($env);

        $this->source = $this->getSourceContext();

        $this->parent = false;

        $this->blocks = [
        ];
    }

    protected function doDisplay(array $context, array $blocks = []): iterable
    {
        $macros = $this->macros;
        $__internal_5a27a8ba21ca79b61932376b2fa922d2 = $this->extensions["Symfony\\Bundle\\WebProfilerBundle\\Twig\\WebProfilerExtension"];
        $__internal_5a27a8ba21ca79b61932376b2fa922d2->enter($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "template", "components/_flash_messages.html.twig"));

        $__internal_6f47bbe9983af81f1e7450e9a3e3768f = $this->extensions["Symfony\\Bridge\\Twig\\Extension\\ProfilerExtension"];
        $__internal_6f47bbe9983af81f1e7450e9a3e3768f->enter($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "template", "components/_flash_messages.html.twig"));

        // line 1
        $context['_parent'] = $context;
        $context['_seq'] = CoreExtension::ensureTraversable(CoreExtension::getAttribute($this->env, $this->source, (isset($context["app"]) || array_key_exists("app", $context) ? $context["app"] : (function () { throw new RuntimeError('Variable "app" does not exist.', 1, $this->source); })()), "flashes", [], "any", false, false, false, 1));
        foreach ($context['_seq'] as $context["type"] => $context["messages"]) {
            // line 2
            yield "    ";
            $context['_parent'] = $context;
            $context['_seq'] = CoreExtension::ensureTraversable($context["messages"]);
            foreach ($context['_seq'] as $context["_key"] => $context["message"]) {
                // line 3
                yield "        <div
            class=\"mb-4 rounded px-4 py-3 text-sm
            ";
                // line 5
                if (($context["type"] == "success")) {
                    // line 6
                    yield "                bg-green-100 text-green-800
            ";
                } elseif ((                // line 7
$context["type"] == "error")) {
                    // line 8
                    yield "                bg-red-100 text-red-800
            ";
                } elseif ((                // line 9
$context["type"] == "warning")) {
                    // line 10
                    yield "                bg-yellow-100 text-yellow-800
            ";
                } else {
                    // line 12
                    yield "                bg-blue-100 text-blue-800
            ";
                }
                // line 14
                yield "            \"
        >
            ";
                // line 16
                yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape($context["message"], "html", null, true);
                yield "
        </div>
    ";
            }
            $_parent = $context['_parent'];
            unset($context['_seq'], $context['_key'], $context['message'], $context['_parent']);
            $context = array_intersect_key($context, $_parent) + $_parent;
        }
        $_parent = $context['_parent'];
        unset($context['_seq'], $context['type'], $context['messages'], $context['_parent']);
        $context = array_intersect_key($context, $_parent) + $_parent;
        
        $__internal_5a27a8ba21ca79b61932376b2fa922d2->leave($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof);

        
        $__internal_6f47bbe9983af81f1e7450e9a3e3768f->leave($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof);

        yield from [];
    }

    /**
     * @codeCoverageIgnore
     */
    public function getTemplateName(): string
    {
        return "components/_flash_messages.html.twig";
    }

    /**
     * @codeCoverageIgnore
     */
    public function isTraitable(): bool
    {
        return false;
    }

    /**
     * @codeCoverageIgnore
     */
    public function getDebugInfo(): array
    {
        return array (  85 => 16,  81 => 14,  77 => 12,  73 => 10,  71 => 9,  68 => 8,  66 => 7,  63 => 6,  61 => 5,  57 => 3,  52 => 2,  48 => 1,);
    }

    public function getSourceContext(): Source
    {
        return new Source("{% for type, messages in app.flashes %}
    {% for message in messages %}
        <div
            class=\"mb-4 rounded px-4 py-3 text-sm
            {% if type == 'success' %}
                bg-green-100 text-green-800
            {% elseif type == 'error' %}
                bg-red-100 text-red-800
            {% elseif type == 'warning' %}
                bg-yellow-100 text-yellow-800
            {% else %}
                bg-blue-100 text-blue-800
            {% endif %}
            \"
        >
            {{ message }}
        </div>
    {% endfor %}
{% endfor %}
", "components/_flash_messages.html.twig", "C:\\Users\\Bratan\\javafx-symfony-test\\backend\\templates\\components\\_flash_messages.html.twig");
    }
}
