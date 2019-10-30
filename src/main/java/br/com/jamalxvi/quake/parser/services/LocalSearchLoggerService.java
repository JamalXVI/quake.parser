package br.com.jamalxvi.quake.parser.services;

import br.com.jamalxvi.quake.parser.error.ResourceNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Busca o Arquivo contendo os logs nos recursos locais e o transforma em linhas legíveis.
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
     * Executa a busca e a transformação em linhas do arquivo de log nos arquivos locais com base nas propriedades
     * definidas em application.properties
     *
     * @return o arquivo de log
     * @throws ResourceNotFound: Erro do Tipo 404 caso não encontrar o arquivo de log
     */
    public List<String> getResource() {
        try {
            final String path = new StringBuilder().append(baseUri).append(logName).toString();
            log.debug("getResource - Tenta buscar arquivo na rota: {}", path);
            final Resource resource = new ClassPathResource(path);
            log.trace("getResource - Pega recurso: {}", resource);
            final File file = resource.getFile();
            log.info("getResource - Arquivo Encontrado: {}", file.getPath());
            final List<String> lines = Files.lines(file.toPath()).collect(Collectors.toList());
            log.trace("getResource - Arquivo convertido em linhas: {}", lines);
            return lines;
        } catch (IOException e) {
            log.error("Error while loading file: {}", e);
            throw new ResourceNotFound("Recurso não encontrado!");
        }
    }
}
