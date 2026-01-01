package org.example.petshoppoo.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.utils.FilePaths;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioRepository {
    private final File arquivo = new File("/data/usuarios.json");

    private List<Usuario> usuarios;

    public UsuarioRepository() throws PersistenciaException {
        this.usuarios = JsonFileManager.carregar(FilePaths.USUARIOS_JSON, Usuario.class);
    }

    public void adicionar(Usuario usuario) throws PersistenciaException {
        usuarios.add(usuario);
        salvarUsuarios();
    }

    public void atualizar(Usuario usuarioAtualizado) throws PersistenciaException {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(usuarioAtualizado.getId())) {
                usuarios.set(i, usuarioAtualizado);
                salvarUsuarios();
                return;
            }
        }
    }

    public Usuario buscarPorId(UUID id) {
        return usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public boolean telefoneExiste(String telefone){
        return usuarios.stream()
                .anyMatch(u -> u.getTelefone().equalsIgnoreCase(telefone));
    }

    public boolean emailExiste(String email) {
        return usuarios.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    public void adicionarPetAoUsuario(UUID idUsuario, UUID idPet) throws PersistenciaException {
        Usuario usuario = buscarPorId(idUsuario);
        if (usuario != null) {
            usuario.adicionarPet(idPet);
            atualizar(usuario);
        }
    }

    public void removerPetDoUsuario(UUID idUsuario, UUID idPet) throws PersistenciaException {
        Usuario usuario = buscarPorId(idUsuario);
        if (usuario != null) {
            usuario.removerPet(idPet);
            atualizar(usuario);
        }
    }

    private void salvarUsuarios() throws PersistenciaException {
        JsonFileManager.salvar(FilePaths.USUARIOS_JSON, usuarios);
    }
}