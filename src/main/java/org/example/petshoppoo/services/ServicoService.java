package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Servico;
import org.example.petshoppoo.model.Servico.TipoServico;
import org.example.petshoppoo.repository.implementations.ServicoRepository;
import org.example.petshoppoo.repository.interfaces.IServicoRepository;
import org.example.petshoppoo.services.interfaces.IServicoService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ServicoService implements IServicoService {
    private final ServicoRepository servicoRepository;

    public ServicoService(IServicoRepository servicoRepository) throws PersistenciaException {
        this.servicoRepository = new ServicoRepository();
    }

    public List<Servico> listarServicosDisponiveis() throws PersistenciaException {
        return servicoRepository.listarAtivos();
    }

    @Override
    public Optional<Servico> buscarPorId(UUID id) throws PersistenciaException {
        return servicoRepository.buscarPorId(id);
    }

    public double calcularPreco(TipoServico tipo, double pesoPet) {
        double precoBase = tipo.getPrecoBase();

        /*Ajuste baseado no peso do pet vou deixar sem

        if (pesoPet > 20) {
            precoBase *= 1.4; // 40% mais caro para pets grandes
        } else if (pesoPet < 5) {
            precoBase *= 0.7; // 30% mais barato para pets pequenos
        }

        */

        return precoBase;
    }


}