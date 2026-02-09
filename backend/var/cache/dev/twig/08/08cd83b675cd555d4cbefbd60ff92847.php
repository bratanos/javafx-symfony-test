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

/* pages/login.html.twig */
class __TwigTemplate_588ddb1748bd684b1862afc89ce2af6e extends Template
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
        $__internal_5a27a8ba21ca79b61932376b2fa922d2->enter($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "template", "pages/login.html.twig"));

        $__internal_6f47bbe9983af81f1e7450e9a3e3768f = $this->extensions["Symfony\\Bridge\\Twig\\Extension\\ProfilerExtension"];
        $__internal_6f47bbe9983af81f1e7450e9a3e3768f->enter($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "template", "pages/login.html.twig"));

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

        yield "Connexion";
        
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
        yield "<div class=\"w-full max-w-md bg-white rounded-3xl shadow-xl px-8 py-10\">

  <!-- LOGO -->
  <div class=\"mx-auto w-20 h-20 rounded-2xl  flex items-center justify-center mb-6\">
   <img src=\"";
        // line 10
        yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape($this->extensions['Symfony\Bridge\Twig\Extension\AssetExtension']->getAssetUrl("images/innertracklogo1.png"), "html", null, true);
        yield "\" alt=\"InnerTrack Logo\" class=\"w-16 h-16\"/>
  </div>

  <h2 class=\"text-center text-2xl font-semibold text-gray-800 mb-2\">
    Bienvenue sur InnerTrack
  </h2>

  <p class=\"text-center text-sm text-gray-500 mb-8\">
    Connectez-vous pour accéder à votre espace bien-être
  </p>

  <form method=\"POST\" class=\"space-y-6\">

    <div class=\"space-y-2\">
      <label class=\"text-sm text-gray-600\">Email</label>
      <input
        type=\"email\"
        name=\"email\"
        required
        class=\"w-full h-11 rounded-xl border border-gray-200 px-4 bg-gray-50
               focus:outline-none focus:ring-2 focus:ring-cyan-400\"
      />
    </div>

    <div class=\"space-y-2\">
      <label class=\"text-sm text-gray-600\">Mot de passe</label>
      <input
        type=\"password\"
        name=\"password\"
        required
        class=\"w-full h-11 rounded-xl border border-gray-200 px-4 bg-gray-50
               focus:outline-none focus:ring-2 focus:ring-cyan-400\"
      />
    </div>

    <button
      type=\"submit\"
      class=\"w-full h-11 rounded-xl bg-cyan-600 hover:bg-cyan-700 transition-colors
             text-white font-medium\"
    >
      Se connecter
    </button>

    <div class=\"text-center\">
      <a href=\"";
        // line 54
        yield $this->extensions['Symfony\Bridge\Twig\Extension\RoutingExtension']->getPath("forgot_password");
        yield "\" class=\"text-sm text-cyan-500 hover:text-cyan-600 transition\">
        Mot de passe oublié ?
      </a>
    </div>

<div class=\"relative py-6 pointer-events-none\">
  <div class=\"absolute inset-0 flex items-center\">
    <span class=\"w-full border-t border-gray-200\"></span>
  </div>
  <div class=\"relative flex justify-center text-xs uppercase\">
    <span class=\"bg-white px-3 text-gray-400\">
      Nouveau sur InnerTrack ?
    </span>
  </div>
</div>

<button
  type=\"button\"
  class=\"relative z-10 w-full h-11 rounded-xl
         border border-emerald-200
         text-emerald-600
         bg-white
         hover:bg-emerald-100
         hover:scale-[1.02]
         hover:border-emerald-300
         hover:text-emerald-700
         transition-all duration-200\"
>
  Créer un compte
</button>


  </form>
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
        return "pages/login.html.twig";
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
        return array (  153 => 54,  106 => 10,  100 => 6,  87 => 5,  64 => 3,  41 => 1,);
    }

    public function getSourceContext(): Source
    {
        return new Source("{% extends 'layouts/auth.html.twig' %}

{% block title %}Connexion{% endblock %}

{% block body %}
<div class=\"w-full max-w-md bg-white rounded-3xl shadow-xl px-8 py-10\">

  <!-- LOGO -->
  <div class=\"mx-auto w-20 h-20 rounded-2xl  flex items-center justify-center mb-6\">
   <img src=\"{{ asset('images/innertracklogo1.png') }}\" alt=\"InnerTrack Logo\" class=\"w-16 h-16\"/>
  </div>

  <h2 class=\"text-center text-2xl font-semibold text-gray-800 mb-2\">
    Bienvenue sur InnerTrack
  </h2>

  <p class=\"text-center text-sm text-gray-500 mb-8\">
    Connectez-vous pour accéder à votre espace bien-être
  </p>

  <form method=\"POST\" class=\"space-y-6\">

    <div class=\"space-y-2\">
      <label class=\"text-sm text-gray-600\">Email</label>
      <input
        type=\"email\"
        name=\"email\"
        required
        class=\"w-full h-11 rounded-xl border border-gray-200 px-4 bg-gray-50
               focus:outline-none focus:ring-2 focus:ring-cyan-400\"
      />
    </div>

    <div class=\"space-y-2\">
      <label class=\"text-sm text-gray-600\">Mot de passe</label>
      <input
        type=\"password\"
        name=\"password\"
        required
        class=\"w-full h-11 rounded-xl border border-gray-200 px-4 bg-gray-50
               focus:outline-none focus:ring-2 focus:ring-cyan-400\"
      />
    </div>

    <button
      type=\"submit\"
      class=\"w-full h-11 rounded-xl bg-cyan-600 hover:bg-cyan-700 transition-colors
             text-white font-medium\"
    >
      Se connecter
    </button>

    <div class=\"text-center\">
      <a href=\"{{ path('forgot_password') }}\" class=\"text-sm text-cyan-500 hover:text-cyan-600 transition\">
        Mot de passe oublié ?
      </a>
    </div>

<div class=\"relative py-6 pointer-events-none\">
  <div class=\"absolute inset-0 flex items-center\">
    <span class=\"w-full border-t border-gray-200\"></span>
  </div>
  <div class=\"relative flex justify-center text-xs uppercase\">
    <span class=\"bg-white px-3 text-gray-400\">
      Nouveau sur InnerTrack ?
    </span>
  </div>
</div>

<button
  type=\"button\"
  class=\"relative z-10 w-full h-11 rounded-xl
         border border-emerald-200
         text-emerald-600
         bg-white
         hover:bg-emerald-100
         hover:scale-[1.02]
         hover:border-emerald-300
         hover:text-emerald-700
         transition-all duration-200\"
>
  Créer un compte
</button>


  </form>
</div>
{% endblock %}
", "pages/login.html.twig", "C:\\Users\\Bratan\\javafx-symfony-test\\backend\\templates\\pages\\login.html.twig");
    }
}
