package br.com.jamalxvi.quake.parser.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Erro que define quando um jogo não for encontrado ou quando não existirem jogos no Log
 *
 * @author jamalxvi
 * @version 0.1
 * @since 0.1
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoGameFoundError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NoGameFoundError(String message) {
        super(message);
    }
}
