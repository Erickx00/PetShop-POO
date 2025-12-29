package org.example.petshoppoo.utils;

import java.io.File;

public class DebugUtils {

    public static void verificarDiretorios() {
        System.out.println("=== VERIFICAÇÃO DE DIRETÓRIOS ===");
        System.out.println("Diretório atual: " + System.getProperty("user.dir"));

        String[] arquivos = {
                FilePaths.USUARIOS_JSON,
                FilePaths.DONOS_JSON,
                FilePaths.PETS_JSON,
                FilePaths.SERVICOS_JSON,
                FilePaths.AGENDAMENTOS_JSON
        };

        for (String arquivo : arquivos) {
            File f = new File(arquivo);
            System.out.println(arquivo + " - Existe: " + f.exists() + " - Caminho: " + f.getAbsolutePath());

            if (!f.getParentFile().exists()) {
                System.out.println("Criando diretório: " + f.getParentFile().getAbsolutePath());
                f.getParentFile().mkdirs();
            }
        }
        System.out.println("=== FIM VERIFICAÇÃO ===");
    }

    public static void log(String mensagem) {
        System.out.println("[DEBUG] " + mensagem);
    }
}
