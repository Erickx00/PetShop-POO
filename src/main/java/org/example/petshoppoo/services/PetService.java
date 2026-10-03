package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.exceptions.PetNaoEncontradoException;
import org.example.petshoppoo.exceptions.ValidacaoException;
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
                             boolean adestrado, boolean castrado, UUID idUsuario) throws PersistenciaException, ValidacaoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("O nome do pet é obrigatório.");
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
            throw new ValidacaoException("Tipo de pet inválido: " + tipo);
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


    public void excluir(UUID idPet) throws PersistenciaException, PetNaoEncontradoException {
        Pet pet = petRepository.buscarPorId(idPet)
                .orElseThrow(() -> new PetNaoEncontradoException("Pet não encontrado."));

        petRepository.deletar(pet.getIdPet());
    }

    public void atualizar(Pet pet) throws PersistenciaException, ValidacaoException {
        validarPet(pet);
        petRepository.atualizar(pet);
    }

    private void validarPet(Pet pet) throws ValidacaoException {
        if (pet == null) {
            throw new ValidacaoException("Pet não pode ser nulo.");
        }

        if (pet.getNome() == null || pet.getNome().trim().isEmpty()) {
            throw new ValidacaoException("O nome do pet é obrigatório.");
        }

        if(pet.getNome().matches(".*\\d.*")){
            throw new ValidacaoException("Nome não pode conter números.");
        }

        if (pet.getIdadePet() < 1 || pet.getIdadePet() > 25) {
            throw new ValidacaoException("Idade deve estar entre 1 e 25 anos.");
        }


        if(pet.getRaca().matches(".*\\d.*")){
            throw new ValidacaoException("Raça não pode conter números.");
        }

        if (pet.getPeso() <= 0) {
            throw new ValidacaoException("O peso deve ser maior que zero.");
        }

        if(pet.getPeso()>250){
            throw new ValidacaoException("O peso não pode ultrapassar 250 kg.");
        }

        if (pet.idadeFormatada() == null) {
            throw new ValidacaoException("A data de nascimento é obrigatória.");
        }

        if (pet.getIdUsuario() == null) {
            throw new ValidacaoException("O pet deve estar associado a um usuário.");
        }
    }


}
