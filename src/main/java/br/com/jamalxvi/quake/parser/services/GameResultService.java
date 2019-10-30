package br.com.jamalxvi.quake.parser.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.jamalxvi.quake.parser.config.Translator;
import br.com.jamalxvi.quake.parser.domain.GameResults;
import br.com.jamalxvi.quake.parser.dto.GameResultStatusHandlerDto;
import br.com.jamalxvi.quake.parser.error.InvalidGameError;
import br.com.jamalxvi.quake.parser.error.NoGameFoundError;
import br.com.jamalxvi.quake.parser.error.PlayerKilledError;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GameResultService {
  // Texto que vai indicar quando um jogo começa
  @Value("${file.patterns.init}")
  private String initPattern;
  // Texto que vai indicar quando um jogo termina
  @Value("${file.patterns.end}")
  private String endPattern;
  // Texto que vai indicar quando um jogador tenha morrido
  @Value("${file.patterns.kill}")
  private String killPattern;
  // Texto que vai indicar o separador de quem matou para quem morreu
  @Value("${file.patterns.killed}")
  private String killedPattern;
  // Texto que vai indica quando o assasino é o mundo
  @Value("${file.patterns.world}")
  private String worldPattern;
  // Texto que vai indicar qual é o padrão do nome do jogo
  @Value("${game.name}")
  private String gameName;


  @Autowired
  private ScorePlayerService scorePlayerService;

  /**
   * Transforma o log do jogo nos resultados do jogo
   * 
   * @param lines as linhas do log
   * @param game o número do jogo, se não for definido, então busca todos os jogos
   * @return o resultado dos jogos
   * @see GameResults a entidade que define o resultado do jogo
   */
  public Map<String, GameResults> parse(List<String> lines, int game) {
    LocalDateTime inicio = LocalDateTime.now();
    log.debug("Inicializa processo de leitura do Log: {}", inicio);
    final TreeMap<String, GameResults> results = new TreeMap();
    GameResultStatusHandlerDto gameStatusHandler = new GameResultStatusHandlerDto(gameName, game);
    if (gameStatusHandler.isGameNotValid()) {
      log.error("Erro durante a busca de um jogo específico, número do jogo Inválido: {}", game);
      throw new InvalidGameError(Translator.toLocale("error.invalid.game"));
    }
    log.trace("Percorre as linhas do log: {}", lines);
    for (String line : lines) {
      log.trace("Linha atual: {}, Jogo: {}", line, gameStatusHandler.getGameNumber());
      if (line.contains(initPattern)) {
        log.debug("Inicia novo jogo: {}, Jogo: {}", line, gameStatusHandler.getGameNumber());
        makeNewGame(results, gameStatusHandler);
      } else if (line.contains(endPattern)) {
        log.debug("Inicia finaliza o jogo: {}, Jogo: {}", line, gameStatusHandler.getGameNumber());
        endGame(results, gameStatusHandler);
      } else if (line.contains(killPattern)) {
        log.debug("Faz a pontuação do jogador: {}, Jogo: {}", line, game);
        scorePlayer(gameStatusHandler, line);
      }
    }
    log.info("Todos os jogos finalizados, verifica se o ultimo jogo não foi terminado: {}",
        gameStatusHandler.getGame());
    endGame(results, gameStatusHandler);
    LocalDateTime fim = LocalDateTime.now();
    log.info("parse - Finaliza processo de leitura do Log: {}, Duração: {}", fim,
        Duration.between(inicio, fim));
    if (results.isEmpty()) {
      log.error("Nenhum jogo encontrado: game: {}, results: {}", game, results);
      throw new NoGameFoundError(Translator.toLocale("error.game.not.found"));
    }
    log.trace("Retorna todos os jogos: {}", results);
    return results;
  }

  /**
   * Verifica o player que matou e foi morto e faz a pontuação dele. Se o player que matou for o
   * mundo, diminui em 1 a pontuação do player que morreu.
   * 
   * @param gameStatusHandler
   * @param line
   */
  private void scorePlayer(GameResultStatusHandlerDto gameStatusHandler, String line) {
    Pattern killStats = Pattern.compile(killedPattern);
    Matcher matcher = killStats.matcher(line);
    if (matcher.find()) {
      final String killer = matcher.group(1);

      final String killed = matcher.group(2);
      if (!killer.contains(worldPattern)) {
        scorePlayerService.scorePlayer(gameStatusHandler, killer, 1);
        scorePlayerService.scorePlayer(gameStatusHandler, killed, 0);
      } else {
        scorePlayerService.scorePlayer(gameStatusHandler, killed, -1);
      }

    } else {
      log.error("Foi encontrado uma morte mas sem personagens mortos no jogo: {}, Linha: {}",
          gameStatusHandler.getGameNumber(), line);
      throw new PlayerKilledError(Translator.toLocale("error.get.player.death"));
    }
  }

  /**
   * Cria um novo jogo
   * 
   * @param results lista de jogos
   * @param gameStatusHandler o estado do jogo atual
   */
  private void makeNewGame(TreeMap<String, GameResults> results,
      GameResultStatusHandlerDto gameStatusHandler) {
    if (!gameStatusHandler.isGameEnded() && !gameStatusHandler.isTheFirstGame()) {
      log.warn("O jogo não foi encerrado corretamente, forçando a encerrar o jogo. Jogo: {}",
          gameStatusHandler.getGameNumber());
      endGame(results, gameStatusHandler);
    }
    gameStatusHandler.newGame();
  }

  /**
   * Finaliza o jogo
   * 
   * @param results lista de jogos
   * @param gameResultStatus o estado do jogo atual
   */
  private void endGame(TreeMap<String, GameResults> results,
      GameResultStatusHandlerDto gameResultStatus) {
    if (gameResultStatus.isAValidGameAndIsInTheCurrentGame()) {
      if (gameResultStatus.doesTheGameExists()) {
        results.put(gameResultStatus.getCurrentGame(), gameResultStatus.getGame());
      }
      gameResultStatus.nextGame();
    } else {
      log.warn("Não foi possível finalizar o jogo atual. Jogo: {}",
          gameResultStatus.getGameNumber());
    }
  }

  /**
   * Transforma o log do jogo nos resultados do jogo, nesta sobrecarga serão considerados todos os
   * jogos
   * 
   * @param lines as linhas do log
   * @return o resultado dos jogos
   * @see GameResults a entidade que define o resultado do jogo
   * @see GameResultStatusHandlerDto para verificar a constante que representa todos os jogos
   */
  public Map<String, GameResults> parse(List<String> lines) {
    return parse(lines, GameResultStatusHandlerDto.ALL_GAMES);
  }
}
