package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.model.Login.exceptions.EmailInvalidoException;
import org.example.petshoppoo.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService() throws PersistenciaException {
        this.usuarioRepository = new UsuarioRepository();
    }

    public Usuario cadastrarUsuario(String nome, String email, String senha)
            throws EmailInvalidoException, PersistenciaException {

        if (usuarioRepository.emailExiste(email)) {
            throw new PersistenciaException("Email já cadastrado");
        }

        Usuario novoUsuario = new Usuario(nome, email, senha);
        usuarioRepository.adicionar(novoUsuario);
        return novoUsuario;
    }


    public Usuario buscarUsuarioPorId(UUID id) throws PersistenciaException {
        Usuario usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            throw new PersistenciaException("Usuário não encontrado");
        }
        return usuario;
    }

    public Usuario buscarUsuarioPorEmail(String email) throws PersistenciaException {
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        if (usuario == null) {
            throw new PersistenciaException("Usuário não encontrado");
        }
        return usuario;
    }

    public void atualizarUsuario(Usuario usuarioAtualizado) throws PersistenciaException {
        usuarioRepository.atualizar(usuarioAtualizado);
    }

    public void atualizarPerfilUsuario(UUID idUsuario, String telefone) throws PersistenciaException {
        Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
        if (usuario != null) {
            usuario.setTelefone(telefone);
            usuarioRepository.atualizar(usuario);
        }
    }

    public List<Usuario> listarTodosUsuarios() throws PersistenciaException {
        return usuarioRepository.listarTodos();
    }

    public boolean verificarCredenciais(String email, String senha) throws PersistenciaException {
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        return usuario != null && usuario.getSenha().equals(senha);
    }

    public void alterarSenha(UUID idUsuario, String novaSenha) throws PersistenciaException {
        Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
        if (usuario != null) {
            usuario.setSenha(novaSenha);
            usuarioRepository.atualizar(usuario);
        }
    }

    public int contarUsuarios() throws PersistenciaException {
        return usuarioRepository.listarTodos().size();
    }
}