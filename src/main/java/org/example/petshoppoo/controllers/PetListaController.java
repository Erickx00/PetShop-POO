package org.example.petshoppoo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.petshoppoo.services.PetService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.PetTableData;
import org.example.petshoppoo.utils.SessionManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PetListaController {

    @FXML private TableView<PetTableData> tabelaPets;
    @FXML private TableColumn<PetTableData, String> clNome;
    @FXML private TableColumn<PetTableData, String> clEspecie;
    @FXML private TableColumn<PetTableData, String> clRaca;
    @FXML private TableColumn<PetTableData, String> clIdade;
    @FXML private TableColumn<PetTableData, Double> clPeso;

    @FXML private VBox painelEdicao;
    @FXML private HBox botoesAcao;
    @FXML private TextField txtNome;
    @FXML private TextField txtRaca;
    @FXML private TextField txtPeso;

    private PetService petService;
    private Object petEmEdicao;

    @FXML
    public void initialize() {
        try {
            petService = new PetService();
            configurarTabela();
            carregarPets();
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Erro ao inicializar: " + e.getMessage());
        }
    }

    private void configurarTabela() {
        clNome.setCellValueFactory(data -> data.getValue().nomeProperty());
        clEspecie.setCellValueFactory(data -> data.getValue().especieProperty());
        clRaca.setCellValueFactory(data -> data.getValue().racaProperty());
        clIdade.setCellValueFactory(data -> data.getValue().idadeProperty());
        clPeso.setCellValueFactory(data -> data.getValue().pesoProperty().asObject());
    }

    private void carregarPets() {
        try {
            List<Object> pets = petService.listarPetsParaTabela(SessionManager.getUsuarioId());
            ObservableList<PetTableData> dadosTabela = FXCollections.observableArrayList();

            for (Object pet : pets) {
                dadosTabela.add(new PetTableData(
                        pet,
                        petService.obterNome(pet),
                        petService.obterTipo(pet),
                        petService.obterRaca(pet),
                        petService.obterIdade(pet),
                        petService.obterPeso(pet)
                ));
            }

            tabelaPets.setItems(dadosTabela);

        } catch (Exception e) {
            AlertUtils.showError("Erro", "Erro ao carregar pets: " + e.getMessage());
        }
    }

    @FXML
    private void handleAdicionar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CadastroPetView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tabelaPets.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            AlertUtils.showError("Erro", "Não foi possível abrir cadastro de pet.");
        }
    }

    @FXML
    private void handleEditar() {
        PetTableData selecionado = tabelaPets.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            AlertUtils.showWarning("Nenhum pet selecionado", "Selecione um pet para editar.");
            return;
        }

        // Armazena o pet em edição
        petEmEdicao = selecionado.getPet();

        // Preenche os campos
        txtNome.setText(petService.obterNome(petEmEdicao));
        txtRaca.setText(petService.obterRaca(petEmEdicao));
        txtPeso.setText(String.valueOf(petService.obterPeso(petEmEdicao)));

        // Mostra painel de edição e esconde botões
        painelEdicao.setVisible(true);
        botoesAcao.setVisible(false);
    }

    @FXML
    private void handleSalvar() {
        try {
            String nome = txtNome.getText().trim();
            String raca = txtRaca.getText().trim();
            String pesoStr = txtPeso.getText().trim();

            if (nome.isEmpty() || raca.isEmpty() || pesoStr.isEmpty()) {
                AlertUtils.showWarning("Campos vazios", "Preencha todos os campos!");
                return;
            }

            double peso;
            try {
                peso = Double.parseDouble(pesoStr);
                if (peso <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                AlertUtils.showError("Peso inválido", "Digite um peso válido (ex: 5.5)");
                return;
            }

            petService.atualizarPet(petEmEdicao, nome, raca, peso);
            AlertUtils.showInfo("Sucesso", "Pet atualizado com sucesso!");

            handleCancelar();
            carregarPets();

        } catch (Exception e) {
            AlertUtils.showError("Erro", "Erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() {
        painelEdicao.setVisible(false);
        botoesAcao.setVisible(true);
        petEmEdicao = null;
        limparCampos();
    }

    private void limparCampos() {
        txtNome.clear();
        txtRaca.clear();
        txtPeso.clear();
    }

    @FXML
    private void handleDeletar() {
        PetTableData selecionado = tabelaPets.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            AlertUtils.showWarning("Nenhum pet selecionado", "Selecione um pet para deletar.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("Deseja realmente deletar o pet?");
        confirmacao.setContentText("Pet: " + selecionado.getNome() + "\nEsta ação não pode ser desfeita!");

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                petService.deletarPet(selecionado.getPet());
                AlertUtils.showInfo("Sucesso", "Pet deletado com sucesso!");
                carregarPets();
            } catch (Exception e) {
                AlertUtils.showError("Erro", "Não foi possível deletar: " + e.getMessage());
            }
        }
    }

    @FXML
    public void handleVoltar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/MenuView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            AlertUtils.showError("Erro", "Não foi possível voltar ao menu.");
        }
    }
}