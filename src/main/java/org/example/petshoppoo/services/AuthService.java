package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.implementations.UsuarioRepository;
import org.example.petshoppoo.repository.interfaces.IUsuarioRepository;
import org.example.petshoppoo.services.interfaces.IAuthService;
import org.example.petshoppoo.utils.SessionManager;

public class AuthService implements IAuthService {
    private final IUsuarioRepository usuarioRepository;

    public AuthService(IUsuarioRepository usuarioRepository) throws PersistenciaException {
        this.usuarioRepository = usuarioRepository;
    }



    public void login(String email, String senha) throws Exception {
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        if (usuario == null || !usuario.verificarSenha(senha)) {
            throw new Exception("Email ou senha invalidos!");
        }
        SessionManager.getInstance().setUsuarioLogado(usuario);
    }

    public void logout() {
        SessionManager.getInstance();
        SessionManager.logout();
    }

    /**
     * Verifica se há usuário logado
     */
    public boolean temUsuarioLogado() {
        return SessionManager.getInstance().getUsuarioLogado() != null;
    }
}