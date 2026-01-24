package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.interfaces.IUsuarioRepository;
import org.example.petshoppoo.services.interfaces.IUsuarioService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.SessionManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioService implements IUsuarioService {
    private final IUsuarioRepository usuarioRepository;

    public UsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void registrar(String nome, String email, String telefone, String senha) throws Exception {
        if (senha == null || senha.length() < 6) {
            throw new Exception("A senha deve ter pelo menos 6 caracteres!");
        }

        if (nome.matches(".*\\d.*")) {
            throw new Exception("Nome nao pode conter numeros");
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
        usuarioRepository.salvar(novoUsuario);
    }

    public void atualizarPerfil(UUID usuarioId, String nome, String email, String telefone) throws Exception {
        Optional<Usuario> usuarioOptional = usuarioRepository.buscarPorId(usuarioId);

        if (usuarioOptional.isEmpty()) {
            throw new PersistenciaException("Usuário não encontrado!");
        }

        Usuario usuario = usuarioOptional.get();

        if (telefone == null) {
            throw new Exception("Telefone não pode ser nulo!");
        }

        String telLimpo = telefone.replaceAll("\\D", "");

        if (telLimpo.startsWith("0")) {
            telLimpo = telLimpo.substring(1);
        }

        if (telLimpo.length() != 11) {
            throw new Exception("Telefone inválido! Use DDD(2) + Número(9). Ex: 83912345678");
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

    @Override
    public void alterarSenha(UUID usuarioId, String senhaAtual, String novaSenha) throws Exception {
        Optional<Usuario> usuarioOptional = usuarioRepository.buscarPorId(usuarioId);

        if (usuarioOptional.isEmpty()) {
            throw new PersistenciaException("Usuário não encontrado!");
        }

        Usuario usuario = usuarioOptional.get();

        // Verifica se a senha atual ta correta
        if (!usuario.getSenha().equals(senhaAtual)) {
            throw new Exception("Senha atual incorreta!");
        }

        // Verifica se a nova senha é igual à atual
        if (usuario.getSenha().equals(novaSenha)) {
            throw new Exception("A nova senha deve ser diferente da senha atual!");
        }

        // Atualiza a senha
        usuario.setSenha(novaSenha);
        usuarioRepository.atualizar(usuario);

        // Atualiza a sessão com o usuário atualizado
        SessionManager.getInstance().setUsuarioLogado(usuario);
    }

    @Override
    public void excluirPetDoUsuario(UUID idUsuario, UUID idPet) throws PersistenciaException {
        usuarioRepository.excluirPetPorId(idUsuario,idPet);
    }



}