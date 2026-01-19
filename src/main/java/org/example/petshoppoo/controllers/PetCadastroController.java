package org.example.petshoppoo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.RepositoryFactory;
import org.example.petshoppoo.services.PetService;
import org.example.petshoppoo.services.ServiceFactory;
import org.example.petshoppoo.services.interfaces.IPetService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.SessionManager;
import org.example.petshoppoo.utils.ViewLoader;


public class PetCadastroController extends BaseController {

    @FXML private TextField txtNome;
    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtRaca;
    @FXML private TextField txtIdade;
    @FXML private TextField txtPeso;
    @FXML private CheckBox chkAdestrado;
    @FXML private CheckBox chkCastrado;

    private IPetService petService;

    public PetCadastroController() throws PersistenciaException {
        this.petService = ServiceFactory.getPetService();
    }

    @FXML
    public void initialize() {
        validarSessao();
        Usuario u = session.getUsuarioLogado();

        cbTipo.getItems().addAll("Cachorro", "Gato");

        cbTipo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isCachorro = "Cachorro".equals(newVal);
            chkAdestrado.setVisible(isCachorro);
            chkCastrado.setVisible(!isCachorro && "Gato".equals(newVal));
        });
    }

    @FXML
    private void handleSalvar() {
        if (!validarCampos()) return;

        try {

            String nome = txtNome.getText();
            String tipo = cbTipo.getValue();
            String raca = txtRaca.getText();
            int idade = Integer.parseInt(txtIdade.getText());
            double peso = Double.parseDouble(txtPeso.getText());
            boolean adestrado = chkAdestrado.isSelected();
            boolean castrado = chkCastrado.isSelected();


            petService.cadastrarPet(nome, tipo, raca, idade, peso, adestrado, castrado, SessionManager.getUsuarioId());

            AlertUtils.showInfo("Sucesso", "Pet cadastrado com sucesso!");
            voltarParaMenu();

        } catch (NumberFormatException e) {
            AlertUtils.showError("Erro", "Idade e peso devem ser valores numéricos.");
            return;
        } catch (PersistenciaException e) {
            AlertUtils.showError("Erro", "Falha ao salvar: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        if (txtNome.getText().isEmpty() || cbTipo.getValue() == null) {
            AlertUtils.showError("Erro", "Campos obrigatórios vazios.");
            return false;
        }
        return true;
    }

    @FXML
    private void handleCancelar() {
        voltarParaMenu();
    }

    private void voltarParaMenu() {
        try {
            Stage stage = (Stage) txtNome.getScene().getWindow();
            ViewLoader.loadView(stage, "/views/MenuView.fxml", "Meus Pets");
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Não foi possível carregar a lista.");
        }
    }
}