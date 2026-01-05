package org.example.petshoppoo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.services.AgendamentoService;
import org.example.petshoppoo.services.PetService;
import org.example.petshoppoo.services.ServicoService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.SessionManager;
import org.example.petshoppoo.utils.ViewLoader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class AgendamentoController {


    @FXML private ComboBox<Object> comboPet;
    @FXML private ComboBox<Object> comboServico;
    @FXML private ComboBox<String> comboHorario;
    @FXML private DatePicker datePicker;
    @FXML private TextArea txtObservacoes;
    @FXML private Label lblResumoPet;
    @FXML private Label lblResumoServico;
    @FXML private Label lblTotal;
    @FXML private Button btnConfirmar;

    //  LISTAS
    private final ObservableList<Object> pets = FXCollections.observableArrayList();
    private final ObservableList<Object> servicos = FXCollections.observableArrayList();
    private final ObservableList<String> horarios = FXCollections.observableArrayList();

    // SERVICES
    private PetService petService;
    private ServicoService servicoService;
    private AgendamentoService agendamentoService;

    // FORMATADORES
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    //  INITIALIZE
    @FXML
    public void initialize() {
        try {
            petService = new PetService();
            servicoService = new ServicoService();
            agendamentoService = new AgendamentoService();

            carregarServicos();
            carregarPets();
            configurarDatePicker();
            configurarListeners();
            atualizarResumo();
        } catch (Exception e) {
            AlertUtils.showError("Erro ao inicializar", e.getMessage());
        }
    }

    //CARGA DE DADOS
    private void carregarServicos() throws PersistenciaException {
        List<Object> servicosDisponiveis = servicoService.listarServicosDisponiveisComoObjetos();
        servicos.setAll(servicosDisponiveis);
        comboServico.setItems(servicos);

        comboServico.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Object servico, boolean empty) {
                super.updateItem(servico, empty);
                if (empty || servico == null) {
                    setText(null);
                } else {
                    setText(servicoService.obterDescricaoCompleta(servico));
                }
            }
        });

        comboServico.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Object servico, boolean empty) {
                super.updateItem(servico, empty);
                if (empty || servico == null) {
                    setText(null);
                } else {
                    setText(servicoService.obterDescricaoSimples(servico));
                }
            }
        });
    }

    private void carregarPets() throws PersistenciaException {
        UUID usuarioId = SessionManager.getUsuarioId();
        if (usuarioId == null) {
            AlertUtils.showError("Sessão expirada", "Faça login novamente.");
            return;
        }

        List<Object> petsUsuario = petService.listarPetsDoUsuarioComoObjetos(usuarioId);
        pets.setAll(petsUsuario);
        comboPet.setItems(pets);

        comboPet.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Object pet, boolean empty) {
                super.updateItem(pet, empty);
                if (empty || pet == null) {
                    setText(null);
                } else {
                    setText(petService.obterDescricaoCompleta(pet));
                }
            }
        });

        comboPet.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Object pet, boolean empty) {
                super.updateItem(pet, empty);
                if (empty || pet == null) {
                    setText(null);
                } else {
                    setText(petService.obterNome(pet));
                }
            }
        });

        if (!pets.isEmpty()) {
            comboPet.getSelectionModel().selectFirst();
        }
    }

    // ===== CONFIGURAÇÕES =====
    private void configurarDatePicker() {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now().plusDays(1)));
            }
        });

        datePicker.setValue(LocalDate.now().plusDays(1));

        datePicker.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? date.format(dateFormatter) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string == null || string.isEmpty()) ? null
                        : LocalDate.parse(string, dateFormatter);
            }
        });
    }

    private void configurarListeners() {
        comboPet.valueProperty().addListener((o, a, b) -> atualizarTudo());
        comboServico.valueProperty().addListener((o, a, b) -> atualizarTudo());
        datePicker.valueProperty().addListener((o, a, b) -> atualizarTudo());
        comboHorario.valueProperty().addListener((o, a, b) -> atualizarResumo());
    }

    private void atualizarTudo() {
        carregarHorariosDisponiveis();
        atualizarResumo();
    }

    // ===== HORÁRIOS =====
    private void carregarHorariosDisponiveis() {
        horarios.clear();
        comboHorario.getItems().clear();

        if (comboServico.getValue() == null || datePicker.getValue() == null) {
            return;
        }

        try {
            int duracao = servicoService.obterDuracaoMinutos(comboServico.getValue());
            List<LocalTime> disponiveis = agendamentoService.listarHorariosDisponiveis(
                    datePicker.getValue(),
                    duracao
            );

            disponiveis.forEach(h -> horarios.add(h.format(timeFormatter)));
            comboHorario.setItems(horarios);

            if (!horarios.isEmpty()) {
                comboHorario.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar horários: " + e.getMessage());
        }
    }

    //  RESUMO PARA CONFIRMAR
    private void atualizarResumo() {
        Object pet = comboPet.getValue();
        Object servico = comboServico.getValue();

        lblResumoPet.setText(pet != null ? petService.obterDescricaoCompleta(pet) : "-");
        lblResumoServico.setText(servico != null ? servicoService.obterDescricaoSimples(servico) : "-");

        if (pet != null && servico != null) {
            double preco = servicoService.calcularPrecoParaPet(servico, pet);
            lblTotal.setText(String.format("R$ %.2f", preco));
        } else {
            lblTotal.setText("R$ 0,00");
        }

        btnConfirmar.setDisable(!validarCampos());
    }

    // AÇÕES
    @FXML
    private void handleConfirmar() {
        try {
            if (!validarCampos()) {
                AlertUtils.showError("Campos inválidos", "Preencha todos os campos.");
                return;
            }

            UUID usuarioId = SessionManager.getUsuarioId();
            Object pet = comboPet.getValue();
            Object servico = comboServico.getValue();

            LocalDateTime dataHora = LocalDateTime.of(
                    datePicker.getValue(),
                    LocalTime.parse(comboHorario.getValue(), timeFormatter)
            );

            int duracao = servicoService.obterDuracaoMinutos(servico);

            if (agendamentoService.existeConflitoHorario(dataHora, duracao)) {
                AlertUtils.showError("Horário indisponível", "Este horário já foi reservado.");
                carregarHorariosDisponiveis();
                return;
            }

            UUID petId = petService.obterId(pet);
            UUID servicoId = servicoService.obterId(servico);

            UUID agendamentoId = agendamentoService.agendar(
                    petId,
                    servicoId,
                    usuarioId,
                    dataHora,
                    txtObservacoes.getText()
            );

            agendamentoService.confirmarAgendamento(agendamentoId);

            AlertUtils.showInfo("Sucesso", "Agendamento confirmado!");
            limparCampos();

        } catch (Exception e) {
            AlertUtils.showError("Erro", e.getMessage());
        }
    }

    @FXML
    private void limparCampos() {
        comboPet.getSelectionModel().clearSelection();
        comboServico.getSelectionModel().clearSelection();
        comboHorario.getItems().clear();
        txtObservacoes.clear();
        datePicker.setValue(LocalDate.now().plusDays(1));
        atualizarResumo();
    }

    @FXML
    private void voltarMenu() {
        try {
            Stage stage = (Stage) comboPet.getScene().getWindow();
            ViewLoader.loadView(stage, "/views/MenuView.fxml", "Menu");
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Não foi possível voltar.");
        }
    }

    // ===== VALIDAÇÃO =====
    private boolean validarCampos() {
        return comboPet.getValue() != null
                && comboServico.getValue() != null
                && comboHorario.getValue() != null
                && datePicker.getValue() != null;
    }
}