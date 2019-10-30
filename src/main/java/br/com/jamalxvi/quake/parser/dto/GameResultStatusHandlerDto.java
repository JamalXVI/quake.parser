package br.com.jamalxvi.quake.parser.dto;

import br.com.jamalxvi.quake.parser.config.Translator;
import br.com.jamalxvi.quake.parser.domain.GameResults;
import br.com.jamalxvi.quake.parser.error.NoGameFoundError;
import br.com.jamalxvi.quake.parser.util.MapUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * Entidade que é responsável por manipular o estado do jogo a ser percorrido
 *
 * @author jamalxvi
 * @version 0.1
 * @since 0.1
 */
@Builder
@AllArgsConstructor
public class GameResultStatusHandlerDto {
  // Número que representa a busca por todos os jogos
  public static final int ALL_GAMES = 0;
  // Número que representa o primeiro jogo
  public static final int FIRST_GAME = 1;
  // Indice do jogo atual
  private Integer index = 1;
  // Se o jogo já finalizou na busca do LOG ou não
  private Boolean gameEnded = Boolean.FALSE;
  // Nome da variável que irá definir o nome do jogo
  private String gameName = "";
  // Jogo a ser pesquisado
  private Integer game = 0;
  private Optional<GameResults> gameResults = Optional.empty();

  public GameResultStatusHandlerDto(String gameName, Integer game) {
    this.gameName = gameName;
    this.game = game;
  }

  /**
   * Faz o Inicio do Próximo jogo
   */
  public void nextGame() {
    index++;
    sortPlayersByKills();
    gameEnded = true;
  }

  /**
   * Ordena os jogadores pelo número de mortes
   */
  private void sortPlayersByKills() {
    getGame().setKills(MapUtil.sortByValue(getGame().getKills()));
  }

  /**
   * Verifica se o jogo terminou
   * 
   * @return Boolean: True se o jogo terminou, false se ele não tiver terminado
   */
  public Boolean isGameEnded() {
    return gameEnded;
  }

  /**
   * Verifica se o jogo atual é o primeiro jogo
   * 
   * @return Boolean: True se sim, False se não
   */
  public Boolean isTheFirstGame() {
    return FIRST_GAME >= index;
  }

  /**
   * Retorna o nome do jogo atual
   * 
   * @return
   */
  public String getCurrentGame() {
    return new StringBuilder().append(gameName).append(index).toString();
  }

  /**
   * Retorna o número do jogo
   * 
   * @return Integer: o número do jogo
   */
  public Integer getGameNumber() {
    return index;
  }

  /**
   * Retorna se o jogo é válido ou não
   * 
   * @return
   */
  public Boolean isGameNotValid() {
    return game != ALL_GAMES && game <= 0;
  }


  /**
   * Verifica se o jogo em questão é o jogo que está no indice atual
   * 
   * @return
   */
  public Boolean isAValidGameAndIsInTheCurrentGame() {
    return !isGameNotInRange() && !isGameEnded();
  }

  public void newGame() {
    gameEnded = Boolean.FALSE;
    gameResults = Optional.of(new GameResults());
  }

  /**
   *
   * @return Verifica se o jogo <b>não</b> está no intervalo desejado
   */
  public boolean isGameNotInRange() {
    return game != ALL_GAMES && index != game;
  }

  /**
   *
   * @return Retorna o jogo atual
   */
  public GameResults getGame() {
    return gameResults
        .orElseThrow(() -> new NoGameFoundError(Translator.toLocale("error.game.not.found")));
  }

  /**
   *
   * @return Verifica se existe algum jogo em memória
   */
  public Boolean doesTheGameExists() {
    return gameResults.isPresent();
  }
}
