package com.cooperativa.voto.api.infrastructure.repository;

import com.cooperativa.voto.api.domain.entity.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PautaRepository extends JpaRepository<Pauta, Long> {
}
