package org.example.petshoppoo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.repository.RepositoryFactory;
import org.example.petshoppoo.services.AuthService;
import org.example.petshoppoo.services.ServiceFactory;
import org.example.petshoppoo.services.interfaces.IAuthService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.ViewLoader;
import java.io.IOException;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private Button btnEntrar;
    @FXML private Hyperlink btnCadastrar;

    private IAuthService authService;


    public void initialize(){
        try{
            this.authService = ServiceFactory.getAuthService();
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
    private void handleLogin() {
        String email = txtEmail.getText();
        String senha = txtSenha.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            AlertUtils.showWarning("Aviso", "Preencha tudo!");
            return;
        }

        try {

            // Autentica e seta o usuário na sessão
            authService.login(email, senha);
            // Isso vai chamar a tela de menu com o tamanho automático
            ViewLoader.changeScene(btnEntrar, "/views/MenuView.fxml", "Menu Principal");
        } catch (Exception e) {
            AlertUtils.showError("Erro", e.getMessage());
        }
    }

    @FXML
    private void handleCadastrar() throws IOException {
        ViewLoader.changeScene(btnCadastrar, "/views/CriarContaView.fxml", "Cadastro");
    }
}