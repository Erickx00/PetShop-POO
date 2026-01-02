package org.example.petshoppoo.repository;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.utils.FilePaths;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PetRepository {
    // Usamos o caminho que você definiu
    private final String caminhoArquivo = FilePaths.PETS_JSON;

    public PetRepository() {
        // O JsonFileManager já trata a existência do arquivo no método carregar
    }

    public List<Pet> listarTodos() {
        try {
            List<Pet> lista = JsonFileManager.carregar(caminhoArquivo, Pet.class);
            System.out.println("Pets carregados: " + lista.size()); // Para debug
            return lista;
        } catch (PersistenciaException e) {
            // Se der erro aqui, ele imprime no console o motivo exato
            System.err.println("ERRO CRÍTICO NA LEITURA: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void salvar(Pet pet) throws PersistenciaException {
        List<Pet> pets = listarTodos();

        // Se o pet já existir, remove o antigo para atualizar (evita duplicata)
        pets.removeIf(p -> p.getId() != null && p.getId().equals(pet.getId()));

        pets.add(pet);

        // SALVA A LISTA TODA
        JsonFileManager.salvar(caminhoArquivo, pets);
    }

    public Optional<Pet> buscarPorId(UUID id) {
        return listarTodos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public void deletar(Pet pet) throws PersistenciaException {
        List<Pet> pets = listarTodos();
        pets.removeIf(p -> p.getId().equals(pet.getId()));
        JsonFileManager.salvar(caminhoArquivo, pets);
    }
}