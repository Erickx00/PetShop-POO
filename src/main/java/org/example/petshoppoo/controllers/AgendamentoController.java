package org.example.petshoppoo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.model.Servico.Servico;
import org.example.petshoppoo.services.*;
import org.example.petshoppoo.utils.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class AgendamentoController extends BaseController {

    @FXML private ComboBox<Pet> comboPet;
    @FXML private ComboBox<Servico> comboServico;
    @FXML private ComboBox<String> comboHorario;
    @FXML private DatePicker datePicker;
    @FXML private TextArea txtObservacoes;
    @FXML private Label lblResumoPet;
    @FXML private Label lblResumoServico;
    @FXML private Label lblTotal;
    @FXML private Button btnConfirmar;

    private final ObservableList<Pet> pets = FXCollections.observableArrayList();
    private final ObservableList<Servico> servicos = FXCollections.observableArrayList();

    private PetService petService;
    private ServicoService servicoService;
    private AgendamentoService agendamentoService;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        try {
            validarSessao();
            Usuario u = session.getUsuarioLogado();

            inicializarServices();
            carregarDados();
            configurarComponentes();
        } catch (Exception e) {
            AlertUtils.showError("Erro ao inicializar", e.getMessage());
        }
    }

    private void inicializarServices() throws PersistenciaException {
        petService = new PetService();
        servicoService = new ServicoService();
        agendamentoService = new AgendamentoService();
    }

    private void carregarDados() throws Exception {
        carregarServicos();
        carregarPets();
    }

    private void carregarServicos() throws Exception {
        servicos.setAll(servicoService.listarServicosDisponiveis());
        comboServico.setItems(servicos);

        comboServico.setCellFactory(p -> new ListCell<>() {
            @Override
            protected void updateItem(Servico s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) {
                    setText(null);
                } else {
                    setText(String.format("%s - R$ %.2f (%d min)",
                            s.getTipo().getDescricao(),
                            s.getPreco(),
                            s.getDuracaoMinutos()));
                }
            }
        });

        comboServico.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Servico s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty || s == null ? null : s.getTipo().getDescricao());
            }
        });
    }

    private void carregarPets() throws Exception {
        UUID usuarioId = SessionManager.getUsuarioId();
        if (usuarioId == null) {
            AlertUtils.showError("Sessão expirada", "Faça login novamente.");
            return;
        }

        pets.setAll(petService.listarPetsDoUsuario(usuarioId));
        comboPet.setItems(pets);

        comboPet.setCellFactory(p -> new ListCell<>() {
            @Override
            protected void updateItem(Pet pet, boolean empty) {
                super.updateItem(pet, empty);
                if (empty || pet == null) {
                    setText(null);
                } else {
                    setText(String.format("%s (%s, %s)",
                            pet.getNome(),
                            pet.getTipo(),
                            pet.getRaca()));
                }
            }
        });

        comboPet.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Pet pet, boolean empty) {
                super.updateItem(pet, empty);
                setText(empty || pet == null ? null : pet.getNome());
            }
        });

        if (!pets.isEmpty()) {
            comboPet.getSelectionModel().selectFirst();
        }
    }

    private void configurarComponentes() {
        configurarDatePicker();
        configurarListeners();
        atualizarResumo();
    }

    private void configurarDatePicker() {
        LocalDate hoje = LocalDate.now();
        LocalDate minimo = hoje.plusDays(1);
        LocalDate maximo = hoje.plusMonths(4);

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(minimo) || date.isAfter(maximo));
            }
        });

        datePicker.setValue(minimo);
    }

    private void configurarListeners() {
        comboPet.valueProperty().addListener((o, a, n) -> atualizarTudo());
        comboServico.valueProperty().addListener((o, a, n) -> atualizarTudo());
        datePicker.valueProperty().addListener((o, a, n) -> atualizarTudo());
        comboHorario.valueProperty().addListener((o, a, n) -> atualizarResumo());
    }

    private void atualizarTudo() {
        carregarHorariosDisponiveis();
        atualizarResumo();
    }

    private void carregarHorariosDisponiveis() {
        comboHorario.getItems().clear();

        Servico servico = comboServico.getValue();
        LocalDate data = datePicker.getValue();

        if (servico == null || data == null) return;

        try {
            var horarios = agendamentoService
                    .listarHorariosDisponiveis(data, servico.getDuracaoMinutos())
                    .stream()
                    .map(h -> h.format(TIME_FMT))
                    .toList();

            comboHorario.getItems().setAll(horarios);
            if (!horarios.isEmpty()) {
                comboHorario.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar horários: " + e.getMessage());
        }
    }

    private void atualizarResumo() {
        Pet pet = comboPet.getValue();
        Servico servico = comboServico.getValue();

        lblResumoPet.setText(pet != null ?
                String.format("%s (%s, %s)", pet.getNome(), pet.getTipo(), pet.getRaca()) : "-");

        lblResumoServico.setText(servico != null ?
                servico.getTipo().getDescricao() : "-");

        double preco = (pet != null && servico != null)
                ? servicoService.calcularPrecoParaPet(servico, pet)
                : 0.0;
        lblTotal.setText(String.format("R$ %.2f", preco));

        btnConfirmar.setDisable(!validarCampos());
    }

    @FXML
    private void handleConfirmar() {
        try {
            if (!validarCampos()) {
                AlertUtils.showError("Campos inválidos", "Preencha todos os campos.");
                return;
            }

            Servico servico = comboServico.getValue();
            LocalDateTime dataHora = LocalDateTime.of(
                    datePicker.getValue(),
                    LocalTime.parse(comboHorario.getValue(), TIME_FMT)
            );

            if (agendamentoService.existeConflitoHorario(dataHora, servico.getDuracaoMinutos())) {
                AlertUtils.showError("Horário indisponível", "Este horário já foi reservado.");
                carregarHorariosDisponiveis();
                return;
            }

            UUID agendamentoId = agendamentoService.agendar(
                    comboPet.getValue().getIdPet(),
                    servico.getId(),
                    SessionManager.getUsuarioId(),
                    dataHora,
                    txtObservacoes.getText()
            );

            agendamentoService.confirmarAgendamento(agendamentoId);

            Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
            sucesso.setTitle("Sucesso!");
            sucesso.setHeaderText("Agendamento confirmado! 🎉");
            sucesso.setContentText(String.format(
                    "Pet: %s\nServiço: %s\nData: %s às %s\nValor: R$ %.2f",
                    comboPet.getValue().getNome(),
                    servico.getTipo().getDescricao(),
                    datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    comboHorario.getValue(),
                    servicoService.calcularPrecoParaPet(servico, comboPet.getValue())
            ));
            sucesso.showAndWait();

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

        if (!pets.isEmpty()) {
            comboPet.getSelectionModel().selectFirst();
        }

        atualizarResumo();
    }

    @FXML
    private void voltarMenu() {
        try {
            ViewLoader.loadView((Stage) comboPet.getScene().getWindow(),
                    "/views/MenuView.fxml", "Menu");
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Não foi possível voltar.");
        }
    }

    private boolean validarCampos() {
        return comboPet.getValue() != null
                && comboServico.getValue() != null
                && comboHorario.getValue() != null
                && datePicker.getValue() != null;
    }
}