package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.model.Login.exceptions.EmailInvalidoException;
import org.example.petshoppoo.repository.UsuarioRepository;
import java.util.List;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService() throws PersistenciaException {
        this.usuarioRepository = new UsuarioRepository();
    }

    public Usuario cadastrarUsuario(String nome, String email, String telefone, String senha)
            throws EmailInvalidoException, PersistenciaException {
        if (usuarioRepository.emailExiste(email)) {
            throw new PersistenciaException("Email ja cadastrado");
        }
        Usuario novoUsuario = new Usuario(nome, email, telefone, senha);
        usuarioRepository.adicionar(novoUsuario);
        return novoUsuario;
    }



    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.listarTodos();
    }
}