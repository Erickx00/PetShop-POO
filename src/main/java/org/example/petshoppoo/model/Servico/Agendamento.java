package org.example.petshoppoo.model.Servico;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Agendamento implements Serializable {
    private UUID id;
    private UUID idPet;
    private UUID idServico;
    private UUID idDono;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private String observacoes;
    private double valorCobrado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public enum StatusAgendamento {
        AGENDADO("Agendado", "⏰"),
        CONFIRMADO("Confirmado", "✓"),
        EM_ANDAMENTO("Em Andamento", "▶"),
        CONCLUIDO("Concluído", "✔"),
        CANCELADO("Cancelado", "✖"),
        NAO_COMPARECEU("Não Compareceu", "⚠");

        private final String descricao;
        private final String icone;

        StatusAgendamento(String descricao, String icone) {
            this.descricao = descricao;
            this.icone = icone;
        }

        public String getDescricao() { return descricao; }
        public String getIcone() { return icone; }
    }

    public Agendamento() {
        this.id = UUID.randomUUID();
        this.status = StatusAgendamento.AGENDADO;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Agendamento(UUID id, UUID idPet, UUID idServico, UUID idDono,
                       LocalDateTime dataHora, String observacoes) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.idPet = idPet;
        this.idServico = idServico;
        this.idDono = idDono;
        this.dataHora = dataHora;
        this.status = StatusAgendamento.AGENDADO;
        this.observacoes = observacoes;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

}
