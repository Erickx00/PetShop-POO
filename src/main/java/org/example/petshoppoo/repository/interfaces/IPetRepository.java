package org.example.petshoppoo.repository.interfaces;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Pet.Pet;

import java.util.List;
import java.util.UUID;

public interface IPetRepository extends IRepository<Pet> {
    List<Pet> buscarPetsPorUsuario(UUID idUsuario);
    void atualizar(Pet pet) throws PersistenciaException;
}