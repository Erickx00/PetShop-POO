package org.example.petshoppoo.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.model.Servico.Servico;
import org.example.petshoppoo.repository.RepositoryFactory;
import org.example.petshoppoo.services.AgendamentoService;
import org.example.petshoppoo.services.PetService;
import org.example.petshoppoo.services.ServiceFactory;
import org.example.petshoppoo.services.ServicoService;
import org.example.petshoppoo.services.interfaces.IAgendamentoService;
import org.example.petshoppoo.services.interfaces.IPetService;
import org.example.petshoppoo.services.interfaces.IServicoService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.SessionManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ServicoController {

    // Vínculos exatos com o FXML
    @FXML private ComboBox<Pet> comboPet;
    @FXML private ComboBox<Servico> comboServico;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> comboHorario;
    @FXML private TextArea txtObservacoes;

    // Services
    private IAgendamentoService agendamentoService;
    private IPetService petService;
    private IServicoService servicoService;

    public ServicoController() throws PersistenciaException {
        this.agendamentoService = ServiceFactory.getAgendamentoService();
        this.petService = ServiceFactory.getPetService();
        this.servicoService = ServiceFactory.getServicoService();
    }


    @FXML
    public void initialize() {
        try {
            carregarCombos();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showError("Erro", "Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void carregarCombos() {//Carrega Pets do Usuário Logado

        try {
            List<Pet> pets = petService.listarPetsDoUsuario(SessionManager.getUsuarioId());
            comboPet.setItems(FXCollections.observableArrayList(pets));

            // Faz aparecer o NOME do pet, e não o código de memória
            comboPet.setConverter(new StringConverter<>() {
                @Override public String toString(Pet p) { return p != null ? p.getNome() : ""; }
                @Override public Pet fromString(String s) { return null; }
            });

            //Carrega Serviços Disponíveis
            List<Servico> servicos = servicoService.listarServicosDisponiveis();
            comboServico.setItems(FXCollections.observableArrayList(servicos));

            // Faz aparecer o NOME do serviço
            comboServico.setConverter(new StringConverter<>() {
                @Override public String toString(Servico s) { return s != null ? s.getTipo().getDescricao() : ""; }
                @Override public Servico fromString(String s) { return null; }
            });

            // Carrega Horários (das 08:00 às 18:00)
            List<String> horarios = new ArrayList<>();
            for (int h = 8; h <= 18; h++) {
                horarios.add(String.format("%02d:00", h));
            }
            comboHorario.setItems(FXCollections.observableArrayList(horarios));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleConfirmar() {
        // Validação simples
        if (comboPet.getValue() == null || comboServico.getValue() == null ||
                datePicker.getValue() == null || comboHorario.getValue() == null) {
            AlertUtils.showWarning("Atenção", "Preencha todos os campos obrigatórios!");
            return;
        }

        try {
            // Junta Data e Hora
            LocalDateTime dataHora = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(comboHorario.getValue()));

            // Cria o Agendamento
            agendamentoService.criarAgendamento(
                    SessionManager.getUsuarioId(),
                    comboPet.getValue().getIdPet(),
                    comboServico.getValue().getId(),
                    dataHora,
                    txtObservacoes.getText()
            );

            AlertUtils.showInfo("Sucesso", "Agendamento realizado com sucesso!");
            voltarMenu(); // Volta pro menu

        } catch (Exception e) {
            AlertUtils.showError("Erro", "Falha ao agendar: " + e.getMessage());
        }
    }


    @FXML
    public void voltarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MenuView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) comboPet.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Principal");
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showError("Erro", "Erro ao voltar para o menu.");
        }
    }
}