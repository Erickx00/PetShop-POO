package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.UsuarioRepository;
import org.example.petshoppoo.utils.SessionManager;

import java.util.Optional;

public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public AuthService() throws PersistenciaException {
        this.usuarioRepository = new UsuarioRepository();
    }

    //  REGISTRO
    public void registrar(String nome, String email, String senha) throws Exception {

        if (email == null || !email.endsWith("@gmail.com")) {
            throw new Exception("O email deve terminar com @gmail.com");
        }

        if (senha == null || senha.length() < 6) {
            throw new Exception("A senha deve ter pelo menos 6 caracteres");
        }

        if (usuarioRepository.emailExiste(email)) {
            throw new PersistenciaException("Email já cadastrado");
        }

        Usuario novoUsuario = new Usuario(nome, email, senha);
        usuarioRepository.adicionar(novoUsuario);
    }

    //  LOGIN
    public void login(String email, String senha) throws Exception {

        Optional<Usuario> usuarioOpt =
                Optional.ofNullable(usuarioRepository.buscarPorEmail(email));

        if (usuarioOpt.isEmpty()) {
            throw new Exception("Usuário não encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.verificarSenha(senha)) {
            throw new Exception("Senha incorreta");
        }

        //  Salva sessão
        SessionManager.getInstance().setUsuarioLogado(usuario);
        System.out.println("Login efetuado: " + usuario.getNome());
    }

    //  LOGOUT
    public void logout() {
        SessionManager.getInstance().logout();
        System.out.println("Usuário deslogado");
    }
}
