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

/* pages/signup.html.twig */
class __TwigTemplate_e25875ab43070ed8f41a8527a7740d32 extends Template
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
        // line 1
        return "layouts/auth.html.twig";
    }

    protected function doDisplay(array $context, array $blocks = []): iterable
    {
        $macros = $this->macros;
        $__internal_5a27a8ba21ca79b61932376b2fa922d2 = $this->extensions["Symfony\\Bundle\\WebProfilerBundle\\Twig\\WebProfilerExtension"];
        $__internal_5a27a8ba21ca79b61932376b2fa922d2->enter($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "template", "pages/signup.html.twig"));

        $__internal_6f47bbe9983af81f1e7450e9a3e3768f = $this->extensions["Symfony\\Bridge\\Twig\\Extension\\ProfilerExtension"];
        $__internal_6f47bbe9983af81f1e7450e9a3e3768f->enter($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "template", "pages/signup.html.twig"));

        $this->parent = $this->load("layouts/auth.html.twig", 1);
        yield from $this->parent->unwrap()->yield($context, array_merge($this->blocks, $blocks));
        
        $__internal_5a27a8ba21ca79b61932376b2fa922d2->leave($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof);

        
        $__internal_6f47bbe9983af81f1e7450e9a3e3768f->leave($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof);

    }

    // line 3
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

        yield " Inscription ";
        
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

        yield " 
<!-- SIGNUP FORM CONTAINER WITH ROLE SELECTION PSYCHOLOGUE/CLIENT ALSO TYPE IN NOM ET PRENOM SEPERATE TABS AND HORIZONTALLY NEXT TO EACH OTHER AND AT THE BOTTOM SPACER THAT SAYS \"Already have an account?\" and a button to login -->
<div class=\"w-full max-w-md bg-white rounded-3xl shadow-xl px-8 py-10\">

<!-- LOGO -->
  <div class=\"mx-auto w-20 h-20 rounded-2xl  flex items-center justify-center mb-6\">
   <img src=\"";
        // line 11
        yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape($this->extensions['Symfony\Bridge\Twig\Extension\AssetExtension']->getAssetUrl("images/innertracklogo1.png"), "html", null, true);
        yield "\" alt=\"InnerTrack Logo\" class=\"w-16 h-16\"/>
  </div>
<h2 class=\"text-center text-2xl font-semibold text-gray-800 mb-2\">
    Rejoignez InnerTrack
</h2>
     <div class=\"text-center text-sm text-gray-500 mb-8\">
        Créez votre compte pour accéder à votre espace bien-être
    </div>
    <form method=\"POST\" class=\"space-y-6\">

    <!-- Problem here when i added this div the form became cramped together and messed up the layouyt.. fix this -->
    <div class=\"flex space-x-4\">
        <div class=\"space-y-1 flex-1\">
            <label class=\"text-sm text-gray-600\"> Prénom </label>
            <input
                type=\"text\"
                name=\"first_name\"
                required
                class=\"w-full h-11 rounded-xl border border-emerald-200 px-4 bg-emerald-50 focus :outline-none focus:ring-2 focus:ring-cyan-400\"
            />
        </div>
        <div class=\"space-y-1 flex-1\">
            <label class=\"text-sm text-gray-600\"> Nom </label>
            <input
                type=\"text\"
                name=\"last_name\"
                required
                class=\"w-full h-11 rounded-xl border border-emerald-200 px-4 bg-emerald-50 focus :outline-none focus:ring-2 focus:ring-cyan-400\"
            />
        </div>
    </div>

        <div class=\"space-y-1\">
            <label class=\"text-sm text-gray-600\">Email </label>
            <input
                type=\"email\"
                name=\"email\"
                required
                class=\"w-full h-11 rounded-xl border border-emerald-200 px-4 bg-emerald-50 focus :outline-none focus:ring-2 focus:ring-cyan-400\"
            />  
        </div>
        <div class=\"space-y-1\">
            <label class=\"text-sm text-gray-600\">Mot de passe </label>
            <input
                type=\"password\"
                name=\"password\"
                required
                class=\"w-full h-11 rounded-xl border border-emerald-200 px-4 bg-emerald-50 focus :outline-none focus:ring-2 focus:ring-cyan-400\"
            />
        </div>
        <div class=\"space-y-1\">
            <label class=\"text-sm text-gray-600\"> Confirmer le mot de passe </label>
            <input 
                type=\"password\"
                name=\"confirm_password\"
                required
                class=\"w-full h-11 rounded-xl border border-emerald-200 px-4 bg-emerald-50 focus :outline-none focus:ring-2 focus:ring-cyan-400\"
            />
        </div>
        <div class=\"space-y-1\">
            <label clas=\"text-sm text-gray-600\"> Je suis un(e) </label>
            <select
                name=\"role\"
                required
                class=\"w-full h-11 rounded-xl border border-emerald-200 px-4 bg-emerald-50 focus :outline-none focus:ring-2 focus:ring-cyan-400\"
            >
                <option value=\"client\"> Client </option>
                <option value=\"psychologue\"> Psychologue </option>
            </select>
        </div>
        <button
            type=\"submit\"
            class=\"w-full h-11 rounded-xl bg-cyan-600 hover:bg-cyan-700 transition-colors text-white font-medium\"
        >
            S'inscrire
        </button>
    </form>
    <div class=\"text-center mt-6 text-sm text-gray-500\">
        Déjà un compte ?
        <a href=\"/login\" class=\"text-cyan-500 hover:text-cyan-600 transition\">
            Se connecter
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
        return "pages/signup.html.twig";
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
        return array (  107 => 11,  87 => 5,  64 => 3,  41 => 1,);
    }

    public function getSourceContext(): Source
    {
        return new Source("{% extends 'layouts/auth.html.twig' %}

{% block title %} Inscription {% endblock %}

{% block body %} 
<!-- SIGNUP FORM CONTAINER WITH ROLE SELECTION PSYCHOLOGUE/CLIENT ALSO TYPE IN NOM ET PRENOM SEPERATE TABS AND HORIZONTALLY NEXT TO EACH OTHER AND AT THE BOTTOM SPACER THAT SAYS \"Already have an account?\" and a button to login -->
<div class=\"w-full max-w-md bg-white rounded-3xl shadow-xl px-8 py-10\">

<!-- LOGO -->
  <div class=\"mx-auto w-20 h-20 rounded-2xl  flex items-center justify-center mb-6\">
   <img src=\"{{ asset('images/innertracklogo1.png') }}\" alt=\"InnerTrack Logo\" class=\"w-16 h-16\"/>
  </div>
<h2 class=\"text-center text-2xl font-semibold text-gray-800 mb-2\">
    Rejoignez InnerTrack
</h2>
     <div class=\"text-center text-sm text-gray-500 mb-8\">
        Créez votre compte pour accéder à votre espace bien-être
    </div>
    <form method=\"POST\" class=\"space-y-6\">

    <!-- Problem here when i added this div the form became cramped together and messed up the layouyt.. fix this -->
    <div class=\"flex space-x-4\">
        <div class=\"space-y-1 flex-1\">
            <label class=\"text-sm text-gray-600\"> Prénom </label>
            <input
                type=\"text\"
                name=\"first_name\"
                required
                class=\"w-full h-11 rounded-xl border border-emerald-200 px-4 bg-emerald-50 focus :outline-none focus:ring-2 focus:ring-cyan-400\"
            />
        </div>
        <div class=\"space-y-1 flex-1\">
            <label class=\"text-sm text-gray-600\"> Nom </label>
            <input
                type=\"text\"
                name=\"last_name\"
                required
                class=\"w-full h-11 rounded-xl border border-emerald-200 px-4 bg-emerald-50 focus :outline-none focus:ring-2 focus:ring-cyan-400\"
            />
        </div>
    </div>

        <div class=\"space-y-1\">
            <label class=\"text-sm text-gray-600\">Email </label>
            <input
                type=\"email\"
                name=\"email\"
                required
                class=\"w-full h-11 rounded-xl border border-emerald-200 px-4 bg-emerald-50 focus :outline-none focus:ring-2 focus:ring-cyan-400\"
            />  
        </div>
        <div class=\"space-y-1\">
            <label class=\"text-sm text-gray-600\">Mot de passe </label>
            <input
                type=\"password\"
                name=\"password\"
                required
                class=\"w-full h-11 rounded-xl border border-emerald-200 px-4 bg-emerald-50 focus :outline-none focus:ring-2 focus:ring-cyan-400\"
            />
        </div>
        <div class=\"space-y-1\">
            <label class=\"text-sm text-gray-600\"> Confirmer le mot de passe </label>
            <input 
                type=\"password\"
                name=\"confirm_password\"
                required
                class=\"w-full h-11 rounded-xl border border-emerald-200 px-4 bg-emerald-50 focus :outline-none focus:ring-2 focus:ring-cyan-400\"
            />
        </div>
        <div class=\"space-y-1\">
            <label clas=\"text-sm text-gray-600\"> Je suis un(e) </label>
            <select
                name=\"role\"
                required
                class=\"w-full h-11 rounded-xl border border-emerald-200 px-4 bg-emerald-50 focus :outline-none focus:ring-2 focus:ring-cyan-400\"
            >
                <option value=\"client\"> Client </option>
                <option value=\"psychologue\"> Psychologue </option>
            </select>
        </div>
        <button
            type=\"submit\"
            class=\"w-full h-11 rounded-xl bg-cyan-600 hover:bg-cyan-700 transition-colors text-white font-medium\"
        >
            S'inscrire
        </button>
    </form>
    <div class=\"text-center mt-6 text-sm text-gray-500\">
        Déjà un compte ?
        <a href=\"/login\" class=\"text-cyan-500 hover:text-cyan-600 transition\">
            Se connecter
        </a>
    </div>
</div>
{% endblock %}


    ", "pages/signup.html.twig", "C:\\Users\\Bratan\\javafx-symfony-test\\backend\\templates\\pages\\signup.html.twig");
    }
}
