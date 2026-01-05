package org.example.petshoppoo.services;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Servico;
import org.example.petshoppoo.model.Servico.TipoServico;
import org.example.petshoppoo.repository.ServicoRepository;

import java.util.ArrayList;
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
            precoBase *= 1.4; // 40% mais caro para pets grandes
        } else if (pesoPet < 5) {
            precoBase *= 0.7; // 30% mais barato para pets pequenos
        }

        return precoBase;
    }

    // ===== MÉTODOS PARA CONTROLLER BURRO =====

    /**
     * Retorna lista de serviços como objetos genéricos
     */
    public List<Object> listarServicosDisponiveisComoObjetos() throws PersistenciaException {
        List<Servico> servicos = listarServicosDisponiveis();
        return new ArrayList<>(servicos);
    }

    /**
     * Retorna descrição completa: "Tipo (R$ preco - duracao min)"
     */
    public String obterDescricaoCompleta(Object servico) {
        if (servico instanceof Servico) {
            Servico s = (Servico) servico;
            return s.getTipo().getDescricao() +
                    " (R$ " + String.format("%.2f", s.getPreco()) +
                    " - " + s.getDuracaoMinutos() + " min)";
        }
        return "";
    }

    /**
     * Retorna descrição simples (apenas o tipo)
     */
    public String obterDescricaoSimples(Object servico) {
        if (servico instanceof Servico) {
            return ((Servico) servico).getTipo().getDescricao();
        }
        return "";
    }

    /**
     * Retorna a duração em minutos do serviço
     */
    public int obterDuracaoMinutos(Object servico) {
        if (servico instanceof Servico) {
            return ((Servico) servico).getDuracaoMinutos();
        }
        return 0;
    }

    /**
     * Retorna o ID do serviço
     */
    public java.util.UUID obterId(Object servico) {
        if (servico instanceof Servico) {
            return ((Servico) servico).getId();
        }
        return null;
    }

    /**
     * Calcula o preço para um pet específico (recebe objetos genéricos)
     */
    public double calcularPrecoParaPet(Object servico, Object pet) {
        if (servico instanceof Servico && pet instanceof org.example.petshoppoo.model.Pet.Pet) {
            Servico s = (Servico) servico;
            org.example.petshoppoo.model.Pet.Pet p = (org.example.petshoppoo.model.Pet.Pet) pet;
            return calcularPreco(s.getTipo(), p.getPeso());
        }
        return 0.0;
    }

    /**
     * Obtém o preço base do serviço
     */
    public double obterPreco(Object servico) {
        if (servico instanceof Servico) {
            return ((Servico) servico).getPreco();
        }
        return 0.0;
    }
}