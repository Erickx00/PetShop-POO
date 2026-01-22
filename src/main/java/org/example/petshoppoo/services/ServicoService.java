package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Servico;
import org.example.petshoppoo.repository.interfaces.IServicoRepository;
import org.example.petshoppoo.services.interfaces.IServicoService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ServicoService implements IServicoService {
    private final IServicoRepository servicoRepository;

    public ServicoService(IServicoRepository servicoRepository) throws PersistenciaException {
        this.servicoRepository = servicoRepository;
    }

    public List<Servico> listarServicosDisponiveis() throws PersistenciaException {
        return servicoRepository.listarAtivos();
    }

    @Override
    public Optional<Servico> buscarPorId(UUID id) throws PersistenciaException {
        return servicoRepository.buscarPorId(id);
    }


}