package br.com.jamalxvi.quake.parser.rest;

import br.com.jamalxvi.quake.parser.config.Translator;
import br.com.jamalxvi.quake.parser.domain.GameResults;
import br.com.jamalxvi.quake.parser.dto.GameResultStatusHandlerDto;
import br.com.jamalxvi.quake.parser.error.InvalidParameterError;
import br.com.jamalxvi.quake.parser.services.GameResultService;
import br.com.jamalxvi.quake.parser.services.LocalSearchLoggerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controlador de Funcionalidades de Leitura de Log: Controller que é responsável por retornar a leitura do log
 */
@RestController
@RequestMapping(value = "/api/services", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class QuakeLogReaderController {

    @Autowired
    LocalSearchLoggerService localSearchLoggerService;

    @Autowired
    GameResultService gameResultService;

    /**
     * Faz a leitura de todos os jogos sem filtro
     * @return
     */
    @GetMapping("/read/all")
    public Map<String, GameResults> readFromLocalFile(){
        log.info("Inicia a busca sem filtro de jogo");
        List<String> linhasLog = localSearchLoggerService.getResource();
        return gameResultService.parse(linhasLog);
    }

    /**
     * Busca determinado jogo
     * @param id o número do jogo
     * @return
     */
    @GetMapping("/read/{id}")
    public Map<String, GameResults> readFromLocalFileById(@PathVariable("id") Integer id){
        log.info("Inicia a busca com filtro de jogo: {}", id);
        if (id == null || id < GameResultStatusHandlerDto.FIRST_GAME){
            log.error("O parâmetro id está inválido: {}", id);
            throw new InvalidParameterError(Translator.toLocale("error.invalid.parameter")+"id");
        }
        List<String> linhasLog = localSearchLoggerService.getResource();
        return gameResultService.parse(linhasLog, id);
    }
}
