package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Dono.Dono;
import org.example.petshoppoo.repository.DonoRepository;

import java.util.UUID;

public class DonoService {
    private final DonoRepository donoRepository;

    public DonoService() throws PersistenciaException {
        this.donoRepository = new DonoRepository();
    }

    public void criarDono(String nome, String telefone, String email) throws PersistenciaException {

        Dono dono = new Dono(UUID.randomUUID(),nome,telefone,email);

        donoRepository.adicionar(dono);
    }

    public Dono buscarPorUsuario(UUID idUsuario) throws PersistenciaException {
        // Implementar lógica para buscar dono pelo ID do usuário
        if(idUsuario == null){
            throw new PersistenciaException("O ID do usuario nao pode se nulo");
        }
        return donoRepository.buscarPorId(idUsuario);
    }
}
