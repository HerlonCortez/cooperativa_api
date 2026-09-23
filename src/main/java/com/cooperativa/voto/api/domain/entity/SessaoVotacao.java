package com.cooperativa.voto.api.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_sessao_votacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pauta_id", nullable = false, unique = true)
    private Pauta pauta;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_fechamento", nullable = false)
    private LocalDateTime dataFechamento;

    public boolean isAberta() {
        LocalDateTime agora = LocalDateTime.now();
        return agora.isAfter(dataAbertura) && agora.isBefore(dataFechamento);
    }
}
