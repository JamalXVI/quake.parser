package br.com.jamalxvi.quake.parser.domain;

import lombok.Data;
import lombok.ToString;

import java.util.*;

/**
 * Entidade que representa o quadro do jogo, com o número de mortes, mortes por jogador e os jogadores participantes
 *
 * @author jamalxvi
 * @version 0.1
 * @since 0.1
 */
@Data
@ToString
public class GameResults {
    // Pontuação padrão do jogador
    public static final int NO_KILLS = 0;
    // O número total de mortes no jogo
    private Integer totalKills = 0;
    // Lista de jogadores
    private List<String> players = new ArrayList<>();
    // Mapeamento de jogadores matados por jogador
    private Map<String, Integer> kills = new TreeMap<>();

    /**
     * Verifica se o jogador existe no quadro de jogadores
     * @param player o nome do jogador
     * @return se ele existe ou não
     */
    public Boolean doesThePlayerExists(String player){
        return getPlayers().contains(player);
    }

    /**
     * Adiciona um jogador ao quadro de jogadores
     * @param player o nome do jogador
     */
    public void addPlayer(String player){
        this.getPlayers().add(player);
    }

    /**
     * Faz a pontuação do jogador
     * @param player nome do jogador
     * @param score a pontuação que ele fez
     */
    public void scorePlayer(String player, Integer score){
        Integer kills = getPlayerKills(player);
        kills = kills + score;
        getKills().put(player, kills);
    }

    /**
     * Retorna o número de execuções do jogador
     * @param player o nome do jogador
     * @return o número de execuções do jogador, se não achar 0
     */
    public Integer getPlayerKills(String player) {
        return Optional.ofNullable(getKills().get(player)).orElse(NO_KILLS);
    }

    /**
     * Adiciona a pontuação, independente se negativa ou não, ao número total de mortes
     * @param score a ponutuação feita
     */
    public void addTotalKills(Integer score){
        totalKills = totalKills+Math.abs(score);
    }
}
