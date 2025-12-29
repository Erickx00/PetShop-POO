package org.example.petshoppoo;

import org.example.petshoppoo.model.Dono.Dono;
import org.example.petshoppoo.repository.DonoRepository;

public class TestJsonSave {
    public static void main(String[] args) {
        System.out.println("=== TESTE DE SALVAMENTO DONOS.JSON ===");

        try {
            DonoRepository donoRepository = new DonoRepository();


            Dono dono = new Dono();
            dono.setNome("João Teste");
            dono.setEmail("joao@teste.com");
            dono.setTelefone("(11) 99999-9999");

            System.out.println("Tentando salvar dono: " + dono.getNome());

            donoRepository.adicionar(dono);

            System.out.println("Dono salvo com sucesso!");
            System.out.println("ID do dono: " + dono.getId());

            // Listar todos os donos
            System.out.println("\n=== DONOS CADASTRADOS ===");
            for (Dono d : donoRepository.listarTodos()) {
                System.out.println("- " + d.getNome() + " | " + d.getEmail());
            }

        } catch (Exception e) {
            System.err.println("ERRO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
