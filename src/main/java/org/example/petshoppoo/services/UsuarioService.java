package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService() throws PersistenciaException {
        this.usuarioRepository = new UsuarioRepository();
    }

    public Usuario buscarPorEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.buscarPorEmail(email);
        return usuario.orElse(null);
    }

    public void salvarUsuario(Usuario usuario) throws PersistenciaException {
        if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
            throw new PersistenciaException("Nome obrigatório");
        }
        usuarioRepository.salvar(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }
}