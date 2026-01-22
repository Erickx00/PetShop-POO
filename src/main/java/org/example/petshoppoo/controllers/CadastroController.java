package org.example.petshoppoo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.repository.RepositoryFactory;
import org.example.petshoppoo.services.ServiceFactory;
import org.example.petshoppoo.services.UsuarioService;
import org.example.petshoppoo.services.interfaces.IUsuarioService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.ViewLoader;
import java.io.IOException;

public class CadastroController {
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private PasswordField txtSenha;
    @FXML private PasswordField txtConfirmarSenha;
    @FXML private Button btnFinalizar;

    private IUsuarioService usuarioService;


    public void initialize(){
        try {
            this.usuarioService = ServiceFactory.getUsuarioService();
        }

        catch (PersistenciaException e) {
            e.printStackTrace();
            AlertUtils.showError(
                    "Erro de Persistência",
                    "Não foi possível carregar os dados do sistema.\n" + e.getMessage()
            );
        }
    }

    @FXML
    public void handleCadastrar() {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String telefone = txtTelefone.getText();
        String senha = txtSenha.getText();
        String confirmarSenha = txtConfirmarSenha.getText();

        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty() || senha.isEmpty()) {
            AlertUtils.showWarning("Aviso", "Preencha todos os campos!");
            return;
        }

        if (nome.matches(".*\\d.*")) {
            AlertUtils.showError("Erro", "Nome não pode conter números.");
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            AlertUtils.showWarning("Aviso", "As senhas não coincidem!");
            return;
        }

        try {
            usuarioService.registrar(nome, email, telefone, senha);
            AlertUtils.showInfo("Sucesso", "Cadastro realizado!");
            ViewLoader.changeScene(btnFinalizar, "/views/LoginView.fxml", "Login");
        } catch (Exception e) {
            AlertUtils.showError("Erro", e.getMessage());
        }
    }

    @FXML
    public void handleCancelar() throws IOException {
        ViewLoader.changeScene(btnFinalizar, "/views/LoginView.fxml", "Login");
    }
}