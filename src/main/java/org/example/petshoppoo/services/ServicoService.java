package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Servico;
import org.example.petshoppoo.model.Servico.TipoServico;
import org.example.petshoppoo.repository.ServicoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServicoService {
    private final ServicoRepository servicoRepository;

    public ServicoService() throws PersistenciaException {
        this.servicoRepository = new ServicoRepository();
    }

    public List<Servico> listarServicosDisponiveis() throws PersistenciaException {
        return servicoRepository.listarAtivos();
    }

    public Servico buscarServicoPorId(UUID id) {
        return servicoRepository.buscarPorId(id);
    }

    public Servico buscarServicoPorTipo(TipoServico tipo) {
        return servicoRepository.buscarPorTipo(tipo);
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

    // ===== MÉTODOS PARA O CONTROLLER (Genéricos) =====

    public List<Object> listarServicosDisponiveisComoObjetos() throws PersistenciaException {
        return new ArrayList<>(listarServicosDisponiveis());
    }

    public String obterDescricaoCompleta(Object obj) {
        if (obj instanceof Servico) {
            Servico s = (Servico) obj;
            return s.getTipo().getDescricao() +
                    " (R$ " + String.format("%.2f", s.getPreco()) +
                    " - " + s.getDuracaoMinutos() + " min)";
        }
        return "";
    }

    public String obterDescricaoSimples(Object obj) {
        if (obj instanceof Servico) {
            return ((Servico) obj).getTipo().getDescricao();
        }
        return "";
    }

    public int obterDuracaoMinutos(Object obj) {
        if (obj instanceof Servico) {
            return ((Servico) obj).getDuracaoMinutos();
        }
        return 0;
    }

    public UUID obterId(Object obj) {
        if (obj instanceof Servico) {
            return ((Servico) obj).getId();
        }
        return null;
    }

    public double obterPreco(Object obj) {
        if (obj instanceof Servico) {
            return ((Servico) obj).getPreco();
        }
        return 0.0;
    }
}