package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.repository.PetRepository;
import java.util.List;
import java.util.UUID;

public class PetService {
    private PetRepository petRepository;

    public PetService() throws PersistenciaException {
        this.petRepository = new PetRepository();
    }

    public void cadastrarPet(Pet pet) throws PersistenciaException {
        if (pet.getNome() == null || pet.getNome().isEmpty()) {
            throw new PersistenciaException("O nome do pet é obrigatório.");
        }
        petRepository.salvar(pet);
    }

    public List<Pet> listarPets() {
        return petRepository.listarTodos();
    }

    public Pet buscarPorId(UUID id) {
        return petRepository.buscarPorId(id).orElse(null);
    }

    public void deletarPet(Pet pet) throws PersistenciaException {
        petRepository.deletar(pet);
    }
}