package br.com.jamalxvi.quake.parser.reader;

import br.com.jamalxvi.quake.parser.error.ResourceNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

/**
 * Busca o Arquivo contendo os logs nos recursos locais.
 *
 * @author jamalxvi
 * @version 0.1
 * @since 0.1
 */
@Service
@Slf4j
public class LocalSearchLoggerService {

    @Value("${log.name}")
    private String logName;
    @Value("${base.uri}")
    private String baseUri;

    /**
     * Executa a busca do arquivo de log nos arquivos locais com base nas propriedades definidas em
     * application.properties
     * @return o arquivo de log
     * @throws ResourceNotFound: Erro do Tipo 404 caso não encontrar o arquivo de log
     */
    public File getResource(){
        try {
            final String path = new StringBuilder().append(baseUri).append(logName).toString();
            final Resource resource = new ClassPathResource(path);
            final File file = resource.getFile();
            return file;
        } catch (IOException e) {
            log.error("Error while loading file: {}", e);
            throw new ResourceNotFound("Recurso não encontrado!");
        }
    }
}
