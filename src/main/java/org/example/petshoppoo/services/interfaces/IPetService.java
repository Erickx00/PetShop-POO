package org.example.petshoppoo.services.interfaces;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Pet.Pet;

import java.util.List;
import java.util.UUID;

public interface IPetService {
    void cadastrarPet(String nome, String tipo, String raca, int idadeAnos, double peso,
                      boolean adestrado, boolean castrado, UUID idUsuario) throws PersistenciaException;
    List<Pet> listarPetsDoUsuario(UUID usuarioId) throws PersistenciaException;
    List<Pet> listarPets();
    Pet buscarPorId(UUID id);
    void excluir(UUID idPet) throws PersistenciaException;
    void atualizar(Pet pet) throws PersistenciaException;
    String formatarIdade(Pet pet);
}