package br.com.jamalxvi.quake.parser.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Erro que define quando não for encontrado um recurso (um arquivo de log)
 *
 * @author jamalxvi
 * @version 0.1
 * @since 0.1
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundError(String message) {
        super(message);
    }
}
