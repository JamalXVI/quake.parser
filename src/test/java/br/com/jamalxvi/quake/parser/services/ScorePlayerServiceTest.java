package br.com.jamalxvi.quake.parser.services;

import br.com.jamalxvi.quake.parser.domain.GameResults;
import br.com.jamalxvi.quake.parser.dto.GameResultStatusHandlerDto;
import br.com.jamalxvi.quake.parser.error.NoGameFoundError;
import br.com.jamalxvi.quake.parser.error.PlayerDefError;
import br.com.jamalxvi.quake.parser.error.UnexpectedGameError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
class ScorePlayerServiceTest {
  @Autowired
  ScorePlayerService scorePlayerService;

  /**
   * Verifica o caminho feliz com um jogador
   */
  @Test
  void scorePlayer_AllValid_Sucess() {
    GameResultStatusHandlerDto currentGame =
        GameResultStatusHandlerDto.builder().game(0).gameEnded(false).gameName("game_")
            .gameResults(Optional.of(new GameResults())).index(0).build();
    String jogador = "joão da silva";
    int score = 1;
    scorePlayerService.scorePlayer(currentGame, jogador, score);
    GameResults game = currentGame.getGame();
    assertTrue("Deve existir um único jogador", game.getPlayers().size() == 1);
    assertTrue("O jogador deve ser o " + jogador, game.getPlayers().get(0).equals(jogador));
    assertTrue("Seu número total de mortes deve ser 1", game.getKills().get(jogador) == score);
    assertTrue("O total de mortes no jogo deve ser 1", game.getTotalKills() == score);
  }

  /**
   * Verifica o comportamento do serviço com mais de um jogador e pontuação negativa
   */
  @Test
  void scorePlayer_MaisDeUmJogador_Sucess() {
    GameResultStatusHandlerDto currentGame =
        GameResultStatusHandlerDto.builder().game(0).gameEnded(false).gameName("game_")
            .gameResults(Optional.of(new GameResults())).index(0).build();

    GameResults game = currentGame.getGame();
    String jogador = "joão da silva";
    String jogadorDois = "guilherme";
    game.setPlayers(Arrays.asList(jogador, jogadorDois));
    game.getKills().put(jogadorDois, 2);
    game.getKills().put(jogador, -1);
    game.setTotalKills(3);
    int score = -1;
    scorePlayerService.scorePlayer(currentGame, jogador, score);
    assertTrue("Deve existir dois jogadores", game.getPlayers().size() == 2);
    assertTrue("O jogador deve ser o " + jogador, game.getPlayers().get(0).equals(jogador));
    assertTrue("O outro jogador deve ser o " + jogadorDois,
        game.getPlayers().get(1).equals(jogadorDois));
    assertTrue("O jogador dois matou: ", game.getKills().get(jogadorDois) == 2);
    assertTrue("Seu número total de mortes deve ser 1", game.getKills().get(jogador) == -2);
    assertTrue("O total de mortes no jogo deve ser 1", game.getTotalKills() == 4);
  }


  /**
   * Caso de Falha: Quando não existe jogo em memória
   */
  @Test
  void scorePlayer_SemJogo_NoGameFoundError() {
    GameResultStatusHandlerDto currentGame = GameResultStatusHandlerDto.builder().game(0)
        .gameEnded(false).gameName("game_").gameResults(Optional.empty()).index(0).build();

    Assertions.assertThrows(NoGameFoundError.class,
        () -> scorePlayerService.scorePlayer(currentGame, "joão", 1));
  }

  /**
   * Caso de Falha: Quando não existe manipulador de jogo em memória
   */
  @Test
  void scorePlayer_ManipuladorDeJogo_NoGameFoundError() {

    Assertions.assertThrows(UnexpectedGameError.class,
        () -> scorePlayerService.scorePlayer(null, "joão", 1));
  }

  /**
   * Caso de Falha: Jogador Nulo
   */
  @Test
  void scorePlayer_JogadorNulo_PlayerDefError() {
    GameResultStatusHandlerDto currentGame =
        GameResultStatusHandlerDto.builder().game(0).gameEnded(false).gameName("game_")
            .gameResults(Optional.of(new GameResults())).index(0).build();

    GameResults game = currentGame.getGame();
    String jogador = "joão da silva";
    String jogadorDois = "guilherme";
    game.setPlayers(Arrays.asList(jogador, jogadorDois));
    game.getKills().put(jogadorDois, 2);
    game.getKills().put(jogador, -1);
    game.setTotalKills(3);
    int score = -1;
    Assertions.assertThrows(PlayerDefError.class,
        () -> scorePlayerService.scorePlayer(currentGame, null, score));
  }

    /**
     * Caso de Falha: Jogador Vazio
     */
    @Test
    void scorePlayer_JogadorVazio_PlayerDefError() {
        GameResultStatusHandlerDto currentGame =
                GameResultStatusHandlerDto.builder().game(0).gameEnded(false).gameName("game_")
                        .gameResults(Optional.of(new GameResults())).index(0).build();

        GameResults game = currentGame.getGame();
        String jogador = "joão da silva";
        String jogadorDois = "guilherme";
        game.setPlayers(Arrays.asList(jogador, jogadorDois));
        game.getKills().put(jogadorDois, 2);
        game.getKills().put(jogador, -1);
        game.setTotalKills(3);
        int score = -1;
        Assertions.assertThrows(PlayerDefError.class,
                () -> scorePlayerService.scorePlayer(currentGame, "", score));
    }

}
