package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.UsuarioRepository;
import org.example.petshoppoo.utils.SessionManager;
import java.util.Optional;

public class AuthService {
    private UsuarioRepository usuarioRepository;

    public AuthService() throws PersistenciaException {
        this.usuarioRepository = new UsuarioRepository();
    }

    public void registrar(String nome, String email, String senha) throws Exception {
        if (!email.endsWith("@gmail.com")) {
            throw new Exception("O email deve terminar com @gmail.com");
        }

        if (senha.length() < 6) {
            throw new Exception("A senha deve ter pelo menos 6 caracteres.");
        }

        Optional<Usuario> existente = usuarioRepository.buscarPorEmail(email);
        if (existente.isPresent()) {
            throw new Exception("Este email já está cadastrado!");
        }

        Usuario novoUsuario = new Usuario(nome, email, senha);
        usuarioRepository.salvar(novoUsuario);
    }

    public void login(String email, String senha) throws Exception {
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new Exception("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        if (!usuario.verificarSenha(senha)) {
            throw new Exception("Senha incorreta.");
        }

        SessionManager.getInstance().setUsuarioLogado(usuario);
        System.out.println("Login efetuado: " + usuario.getNome());
    }
}