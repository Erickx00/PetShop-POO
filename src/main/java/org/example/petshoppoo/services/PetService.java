package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Pet.Cachorro;
import org.example.petshoppoo.model.Pet.Gato;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.repository.implementations.PetRepository;
import org.example.petshoppoo.repository.implementations.UsuarioRepository;
import org.example.petshoppoo.repository.interfaces.IPetRepository;
import org.example.petshoppoo.repository.interfaces.IUsuarioRepository;
import org.example.petshoppoo.services.interfaces.IPetService;
import org.example.petshoppoo.utils.SessionManager;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PetService implements IPetService {
    private final IPetRepository petRepository;
    private final IUsuarioRepository usuarioRepository;

    public PetService(IPetRepository petRepository) throws PersistenciaException {
        this.petRepository = petRepository;
        this.usuarioRepository = new UsuarioRepository();
    }

    public void cadastrarPet(String nome, String tipo, String raca, int idadeAnos, double peso,
                             boolean adestrado, boolean castrado, UUID idUsuario) throws PersistenciaException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new PersistenciaException("O nome do pet é obrigatório.");
        }


        Pet novoPet;
        if ("Cachorro".equalsIgnoreCase(tipo)) {
            novoPet = new Cachorro(
                    null, nome, idadeAnos, raca, peso, idUsuario, adestrado,castrado
            );
        } else if ("Gato".equalsIgnoreCase(tipo)) {
            novoPet = new Gato(
                    null, nome, idadeAnos, raca, peso, idUsuario, adestrado,castrado
            );
        } else {
            throw new PersistenciaException("Tipo de pet inválido: " + tipo);
        }

        validarPet(novoPet);
        petRepository.salvar(novoPet);
        usuarioRepository.adicionarPetAoUsuario(idUsuario, novoPet.getIdPet());
    }

    @Override
    public List<Pet> listarPetsDoUsuario(UUID usuarioId) throws PersistenciaException {
        return petRepository.buscarPetsPorUsuario(usuarioId);
    }

    public List<Pet> listarPets() {
        return petRepository.listarTodos();
    }

    public Pet buscarPorId(UUID id) {
        return petRepository.buscarPorId(id).orElse(null);
    }


    public void excluir(UUID idPet) throws PersistenciaException {
        Pet pet = petRepository.buscarPorId(idPet)
                .orElseThrow(() -> new PersistenciaException("Pet não encontrado."));

        petRepository.deletar(pet.getIdPet());
    }

    public void atualizar(Pet pet) throws PersistenciaException {
        validarPet(pet);
        petRepository.atualizar(pet);
    }

    private void validarPet(Pet pet) throws PersistenciaException {
        if (pet == null) {
            throw new PersistenciaException("Pet não pode ser nulo.");
        }

        if (pet.getNome() == null || pet.getNome().trim().isEmpty()) {
            throw new PersistenciaException("O nome do pet é obrigatório.");
        }

        if(pet.getNome().matches(".*\\d.*")){
            throw new PersistenciaException("Nome nao pode conter numeros");
        }

        if (pet.getIdadePet() < 1 || pet.getIdadePet() > 25) {
            throw new PersistenciaException("Idade tem que estar entre 1 a 25");
        }


        if(pet.getRaca().matches(".*\\d.*")){
            throw new PersistenciaException("Raca nao pode conter numeros");
        }

        if (pet.getPeso() <= 0) {
            throw new PersistenciaException("O peso deve ser maior que zero.");
        }

        if(pet.getPeso()>250){
            throw new PersistenciaException("Peso nao pode passar de 250");
        }

        if (pet.idadeFormatada() == null) {
            throw new PersistenciaException("A data de nascimento é obrigatória.");
        }

        if (pet.getIdUsuario() == null) {
            throw new PersistenciaException("O pet deve estar associado a um usuário.");
        }
    }


}