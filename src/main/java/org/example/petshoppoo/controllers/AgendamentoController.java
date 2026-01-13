package org.example.petshoppoo.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.example.petshoppoo.model.Servico.Agendamento;
import org.example.petshoppoo.services.AgendamentoService;
import org.example.petshoppoo.services.PetService;
import org.example.petshoppoo.services.ServicoService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.SessionManager;



public class AgendamentoController {


    @FXML private TableView<Agendamento> tabelaAgendamentos;
    @FXML private TableColumn<Agendamento, String> colPet;
    @FXML private TableColumn<Agendamento, String> colData;
    @FXML private TableColumn<Agendamento, String> colServico;
    @FXML private TableColumn<Agendamento, String> colDuracao;
    @FXML private TableColumn<Agendamento, String> colStatus;

    // Services
    private AgendamentoService agendamentoService;
    private PetService petService;
    private ServicoService servicoService;

    @FXML
    public void initialize() { //Aqui ta inicializando os servicos
        try {

            this.agendamentoService = new AgendamentoService();
            this.petService = new PetService();
            this.servicoService = new ServicoService();

            configurarColunas();
            carregarTabela();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showError("Erro", "Erro ao inicializar tela: " + e.getMessage());
        }
    }

    private void configuringColunas() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Data e Hora
        colData.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDataHora().format(dtf)));

        // Status (Andamento)
        colStatus.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getStatus().getDescricao()));

         // Nome do Pet
        colPet.setCellValueFactory(cell -> {
            try {
                var pet = petService.buscarPorId(cell.getValue().getIdPet());
                return new SimpleStringProperty(pet != null ? pet.getNome() : "Desconhecido");
            } catch (Exception e) { return new SimpleStringProperty("-"); }
        });

        // Nome do Serviço
        colServico.setCellValueFactory(cell -> {
            try {
                var serv = servicoService.buscarServicoPorId(cell.getValue().getIdServico());
                return new SimpleStringProperty(serv != null ? serv.getTipo().getDescricao() : "Desconhecido");
            } catch (Exception e) { return new SimpleStringProperty("-"); }
        });

        // Valor
        colDuracao.setCellValueFactory(cell -> {
            try {
                var serv = servicoService.buscarServicoPorId(cell.getValue().getIdServico());
                // Exemplo: pega o preço ou um texto fixo, já que duração não tem no model padrão
                return new SimpleStringProperty(serv != null ? "R$ " + serv.getPreco() : "-");
            } catch (Exception e) { return new SimpleStringProperty("-"); }
        });
    }

    // Método auxiliar para chamar o de cima
    private void configurarColunas() {
        configuringColunas();
    }

    private void carregarTabela() {
        try {
            List<Agendamento> lista = agendamentoService.listarAgendamentosPorUsuario(SessionManager.getUsuarioId());
            tabelaAgendamentos.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancelar() {
        Agendamento selecionado = tabelaAgendamentos.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            AlertUtils.showWarning("Atenção", "Selecione um agendamento na lista para cancelar.");
            return;
        }

        try {
            agendamentoService.cancelarAgendamento(selecionado);
            carregarTabela(); // Atualiza a lista na hora
            AlertUtils.showInfo("Sucesso", "Serviço cancelado com sucesso.");
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Não foi possível cancelar: " + e.getMessage());
        }
    }

    @FXML
    public void handleVoltar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MenuView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tabelaAgendamentos.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Principal");
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showError("Erro", "Erro ao voltar para o menu.");
        }
    }

    @FXML
    public void handleLimpar(ActionEvent event) {
        List<Agendamento> paraRemover = new ArrayList<>();
        for (Agendamento ag : tabelaAgendamentos.getItems()) {
            if (ag.getStatus() == Agendamento.StatusAgendamento.CANCELADO) {
                paraRemover.add(ag);
            }
        }

        if (paraRemover.isEmpty()) {
            AlertUtils.showWarning("Aviso", "Não há agendamentos cancelados para limpar.");
            return;
        }
        boolean confirmar = AlertUtils.showConfirmation("Limpar Histórico",
                "Tem certeza que deseja excluir permanentemente " + paraRemover.size() + " agendamentos cancelados?");

        if (confirmar) {
            try {
                for (Agendamento ag : paraRemover) {
                    agendamentoService.excluirAgendamento(ag);
                }
                carregarTabela();
                AlertUtils.showInfo("Sucesso", "Histórico limpo com sucesso!");
            } catch (Exception e) {
                AlertUtils.showError("Erro", "Erro ao limpar agendamentos: " + e.getMessage());
            }
        }
    }
}