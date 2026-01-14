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
        inicializarServicosPadrao();
    }

    private void carregarDados() throws PersistenciaException {
        this.servicos = JsonFileManager.carregar(FilePaths.SERVICOS_JSON, Servico.class);
    }

    private void inicializarServicosPadrao() throws PersistenciaException {
        if (servicos.isEmpty()) {
            for (TipoServico tipo : TipoServico.values()) {
                Servico servico = new Servico(tipo);
                servicos.add(servico);
            }
            salvarServicos();
        }
    }

    private void salvarServicos() throws PersistenciaException {
        JsonFileManager.salvar(FilePaths.SERVICOS_JSON, servicos);
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