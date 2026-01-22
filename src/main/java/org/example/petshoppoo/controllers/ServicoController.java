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
import org.example.petshoppoo.services.ServiceFactory;
import org.example.petshoppoo.services.interfaces.IAgendamentoService;
import org.example.petshoppoo.services.interfaces.IPetService;
import org.example.petshoppoo.services.interfaces.IServicoService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.SessionManager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ServicoController {

    @FXML private ComboBox<Pet> comboPet;
    @FXML private ComboBox<Servico> comboServico;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> comboHorario;
    @FXML private TextArea txtObservacoes;
    @FXML private Label lblResumo;
    @FXML private Label lblValor;

    private IAgendamentoService agendamentoService;
    private IPetService petService;
    private IServicoService servicoService;
    private Servico servicoSelecionado;
    private Pet petSelecionado;


    @FXML
    public void initialize() {
        try {
            this.agendamentoService = ServiceFactory.getAgendamentoService();
            this.petService = ServiceFactory.getPetService();
            this.servicoService = ServiceFactory.getServicoService();

        }

        catch (PersistenciaException e) {
            e.printStackTrace();
            AlertUtils.showError(
                    "Erro de Persistência",
                    "Não foi possível carregar os dados do sistema.\n" + e.getMessage()
            );
        }

        configurarCalendario();
        carregarCombos();
        configurarListeners();
    }

    private void configurarCalendario() {
        LocalDate hoje = LocalDate.now();
        datePicker.setValue(hoje.plusDays(1));

        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate data, boolean vazio) {
                super.updateItem(data, vazio);
                boolean bloqueado = data.isBefore(hoje.plusDays(1)) ||
                        data.isAfter(hoje.plusMonths(4));
                setDisable(bloqueado);
            }
        });
    }

    private void carregarCombos() {
        try {
            List<Pet> pets = petService.listarPetsDoUsuario(SessionManager.getUsuarioId());
            comboPet.setItems(FXCollections.observableArrayList(pets));
            comboPet.setConverter(new StringConverter<Pet>() {
                public String toString(Pet p) {
                    return p != null ? p.getNome() : "";
                }
                public Pet fromString(String s) {
                    return null;
                }
            });

            List<Servico> servicos = servicoService.listarServicosDisponiveis();
            comboServico.setItems(FXCollections.observableArrayList(servicos));
            comboServico.setConverter(new StringConverter<Servico>() {
                public String toString(Servico s) {
                    return s != null ? s.getTipo().getDescricao() : "";
                }
                public Servico fromString(String s) {
                    return null;
                }
            });

        } catch (Exception e) {
            AlertUtils.showError("Erro", "Não foi possível carregar os dados");
        }
    }

    private void configurarListeners() {
        comboPet.valueProperty().addListener((obs, old, novo) -> {
            petSelecionado = novo;
            atualizarResumo();
        });

        comboServico.valueProperty().addListener((obs, old, novo) -> {
            servicoSelecionado = novo;
            if (novo != null) {
                lblValor.setText(String.format("R$ %.2f", novo.getPreco()));
                atualizarHorarios();
            }
            atualizarResumo();
        });

        datePicker.valueProperty().addListener((obs, old, novo) -> {
            atualizarResumo();
            if (servicoSelecionado != null) {
                atualizarHorarios();
            }
        });

        comboHorario.valueProperty().addListener((obs, old, novo) -> atualizarResumo());
        txtObservacoes.textProperty().addListener((obs, old, novo) -> atualizarResumo());
    }

    private void atualizarHorarios() {
        if (datePicker.getValue() == null || servicoSelecionado == null) {
            comboHorario.getItems().clear();
            return;
        }

        try {
            List<LocalDateTime> horarios = agendamentoService.listarHorariosDisponiveis(
                    datePicker.getValue(),
                    servicoSelecionado.getDuracaoMinutos()
            );

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            List<String> horariosFormatados = new java.util.ArrayList<>();

            for (LocalDateTime horario : horarios) {
                horariosFormatados.add(horario.format(formatter));
            }

            comboHorario.setItems(FXCollections.observableArrayList(horariosFormatados));

        } catch (Exception e) {
            AlertUtils.showError("Erro", "Não foi possível buscar os horários");
        }
    }

    private void atualizarResumo() {
        StringBuilder sb = new StringBuilder();

        if (petSelecionado != null) {
            sb.append("Pet: ").append(petSelecionado.getNome()).append("\n");
        }

        if (servicoSelecionado != null) {
            sb.append("Serviço: ").append(servicoSelecionado.getTipo().getDescricao()).append("\n");
        }

        if (datePicker.getValue() != null && comboHorario.getValue() != null) {
            sb.append("Data: ").append(datePicker.getValue())
                    .append(" às ").append(comboHorario.getValue());
        }

        lblResumo.setText(sb.toString());
    }

    @FXML
    private void handleConfirmar() {
        if (!validarCampos()) return;

        try {
            LocalDateTime dataHora = LocalDateTime.of(
                    datePicker.getValue(),
                    LocalTime.parse(comboHorario.getValue())
            );

            agendamentoService.criarAgendamento(
                    SessionManager.getUsuarioId(),
                    petSelecionado.getIdPet(),
                    servicoSelecionado.getId(),
                    dataHora,
                    txtObservacoes.getText()
            );

            AlertUtils.showInfo("Sucesso", "Agendamento realizado!");
            voltarMenu();

        } catch (Exception e) {
            AlertUtils.showError("Erro", e.getMessage());
        }
    }

    private boolean validarCampos() {
        if (comboPet.getValue() == null || comboServico.getValue() == null ||
                datePicker.getValue() == null || comboHorario.getValue() == null) {
            AlertUtils.showWarning("Aviso", "Preencha todos os campos");
            return false;
        }

        LocalDate data = datePicker.getValue();
        LocalDate hoje = LocalDate.now();

        if (data.isBefore(hoje.plusDays(1))) {
            AlertUtils.showWarning("Data inválida", "Escolha uma data futura");
            return false;
        }

        if (data.isAfter(hoje.plusMonths(4))) {
            AlertUtils.showWarning("Data inválida", "Máximo de 4 meses");
            return false;
        }

        return true;
    }

    @FXML
    private void voltarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MenuView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) comboPet.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu");
        } catch (IOException e) {
            AlertUtils.showError("Erro", "Não foi possível voltar");
        }
    }
}