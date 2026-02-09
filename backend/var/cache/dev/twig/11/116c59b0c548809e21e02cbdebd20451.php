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

/* pages/forgotPassword.html.twig */
class __TwigTemplate_8bb4efa405c19aefacc4f0a5259fff5d extends Template
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

        $this->blocks = [
            'title' => [$this, 'block_title'],
            'body' => [$this, 'block_body'],
        ];
    }

    protected function doGetParent(array $context): bool|string|Template|TemplateWrapper
    {
        // line 2
        return "layouts/auth.html.twig";
    }

    protected function doDisplay(array $context, array $blocks = []): iterable
    {
        $macros = $this->macros;
        $__internal_5a27a8ba21ca79b61932376b2fa922d2 = $this->extensions["Symfony\\Bundle\\WebProfilerBundle\\Twig\\WebProfilerExtension"];
        $__internal_5a27a8ba21ca79b61932376b2fa922d2->enter($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "template", "pages/forgotPassword.html.twig"));

        $__internal_6f47bbe9983af81f1e7450e9a3e3768f = $this->extensions["Symfony\\Bridge\\Twig\\Extension\\ProfilerExtension"];
        $__internal_6f47bbe9983af81f1e7450e9a3e3768f->enter($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "template", "pages/forgotPassword.html.twig"));

        $this->parent = $this->load("layouts/auth.html.twig", 2);
        yield from $this->parent->unwrap()->yield($context, array_merge($this->blocks, $blocks));
        
        $__internal_5a27a8ba21ca79b61932376b2fa922d2->leave($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof);

        
        $__internal_6f47bbe9983af81f1e7450e9a3e3768f->leave($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof);

    }

    // line 4
    /**
     * @return iterable<null|scalar|\Stringable>
     */
    public function block_title(array $context, array $blocks = []): iterable
    {
        $macros = $this->macros;
        $__internal_5a27a8ba21ca79b61932376b2fa922d2 = $this->extensions["Symfony\\Bundle\\WebProfilerBundle\\Twig\\WebProfilerExtension"];
        $__internal_5a27a8ba21ca79b61932376b2fa922d2->enter($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "block", "title"));

        $__internal_6f47bbe9983af81f1e7450e9a3e3768f = $this->extensions["Symfony\\Bridge\\Twig\\Extension\\ProfilerExtension"];
        $__internal_6f47bbe9983af81f1e7450e9a3e3768f->enter($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "block", "title"));

        yield " Mot de passe oublié ";
        
        $__internal_6f47bbe9983af81f1e7450e9a3e3768f->leave($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof);

        
        $__internal_5a27a8ba21ca79b61932376b2fa922d2->leave($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof);

        yield from [];
    }

    // line 5
    /**
     * @return iterable<null|scalar|\Stringable>
     */
    public function block_body(array $context, array $blocks = []): iterable
    {
        $macros = $this->macros;
        $__internal_5a27a8ba21ca79b61932376b2fa922d2 = $this->extensions["Symfony\\Bundle\\WebProfilerBundle\\Twig\\WebProfilerExtension"];
        $__internal_5a27a8ba21ca79b61932376b2fa922d2->enter($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "block", "body"));

        $__internal_6f47bbe9983af81f1e7450e9a3e3768f = $this->extensions["Symfony\\Bridge\\Twig\\Extension\\ProfilerExtension"];
        $__internal_6f47bbe9983af81f1e7450e9a3e3768f->enter($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "block", "body"));

        // line 6
        yield "<div class=\"w-full max-w-md bg-white rounded-3xl shadow-xl px-8 py-10 mx-auto mt-20\">
    <!-- LOGO -->
    <div class=\"mx-auto w-20 h-20 rounded-2xl flex items-center justify-center mb-6\">
        <img src=\"";
        // line 9
        yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape($this->extensions['Symfony\Bridge\Twig\Extension\AssetExtension']->getAssetUrl("images/innertracklogo1.png"), "html", null, true);
        yield "\" alt=\"InnerTrack Logo\" class=\"w-16 h-16\"/>
    </div>
    <h2 class=\"text-center text-2xl font-semibold text-gray-800 mb-2\">
        Mot de passe oublié
    </h2>
    <div class=\"text-center text-sm text-gray-500 mb-8\">
        Entrez votre adresse e-mail pour recevoir un lien de réinitialisation de mot de passe
    </div>
    <form method=\"POST\" class=\"space-y-6\">
        <div class=\"space-y-1\">
            <label class=\"text-sm text-gray-600\">Email </label>
            <input
                type=\"email\"
                name=\"email\"
                required
                class=\"w-full h-11 rounded-xl border border-emerald-200 px-4 bg-emerald-50 focus:outline-none focus:ring-2 focus:ring-cyan-400\"
            />  
        </div>
        <button
            type=\"submit\"
            class=\"w-full h-11 bg-cyan-500 text-white rounded-xl hover:bg-cyan-600 transition-colors\"
        >
            Envoyer le lien
        </button>
    </form>
    <div class=\"mt-6 text-center\">
        <a href=\"";
        // line 35
        yield $this->extensions['Symfony\Bridge\Twig\Extension\RoutingExtension']->getPath("login");
        yield "\" class=\"text-sm text-cyan-500 hover:underline flex items-center justify-center\">
            Retour à la connexion
        </a>
    </div>
</div>
";
        
        $__internal_6f47bbe9983af81f1e7450e9a3e3768f->leave($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof);

        
        $__internal_5a27a8ba21ca79b61932376b2fa922d2->leave($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof);

        yield from [];
    }

    /**
     * @codeCoverageIgnore
     */
    public function getTemplateName(): string
    {
        return "pages/forgotPassword.html.twig";
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
        return array (  134 => 35,  105 => 9,  100 => 6,  87 => 5,  64 => 4,  41 => 2,);
    }

    public function getSourceContext(): Source
    {
        return new Source("
{% extends 'layouts/auth.html.twig' %}

{% block title %} Mot de passe oublié {% endblock %}
{% block body %}
<div class=\"w-full max-w-md bg-white rounded-3xl shadow-xl px-8 py-10 mx-auto mt-20\">
    <!-- LOGO -->
    <div class=\"mx-auto w-20 h-20 rounded-2xl flex items-center justify-center mb-6\">
        <img src=\"{{ asset('images/innertracklogo1.png') }}\" alt=\"InnerTrack Logo\" class=\"w-16 h-16\"/>
    </div>
    <h2 class=\"text-center text-2xl font-semibold text-gray-800 mb-2\">
        Mot de passe oublié
    </h2>
    <div class=\"text-center text-sm text-gray-500 mb-8\">
        Entrez votre adresse e-mail pour recevoir un lien de réinitialisation de mot de passe
    </div>
    <form method=\"POST\" class=\"space-y-6\">
        <div class=\"space-y-1\">
            <label class=\"text-sm text-gray-600\">Email </label>
            <input
                type=\"email\"
                name=\"email\"
                required
                class=\"w-full h-11 rounded-xl border border-emerald-200 px-4 bg-emerald-50 focus:outline-none focus:ring-2 focus:ring-cyan-400\"
            />  
        </div>
        <button
            type=\"submit\"
            class=\"w-full h-11 bg-cyan-500 text-white rounded-xl hover:bg-cyan-600 transition-colors\"
        >
            Envoyer le lien
        </button>
    </form>
    <div class=\"mt-6 text-center\">
        <a href=\"{{ path('login') }}\" class=\"text-sm text-cyan-500 hover:underline flex items-center justify-center\">
            Retour à la connexion
        </a>
    </div>
</div>
{% endblock %}
", "pages/forgotPassword.html.twig", "C:\\Users\\Bratan\\javafx-symfony-test\\backend\\templates\\pages\\forgotPassword.html.twig");
    }
}
