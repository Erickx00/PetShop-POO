package org.example.petshoppoo.repository.implementations;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.interfaces.IUsuarioRepository;
import org.example.petshoppoo.utils.FilePaths;
import org.example.petshoppoo.utils.JsonFileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioRepository implements IUsuarioRepository {
    private List<Usuario> usuarios;

    public UsuarioRepository() throws PersistenciaException {
        this.usuarios = JsonFileManager.carregar(FilePaths.USUARIOS_JSON, Usuario.class);
    }

    // Refatorado: Substitui o loop for por removeIf + add (mais limpo para listas)
    public void atualizar(Usuario usuarioAtualizado) throws PersistenciaException {
        if (usuarios.removeIf(u -> u.getIdUsuario().equals(usuarioAtualizado.getIdUsuario()))) {
            usuarios.add(usuarioAtualizado);
            salvarUsuarios();
        }
    }

    @Override
    public void deletar(UUID id) {
        usuarios.stream()
                .filter(usuario -> usuario.getIdUsuario().equals(id))
                .findFirst()
                .ifPresent(usuario -> usuarios.remove(usuario));
    }



    @Override
    public void salvar(Usuario usuario) throws PersistenciaException {
        usuarios.add(usuario);
        salvarUsuarios();
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

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarios.stream()
                .filter(u -> u.getIdUsuario().equals(id))
                .findFirst();
    }

    // Refatorado com Optional e Stream para ser mais direto
    @Override
    public void adicionarPetAoUsuario(UUID idUsuario, UUID idPet) {
        buscarPorId(idUsuario)
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

    @Override
    public void excluirPetPorId(UUID idUsuario, UUID idPet) throws PersistenciaException {
        buscarPorId(idUsuario)
                .ifPresent(usuario -> {
                    usuario.removerPet(idPet);
                    // Como o objeto foi alterado na lista, basta salvar
                    try {
                        salvarUsuarios();
                    } catch (PersistenciaException e) {
                        throw new RuntimeException("Erro ao apagar Pet", e);
                    }
                });
    }


    private void salvarUsuarios() throws PersistenciaException {
        JsonFileManager.salvar(FilePaths.USUARIOS_JSON, usuarios);
    }
}