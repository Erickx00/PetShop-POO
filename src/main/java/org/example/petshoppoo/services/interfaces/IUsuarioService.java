package org.example.petshoppoo.services.interfaces;

import org.example.petshoppoo.exceptions.PersistenciaException;

import java.util.UUID;

public interface IUsuarioService {
    void registrar(String nome, String email, String telefone, String senha) throws Exception;

    void atualizarPerfil(UUID usuarioId, String nome, String email, String telefone) throws Exception;

    void alterarSenha(UUID usuarioId, String senhaAtual, String novaSenha) throws Exception;
    void excluirPetDoUsuario(UUID idUsuario, UUID idPet) throws PersistenciaException;

}