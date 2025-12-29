package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.exceptions.PetNaoEncontradoException;
import org.example.petshoppoo.model.DTO.PetDTO;
import org.example.petshoppoo.model.Pet.Cao;
import org.example.petshoppoo.model.Pet.Gato;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.repository.DonoRepository;
import org.example.petshoppoo.repository.PetRepository;
import org.example.petshoppoo.utils.SessionManager;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PetService {
    private final PetRepository petRepository;
    private final DonoRepository donoRepository;

    public PetService() throws PersistenciaException {
        this.petRepository = new PetRepository();
        this.donoRepository = new DonoRepository();
    }

    public void cadastrarPet(String nome, String tipo, String raca, int idade,
                             double peso, boolean adestrado, boolean castrado)
            throws PersistenciaException {

        // Obter ID do dono da sessão
        UUID idDono = SessionManager.getDonoId();
        if (idDono == null) {
            throw new PersistenciaException("Nenhum dono logado na sessão");
        }

        System.out.println("Cadastrando pet para dono ID: " + idDono);

        LocalDate dataNascimento = LocalDate.now().minusYears(idade);

        Pet pet;
        if ("Cachorro".equalsIgnoreCase(tipo)) {
            Cao cao = new Cao();
            cao.setNome(nome);
            cao.setRaca(raca);
            cao.setDataNascimento(dataNascimento);
            cao.setPeso(peso);
            cao.setIdDono(idDono);
            cao.setAdestrado(adestrado);
            pet = cao;
        } else {
            Gato gato = new Gato();
            gato.setNome(nome);
            gato.setRaca(raca);
            gato.setDataNascimento(dataNascimento);
            gato.setPeso(peso);
            gato.setIdDono(idDono);
            gato.setCastrado(castrado);
            pet = gato;
        }

        petRepository.adicionar(pet);
        System.out.println("Pet cadastrado: " + nome + " para dono: " + idDono);
    }

    public List<PetDTO> listarPetsPorDono(UUID idDono) throws PersistenciaException {
        return petRepository.buscarPorDono(idDono).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public void excluirPet(UUID idPet) throws PersistenciaException, PetNaoEncontradoException {
        Pet pet = petRepository.buscarPorId(idPet);
        if (pet == null) {
            throw new PetNaoEncontradoException("Pet não encontrado");
        }
        petRepository.remover(idPet);
    }

    private PetDTO converterParaDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setNome(pet.getNome());
        dto.setTipo(pet.getTipo());
        dto.setRaca(pet.getRaca());
        dto.setIdade(pet.calcularIdade());
        dto.setPeso(pet.getPeso());
        dto.setIdDono(pet.getIdDono());

        if (pet instanceof Cao) {
            dto.setAdestrado(((Cao) pet).isAdestrado());
        } else if (pet instanceof Gato) {
            dto.setCastrado(((Gato) pet).isCastrado());
        }

        return dto;
    }
}