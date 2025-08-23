package com.lucasgalliani.credit_card_payments.service;


import com.lucasgalliani.credit_card_payments.dto.TransacaoDto;
import com.lucasgalliani.credit_card_payments.dto.TransacaoResponseDto;
import com.lucasgalliani.credit_card_payments.enums.StatusTransacao;
import com.lucasgalliani.credit_card_payments.infra.exception.EntityNotFoundException;
import com.lucasgalliani.credit_card_payments.infra.exception.TransacaoNaoAutorizadaException;
import com.lucasgalliani.credit_card_payments.mapper.TransacaoMapper;
import com.lucasgalliani.credit_card_payments.model.FormaDePagamento;
import com.lucasgalliani.credit_card_payments.model.Transacao;
import com.lucasgalliani.credit_card_payments.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;


    @Transactional
    public TransacaoResponseDto processarPagamento(TransacaoDto dto) {

        Transacao transacao = new Transacao();

        transacao.setCartao(dto.cartao());
        transacao.setValor(dto.descricao().valor());
        transacao.setEstabelecimento(dto.descricao().estabelecimento());
        transacao.setDataHora(LocalDateTime.now());
        transacao.setNsu(gerarNSU());
        transacao.setCodigoAutorizacao(gerarCodigoAutorizacao());

        FormaDePagamento forma = new FormaDePagamento();
        forma.setTipoDePgamento(dto.formaPagamento().tipo());
        forma.setParcelas(dto.formaPagamento().parcelas());
        transacao.setFormaDePagamento(forma);



        if (transacao.getValor() != null && transacao.getValor() > 0) {
            transacao.setStatus(StatusTransacao.AUTORIZADO);
        } else {
            transacao.setStatus(StatusTransacao.NEGADO);
        }

        Transacao save = transacaoRepository.save(transacao);

        System.out.println("Status depois do save: " + save.getStatus());

        return TransacaoMapper.toResponse(save);

    }

    @Transactional
    public TransacaoResponseDto estornarPagamento(Long id) {

        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada!"));


        if (transacao.getStatus() == StatusTransacao.AUTORIZADO) {
            transacao.setStatus(StatusTransacao.CANCELADO);
            Transacao save = transacaoRepository.save(transacao);
            return TransacaoMapper.toResponse(save);
        } else {
            throw new TransacaoNaoAutorizadaException("Só é possível estornar transações autorizadas!");
        }
    }

    public List<TransacaoResponseDto> listarTodos() {

        return transacaoRepository.findAll()
                .stream()
                .map(TransacaoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TransacaoResponseDto buscarPorId(Long id) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada!"));

        return TransacaoMapper.toResponse(transacao);
    }


    private String gerarNSU() {
        return String.valueOf(System.currentTimeMillis());
    }

    private String gerarCodigoAutorizacao() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }


}
