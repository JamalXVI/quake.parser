package br.com.jamalxvi.quake.parser.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Erro que define quando um parâmetro da busca estiver inválido
 *
 * @author jamalxvi
 * @version 0.1
 * @since 0.1
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidParameterError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidParameterError(String message) {
        super(message);
    }
}
