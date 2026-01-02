package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.repository.PetRepository;
import org.example.petshoppoo.repository.UsuarioRepository;

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

        // 1Validação básica (Regra de Negócio)
        if (nome == null || nome.trim().isEmpty()) {
            throw new PersistenciaException("O nome do pet é obrigatório.");
        }

        // Lógica de conversão de idade para data (Regra de Negócio)
        java.time.LocalDate dataNasc = java.time.LocalDate.now().minusYears(idadeAnos);

        // Decisão de qual objeto criar (Fábrica/Polimorfismo)
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

        // Salva o Pet no pets.json
        petRepository.salvar(novoPet);

        // VINCULAÇÃO: Adiciona o ID do Pet ao usuário no usuarios.json
        usuarioRepository.adicionarPetAoUsuario(idUsuario, novoPet.getId());

    }

    public List<Pet> listarPets() {
        return petRepository.listarTodos();
    }

    public Pet buscarPorId(UUID id) {
        return petRepository.buscarPorId(id).orElse(null);
    }

    public void deletarPet(Pet pet) throws PersistenciaException {
        petRepository.deletar(pet);
    }
}