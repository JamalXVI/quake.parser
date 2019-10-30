package br.com.jamalxvi.quake.parser.services.game.result;

import br.com.jamalxvi.quake.parser.domain.GameResults;
import br.com.jamalxvi.quake.parser.error.InvalidGameError;
import br.com.jamalxvi.quake.parser.error.NoGameFoundError;
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
                "log.name=game_result_test_1.log"
        }
)
class GameResultServiceTest {
    @Autowired
    LocalSearchLoggerService localSearchLoggerService;

    @Autowired
    GameResultService gameResultService;

    /**
     * Caso de sucesso: Quando o arquivo de log está ok
     */
    @Test
    void parse() {
        final Map<String, Integer> playerKills = new HashMap<>();
        playerKills.put("Zeh", -2);
        playerKills.put("Dono da Bola", -1);
        playerKills.put("Isgalamido", 1);
        playerKills.put("Mocinha", 0);
        final Integer totalKills = 4;
        List<String> log = localSearchLoggerService.getResource();
        Map<String, GameResults> gameResultsMap = gameResultService.parse(log);
        assertTrue("Não deve ser nulo", gameResultsMap != null);
        assertTrue("Não deve estar vazio", !gameResultsMap.isEmpty());
        assertTrue("O tamanho deve ser exatamente 1!", gameResultsMap.size() == 1);
        GameResults resultado = gameResultsMap.get("game_1");
        assertTrue("O número total de mortos deve ser " + totalKills, resultado.getTotalKills().equals(totalKills));
        for (String player : playerKills.keySet()) {
            assertTrue("O jogador deve constar no resultado", resultado.getPlayers().contains(player));
            assertTrue("Ele deve constar no quadro de kills", resultado.getKills().get(player) != null);
            assertTrue("O número de kills deve bater", resultado.getKills().get(player).equals(playerKills.get(player)));
        }
    }

    /**
     * Caso de falha: busca por um jogo com o valor inválido
     */
    @Test
    void parse_InvalidGame_InvalidGameError() {
        List<String> log = localSearchLoggerService.getResource();
        Assertions.assertThrows(InvalidGameError.class, () -> gameResultService.parse(log, -3));
    }
    /**
     * Caso de falha: busca por um jogo inexistente
     */
    @Test
    void parse_GameNotFound_NoGameFoundError() {
        List<String> log = localSearchLoggerService.getResource();
        Assertions.assertThrows(NoGameFoundError.class, () -> gameResultService.parse(log, 999));
    }
    /**
     * Caso de sucesso: busca por um jogo específico
     */
    @Test
    void parse_EspecificGame_Sucess() {
        final Map<String, Integer> playerKills = new HashMap<>();
        playerKills.put("Zeh", -2);
        playerKills.put("Dono da Bola", -1);
        playerKills.put("Isgalamido", 1);
        playerKills.put("Mocinha", 0);
        final Integer totalKills = 4;
        List<String> log = localSearchLoggerService.getResource();
        Map<String, GameResults> gameResultsMap = gameResultService.parse(log, 1);
        assertTrue("Não deve ser nulo", gameResultsMap != null);
        assertTrue("Não deve estar vazio", !gameResultsMap.isEmpty());
        assertTrue("O tamanho deve ser exatamente 1!", gameResultsMap.size() == 1);
        GameResults resultado = gameResultsMap.get("game_1");
        assertTrue("O número total de mortos deve ser " + totalKills, resultado.getTotalKills().equals(totalKills));
        for (String player : playerKills.keySet()) {
            assertTrue("O jogador deve constar no resultado", resultado.getPlayers().contains(player));
            assertTrue("Ele deve constar no quadro de kills", resultado.getKills().get(player) != null);
            assertTrue("O número de kills deve bater", resultado.getKills().get(player).equals(playerKills.get(player)));
        }
    }
}