package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Servico;
import org.example.petshoppoo.model.Servico.TipoServico;
import org.example.petshoppoo.repository.ServicoRepository;

import java.util.List;

public class ServicoService {
    private final ServicoRepository servicoRepository;

    public ServicoService() throws PersistenciaException {
        this.servicoRepository = new ServicoRepository();
    }

    public List<Servico> listarServicosDisponiveis() throws PersistenciaException {
        return servicoRepository.listarAtivos();
    }

    public Servico buscarServicoPorTipo(TipoServico tipo) throws PersistenciaException {
        return servicoRepository.buscarPorTipo(tipo);
    }

    public double calcularPreco(TipoServico tipo, double pesoPet) {
        double precoBase = tipo.getPrecoBase();

        // Ajuste baseado no peso do pet
        if (pesoPet > 20) {
            precoBase *= 1.4; // 20% mais caro para pets grandes
        } else if (pesoPet < 5) {
            precoBase *= 0.7; // 10% mais barato para pets pequenos
        }

        return precoBase;
    }
}