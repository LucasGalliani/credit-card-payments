package com.lucasgalliani.credit_card_payments.mapper;

import com.lucasgalliani.credit_card_payments.dto.DescricaoDto;
import com.lucasgalliani.credit_card_payments.dto.FormaPagamentoDto;
import com.lucasgalliani.credit_card_payments.dto.TransacaoDto;
import com.lucasgalliani.credit_card_payments.dto.TransacaoResponseDto;
import com.lucasgalliani.credit_card_payments.model.Transacao;

public class TransacaoMapper {

    public static TransacaoResponseDto toResponse(Transacao transacao) {


        DescricaoDto descricao = new DescricaoDto(
                transacao.getValor(),
                transacao.getDataHora(),
                transacao.getEstabelecimento(),
                transacao.getNsu(),
                transacao.getCodigoAutorizacao(),
                transacao.getStatus()
        );

        FormaPagamentoDto formaPagamento = new FormaPagamentoDto(
                transacao.getFormaDePagamento().getTipoDePgamento(),
                transacao.getFormaDePagamento().getParcelas()
        );

        TransacaoDto transacaoDto = new TransacaoDto(
                transacao.getId(),
                transacao.getCartao(),
                descricao,
                formaPagamento
        );

        return new TransacaoResponseDto(transacaoDto);
    }
}
