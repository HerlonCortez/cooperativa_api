package com.cooperativa.voto.api.infrastructure.repository;

import com.cooperativa.voto.api.domain.entity.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VotingSessionRepository extends JpaRepository<SessaoVotacao, Long> {
    Optional<SessaoVotacao> findByPautaId(Long pautaId);
    boolean existsByPautaId(Long pautaId);
}
