package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.repository.PetRepository;
import org.example.petshoppoo.repository.UsuarioRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PetService {
    private PetRepository petRepository;
    private UsuarioRepository usuarioRepository;

    public PetService() throws PersistenciaException {
        this.petRepository = new PetRepository();
        this.usuarioRepository = new UsuarioRepository();
    }

    public void cadastrarPet(String nome, String tipo, String raca, int idadeAnos, double peso,
                             boolean adestrado, boolean castrado, UUID idUsuario) throws PersistenciaException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new PersistenciaException("O nome do pet é obrigatório.");
        }

        LocalDate dataNasc = LocalDate.now().minusYears(idadeAnos);

        Pet novoPet;
        if ("Cachorro".equalsIgnoreCase(tipo)) {
            novoPet = new org.example.petshoppoo.model.Pet.Cachorro(
                    null, nome, dataNasc, raca, peso, idUsuario, adestrado
            );
        } else if ("Gato".equalsIgnoreCase(tipo)) {
            novoPet = new org.example.petshoppoo.model.Pet.Gato(
                    null, nome, dataNasc, raca, peso, idUsuario, castrado
            );
        } else {
            throw new PersistenciaException("Tipo de pet inválido: " + tipo);
        }

        petRepository.salvar(novoPet);
        usuarioRepository.adicionarPetAoUsuario(idUsuario, novoPet.getId());
    }

    public List<Pet> listarPetsDoUsuario(UUID usuarioId) throws PersistenciaException {
        List<Pet> resultado = new ArrayList<>();
        for (Pet pet : listarPets()) {
            if (pet.getIdUsuario() != null && pet.getIdUsuario().equals(usuarioId)) {
                resultado.add(pet);
            }
        }
        return resultado;
    }

    public List<Pet> listarPets() {
        return petRepository.listarTodos();
    }

    public Pet buscarPorId(UUID id) {
        return petRepository.buscarPorId(id).orElse(null);
    }

    public void deletarPet(Object pet) throws PersistenciaException {
        if (pet instanceof Pet) {
            petRepository.deletar((Pet) pet);
        } else {
            throw new PersistenciaException("Objeto inválido para deletar");
        }
    }

    // ===== MÉTODOS PARA CONTROLLER BURRO =====

    public List<Object> listarPetsDoUsuarioComoObjetos(UUID usuarioId) throws PersistenciaException {
        return new ArrayList<>(listarPetsDoUsuario(usuarioId));
    }

    public String obterDescricaoCompleta(Object pet) {
        return pet instanceof Pet ? ((Pet) pet).getNome() + " (" + ((Pet) pet).getRaca() + ")" : "";
    }

    public String obterNome(Object pet) {
        return pet instanceof Pet ? ((Pet) pet).getNome() : "";
    }

    public UUID obterId(Object pet) {
        return pet instanceof Pet ? ((Pet) pet).getId() : null;
    }

    public double obterPeso(Object pet) {
        return pet instanceof Pet ? ((Pet) pet).getPeso() : 0.0;
    }

    // ===== MÉTODOS PARA TABELA (PetListaController) =====

    /**
     * Lista pets para exibição em tabela
     */
    public List<Object> listarPetsParaTabela(UUID usuarioId) throws PersistenciaException {
        return new ArrayList<>(listarPetsDoUsuario(usuarioId));
    }

    /**
     * Obtém o tipo/espécie do pet (Cachorro/Gato)
     */
    public String obterTipo(Object pet) {
        if (pet instanceof org.example.petshoppoo.model.Pet.Cachorro) {
            return "Cachorro";
        } else if (pet instanceof org.example.petshoppoo.model.Pet.Gato) {
            return "Gato";
        }
        return "";
    }

    /**
     * Obtém a raça do pet
     */
    public String obterRaca(Object pet) {
        return pet instanceof Pet ? ((Pet) pet).getRaca() : "";
    }

    /**
     * Calcula e retorna a idade em anos baseado na data de nascimento
     */
    public String obterIdade(Object pet) {
        if (pet instanceof Pet) {
            LocalDate dataNasc = ((Pet) pet).getDataNascimento();
            if (dataNasc != null) {
                int anos = Period.between(dataNasc, LocalDate.now()).getYears();
                return anos + " ano(s)";
            }
        }
        return "N/A";
    }

    public void atualizarPet(Object pet, String nome, String raca, double peso) throws PersistenciaException {
        if (!(pet instanceof Pet)) {
            throw new PersistenciaException("Objeto inválido");
        }

        Pet p = (Pet) pet;
        p.setNome(nome);
        p.setRaca(raca);
        p.setPeso(peso);

        petRepository.atualizar(p);
    }
}