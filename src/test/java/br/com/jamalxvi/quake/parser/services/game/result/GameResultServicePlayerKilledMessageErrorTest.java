package br.com.jamalxvi.quake.parser.services.game.result;

import br.com.jamalxvi.quake.parser.domain.GameResults;
import br.com.jamalxvi.quake.parser.error.PlayerKilledError;
import br.com.jamalxvi.quake.parser.services.GameResultService;
import br.com.jamalxvi.quake.parser.services.LocalSearchLoggerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@TestPropertySource(
        properties = {
                "base.uri=/static/test/",
                "log.name=game_result_test_4.log"
        }
)
class GameResultServicePlayerKilledMessageErrorTest {
    @Autowired
    LocalSearchLoggerService localSearchLoggerService;

    @Autowired
    GameResultService gameResultService;

    /**
     * Caso de sucesso: Quando há um erro no log de morte do personagem
     */
    @Test
    void parse() {
        List<String> log = localSearchLoggerService.getResource();
        Assertions.assertThrows(PlayerKilledError.class, () -> gameResultService.parse(log));


    }
}