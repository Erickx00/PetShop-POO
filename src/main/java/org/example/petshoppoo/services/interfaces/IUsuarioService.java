package org.example.petshoppoo.services.interfaces;

import org.example.petshoppoo.model.Login.Usuario;

import java.util.List;
import java.util.UUID;

public interface IUsuarioService {
    void registrar(String nome, String email, String telefone, String senha) throws Exception;
    void atualizarPerfil(UUID usuarioId, String nome, String email, String telefone) throws Exception;
    String obterNomeUsuarioLogado();
    String obterEmailUsuarioLogado();
    String obterTelefoneUsuarioLogado();
    List<Usuario> listarTodosUsuarios();
}