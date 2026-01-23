package org.example.petshoppoo.services.interfaces;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;

import java.util.List;
import java.util.UUID;

public interface IUsuarioService {
    void registrar(String nome, String email, String telefone, String senha) throws Exception;

    void atualizarPerfil(UUID usuarioId, String nome, String email, String telefone) throws Exception;

    void alterarSenha(UUID usuarioId, String senhaAtual, String novaSenha) throws Exception;
    void excluirPetPorId(UUID idUsuario, UUID idPet) throws PersistenciaException;

}