package org.example.petshoppoo.services.interfaces;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.exceptions.AutenticacaoException;
import org.example.petshoppoo.exceptions.UsuarioNaoEncontradoException;
import org.example.petshoppoo.exceptions.ValidacaoException;

import java.util.UUID;

public interface IUsuarioService {
    void registrar(String nome, String email, String telefone, String senha) throws ValidacaoException, PersistenciaException;

    void atualizarPerfil(UUID usuarioId, String nome, String email, String telefone) throws ValidacaoException, UsuarioNaoEncontradoException, PersistenciaException;

    void alterarSenha(UUID usuarioId, String senhaAtual, String novaSenha) throws ValidacaoException, AutenticacaoException, UsuarioNaoEncontradoException, PersistenciaException;
    void excluirPetDoUsuario(UUID idUsuario, UUID idPet) throws PersistenciaException;

}
