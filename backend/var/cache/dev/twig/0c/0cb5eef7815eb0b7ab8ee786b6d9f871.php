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

/* pages/verifyEmail.html.twig */
class __TwigTemplate_2bf60f8d5bc4fdea06aafe4ab9f46e17 extends Template
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
        $__internal_5a27a8ba21ca79b61932376b2fa922d2->enter($__internal_5a27a8ba21ca79b61932376b2fa922d2_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "template", "pages/verifyEmail.html.twig"));

        $__internal_6f47bbe9983af81f1e7450e9a3e3768f = $this->extensions["Symfony\\Bridge\\Twig\\Extension\\ProfilerExtension"];
        $__internal_6f47bbe9983af81f1e7450e9a3e3768f->enter($__internal_6f47bbe9983af81f1e7450e9a3e3768f_prof = new \Twig\Profiler\Profile($this->getTemplateName(), "template", "pages/verifyEmail.html.twig"));

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

        yield "Vérifiez votre email";
        
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
        // line 10
        yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape($this->extensions['Symfony\Bridge\Twig\Extension\AssetExtension']->getAssetUrl("images/innertracklogo1.png"), "html", null, true);
        yield "\" alt=\"InnerTrack Logo\" class=\"w-16 h-16\"/>
    </div>

    <!-- Title -->
    <h2 class=\"text-center text-2xl font-semibold text-gray-800 mb-2\">
        Vérifiez votre email
    </h2>

    <!-- Subtitle -->
    <p class=\"text-center text-sm text-gray-500 mb-8 px-4\">
        Nous avons envoyé un code de vérification à <br>
        <span class=\"font-medium text-gray-900\">";
        // line 21
        yield $this->env->getRuntime('Twig\Runtime\EscaperRuntime')->escape(((array_key_exists("email", $context)) ? (Twig\Extension\CoreExtension::default((isset($context["email"]) || array_key_exists("email", $context) ? $context["email"] : (function () { throw new RuntimeError('Variable "email" does not exist.', 21, $this->source); })()), "votre adresse email")) : ("votre adresse email")), "html", null, true);
        yield "</span>
    </p>

    <!-- Form -->
    <form method=\"POST\" class=\"space-y-6\">
        
        <div class=\"space-y-4\">
            <label class=\"text-sm text-gray-600 block text-center mb-2\">Code de vérification</label>
            
            <!-- OTP Inputs Container -->
            <div class=\"flex justify-center gap-2\" id=\"otp-container\">
                ";
        // line 32
        $context['_parent'] = $context;
        $context['_seq'] = CoreExtension::ensureTraversable(range(1, 6));
        foreach ($context['_seq'] as $context["_key"] => $context["i"]) {
            // line 33
            yield "                <input
                    type=\"text\"
                    maxlength=\"1\"
                    class=\"otp-input w-12 h-14 rounded-xl border border-emerald-200 bg-emerald-50 text-center text-gray-800 font-bold text-xl focus:outline-none focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400 transition-all\"
                    autocomplete=\"off\"
                />
                ";
        }
        $_parent = $context['_parent'];
        unset($context['_seq'], $context['_key'], $context['i'], $context['_parent']);
        $context = array_intersect_key($context, $_parent) + $_parent;
        // line 40
        yield "            </div>

            <!-- Hidden input for form submission -->
            <input type=\"hidden\" name=\"verification_code\" id=\"verification_code\">

             <p class=\"text-center text-xs text-gray-400 mt-2\">
                Entrez le code à 6 chiffres
            </p>
        </div>

        <script>
            document.addEventListener('DOMContentLoaded', function() {
                const container = document.getElementById('otp-container');
                const inputs = container.querySelectorAll('.otp-input');
                const hiddenInput = document.getElementById('verification_code');

                const updateHiddenInput = () => {
                    let code = '';
                    inputs.forEach(input => code += input.value);
                    hiddenInput.value = code;
                };

                inputs.forEach((input, index) => {
                    // Handle input
                    input.addEventListener('input', (e) => {
                        // Allow only numbers
                        e.target.value = e.target.value.replace(/[^0-9]/g, '');

                        if (e.target.value.length === 1) {
                            // Move to next input if available
                            if (index < inputs.length - 1) {
                                inputs[index + 1].focus();
                            }
                        }
                        updateHiddenInput();
                    });

                    // Handle backspace
                    input.addEventListener('keydown', (e) => {
                        if (e.key === 'Backspace' && !e.target.value && index > 0) {
                            inputs[index - 1].focus();
                        }
                    });

                    // Handle paste
                    input.addEventListener('paste', (e) => {
                        e.preventDefault();
                        const pasteData = e.clipboardData.getData('text').replace(/[^0-9]/g, '').slice(0, 6);
                        
                        pasteData.split('').forEach((char, i) => {
                            if (inputs[i]) {
                                inputs[i].value = char;
                            }
                        });
                        
                        // Focus the next empty input or the last one
                        const nextEmptyIndex = pasteData.length < 6 ? pasteData.length : 5;
                        inputs[nextEmptyIndex].focus();
                        
                        updateHiddenInput();
                    });
                });
            });
        </script>

        <!-- Button -->
        <button
            type=\"submit\"
            class=\"w-full h-11 bg-cyan-500 text-white rounded-xl hover:bg-cyan-600 transition-colors font-medium mt-4\"
        >
            Vérifier mon email
        </button>

    </form>

    <!-- Resend Link -->
    <div class=\"mt-6 text-center\">
        <div class=\"text-sm text-gray-500 mb-1\">Vous n'avez pas reçu le code ?</div>
        <a href=\"#\" class=\"text-sm text-cyan-500 hover:underline inline-block\">
            Renvoyer le code
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
        return "pages/verifyEmail.html.twig";
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
        return array (  150 => 40,  138 => 33,  134 => 32,  120 => 21,  106 => 10,  100 => 6,  87 => 5,  64 => 3,  41 => 1,);
    }

    public function getSourceContext(): Source
    {
        return new Source("{% extends 'layouts/auth.html.twig' %}

{% block title %}Vérifiez votre email{% endblock %}

{% block body %}
<div class=\"w-full max-w-md bg-white rounded-3xl shadow-xl px-8 py-10 mx-auto mt-20\">
    
    <!-- LOGO -->
    <div class=\"mx-auto w-20 h-20 rounded-2xl flex items-center justify-center mb-6\">
        <img src=\"{{ asset('images/innertracklogo1.png') }}\" alt=\"InnerTrack Logo\" class=\"w-16 h-16\"/>
    </div>

    <!-- Title -->
    <h2 class=\"text-center text-2xl font-semibold text-gray-800 mb-2\">
        Vérifiez votre email
    </h2>

    <!-- Subtitle -->
    <p class=\"text-center text-sm text-gray-500 mb-8 px-4\">
        Nous avons envoyé un code de vérification à <br>
        <span class=\"font-medium text-gray-900\">{{ email|default('votre adresse email') }}</span>
    </p>

    <!-- Form -->
    <form method=\"POST\" class=\"space-y-6\">
        
        <div class=\"space-y-4\">
            <label class=\"text-sm text-gray-600 block text-center mb-2\">Code de vérification</label>
            
            <!-- OTP Inputs Container -->
            <div class=\"flex justify-center gap-2\" id=\"otp-container\">
                {% for i in 1..6 %}
                <input
                    type=\"text\"
                    maxlength=\"1\"
                    class=\"otp-input w-12 h-14 rounded-xl border border-emerald-200 bg-emerald-50 text-center text-gray-800 font-bold text-xl focus:outline-none focus:ring-2 focus:ring-cyan-400 focus:border-cyan-400 transition-all\"
                    autocomplete=\"off\"
                />
                {% endfor %}
            </div>

            <!-- Hidden input for form submission -->
            <input type=\"hidden\" name=\"verification_code\" id=\"verification_code\">

             <p class=\"text-center text-xs text-gray-400 mt-2\">
                Entrez le code à 6 chiffres
            </p>
        </div>

        <script>
            document.addEventListener('DOMContentLoaded', function() {
                const container = document.getElementById('otp-container');
                const inputs = container.querySelectorAll('.otp-input');
                const hiddenInput = document.getElementById('verification_code');

                const updateHiddenInput = () => {
                    let code = '';
                    inputs.forEach(input => code += input.value);
                    hiddenInput.value = code;
                };

                inputs.forEach((input, index) => {
                    // Handle input
                    input.addEventListener('input', (e) => {
                        // Allow only numbers
                        e.target.value = e.target.value.replace(/[^0-9]/g, '');

                        if (e.target.value.length === 1) {
                            // Move to next input if available
                            if (index < inputs.length - 1) {
                                inputs[index + 1].focus();
                            }
                        }
                        updateHiddenInput();
                    });

                    // Handle backspace
                    input.addEventListener('keydown', (e) => {
                        if (e.key === 'Backspace' && !e.target.value && index > 0) {
                            inputs[index - 1].focus();
                        }
                    });

                    // Handle paste
                    input.addEventListener('paste', (e) => {
                        e.preventDefault();
                        const pasteData = e.clipboardData.getData('text').replace(/[^0-9]/g, '').slice(0, 6);
                        
                        pasteData.split('').forEach((char, i) => {
                            if (inputs[i]) {
                                inputs[i].value = char;
                            }
                        });
                        
                        // Focus the next empty input or the last one
                        const nextEmptyIndex = pasteData.length < 6 ? pasteData.length : 5;
                        inputs[nextEmptyIndex].focus();
                        
                        updateHiddenInput();
                    });
                });
            });
        </script>

        <!-- Button -->
        <button
            type=\"submit\"
            class=\"w-full h-11 bg-cyan-500 text-white rounded-xl hover:bg-cyan-600 transition-colors font-medium mt-4\"
        >
            Vérifier mon email
        </button>

    </form>

    <!-- Resend Link -->
    <div class=\"mt-6 text-center\">
        <div class=\"text-sm text-gray-500 mb-1\">Vous n'avez pas reçu le code ?</div>
        <a href=\"#\" class=\"text-sm text-cyan-500 hover:underline inline-block\">
            Renvoyer le code
        </a>
    </div>

</div>
{% endblock %}
", "pages/verifyEmail.html.twig", "C:\\Users\\Bratan\\javafx-symfony-test\\backend\\templates\\pages\\verifyEmail.html.twig");
    }
}
