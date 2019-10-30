package br.com.jamalxvi.quake.parser.services;

import br.com.jamalxvi.quake.parser.config.Translator;
import br.com.jamalxvi.quake.parser.error.UnexpectedGameError;
import org.springframework.stereotype.Service;

import br.com.jamalxvi.quake.parser.domain.GameResults;
import br.com.jamalxvi.quake.parser.dto.GameResultStatusHandlerDto;
import br.com.jamalxvi.quake.parser.error.PlayerDefError;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScorePlayerService {

  /**
   * Serviço que faz a pontuação do jogador
   * @param gameStatusHandler o manipulador de estado do jogo
   * @param player o nome do jogador
   * @param score a pontuação que ele fez
   */
  public void scorePlayer(GameResultStatusHandlerDto gameStatusHandler, String player, int score) {
    if (gameStatusHandler == null){
      log.error("Não existe manipulador de jogo: jogador: {}, pontuação: {}", player, score);
      throw new UnexpectedGameError(Translator.toLocale("error.game.unexpected.error"));
    }
    log.debug("Faz pontuação do jogador: {}, pontos: {}, jogo: {}", player, score, gameStatusHandler.getGame());
    if (player == null || player.isEmpty()) {
      log.error("Erro jogador vazio ou não existe: {}", player);
      throw new PlayerDefError("error.get.player.def");
    }
    GameResults game = gameStatusHandler.getGame();
    log.trace("Busca o jogo em memória e faz pontuação dele: {}", game, score);
    game.scorePlayer(player, score);
    if (!game.doesThePlayerExists(player)) {
      log.debug("Jogador não existe no quadro de jogadores: {}", player);
      game.addPlayer(player);
    }
    log.trace("Adiciona no total de mortes a pontuação: {}", score);
    game.addTotalKills(score);
  }
}
