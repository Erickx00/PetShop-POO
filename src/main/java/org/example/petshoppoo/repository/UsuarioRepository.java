package org.example.petshoppoo.repository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.petshoppoo.model.Login.Usuario;

public class UsuarioRepository {
    private static final String CAMINHO_ARQUIVO = "usuarios.json";
    private final ObjectMapper objectMapper;
    private List<Usuario> usuarios;

    public UsuarioRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        carregarUsuarios();
    }

    private void carregarUsuarios() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            this.usuarios = new ArrayList<>();
            return;
        }
        try {
            this.usuarios = objectMapper.readValue(arquivo, new TypeReference<List<Usuario>>() {
            });
        } catch (IOException e) {
            this.usuarios = new ArrayList<>();
        }
    }

    public void adicionar(Usuario usuario) {
        usuarios.add(usuario);
        salvarNoArquivo();
    }


    private void salvarNoArquivo() {
        try {
            objectMapper.writeValue(new File(CAMINHO_ARQUIVO), usuarios);
        } catch (IOException e) {
            System.err.println("Erro ao salvar!");
        }
    }

}
