package org.example.petshoppoo.repository;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Servico.Agendamento;
import org.example.petshoppoo.utils.FilePaths;
import org.example.petshoppoo.utils.JsonFileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AgendamentoRepository {
    private List<Agendamento> agendamentos;

    public AgendamentoRepository() throws PersistenciaException {
        carregarDados();
    }

    private void carregarDados() throws PersistenciaException {
        try {
            agendamentos = JsonFileManager.carregar(FilePaths.AGENDAMENTOS_JSON, Agendamento.class);
            if (agendamentos == null) {
                agendamentos = new ArrayList<>();
            }
        } catch (Exception e) {
            agendamentos = new ArrayList<>();
            // Se o arquivo não existir ou der erro, inicia lista vazia para não travar o sistema
            System.out.println("Aviso: Iniciando lista de agendamentos vazia.");
        }
    }

    public void salvar(Agendamento agendamento) throws PersistenciaException {
        agendamentos.add(agendamento);
        JsonFileManager.salvar(FilePaths.AGENDAMENTOS_JSON, agendamentos);
    }

    public void atualizar(Agendamento agendamento) throws PersistenciaException {
        for (int i = 0; i < agendamentos.size(); i++) {
            if (agendamentos.get(i).getId().equals(agendamento.getId())) {
                agendamentos.set(i, agendamento);
                break;
            }
        }
        JsonFileManager.salvar(FilePaths.AGENDAMENTOS_JSON, agendamentos);
    }

    // --- ESSE ERA O MÉTODO QUE FALTAVA ---
    public void deletar(Agendamento agendamento) throws PersistenciaException {
        if (agendamento != null) {
            // Chama o método remover passando o ID do objeto
            remover(agendamento.getId());
        }
    }
    // -------------------------------------

    public void remover(UUID id) throws PersistenciaException {
        boolean removido = agendamentos.removeIf(a -> a.getId().equals(id));

        // Só grava no arquivo se realmente removeu alguém, pra evitar I/O desnecessário
        if (removido) {
            JsonFileManager.salvar(FilePaths.AGENDAMENTOS_JSON, agendamentos);
        }
    }

    public List<Agendamento> listarTodos() {
        return new ArrayList<>(agendamentos);
    }

    public List<Agendamento> buscarPorUsuario(UUID idUsuario) {
        return agendamentos.stream()
                .filter(a -> a.getIdUsuario().equals(idUsuario))
                .collect(Collectors.toList());
    }

    public List<Agendamento> buscarPorPet(UUID idPet) {
        return agendamentos.stream()
                .filter(a -> a.getIdPet().equals(idPet))
                .collect(Collectors.toList());
    }
}