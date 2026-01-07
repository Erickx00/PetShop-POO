package org.example.petshoppoo.repository;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.utils.FilePaths;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class PetRepository {
    private final String caminhoArquivo = FilePaths.PETS_JSON;

    public PetRepository() {
        // O JsonFileManager já trata a existência do arquivo no método carregar
    }

    public List<Pet> listarTodos() {
        try {
            List<Pet> lista = JsonFileManager.carregar(caminhoArquivo, Pet.class);
            System.out.println("Pets carregados: " + lista.size());
            return lista;
        } catch (PersistenciaException e) {
            System.err.println("ERRO CRÍTICO NA LEITURA: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void salvar(Pet pet) throws PersistenciaException {
        List<Pet> pets = listarTodos();
        pets.removeIf(p -> p.getId() != null && p.getId().equals(pet.getId()));
        pets.add(pet);
        JsonFileManager.salvar(caminhoArquivo, pets);
    }

    public Optional<Pet> buscarPorId(UUID id) {
        return listarTodos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public List<Pet> buscarPetsPorUsuario(UUID idUsuario) {
        return listarTodos().stream()
                .filter(pet -> pet.getIdUsuario() != null && pet.getIdUsuario().equals(idUsuario))
                .collect(Collectors.toList());
    }

    public void deletar(Pet pet) throws PersistenciaException {
        List<Pet> pets = listarTodos();
        pets.removeIf(p -> p.getId().equals(pet.getId()));
        JsonFileManager.salvar(caminhoArquivo, pets);
    }

    /**
     * Atualiza um pet existente
     */
    public void atualizar(Pet petAtualizado) throws PersistenciaException {
        List<Pet> pets = listarTodos();

        if (pets.stream().noneMatch(p -> p.getId().equals(petAtualizado.getId()))) {
            throw new PersistenciaException("Pet não encontrado para atualizar");
        }

        List<Pet> petsAtualizados = pets.stream()
                .map(p -> p.getId().equals(petAtualizado.getId()) ? petAtualizado : p)
                .collect(Collectors.toList());

        JsonFileManager.salvar(caminhoArquivo, petsAtualizados);
    }
}