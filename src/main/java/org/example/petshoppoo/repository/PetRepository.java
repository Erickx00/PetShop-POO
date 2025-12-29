package org.example.petshoppoo.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Pet.Cao;
import org.example.petshoppoo.model.Pet.Gato;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.utils.FilePaths;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class PetRepository {
    private List<Pet> pets;
    private final ObjectMapper objectMapper;

    public PetRepository() throws PersistenciaException {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.registerSubtypes(
                new NamedType(Cao.class, "Cao"),
                new NamedType(Gato.class, "Gato")
        );
        carregarPets();
    }

    private void carregarPets() throws PersistenciaException {
        this.pets = JsonFileManager.carregar(FilePaths.PETS_JSON, Pet.class);
    }

    public void adicionar(Pet pet) throws PersistenciaException {
        pets.add(pet);
        salvarPets();
    }

    public void atualizar(Pet petAtualizado) throws PersistenciaException {
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getId().equals(petAtualizado.getId())) {
                pets.set(i, petAtualizado);
                salvarPets();
                return;
            }
        }
    }

    public void remover(UUID id) throws PersistenciaException {
        pets.removeIf(pet -> pet.getId().equals(id));
        salvarPets();
    }

    public Pet buscarPorId(UUID id) {
        return pets.stream()
                .filter(pet -> pet.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Pet> buscarPorDono(UUID idDono) {
        return pets.stream()
                .filter(pet -> pet.getIdDono().equals(idDono))
                .collect(Collectors.toList());
    }

    public List<Pet> buscarPorTipo(String tipo) {
        return pets.stream()
                .filter(pet -> pet.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    public List<Pet> listarTodos() {
        return new ArrayList<>(pets);
    }

    private void salvarPets() throws PersistenciaException {
        JsonFileManager.salvar(FilePaths.PETS_JSON, pets);
    }
}
