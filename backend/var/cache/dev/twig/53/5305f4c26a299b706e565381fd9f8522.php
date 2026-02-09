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

/* macros/ui.html.twig */
class __TwigTemplate_1b7841f05602c3d3cef4a9ca85f3a581 extends Template
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
        $__internal_5a27a8ba21ca79b61932376b2fa922d2->enter($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "template", "macros/ui.html.twig"));

        $__internal_6f47bbe9983af81f1e7450e9a3e3768f = $this->extensions["Symfony\\Bridge\\Twig\\Extension\\ProfilerExtension"];
        $__internal_6f47bbe9983af81f1e7450e9a3e3768f->enter($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "template", "macros/ui.html.twig"));

        // line 2
        yield "
";
        // line 23
        yield "
";
        // line 36
        yield "
";
        // line 51
        yield "
";
        
        $__internal_5a27a8ba21ca79b61932376b2fa922d2->leave($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof);

        
        $__internal_6f47bbe9983af81f1e7450e9a3e3768f->leave($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof);

        yield from [];
    }

    // line 3
    public function macro_button($text = null, $type = "primary", $href = null, $extra = "", ...$varargs): string|Markup
    {
        $macros = $this->macros;
        $context = [
            "text" => $text,
            "type" => $type,
            "href" => $href,
            "extra" => $extra,
            "varargs" => $varargs,
        ] + $this->env->getGlobals();

        $blocks = [];

        return ('' === $tmp = \Twig\Extension\CoreExtension::captureOutput((function () use (&$context, $macros, $blocks) {
            $__internal_5a27a8ba21ca79b61932376b2fa922d2 = $this->extensions["Symfony\\Bundle\\WebProfilerBundle\\Twig\\WebProfilerExtension"];
            $__internal_5a27a8ba21ca79b61932376b2fa922d2->enter($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "macro", "button"));

            $__internal_6f47bbe9983af81f1e7450e9a3e3768f = $this->extensions["Symfony\\Bridge\\Twig\\Extension\\ProfilerExtension"];
            $__internal_6f47bbe9983af81f1e7450e9a3e3768f->enter($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "macro", "button"));

            // line 4
            yield "    ";
            $context["classes"] = Twig\Extension\CoreExtension::join(["inline-flex items-center justify-center gap-2 rounded-lg px-4 py-2 text-sm font-medium transition-colors", (((            // line 6
(isset($context["type"]) || array_key_exists("type", $context) ? $context["type"] : (function () { throw new RuntimeError('Variable "type" does not exist.', 6, $this->source); })()) == "primary")) ? ("bg-primary text-white hover:bg-primary/90") : ("")), (((            // line 7
(isset($context["type"]) || array_key_exists("type", $context) ? $context["type"] : (function () { throw new RuntimeError('Variable "type" does not exist.', 7, $this->source); })()) == "secondary")) ? ("bg-secondary text-white hover:bg-secondary/90") : ("")), (((            // line 8
(isset($context["type"]) || array_key_exists("type", $context) ? $context["type"] : (function () { throw new RuntimeError('Variable "type" does not exist.', 8, $this->source); })()) == "outline")) ? ("border border-border bg-transparent text-foreground hover:bg-muted") : ("")), (((            // line 9
(isset($context["type"]) || array_key_exists("type", $context) ? $context["type"] : (function () { throw new RuntimeError('Variable "type" does not exist.', 9, $this->source); })()) == "danger")) ? ("bg-destructive text-white hover:bg-destructive/90") : ("")), (((            // line 10
(isset($context["type"]) || array_key_exists("type", $context) ? $context["type"] : (function () { throw new RuntimeError('Variable "type" does not exist.', 10, $this->source); })()) == "ghost")) ? ("bg-transparent text-foreground hover:bg-muted") : (""))], " ");
            // line 12
            yield "
    ";
            // line 13
            if ((($tmp = (isset($context["href"]) || array_key_exists("href", $context) ? $context["href"] : (function () { throw new RuntimeError('Variable "href" does not exist.', 13, $this->source); })())) && $tmp instanceof Markup ? (string) $tmp : $tmp)) {
                // line 14
                yield "        <a href=\"";
                yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape((isset($context["href"]) || array_key_exists("href", $context) ? $context["href"] : (function () { throw new RuntimeError('Variable "href" does not exist.', 14, $this->source); })()), "html", null, true);
                yield "\" class=\"";
                yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape((isset($context["classes"]) || array_key_exists("classes", $context) ? $context["classes"] : (function () { throw new RuntimeError('Variable "classes" does not exist.', 14, $this->source); })()), "html", null, true);
                yield " ";
                yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape((isset($context["extra"]) || array_key_exists("extra", $context) ? $context["extra"] : (function () { throw new RuntimeError('Variable "extra" does not exist.', 14, $this->source); })()), "html", null, true);
                yield "\">
            ";
                // line 15
                yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape((isset($context["text"]) || array_key_exists("text", $context) ? $context["text"] : (function () { throw new RuntimeError('Variable "text" does not exist.', 15, $this->source); })()), "html", null, true);
                yield "
        </a>
    ";
            } else {
                // line 18
                yield "        <button class=\"";
                yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape((isset($context["classes"]) || array_key_exists("classes", $context) ? $context["classes"] : (function () { throw new RuntimeError('Variable "classes" does not exist.', 18, $this->source); })()), "html", null, true);
                yield " ";
                yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape((isset($context["extra"]) || array_key_exists("extra", $context) ? $context["extra"] : (function () { throw new RuntimeError('Variable "extra" does not exist.', 18, $this->source); })()), "html", null, true);
                yield "\">
            ";
                // line 19
                yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape((isset($context["text"]) || array_key_exists("text", $context) ? $context["text"] : (function () { throw new RuntimeError('Variable "text" does not exist.', 19, $this->source); })()), "html", null, true);
                yield "
        </button>
    ";
            }
            
            $__internal_6f47bbe9983af81f1e7450e9a3e3768f->leave($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof);

            
            $__internal_5a27a8ba21ca79b61932376b2fa922d2->leave($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof);

            yield from [];
        })())) ? '' : new Markup($tmp, $this->env->getCharset());
    }

    // line 24
    public function macro_alert($type = null, $message = null, ...$varargs): string|Markup
    {
        $macros = $this->macros;
        $context = [
            "type" => $type,
            "message" => $message,
            "varargs" => $varargs,
        ] + $this->env->getGlobals();

        $blocks = [];

        return ('' === $tmp = \Twig\Extension\CoreExtension::captureOutput((function () use (&$context, $macros, $blocks) {
            $__internal_5a27a8ba21ca79b61932376b2fa922d2 = $this->extensions["Symfony\\Bundle\\WebProfilerBundle\\Twig\\WebProfilerExtension"];
            $__internal_5a27a8ba21ca79b61932376b2fa922d2->enter($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "macro", "alert"));

            $__internal_6f47bbe9983af81f1e7450e9a3e3768f = $this->extensions["Symfony\\Bridge\\Twig\\Extension\\ProfilerExtension"];
            $__internal_6f47bbe9983af81f1e7450e9a3e3768f->enter($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "macro", "alert"));

            // line 25
            yield "    ";
            $context["classes"] = Twig\Extension\CoreExtension::join(["rounded-lg px-4 py-3 text-sm font-medium", (((            // line 27
(isset($context["type"]) || array_key_exists("type", $context) ? $context["type"] : (function () { throw new RuntimeError('Variable "type" does not exist.', 27, $this->source); })()) == "success")) ? ("bg-accent text-white") : ("")), (((            // line 28
(isset($context["type"]) || array_key_exists("type", $context) ? $context["type"] : (function () { throw new RuntimeError('Variable "type" does not exist.', 28, $this->source); })()) == "error")) ? ("bg-destructive text-white") : ("")), (((            // line 29
(isset($context["type"]) || array_key_exists("type", $context) ? $context["type"] : (function () { throw new RuntimeError('Variable "type" does not exist.', 29, $this->source); })()) == "info")) ? ("bg-secondary text-white") : (""))], " ");
            // line 31
            yield "
    <div class=\"";
            // line 32
            yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape((isset($context["classes"]) || array_key_exists("classes", $context) ? $context["classes"] : (function () { throw new RuntimeError('Variable "classes" does not exist.', 32, $this->source); })()), "html", null, true);
            yield "\">
        ";
            // line 33
            yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape((isset($context["message"]) || array_key_exists("message", $context) ? $context["message"] : (function () { throw new RuntimeError('Variable "message" does not exist.', 33, $this->source); })()), "html", null, true);
            yield "
    </div>
";
            
            $__internal_6f47bbe9983af81f1e7450e9a3e3768f->leave($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof);

            
            $__internal_5a27a8ba21ca79b61932376b2fa922d2->leave($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof);

            yield from [];
        })())) ? '' : new Markup($tmp, $this->env->getCharset());
    }

    // line 37
    public function macro_navbar($active = "home", ...$varargs): string|Markup
    {
        $macros = $this->macros;
        $context = [
            "active" => $active,
            "varargs" => $varargs,
        ] + $this->env->getGlobals();

        $blocks = [];

        return ('' === $tmp = \Twig\Extension\CoreExtension::captureOutput((function () use (&$context, $macros, $blocks) {
            $__internal_5a27a8ba21ca79b61932376b2fa922d2 = $this->extensions["Symfony\\Bundle\\WebProfilerBundle\\Twig\\WebProfilerExtension"];
            $__internal_5a27a8ba21ca79b61932376b2fa922d2->enter($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "macro", "navbar"));

            $__internal_6f47bbe9983af81f1e7450e9a3e3768f = $this->extensions["Symfony\\Bridge\\Twig\\Extension\\ProfilerExtension"];
            $__internal_6f47bbe9983af81f1e7450e9a3e3768f->enter($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "macro", "navbar"));

            // line 38
            yield "    <nav class=\"bg-white shadow\">
        <div class=\"container mx-auto px-4 py-4 flex justify-between items-center\">
            <a href=\"/\" class=\"text-xl font-bold text-gray-800\">
                InnerTrack
            </a>

            <div class=\"space-x-4\">
                <a href=\"/\" class=\"";
            // line 45
            yield ((((isset($context["active"]) || array_key_exists("active", $context) ? $context["active"] : (function () { throw new RuntimeError('Variable "active" does not exist.', 45, $this->source); })()) == "home")) ? ("text-primary font-semibold") : ("text-gray-600 hover:text-gray-900"));
            yield "\">Home</a>
                <a href=\"/login\" class=\"";
            // line 46
            yield ((((isset($context["active"]) || array_key_exists("active", $context) ? $context["active"] : (function () { throw new RuntimeError('Variable "active" does not exist.', 46, $this->source); })()) == "login")) ? ("text-primary font-semibold") : ("text-gray-600 hover:text-gray-900"));
            yield "\">Login</a>
            </div>
        </div>
    </nav>
";
            
            $__internal_6f47bbe9983af81f1e7450e9a3e3768f->leave($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof);

            
            $__internal_5a27a8ba21ca79b61932376b2fa922d2->leave($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof);

            yield from [];
        })())) ? '' : new Markup($tmp, $this->env->getCharset());
    }

    // line 52
    public function macro_footer(...$varargs): string|Markup
    {
        $macros = $this->macros;
        $context = [
            "varargs" => $varargs,
        ] + $this->env->getGlobals();

        $blocks = [];

        return ('' === $tmp = \Twig\Extension\CoreExtension::captureOutput((function () use (&$context, $macros, $blocks) {
            $__internal_5a27a8ba21ca79b61932376b2fa922d2 = $this->extensions["Symfony\\Bundle\\WebProfilerBundle\\Twig\\WebProfilerExtension"];
            $__internal_5a27a8ba21ca79b61932376b2fa922d2->enter($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "macro", "footer"));

            $__internal_6f47bbe9983af81f1e7450e9a3e3768f = $this->extensions["Symfony\\Bridge\\Twig\\Extension\\ProfilerExtension"];
            $__internal_6f47bbe9983af81f1e7450e9a3e3768f->enter($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "macro", "footer"));

            // line 53
            yield "    <footer class=\"bg-white border-t mt-6\">
        <div class=\"container mx-auto px-4 py-4 text-center text-gray-500 text-sm\">
            © ";
            // line 55
            yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape($this->extensions['Twig\Extension\CoreExtension']->formatDate("now", "Y"), "html", null, true);
            yield " InnerTrack — All rights reserved
        </div>
    </footer>
";
            
            $__internal_6f47bbe9983af81f1e7450e9a3e3768f->leave($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof);

            
            $__internal_5a27a8ba21ca79b61932376b2fa922d2->leave($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof);

            yield from [];
        })())) ? '' : new Markup($tmp, $this->env->getCharset());
    }

    /**
     * @codeCoverageIgnore
     */
    public function getTemplateName(): string
    {
        return "macros/ui.html.twig";
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
        return array (  257 => 55,  253 => 53,  236 => 52,  219 => 46,  215 => 45,  206 => 38,  188 => 37,  173 => 33,  169 => 32,  166 => 31,  164 => 29,  163 => 28,  162 => 27,  160 => 25,  141 => 24,  125 => 19,  118 => 18,  112 => 15,  103 => 14,  101 => 13,  98 => 12,  96 => 10,  95 => 9,  94 => 8,  93 => 7,  92 => 6,  90 => 4,  69 => 3,  57 => 51,  54 => 36,  51 => 23,  48 => 2,);
    }

    public function getSourceContext(): Source
    {
        return new Source("{# templates/macros/ui.html.twig #}

{% macro button(text, type = 'primary', href = null, extra = '') %}
    {% set classes = [
        'inline-flex items-center justify-center gap-2 rounded-lg px-4 py-2 text-sm font-medium transition-colors',
        type == 'primary' ? 'bg-primary text-white hover:bg-primary/90' : '',
        type == 'secondary' ? 'bg-secondary text-white hover:bg-secondary/90' : '',
        type == 'outline' ? 'border border-border bg-transparent text-foreground hover:bg-muted' : '',
        type == 'danger' ? 'bg-destructive text-white hover:bg-destructive/90' : '',
        type == 'ghost' ? 'bg-transparent text-foreground hover:bg-muted' : ''
    ] | join(' ') %}

    {% if href %}
        <a href=\"{{ href }}\" class=\"{{ classes }} {{ extra }}\">
            {{ text }}
        </a>
    {% else %}
        <button class=\"{{ classes }} {{ extra }}\">
            {{ text }}
        </button>
    {% endif %}
{% endmacro %}

{% macro alert(type, message) %}
    {% set classes = [
        'rounded-lg px-4 py-3 text-sm font-medium',
        type == 'success' ? 'bg-accent text-white' : '',
        type == 'error' ? 'bg-destructive text-white' : '',
        type == 'info' ? 'bg-secondary text-white' : '',
    ] | join(' ') %}

    <div class=\"{{ classes }}\">
        {{ message }}
    </div>
{% endmacro %}

{% macro navbar(active = 'home') %}
    <nav class=\"bg-white shadow\">
        <div class=\"container mx-auto px-4 py-4 flex justify-between items-center\">
            <a href=\"/\" class=\"text-xl font-bold text-gray-800\">
                InnerTrack
            </a>

            <div class=\"space-x-4\">
                <a href=\"/\" class=\"{{ active == 'home' ? 'text-primary font-semibold' : 'text-gray-600 hover:text-gray-900' }}\">Home</a>
                <a href=\"/login\" class=\"{{ active == 'login' ? 'text-primary font-semibold' : 'text-gray-600 hover:text-gray-900' }}\">Login</a>
            </div>
        </div>
    </nav>
{% endmacro %}

{% macro footer() %}
    <footer class=\"bg-white border-t mt-6\">
        <div class=\"container mx-auto px-4 py-4 text-center text-gray-500 text-sm\">
            © {{ \"now\"|date(\"Y\") }} InnerTrack — All rights reserved
        </div>
    </footer>
{% endmacro %}
", "macros/ui.html.twig", "C:\\Users\\Bratan\\javafx-symfony-test\\backend\\templates\\macros\\ui.html.twig");
    }
}
