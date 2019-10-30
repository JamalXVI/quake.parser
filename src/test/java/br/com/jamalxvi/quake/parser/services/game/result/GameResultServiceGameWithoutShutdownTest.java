package br.com.jamalxvi.quake.parser.services.game.result;

import br.com.jamalxvi.quake.parser.domain.GameResults;
import br.com.jamalxvi.quake.parser.services.GameResultService;
import br.com.jamalxvi.quake.parser.services.LocalSearchLoggerService;
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
                "log.name=game_result_test_2.log"
        }
)
class GameResultServiceGameWithoutShutdownTest {
    @Autowired
    LocalSearchLoggerService localSearchLoggerService;

    @Autowired
    GameResultService gameResultService;

    /**
     * Caso de sucesso: Quando o último jogo está sem o termo shutdown
     */
    @Test
    void parse() {
        final Map<String, Integer> playerKills = new HashMap<>();
        playerKills.put("Isgalamido", -5);
        playerKills.put("Mocinha", 0);
        final Integer totalKills = 11;
        List<String> log = localSearchLoggerService.getResource();
        Map<String, GameResults> gameResultsMap = gameResultService.parse(log);
        assertTrue("Não deve ser nulo", gameResultsMap != null);
        assertTrue("Não deve estar vazio", !gameResultsMap.isEmpty());
        assertTrue("O tamanho deve ser exatamente 2!", gameResultsMap.size() == 2);
        GameResults gameOne = gameResultsMap.get("game_1");
        assertTrue("O Resultado do jogo 1 deve ser 0", gameOne.getTotalKills() == 0);
        assertTrue("Não deve existir jogadores", gameOne.getPlayers().size() == 0);
        GameResults gameTwo = gameResultsMap.get("game_2");
        assertTrue("O Resultado do jogo 2 deve ser "+totalKills, gameTwo.getTotalKills() == totalKills);
        assertTrue("Deve existir jogadores", gameTwo.getPlayers().size() == playerKills.size());
        for (String player : playerKills.keySet()) {
            assertTrue("O jogador deve constar no resultado", gameTwo.getPlayers().contains(player));
            assertTrue("Ele deve constar no quadro de kills", gameTwo.getKills().get(player) != null);
            assertTrue("O número de kills deve bater", gameTwo.getKills().get(player).equals(playerKills.get(player)));
        }

    }
}