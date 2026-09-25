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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {
    private final VoteRepository voteRepository;
    private final VotingSessionService votingSessionService;
    private final CpfValidationService cpfValidationService;

    @Override
    public void castVote(Long agendaId, String cpf, OptionVoteEnum option){
        log.info("Iniciando o registro do voto de um associado");

        log.info("Buscando uma sessão por id da pauta");
        var session = votingSessionService.getAgendaId(agendaId);

        log.info("Verificando se a sessão está aberta");
        if (!session.isAberta()){
            throw new RegraNegocioException("A sessão de votação desta pauta encontra-se fechada.");
        }

        log.info("Verificando se o associado já tem voto registrado");
        if (voteRepository.existsByPautaIdAndAssociadoId(agendaId, cpf)){
            throw new RegraNegocioException("O associado já registrou seu voto nesta pauta.");
        }

        Voto vote = Voto.builder()
                .pauta(session.getPauta())
                .associadoId(cpf)
                .opcao(option)
                .build();

        log.info("Registrando o voto do associado: "+cpf);
        voteRepository.save(vote);
        log.info("Voto registrado com sucesso");
    }

    @Override
    public ResultVotingDTO countVotes(Long pautaId) {
        log.info("Iniciando a contagem de votos da sessão");

        log.info("Buscando uma sessão por id da pauta");
        var sessao = votingSessionService.getAgendaId(pautaId);

        log.info("Totalizando os votos");
        long totalVotos = voteRepository.countByPautaId(pautaId);
        long votosSim = voteRepository.countByPautaIdAndOpcao(pautaId, OptionVoteEnum.SIM);
        long votosNao = voteRepository.countByPautaIdAndOpcao(pautaId, OptionVoteEnum.NAO);

        log.info("Montando o resultado");
        String resultado;
        if (votosSim > votosNao) {
            resultado = "APROVADA";
        } else if (votosNao > votosSim) {
            resultado = "REPROVADA";
        } else {
            resultado = "EMPATE";
        }

        log.info("Retornado o resultado");
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
