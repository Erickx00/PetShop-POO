package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.UsuarioRepository;
import org.example.petshoppoo.utils.SessionManager;

import java.util.List;
import java.util.UUID;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService() throws PersistenciaException {
        this.usuarioRepository = new UsuarioRepository();
    }

    public void registrar(String nome, String email, String telefone, String senha) throws Exception {
        if (senha == null || senha.length() < 6) {
            throw new Exception("A senha deve ter pelo menos 6 caracteres!");
        }

        String telLimpo = telefone.replaceAll("\\D", "");
        if (telLimpo.startsWith("0")) telLimpo = telLimpo.substring(1);

        if (telLimpo.length() != 11) {
            throw new Exception("Telefone invalido! Use DDD(2) + Numeros(9). Ex: 83912345678");
        }

        if (usuarioRepository.emailExiste(email)) {
            throw new PersistenciaException("Email ja cadastrado!");
        }

        if (usuarioRepository.telefoneExiste(telefone)) {
            throw new PersistenciaException("Telefone ja cadastrado");
        }

        Usuario novoUsuario = new Usuario(nome, email, telLimpo, senha);
        usuarioRepository.adicionar(novoUsuario);
    }

    public void atualizarPerfil(UUID usuarioId, String nome, String email, String telefone) throws Exception {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new PersistenciaException("Usuário não encontrado!");
        }

        String telLimpo = telefone.replaceAll("\\D", "");
        if (telLimpo.startsWith("0")) telLimpo = telLimpo.substring(1);

        if (telLimpo.length() != 11) {
            throw new Exception("Telefone invalido! Use DDD(2) + Numeros(9). Ex: 83912345678");
        }

        if (!usuario.getEmail().equals(email) && usuarioRepository.emailExiste(email)) {
            throw new PersistenciaException("Email já cadastrado para outro usuário!");
        }

        if (!usuario.getTelefone().equals(telLimpo) && usuarioRepository.telefoneExiste(telLimpo)) {
            throw new PersistenciaException("Telefone já cadastrado para outro usuário!");
        }

        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTelefone(telLimpo);

        usuarioRepository.atualizar(usuario);
        SessionManager.getInstance().setUsuarioLogado(usuario);
    }

    /**
     * Retorna o nome do usuário logado
     */
    public String obterNomeUsuarioLogado() {
        Usuario usuario = SessionManager.getInstance().getUsuarioLogado();
        return usuario != null ? usuario.getNome() : "";
    }

    /**
     * Retorna o email do usuário logado
     */
    public String obterEmailUsuarioLogado() {
        Usuario usuario = SessionManager.getInstance().getUsuarioLogado();
        return usuario != null ? usuario.getEmail() : "";
    }

    /**
     * Retorna o telefone do usuário logado
     */
    public String obterTelefoneUsuarioLogado() {
        Usuario usuario = SessionManager.getInstance().getUsuarioLogado();
        return usuario != null ? usuario.getTelefone() : "";
    }



    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.listarTodos();
    }
}