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

    // METODOS PARA CONTINUAR EM ARQUITETURA MVC SEM ACESSAR JSON DIRETO

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

    /**
     * Verifica se há usuário logado
     */
    public boolean temUsuarioLogado() {
        return SessionManager.getInstance().getUsuarioLogado() != null;
    }
}