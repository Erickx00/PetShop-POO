package org.example.petshoppoo.services.interfaces;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.model.Servico.Servico;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IServicoService {
    List<Servico> listarServicosDisponiveis() throws PersistenciaException;
    Optional<Servico> buscarPorId(UUID id) throws PersistenciaException;
    double calcularPrecoParaPet(Servico servico, Pet pet);
    void adicionarServico(Servico servico) throws PersistenciaException;
    void atualizarServico(Servico servico) throws PersistenciaException;
    void removerServico(UUID id) throws PersistenciaException;
    ArrayList<Servico> servicoDisponivel(UUID id) throws PersistenciaException;
    double calcularPrecoComDesconto(Servico servico, double percentualDesconto);
}