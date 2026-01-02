package org.example.petshoppoo.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.petshoppoo.exceptions.PersistenciaException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonFileManager {
    private static final ObjectMapper objectMapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    public static <T> List<T> carregar(String caminhoArquivo, Class<T> tipo) throws PersistenciaException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(arquivo,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, tipo));
        } catch (IOException e) {
            System.err.println("Erro ao carregar arquivo " + caminhoArquivo + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static <T> void salvar(String caminhoArquivo, List<T> dados) throws PersistenciaException {
        try {
            File arquivo = new File(caminhoArquivo);

            objectMapper.writeValue(arquivo, dados);
            System.out.println("Arquivo salvo com sucesso: " + caminhoArquivo);
        } catch (IOException e) {
            throw new PersistenciaException("Erro ao salvar arquivo " + caminhoArquivo + ": " + e.getMessage(), e);
        }
    }

    public static <T> void salvar(String caminhoArquivo, T objeto) throws PersistenciaException {
        try {
            File arquivo = new File(caminhoArquivo);

            objectMapper.writeValue(arquivo, objeto);
        } catch (IOException e) {
            throw new PersistenciaException("Erro ao salvar arquivo " + caminhoArquivo + ": " + e.getMessage(), e);
        }
    }
}