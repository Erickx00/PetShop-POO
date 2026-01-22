package org.example.petshoppoo.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.model.Pet.Pet;
import org.example.petshoppoo.repository.RepositoryFactory;
import org.example.petshoppoo.services.PetService;
import org.example.petshoppoo.services.ServiceFactory;
import org.example.petshoppoo.services.interfaces.IPetService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.SessionManager;
import org.example.petshoppoo.utils.ViewLoader;

public class PetListaController  {

    @FXML private TableView<Pet> tabelaPets;
    @FXML private TableColumn<Pet, String> clNome;
    @FXML private TableColumn<Pet, String> clEspecie;
    @FXML private TableColumn<Pet, String> clRaca;
    @FXML private TableColumn<Pet, String> clIdade;
    @FXML private TableColumn<Pet, Double> clPeso;

    @FXML private VBox painelEdicao;
    @FXML private HBox botoesAcao;

    @FXML private TextField txtNome;
    @FXML private TextField txtRaca;
    @FXML private TextField txtPeso;


    @FXML private Button btnEditar;
    @FXML private Button btnDeletar;



    private final ObservableList<Pet> pets = FXCollections.observableArrayList();
    private IPetService petService;
    private Pet petEmEdicao;


    @FXML
    public void initialize() {
        try {
            this.petService = ServiceFactory.getPetService();

            configurarTabela();
            carregarPets();
        }

        catch (PersistenciaException e) {
            e.printStackTrace();
            AlertUtils.showError(
                    "Erro de Persistência",
                    "Não foi possível carregar os dados do sistema.\n" + e.getMessage()
            );
        }

        catch (Exception e) {
            AlertUtils.showError("Erro ao inicializar", e.getMessage());
        }
    }

    private void configurarTabela() {
        // Service faz a formatação, controller só exibe
        clNome.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNome()));

        clEspecie.setCellValueFactory(data ->
                new SimpleStringProperty((String) data.getValue().getTipo()));

        clRaca.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRaca()));

        clIdade.setCellValueFactory(data ->
                new SimpleStringProperty(petService.formatarIdade(data.getValue())));

        clPeso.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPeso()).asObject());

        clPeso.setCellFactory(col -> new TableCell<Pet, Double>() {
            @Override
            protected void updateItem(Double peso, boolean empty) {
                super.updateItem(peso, empty);
                setText(empty || peso == null ? null : String.format("%.1f", peso));
            }
        });

        tabelaPets.setItems(pets);
        tabelaPets.setPlaceholder(new Label("Nenhum pet cadastrado"));

        tabelaPets.getSelectionModel().selectedItemProperty().addListener((obs, old, pet) -> {
            btnEditar.setDisable(pet == null);
            btnDeletar.setDisable(pet == null);
        });

        btnEditar.setDisable(true);
        btnDeletar.setDisable(true);
    }

    private void carregarPets() {
        try {
            pets.setAll(petService.listarPetsDoUsuario(SessionManager.getUsuarioId()));
        } catch (Exception e) {
            AlertUtils.showError("Erro ao carregar", e.getMessage());
        }
    }

    @FXML
    private void handleEditar() {
        Pet pet = tabelaPets.getSelectionModel().getSelectedItem();

        if (pet == null) {
            AlertUtils.showWarning("Nenhum pet selecionado", "Selecione um pet na tabela para editar.");
            return;
        }

        petEmEdicao = pet;


        txtNome.setText(pet.getNome());
        txtRaca.setText(pet.getRaca());
        txtPeso.setText(String.format("%.1f", pet.getPeso()));


        painelEdicao.setVisible(true);
        botoesAcao.setVisible(false);
    }

    @FXML
    private void handleSalvar() {
        try {
            if (!validarCampos()) {
                return;
            }

            double peso = Double.parseDouble(txtPeso.getText().trim());

            // Atualiza o pet
            petEmEdicao.setNome(txtNome.getText().trim());
            petEmEdicao.setRaca(txtRaca.getText().trim());
            petEmEdicao.setPeso(peso);

            // Service faz a persistência
            petService.atualizar(petEmEdicao);

            AlertUtils.showInfo("Sucesso", "Pet atualizado com sucesso! ✅");
            handleCancelar();
            carregarPets();
            tabelaPets.refresh();

        } catch (NumberFormatException e) {
            AlertUtils.showError("Peso inválido", "Digite um peso válido (ex: 10.5)");
        } catch (Exception e) {
            AlertUtils.showError("Erro ao salvar", e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() {
        painelEdicao.setVisible(false);
        botoesAcao.setVisible(true);
        petEmEdicao = null;
        limparCampos();
        tabelaPets.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleDeletar() {
        Pet pet = tabelaPets.getSelectionModel().getSelectedItem();

        if (pet == null) {
            AlertUtils.showWarning("Nenhum pet selecionado", "Selecione um pet na tabela para deletar.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("🗑️ Deseja realmente deletar este pet?");
        confirmacao.setContentText(String.format(
                "Pet: %s\nTipo: %s\nRaça: %s\n\n⚠️ Esta ação não pode ser desfeita!",
                pet.getNome(),
                pet.getTipo(),
                pet.getRaca()
        ));

        if (confirmacao.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                petService.excluir(pet.getIdPet());
                AlertUtils.showInfo("Sucesso", "Pet deletado com sucesso!");
                carregarPets();
            } catch (Exception e) {
                AlertUtils.showError("Erro ao deletar", e.getMessage());
            }
        }
    }

    @FXML
    private void handleVoltar() {
        try {
            Stage stage = (Stage) tabelaPets.getScene().getWindow();
            ViewLoader.loadView(stage, "/views/MenuView.fxml", "Menu");
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Não foi possível voltar ao menu.");
        }
    }

    private void limparCampos() {
        txtNome.clear();
        txtRaca.clear();
        txtPeso.clear();
    }

    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty()) {
            AlertUtils.showError("Campo obrigatório", "Informe o nome do pet.");
            return false;
        }

        if (txtRaca.getText().trim().isEmpty()) {
            AlertUtils.showError("Campo obrigatório", "Informe a raça do pet.");
            return false;
        }

        if (txtPeso.getText().trim().isEmpty()) {
            AlertUtils.showError("Campo obrigatório", "Informe o peso do pet.");
            return false;
        }

        try {
            double peso = Double.parseDouble(txtPeso.getText().trim());
            if (peso > 0 && peso <= 120) {
                AlertUtils.showError("Peso inválido", "O peso deve estar entre 1 a 30.");
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtils.showError("Peso inválido", "Digite um peso válido (ex: 10.5)");
            return false;
        }

        return true;
    }
}