package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.UsuarioRepository;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService() throws PersistenciaException {
        this.usuarioRepository = new UsuarioRepository();
    }

    public Usuario buscarPorEmail(String email) throws PersistenciaException {
        return usuarioRepository.buscarPorEmail(email);
    }

    public void atualizarUsuario(Usuario usuario) throws PersistenciaException {
        // Lógica para atualizar usuário
    }
}
