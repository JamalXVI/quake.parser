package br.com.jamalxvi.quake.parser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Cria o componente de tradução de textos e as possíveis linguagens aceitas. A mudança de linguagem é feita através
 * do cabeçalho, cuja a chave é definida em {@link #localeHeader}
 *
 * @author jamalxvi
 * @version 0.1
 * @since 0.1
 */
@Configuration
public class CustomLocaleResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {
    @Value("${locale.header.name}")
    private String localeHeader;

    final static List<Locale> LOCALES = Arrays.asList(new Locale("pt", "br"), Locale.US);


    @Bean
    public LocaleResolver localeResolver() {
        CustomLocaleResolver sessionLocaleResolver = new CustomLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(new Locale("pt", "BR"));
        return sessionLocaleResolver;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("messages");
        resourceBundleMessageSource.setUseCodeAsDefaultMessage(Boolean.TRUE);
        return resourceBundleMessageSource;
    }


    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Optional<String> headerLocale = Optional.ofNullable(request.getHeader(localeHeader));

        return headerLocale.map(locale -> Locale.lookup(Locale.LanguageRange.parse(locale), LOCALES))
                .orElseGet(() -> Locale.getDefault());

    }
}
