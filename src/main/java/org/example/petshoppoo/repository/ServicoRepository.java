package org.example.petshoppoo.repository;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Servico;
import org.example.petshoppoo.model.Servico.TipoServico;
import org.example.petshoppoo.utils.FilePaths;
import org.example.petshoppoo.utils.JsonFileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServicoRepository {
    private List<Servico> servicos;

    public ServicoRepository() throws PersistenciaException {
        carregarDados();
    }

    private void carregarDados() throws PersistenciaException {
        try {
            servicos = JsonFileManager.carregarLista(FilePaths.SERVICOS_JSON, Servico.class);
            if (servicos == null) {
                servicos = new ArrayList<>();
            }
        } catch (Exception e) {
            servicos = new ArrayList<>();
            throw new PersistenciaException("Erro ao carregar serviços: " + e.getMessage());
        }
    }

    public List<Servico> listarAtivos() {
        return servicos.stream()
                .filter(Servico::isAtivo)
                .collect(Collectors.toList());
    }

    public Servico buscarPorId(UUID id) {
        return servicos.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Servico buscarPorTipo(TipoServico tipo) {
        return servicos.stream()
                .filter(s -> s.getTipo() == tipo && s.isAtivo())
                .findFirst()
                .orElse(null);
    }
}