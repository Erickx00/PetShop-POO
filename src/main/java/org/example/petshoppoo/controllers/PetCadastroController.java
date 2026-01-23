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


public class PetCadastroController  {

    @FXML private TextField txtNome;
    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtRaca;
    @FXML private TextField txtIdade;
    @FXML private TextField txtPeso;
    @FXML private CheckBox chkAdestrado;
    @FXML private CheckBox chkCastrado;

    private IPetService petService;


    @FXML
    public void initialize() {

        try {
            this.petService = ServiceFactory.getPetService();
        }

        catch (PersistenciaException e) {
            e.printStackTrace();
            AlertUtils.showError(
                    "Erro de Persistência",
                    "Não foi possível carregar os dados do sistema.\n" + e.getMessage()
            );
        }

        cbTipo.getItems().addAll("Cachorro", "Gato");

        cbTipo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            // Verifica se o que foi selecionado é Gato ou Cachorro
            boolean animalSelecionado = "Cachorro".equals(newVal) || "Gato".equals(newVal);

            // Agora os dois checkboxes aparecem se for qualquer um dos dois
            chkAdestrado.setVisible(animalSelecionado);
            chkCastrado.setVisible(animalSelecionado);
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
        } catch (PersistenciaException e) {
            AlertUtils.showError("Erro", "Falha ao salvar: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        // Validação do nome
        String nome = txtNome.getText().trim();
        if (nome.isEmpty()) {
            AlertUtils.showError("Erro", "Nome é obrigatório.");
            return false;
        }
        if (nome.matches(".*\\d.*")) {
            AlertUtils.showError("Erro", "Nome não pode conter números.");
            return false;
        }

        // Validação do tipo
        if (cbTipo.getValue() == null) {
            AlertUtils.showError("Erro", "Selecione o tipo do animal.");
            return false;
        }

        // Validação de idade (0 a 25)
        try {
            String idadeTexto = txtIdade.getText().trim();
            if (idadeTexto.isEmpty()) {
                AlertUtils.showError("Erro", "Idade é obrigatória.");
                return false;
            }
            int idade = Integer.parseInt(idadeTexto);
            if (idade < 0 || idade > 25) {
                AlertUtils.showError("Erro", "Idade deve estar entre 0 e 25 anos.");
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtils.showError("Erro", "Idade deve conter apenas números.");
            return false;
        }

        // Validação de peso (0 a 120) - CORREÇÃO
        try {
            String pesoTexto = txtPeso.getText().replace(",", ".").trim();
            if (pesoTexto.isEmpty()) {
                AlertUtils.showError("Erro", "Peso é obrigatório.");
                return false;
            }
            double peso = Double.parseDouble(pesoTexto);
            if (peso <= 0 || peso > 120) {  // Corrigido a lógica
                AlertUtils.showError("Erro", "Peso deve estar entre 0 e 120 kg (excluindo 0).");
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtils.showError("Erro", "Digite um peso válido (ex: 10.5 ou 10,5)");
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