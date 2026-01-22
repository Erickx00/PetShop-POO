package org.example.petshoppoo.repository.implementations;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Servico;
import org.example.petshoppoo.model.Servico.TipoServico;
import org.example.petshoppoo.repository.interfaces.IServicoRepository;
import org.example.petshoppoo.utils.FilePaths;
import org.example.petshoppoo.utils.JsonFileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServicoRepository implements IServicoRepository {
    private List<Servico> servicos;

    public ServicoRepository() throws PersistenciaException {
        carregarDados();
        inicializarServicosPadrao();
    }

    private void carregarDados() {
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

    @Override
    public List<Servico> listarTodos() {
        return new ArrayList<>(servicos) ;
    }

    public Optional<Servico> buscarPorId(UUID id) {
        return servicos.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    @Override
    public void salvar(Servico servico) throws PersistenciaException {
        servicos.add(servico);
        salvarServicos();

    }

    @Override
    public void atualizar(Servico servico) throws PersistenciaException{
        boolean removido = servicos.removeIf(s -> s.getId().equals(servico.getId()));

        if (!removido) {
            throw new PersistenciaException("Serviço não encontrado");
        }

        servicos.add(servico);
        salvarServicos();
    }


    @Override
    public void deletar(UUID id) throws PersistenciaException {
        servicos.stream()
                .filter(servico -> servico.getId().equals(id))
                .findFirst()
                .ifPresent(servicoParaRemover -> servicos.remove(servicoParaRemover) );
        salvarServicos();

    }

}