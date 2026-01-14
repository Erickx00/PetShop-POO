package org.example.petshoppoo.repository;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.utils.FilePaths;
import org.example.petshoppoo.utils.JsonFileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioRepository {
    private List<Usuario> usuarios;

    public UsuarioRepository() throws PersistenciaException {
        this.usuarios = JsonFileManager.carregar(FilePaths.USUARIOS_JSON, Usuario.class);
    }

    public void adicionar(Usuario usuario) throws PersistenciaException {
        usuarios.add(usuario);
        salvarUsuarios();
    }

    // Refatorado: Substitui o loop for por removeIf + add (mais limpo para listas)
    public void atualizar(Usuario usuarioAtualizado) throws PersistenciaException {
        if (usuarios.removeIf(u -> u.getIdUsuario().equals(usuarioAtualizado.getIdUsuario()))) {
            usuarios.add(usuarioAtualizado);
            salvarUsuarios();
        }
    }

    public Usuario buscarPorId(UUID id) {
        return usuarios.stream()
                .filter(u -> u.getIdUsuario().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public boolean telefoneExiste(String telefone) {
        return usuarios.stream().anyMatch(u -> u.getTelefone().equalsIgnoreCase(telefone));
    }

    public boolean emailExiste(String email) {
        return usuarios.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    // Refatorado com Optional e Stream para ser mais direto
    public void adicionarPetAoUsuario(UUID idUsuario, UUID idPet) throws PersistenciaException {
        Optional.ofNullable(buscarPorId(idUsuario))
                .ifPresent(usuario -> {
                    usuario.adicionarPet(idPet);
                    // Como o objeto foi alterado na lista, basta salvar
                    try {
                        salvarUsuarios();
                    } catch (PersistenciaException e) {
                        throw new RuntimeException("Erro ao salvar após adicionar pet", e);
                    }
                });
    }

    public void removerPetDoUsuario(UUID idUsuario, UUID idPet) throws PersistenciaException {
        Optional.ofNullable(buscarPorId(idUsuario))
                .ifPresent(usuario -> {
                    usuario.removerPet(idPet);
                    try {
                        salvarUsuarios();
                    } catch (PersistenciaException e) {
                        throw new RuntimeException("Erro ao salvar após remover pet", e);
                    }
                });
    }

    private void salvarUsuarios() throws PersistenciaException {
        JsonFileManager.salvar(FilePaths.USUARIOS_JSON, usuarios);
    }
}