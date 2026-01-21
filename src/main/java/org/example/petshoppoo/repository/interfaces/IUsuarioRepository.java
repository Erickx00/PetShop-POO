package org.example.petshoppoo.repository.interfaces;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;

import java.util.UUID;

public interface IUsuarioRepository extends IRepository<Usuario> {
    Usuario buscarPorEmail(String email);
    boolean emailExiste(String email);
    boolean telefoneExiste(String telefone);
    void adicionarPetAoUsuario(UUID idUsuario, UUID idPet) throws PersistenciaException;
}