package org.example.petshoppoo.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.petshoppoo.model.Login.Usuario;


import java.io.IOException;
import java.io.InputStream;


public class PerfilController {



    @FXML
    private TextField nome;
    @FXML
    private TextField email;
    @FXML
    private TextField telefone;


    @FXML
    public void initialize() {
        carregarDadosPerfil();
    }


    private void carregarDadosPerfil() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getResourceAsStream("/data/usuarios.json");


            if (is != null) {
                Usuario[] usuarios = mapper.readValue(is, Usuario[].class);
                if (usuarios.length > 0) {
                    Usuario usuarioLogado = usuarios[0];
                    nome.setText(usuarioLogado.getNome());
                    email.setText(usuarioLogado.getEmail());
                    telefone.setText(usuarioLogado.getTelefone());
                    System.out.println("Perfil carregado: " + usuarioLogado.getNome());
                } else {
                    System.out.println("O arquivo JSON está vazio.");
                }
            } else {
                System.err.println("Arquivo não encontrado!");
            }


        } catch (Exception e) {
            System.err.println("Erro  ao processar JSON: " + e.getMessage());
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}