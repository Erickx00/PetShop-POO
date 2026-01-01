package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.UsuarioRepository;
import org.example.petshoppoo.utils.SessionManager;

public class AuthService {
    private final UsuarioRepository usuarioRepository;

    public AuthService() throws PersistenciaException {
        this.usuarioRepository = new UsuarioRepository();
    }

    public void registrar(String nome, String email, String telefone, String senha) throws Exception {
        if (senha == null || senha.length() < 6) {
            throw new Exception("A senha deve ter pelo menos 6 caracteres!");
        }

        String telLimpo = telefone.replaceAll("\\D", "");
        if (telLimpo.startsWith("0")) {
            telLimpo = telLimpo.substring(1);
        }

        if (telLimpo.length() != 11) {
            throw new Exception("Telefone invalido! Use DDD(2) + Numero(9). Ex: 83912345678");
        }

        if (usuarioRepository.emailExiste(email)) {
            throw new PersistenciaException("Email ja cadastrado!");
        }

        Usuario novoUsuario = new Usuario(nome, email, telLimpo, senha);
        usuarioRepository.adicionar(novoUsuario);
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
}