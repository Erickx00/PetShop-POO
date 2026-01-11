package org.example.petshoppoo.controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.services.AuthService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.SessionManager;
import org.example.petshoppoo.utils.ViewLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController extends BaseController{

    @FXML private Button CadastroPet;
    @FXML private Button ListaPets;
    @FXML private Button Servicos;
    @FXML private Button Agendamento;
    @FXML private VBox menuLateral;
    @FXML private Button btnMenu;

    private AuthService authService;

    public void initialize() {
        try {
            validarSessao();
            Usuario u = session.getUsuarioLogado();

            this.authService = new AuthService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isMenuOpen = false;

    @FXML
    void toggleMenu(ActionEvent event) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), menuLateral);
        if (!isMenuOpen) {
            transition.setToX(-200);
            isMenuOpen = true;
        } else {
            transition.setToX(0);
            isMenuOpen = false;
        }
        transition.play();
    }

    @FXML
    void handleVoltar(ActionEvent event) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), menuLateral);
        transition.setToX(0);
        transition.play();
        isMenuOpen = false;
    }

    @FXML
    public void handleCadastrarPet(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/PetCadastroView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleListarPets(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/PetListaView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleServicos(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/ServicoListaView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAgendamentos(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/Agendamento.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handlePerfil(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/PerfilView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSuporte(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(""));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleSairDaConta(ActionEvent event) {
        authService.logout();

        try {
            ViewLoader.changeScene(
                    (Node) event.getSource(),
                    "/views/LoginView.fxml",
                    "Login"
            );
        } catch (IOException e) {
            AlertUtils.showError("Erro", "Não foi possível sair da conta");
        }
    }

}