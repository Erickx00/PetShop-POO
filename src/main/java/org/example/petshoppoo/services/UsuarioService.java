package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.exceptions.AutenticacaoException;
import org.example.petshoppoo.exceptions.ValidacaoException;
import org.example.petshoppoo.exceptions.UsuarioNaoEncontradoException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.interfaces.IUsuarioRepository;
import org.example.petshoppoo.services.interfaces.IUsuarioService;
import org.example.petshoppoo.utils.SessionManager;
import org.example.petshoppoo.utils.PasswordHasher;

import java.util.Optional;
import java.util.UUID;

public class UsuarioService implements IUsuarioService {
    private final IUsuarioRepository usuarioRepository;

    public UsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void registrar(String nome, String email, String telefone, String senha) throws ValidacaoException, PersistenciaException {
        if (senha == null || senha.length() < 6) {
            throw new ValidacaoException("A senha deve ter pelo menos 6 caracteres!");
        }

        if (nome == null || nome.isBlank()) {
            throw new ValidacaoException("O nome é obrigatório.");
        }
        if (nome.matches(".*\\d.*")) {
            throw new ValidacaoException("Nome não pode conter números.");
        }

        if (telefone == null) {
            throw new ValidacaoException("Telefone é obrigatório.");
        }
        String telLimpo = telefone.replaceAll("\\D", "");
        if (telLimpo.startsWith("0")) telLimpo = telLimpo.substring(1);

        if (telLimpo.length() != 11) {
            throw new ValidacaoException("Telefone inválido. Use DDD (2 dígitos) + número (9 dígitos), por exemplo: 83912345678.");
        }

        if (usuarioRepository.emailExiste(email)) {
            throw new ValidacaoException("E-mail já cadastrado.");
        }

        if (usuarioRepository.telefoneExiste(telefone)) {
            throw new ValidacaoException("Telefone já cadastrado.");
        }

        Usuario novoUsuario = new Usuario(nome, email, telLimpo, PasswordHasher.hash(senha));
        usuarioRepository.salvar(novoUsuario);
    }

    public void atualizarPerfil(UUID usuarioId, String nome, String email, String telefone) throws ValidacaoException, UsuarioNaoEncontradoException, PersistenciaException {
        Optional<Usuario> usuarioOptional = usuarioRepository.buscarPorId(usuarioId);

        if (usuarioOptional.isEmpty()) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOptional.get();

        if (telefone == null) {
            throw new ValidacaoException("Telefone não pode ser nulo.");
        }

        String telLimpo = telefone.replaceAll("\\D", "");

        if (telLimpo.startsWith("0")) {
            telLimpo = telLimpo.substring(1);
        }

        if (telLimpo.length() != 11) {
            throw new ValidacaoException("Telefone inválido. Use DDD (2 dígitos) + número (9 dígitos), por exemplo: 83912345678.");
        }

        if (!usuario.getEmail().equals(email) && usuarioRepository.emailExiste(email)) {
            throw new ValidacaoException("E-mail já cadastrado para outro usuário.");
        }

        if (!usuario.getTelefone().equals(telLimpo) && usuarioRepository.telefoneExiste(telLimpo)) {
            throw new ValidacaoException("Telefone já cadastrado para outro usuário.");
        }

        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTelefone(telLimpo);

        usuarioRepository.atualizar(usuario);
        SessionManager.getInstance().setUsuarioLogado(usuario);
    }

    @Override
    public void alterarSenha(UUID usuarioId, String senhaAtual, String novaSenha) throws ValidacaoException, AutenticacaoException, UsuarioNaoEncontradoException, PersistenciaException {
        Optional<Usuario> usuarioOptional = usuarioRepository.buscarPorId(usuarioId);

        if (usuarioOptional.isEmpty()) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOptional.get();

        // Verifica se a senha atual ta correta
        if (!usuario.verificarSenha(senhaAtual)) {
            throw new AutenticacaoException("Senha atual incorreta.");
        }

        // Verifica se a nova senha é igual à atual
        if (usuario.verificarSenha(novaSenha)) {
            throw new ValidacaoException("A nova senha deve ser diferente da senha atual.");
        }

        if (novaSenha == null || novaSenha.length() < 6) {
            throw new ValidacaoException("A nova senha deve ter pelo menos 6 caracteres.");
        }

        // Atualiza a senha
        usuario.setSenha(PasswordHasher.hash(novaSenha));
        usuarioRepository.atualizar(usuario);

        // Atualiza a sessão com o usuário atualizado
        SessionManager.getInstance().setUsuarioLogado(usuario);
    }

    @Override
    public void excluirPetDoUsuario(UUID idUsuario, UUID idPet) throws PersistenciaException {
        usuarioRepository.excluirPetPorId(idUsuario,idPet);
    }



}
