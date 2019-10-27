package br.com.jamalxvi.quake.parser.reader;

import br.com.jamalxvi.quake.parser.error.ResourceNotFound;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import java.io.File;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@TestPropertySource(
        properties = {
                "base.uri=invalid",
                "log.name=notFound"
        }
)
class LocalSearchLoggerServiceFailTest {

    @Autowired
    LocalSearchLoggerService localSearchLoggerService;


    /**
     * Caso de Falha: Recurso não encontrado, espera um erro do tipo {@link ResourceNotFound}
     */
    @Test
    void getResource_InvalidPath_ResourceNotFound() {

        Assertions.assertThrows(ResourceNotFound.class, () ->localSearchLoggerService.getResource());
    }
}