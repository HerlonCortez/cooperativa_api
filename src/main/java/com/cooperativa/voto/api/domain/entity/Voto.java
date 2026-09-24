package com.cooperativa.voto.api.domain.entity;

import com.cooperativa.voto.api.infrastructure.enums.OptionVoteEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "tb_voto",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_pauta_associado",
                        columnNames = {"pauta_id", "associado_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    @Column(name = "associado_id", nullable = false, length = 50)
    private String associadoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "opcao_voto", nullable = false, length = 10)
    private OptionVoteEnum opcao;

    @Column(name = "data_voto", nullable = false, updatable = false)
    private LocalDateTime dataVoto;

    @PrePersist
    public void prePersist() {
        this.dataVoto = LocalDateTime.now();
    }
}
