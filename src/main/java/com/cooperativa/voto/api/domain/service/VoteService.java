package com.cooperativa.voto.api.domain.service;

import com.cooperativa.voto.api.domain.dto.ResultVotingDTO;
import com.cooperativa.voto.api.infrastructure.enums.OptionVoteEnum;

public interface VoteService {
    void castVote(Long agendaId, String cpf, OptionVoteEnum option);
    ResultVotingDTO countVotes(Long pautaId);
}
