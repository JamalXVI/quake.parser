package br.com.jamalxvi.quake.parser.config;

import br.com.jamalxvi.quake.parser.error.TranslationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;


/**
 * Classe estática responsável por encontrar a tradução para a mensagem.
 *
 * @author jamalxvi
 * @version 0.1
 * @since 0.1
 */
@Component
@Slf4j
public class Translator {

    private static ResourceBundleMessageSource messageSource;

    @Autowired
    Translator(ResourceBundleMessageSource messageSource) {
        Translator.messageSource = messageSource;
    }

    /**
     * Encontra a tradução
     * @param msgCode o código da mensagem
     * @return a mensagem traduzida
     */
    public static String toLocale(String msgCode) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            return messageSource.getMessage(msgCode, null, locale);
        }catch (Exception e){
            log.error("Erro inesperado ao buscar tradução: {}", e);
            throw new TranslationError("Erro ao encontrar tradução da mensagem!");
        }

    }
}

