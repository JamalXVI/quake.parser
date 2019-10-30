package br.com.jamalxvi.quake.parser.reader;

import br.com.jamalxvi.quake.parser.error.ResourceNotFound;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
class LocalSearchLoggerServiceTest {

    @Autowired
    LocalSearchLoggerService localSearchLoggerService;


    /**
     * Caso de Sucesso: Espera encontrar o recurso na pesquisa.
     */
    @Test
    void getResource_AllValid_True() {

        List<String> linhas = localSearchLoggerService.getResource();
        assertTrue("O arquivo não é nulo", linhas != null);
        assertTrue("O arquivo não está vazio", !linhas.isEmpty());
        assertTrue("As linhas estão preenchidas", linhas.size() > 0);
    }
}