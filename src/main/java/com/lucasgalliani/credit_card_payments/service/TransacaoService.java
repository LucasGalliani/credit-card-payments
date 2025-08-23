package com.lucasgalliani.credit_card_payments.service;


import com.lucasgalliani.credit_card_payments.dto.TransacaoDto;
import com.lucasgalliani.credit_card_payments.enums.StatusTransacao;
import com.lucasgalliani.credit_card_payments.exception.EntityNotFoundException;
import com.lucasgalliani.credit_card_payments.exception.TransacaoNaoAutorizadaException;
import com.lucasgalliani.credit_card_payments.model.FormaDePagamento;
import com.lucasgalliani.credit_card_payments.model.Transacao;
import com.lucasgalliani.credit_card_payments.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Transactional
    public Transacao processarPagamento(TransacaoDto dto) {

        Transacao transacao = new Transacao();

        transacao.setCartao(dto.cartao());
        transacao.setValor(dto.valor());
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

        return transacaoRepository.save(transacao);

    }

    @Transactional
    public Transacao estornarPagamento(Long id) {

        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada!"));

        if (transacao.getStatus() == StatusTransacao.AUTORIZADO) {
            transacao.setStatus(StatusTransacao.CANCELADO);
            return transacaoRepository.save(transacao);
        } else {
            throw new TransacaoNaoAutorizadaException("Só é possível estornar transações autorizadas!");
        }
    }

    public List<Transacao> listarTodos() {
        return transacaoRepository.findAll();
    }

    public Transacao buscarPorId(Long id) {
        return transacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada!"));
    }


    private String gerarNSU() {
        return String.valueOf(System.currentTimeMillis());
    }

    private String gerarCodigoAutorizacao() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }


}
