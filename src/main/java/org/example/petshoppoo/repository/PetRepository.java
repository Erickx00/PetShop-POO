package org.example.petshoppoo.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.petshoppoo.model.Pet.Cao;
import org.example.petshoppoo.model.Pet.Gato;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.model.Pet.Vacina;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PetRepository {
    private static final String CAMINHO_ARQUIVO = "src/main/resources/data/pets.json";
    private final ObjectMapper objectMapper;
    private List<Pet> pets;

    public PetRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.registerSubtypes(Cao.class, Gato.class);
        carregarPets();
    }

    private void carregarPets() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            this.pets = new ArrayList<>();
            return;
        }
        try {
            this.pets = objectMapper.readValue(arquivo, new TypeReference<List<Pet>>() {});
        } catch (IOException e) {
            this.pets = new ArrayList<>();
        }
    }

    public void adicionar(Pet pet) {
        this.pets.add(pet);
        salvarNoArquivo();
    }

    public void atualizar(Pet petAtualizado) {
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getId().equals(petAtualizado.getId())) {
                pets.set(i, petAtualizado);
                salvarNoArquivo();
                return;
            }
        }
    }

    public void remover(UUID idPet) {
        pets.removeIf(pet -> pet.getId().equals(idPet));
        salvarNoArquivo();
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

    public void adicionarVacina(UUID idPet, Vacina vacina) {
        Pet pet = buscarPorId(idPet);
        if (pet instanceof org.example.petshoppoo.model.Pet.Vacinavel) {
            ((org.example.petshoppoo.model.Pet.Vacinavel) pet).adicionarVacina(vacina);
            atualizar(pet);
        }
    }

    public List<Vacina> getVacinas(UUID idPet) {
        Pet pet = buscarPorId(idPet);
        if (pet instanceof org.example.petshoppoo.model.Pet.Vacinavel) {
            return ((org.example.petshoppoo.model.Pet.Vacinavel) pet).getVacinas();
        }
        return new ArrayList<>();
    }

    private void salvarNoArquivo() {
        try {
            objectMapper.writeValue(new File(CAMINHO_ARQUIVO), pets);
        } catch (IOException e) {
            System.err.println("Erro ao salvar pets!");
            e.printStackTrace();
        }
    }
}