package org.example.petshoppoo.services.interfaces;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Servico;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IServicoService {
    List<Servico> listarServicosDisponiveis() throws PersistenciaException;
    Optional<Servico> buscarPorId(UUID id) throws PersistenciaException;
}