package com.cooperativa.voto.api.infrastructure.repository;

import com.cooperativa.voto.api.domain.entity.Voto;
import com.cooperativa.voto.api.infrastructure.enums.OptionVoteEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Voto, Long> {
    boolean existsByPautaIdAndAssociadoId(Long pautaId, String associadoId);
    long countByPautaIdAndOpcao(Long pautaId, OptionVoteEnum opcao);
    long countByPautaId(Long pautaId);
}
