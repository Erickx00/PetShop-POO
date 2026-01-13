package org.example.petshoppoo.controllers;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.petshoppoo.utils.SessionManager;

import java.io.IOException;

public class MenuController {

    @FXML private VBox menuLateral;
    @FXML private Button btnMenu;

    @FXML
    public void initialize() {
        menuLateral.setTranslateX(0);
    }

    @FXML
    public void toggleMenu() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(menuLateral);
        if (menuLateral.getTranslateX() == 0) {
            slide.setToX(-210);
        } else {
            slide.setToX(0);
        }
        slide.play();
    }

    @FXML
    public void handleServicos() {
        abrirTela("/views/ServicoListaView.fxml", "Agendar Serviço");
    }

    @FXML
    public void handleAgendamentos() {
        abrirTela("/views/AgendamentoView.fxml", "Meus Agendamentos");
    }

    @FXML
    public void handleCadastrarPet() {
        abrirTela("/views/PetCadastroView.fxml", "Cadastrar Novo Pet");
    }

    @FXML
    public void handleListarPets() {
        abrirTela("/views/PetListaView.fxml", "Meus Pets");
    }

    @FXML
    public void handleSairDaConta() {
        SessionManager.limparSessao();
        abrirTela("/views/LoginView.fxml", "Login - Golden Pet");
    }

    @FXML
    public void handleVoltar() {
        toggleMenu(); // Apenas fecha o menu lateral
    }

    @FXML public void handlePerfil() {
    abrirTela("/views/PerfilView.fxml", "Perfil");

    }
    @FXML public void handleSuporte() {

    }
    private void abrirTela(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stageAtual = (Stage) btnMenu.getScene().getWindow();
            stageAtual.setScene(new Scene(root));
            stageAtual.setTitle(titulo);
            stageAtual.centerOnScreen();
            stageAtual.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao abrir tela: " + fxmlPath);
        }
    }
}