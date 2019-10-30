package br.com.jamalxvi.quake.parser.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.jamalxvi.quake.parser.domain.GameResults;
import br.com.jamalxvi.quake.parser.error.InvalidParameterError;
import br.com.jamalxvi.quake.parser.services.GameResultService;
import br.com.jamalxvi.quake.parser.services.LocalSearchLoggerService;

@SpringBootTest
class QuakeLogReaderControllerTest {

  @Spy
  QuakeLogReaderController quakeLogReaderController;

  @Mock
  GameResultService gameResultService = new GameResultService();

  @Mock
  LocalSearchLoggerService localSearchLoggerService = new LocalSearchLoggerService();

  @BeforeEach
  public void before() {
    quakeLogReaderController =
        Mockito.spy(new QuakeLogReaderController(localSearchLoggerService, gameResultService));
    when(localSearchLoggerService.getResource()).thenReturn(new ArrayList<>());
    when(gameResultService.parse(any(List.class))).thenReturn(new HashMap());
    when(gameResultService.parse(any(List.class), anyInt())).thenReturn(new HashMap());
  }

  /**
   * Teste de Sucesso: Parâmetro válido
   */
  @Test
  void readFromLocalFileById_AllValid_Sucess() {
    Map<String, GameResults> gameResultsMap = quakeLogReaderController.readFromLocalFileById(2);
    assertTrue("Não deve ser nulo o resultado", gameResultsMap != null);
  }

  /**
   * Teste de Falha: Parâmetro Equivalente a todos os jogos
   */
  @Test
  void readFromLocalFileById_ZeroGame_InvalidParameterError() {
    Assertions.assertThrows(InvalidParameterError.class,
        () -> quakeLogReaderController.readFromLocalFileById(0));
  }

  /**
   * Teste de Falha: Parâmetro Nulo
   */
  @Test
  void readFromLocalFileById_NullGame_InvalidParameterError() {
    Assertions.assertThrows(InvalidParameterError.class,
        () -> quakeLogReaderController.readFromLocalFileById(null));
  }


  /**
   * Teste de Falha: Parâmetro Negativo
   */
  @Test
  void readFromLocalFileById_NegativeGame_InvalidParameterError() {
    Assertions.assertThrows(InvalidParameterError.class,
        () -> quakeLogReaderController.readFromLocalFileById(null));
  }
}
