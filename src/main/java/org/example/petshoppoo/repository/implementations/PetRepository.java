package org.example.petshoppoo.repository.implementations;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.repository.interfaces.IPetRepository;
import org.example.petshoppoo.utils.FilePaths;
import org.example.petshoppoo.utils.JsonFileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class PetRepository implements IPetRepository {
    private List<Pet> pets;

    public PetRepository() throws PersistenciaException {
        //  Carrega dados do arquivo
        this.pets = JsonFileManager.carregar(FilePaths.PETS_JSON, Pet.class);
    }

    @Override
    public List<Pet> listarTodos() {

        if (pets == null) {
            pets = new ArrayList<>();
        }
        return new ArrayList<>(pets); // Retorna cópia
    }

    @Override
    public void salvar(Pet pet) throws PersistenciaException {
        List<Pet> listaAtual = listarTodos();

        // Remove se já existir
        listaAtual.removeIf(p -> p.getIdPet() != null && p.getIdPet().equals(pet.getIdPet()));

        // Adiciona novo
        listaAtual.add(pet);

        // Atualiza a lista interna
        this.pets = listaAtual;

        // Salva no arquivo
        salvarNoArquivo();
    }

    @Override
    public Optional<Pet> buscarPorId(UUID id) {
        return listarTodos().stream()
                .filter(p -> p.getIdPet() != null && p.getIdPet().equals(id))
                .findFirst();
    }

    @Override
    public List<Pet> buscarPetsPorUsuario(UUID idUsuario) {
        return listarTodos().stream()
                .filter(pet -> pet.getIdUsuario() != null && pet.getIdUsuario().equals(idUsuario))
                .collect(Collectors.toList());
    }



    @Override
    public void atualizar(Pet petAtualizado) throws PersistenciaException {
        List<Pet> listaAtual = listarTodos();

        if (listaAtual.stream().noneMatch(p -> p.getIdPet() != null && p.getIdPet().equals(petAtualizado.getIdPet()))) {
            throw new PersistenciaException("Pet não encontrado para atualizar");
        }

        this.pets = listaAtual.stream()
                .map(p -> p.getIdPet() != null && p.getIdPet().equals(petAtualizado.getIdPet()) ? petAtualizado : p)
                .collect(Collectors.toList());
        salvarNoArquivo();
    }

    @Override
    public void deletar(UUID id) throws PersistenciaException {
        List<Pet> listaAtual = listarTodos();
        listaAtual.removeIf(p -> p.getIdPet() != null && p.getIdPet().equals(id));
        this.pets = listaAtual;
        salvarNoArquivo();
    }

    private void salvarNoArquivo() throws PersistenciaException {
        if (pets == null) {
            pets = new ArrayList<>();
        }
        JsonFileManager.salvar(FilePaths.PETS_JSON, pets);
    }
}
