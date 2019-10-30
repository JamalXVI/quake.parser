package br.com.jamalxvi.quake.parser.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Erro que define quando não for encontrado uma tradução
 *
 * @author jamalxvi
 * @version 0.1
 * @since 0.1
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class TranslationError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TranslationError(String message) {
        super(message);
    }
}
