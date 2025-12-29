package org.example.petshoppoo.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.petshoppoo.model.Pet.Pet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PetRepository {
    private final File arquivo = new File("src/main/resources/data/pets.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public PetRepository() {
        if (!arquivo.exists()) {
            try {
                if (arquivo.getParentFile().mkdirs()) {
                    arquivo.createNewFile();
                    mapper.writeValue(arquivo, new ArrayList<Pet>());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Pet> listarTodos() {
        try {
            if (!arquivo.exists() || arquivo.length() == 0) return new ArrayList<>();
            return mapper.readValue(arquivo, new TypeReference<List<Pet>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void salvar(Pet pet) {
        List<Pet> pets = listarTodos();
        pets.removeIf(p -> p.getId().equals(pet.getId()));
        pets.add(pet);
        try {
            mapper.writeValue(arquivo, pets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<Pet> buscarPorId(UUID id) {
        return listarTodos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public void deletar(Pet pet) {
        List<Pet> pets = listarTodos();
        pets.removeIf(p -> p.getId().equals(pet.getId()));
        try {
            mapper.writeValue(arquivo, pets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}