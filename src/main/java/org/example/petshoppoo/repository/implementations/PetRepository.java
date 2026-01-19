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

    public PetRepository() {
        // ✅ CORRETO: Carrega dados do arquivo
        this.pets = carregarDoArquivo();
    }

    private List<Pet> carregarDoArquivo() {
        try {
            List<Pet> carregados = JsonFileManager.carregar(FilePaths.PETS_JSON, Pet.class);
            return carregados != null ? carregados : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Erro ao carregar pets (inicializando lista vazia): " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Pet> listarTodos() {
        // ✅ SEGURO: Nunca retorna null
        if (pets == null) {
            pets = new ArrayList<>();
        }
        return new ArrayList<>(pets); // Retorna cópia
    }

    @Override
    public void salvar(Pet pet) throws PersistenciaException {
        List<Pet> listaAtual = listarTodos();

        // Remove se já existir (para atualização)
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

        List<Pet> petsAtualizados = listaAtual.stream()
                .map(p -> p.getIdPet() != null && p.getIdPet().equals(petAtualizado.getIdPet()) ? petAtualizado : p)
                .collect(Collectors.toList());

        this.pets = petsAtualizados;
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
        // ✅ CORRETO: Usa o campo 'pets' que sempre existe
        if (pets == null) {
            pets = new ArrayList<>();
        }
        JsonFileManager.salvar(FilePaths.PETS_JSON, pets);
    }
}