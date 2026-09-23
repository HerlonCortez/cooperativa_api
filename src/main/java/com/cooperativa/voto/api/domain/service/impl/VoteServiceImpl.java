package com.cooperativa.voto.api.domain.service.impl;

import com.cooperativa.voto.api.domain.dto.ResultVotingDTO;
import com.cooperativa.voto.api.domain.entity.Voto;
import com.cooperativa.voto.api.domain.exception.RegraNegocioException;
import com.cooperativa.voto.api.domain.service.CpfValidationService;
import com.cooperativa.voto.api.domain.service.VoteService;
import com.cooperativa.voto.api.domain.service.VotingSessionService;
import com.cooperativa.voto.api.infrastructure.enums.OptionVoteEnum;
import com.cooperativa.voto.api.infrastructure.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {
    private final VoteRepository voteRepository;
    private final VotingSessionService votingSessionService;
    private final CpfValidationService cpfValidationService;

    @Override
    public void castVote(Long agendaId, String cpf, OptionVoteEnum option){
        //cpfValidationService.validateMemberIsEligibleToVote(cpf);

        var session = votingSessionService.getAgendaId(agendaId);
        if (!session.isAberta()){
            throw new RegraNegocioException("A sessão de votação desta pauta encontra-se fechada.");
        }

        if (voteRepository.existsByPautaIdAndAssociadoId(agendaId, cpf)){
            throw new RegraNegocioException("O associado já registrou seu voto nesta pauta.");
        }

        Voto vote = Voto.builder()
                .pauta(session.getPauta())
                .associadoId(cpf)
                .opcao(option)
                .build();

        voteRepository.save(vote);
    }

    @Override
    public ResultVotingDTO countVotes(Long pautaId) {
        var sessao = votingSessionService.getAgendaId(pautaId);

        long totalVotos = voteRepository.countByPautaId(pautaId);
        long votosSim = voteRepository.countByPautaIdAndOpcao(pautaId, OptionVoteEnum.SIM);
        long votosNao = voteRepository.countByPautaIdAndOpcao(pautaId, OptionVoteEnum.NAO);

        String resultado;
        if (votosSim > votosNao) {
            resultado = "APROVADA";
        } else if (votosNao > votosSim) {
            resultado = "REPROVADA";
        } else {
            resultado = "EMPATE";
        }

        return new ResultVotingDTO(
                pautaId,
                sessao.getPauta().getTitulo(),
                totalVotos,
                votosSim,
                votosNao,
                resultado
        );
    }
}
