package org.example.petshoppoo.model.Servico;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class
Agendamento {
    private UUID id;
    private UUID idPet;
    private UUID idServico; // Aponta para o ID da classe Servico acima
    private UUID idUsuario;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private String observacoes;
    private double valorCobrado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;
    private int duracaoMinutos;

    public enum StatusAgendamento {
        AGENDADO("Agendado", "⏰", "#FFE0B2"),
        CANCELADO("Cancelado", "✖", "#FFCDD2");

        private final String descricao;
        private final String icone;
        private final String cor;

        StatusAgendamento(String descricao, String icone, String cor) {
            this.descricao = descricao;
            this.icone = icone;
            this.cor = cor;
        }

        public String getDescricao() { return descricao; }
        public String getIcone() { return icone; }
        public String getCor() { return cor; }
    }

    public Agendamento() {
        this.id = UUID.randomUUID();
        this.status = StatusAgendamento.AGENDADO;
        this.dataCriacao = LocalDateTime.now();
        this.valorCobrado = 0.0;
        this.duracaoMinutos = 0;
    }

    public Agendamento(UUID idPet, UUID idServico, UUID idUsuario,
                       LocalDateTime dataHora, String observacoes, int duracaoMinutos) {
        this();
        this.idPet = idPet;
        this.idServico = idServico;
        this.idUsuario = idUsuario;
        this.dataHora = dataHora;
        this.observacoes = observacoes;
        this.duracaoMinutos = duracaoMinutos;
    }

    // Getters e Setters padrão
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getIdPet() { return idPet; }
    public void setIdPet(UUID idPet) { this.idPet = idPet; }

    public UUID getIdServico() { return idServico; }
    public void setIdServico(UUID idServico) { this.idServico = idServico; }

    public UUID getIdUsuario() { return idUsuario; }
    public void setIdUsuario(UUID idUsuario) { this.idUsuario = idUsuario; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public double getValorCobrado() { return valorCobrado; }
    public void setValorCobrado(double valorCobrado) { this.valorCobrado = valorCobrado; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

    public int getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(int duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }

    // Helpers Visuais
    @JsonIgnore
    public String getDescricaoStatus() { return status.getIcone() + " " + status.getDescricao(); }

    @JsonIgnore
    public String getCorStatus() { return status.getCor(); }

    @JsonIgnore
    public String getDataHoraFormatada() {
        return dataHora != null ? dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
    }
}