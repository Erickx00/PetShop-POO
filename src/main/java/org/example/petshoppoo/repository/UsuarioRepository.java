package org.example.petshoppoo.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.petshoppoo.model.Login.Usuario;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {
    private final File arquivo = new File("src/main/resources/data/usuarios.json");
    private final ObjectMapper mapper;

    public UsuarioRepository() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.registerModule(new JavaTimeModule());

        if (!arquivo.exists()) {
            try {
                File parent = arquivo.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                arquivo.createNewFile();
                mapper.writeValue(arquivo, new ArrayList<Usuario>());
            } catch (IOException e) {
                System.err.println("Erro ao criar arquivo: " + e.getMessage());
            }
        }
    }

    public List<Usuario> listarTodos() {
        try {
            if (!arquivo.exists() || arquivo.length() == 0) return new ArrayList<>();
            return mapper.readValue(arquivo, new TypeReference<List<Usuario>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void salvar(Usuario usuario) {
        List<Usuario> usuarios = listarTodos();
        // Evita duplicados baseado no ID se você atualizar o usuário
        usuarios.removeIf(u -> u.getId().equals(usuario.getId()));
        usuarios.add(usuario);
        try {
            mapper.writeValue(arquivo, usuarios);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return listarTodos().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}