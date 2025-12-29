package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Dono.Dono;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.DonoRepository;
import org.example.petshoppoo.repository.UsuarioRepository;

public class AuthService {
    private static Usuario usuarioLogado;
    private static Dono donoLogado;

    private final UsuarioRepository usuarioRepository;
    private final DonoRepository donoRepository;

    public AuthService() throws PersistenciaException {
        this.usuarioRepository = new UsuarioRepository();
        this.donoRepository = new DonoRepository();

        // Verificar diretórios
        org.example.petshoppoo.utils.DebugUtils.verificarDiretorios();
    }

    public boolean login(String email, String senha) throws PersistenciaException {
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            usuarioLogado = usuario;

            // Buscar dono associado
            donoLogado = donoRepository.buscarPorEmail(email);
            if (donoLogado == null) {
                // Criar dono automaticamente se não existir
                donoLogado = criarDonoAutomatico(usuario);
                donoRepository.salvarDono(donoLogado);
            }

            System.out.println("Login bem-sucedido: " + usuario.getNome());
            return true;
        }
        return false;
    }

    public boolean registrar(String nome, String email, String senha) throws Exception {
        if (usuarioRepository.emailExiste(email)) {
            throw new Exception("Email já cadastrado");
        }

        try {
            // Criar usuário
            Usuario novoUsuario = new Usuario(nome, email, senha);
            usuarioRepository.adicionar(novoUsuario);
            usuarioLogado = novoUsuario;

            // Criar dono associado
            Dono novoDono = criarDonoAutomatico(novoUsuario);
            donoRepository.adicionar(novoDono);
            donoLogado = novoDono;

            System.out.println("Registro bem-sucedido para: " + nome);
            return true;

        } catch (Exception e) {
            System.err.println("Erro no registro: " + e.getMessage());
            throw e;
        }
    }

    private Dono criarDonoAutomatico(Usuario usuario) {
        Dono dono = new Dono();
        dono.setId(usuario.getId()); // Usar mesmo ID do usuário
        dono.setNome(usuario.getNome());
        dono.setEmail(usuario.getEmail());
        dono.setTelefone("");

        return dono;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static Dono getDonoLogado() {
        return donoLogado;
    }

    public static void logout() {
        usuarioLogado = null;
        donoLogado = null;
    }
}
