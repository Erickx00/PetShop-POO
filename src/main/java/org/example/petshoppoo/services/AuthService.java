package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.UsuarioRepository;
import org.example.petshoppoo.utils.SessionManager;

import java.util.UUID;

public class AuthService {
    private final UsuarioRepository usuarioRepository;

    public AuthService() throws PersistenciaException {
        this.usuarioRepository = new UsuarioRepository();
    }



    public void login(String email, String senha) throws Exception {
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        if (usuario == null || !usuario.verificarSenha(senha)) {
            throw new Exception("Email ou senha invalidos!");
        }
        SessionManager.getInstance().setUsuarioLogado(usuario);
    }

    public void logout() {
        SessionManager.getInstance().logout();
    }

    /**
     * Verifica se há usuário logado
     */
    public boolean temUsuarioLogado() {
        return SessionManager.getInstance().getUsuarioLogado() != null;
    }
}