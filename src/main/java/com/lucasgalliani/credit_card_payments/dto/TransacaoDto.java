package com.lucasgalliani.credit_card_payments.dto;

import com.lucasgalliani.credit_card_payments.enums.TipoDePgamento;

public record TransacaoDto(
        String cartao,
        Double valor,
        DescricaoDto descricao,
        FormaPagamentoDto formaPagamento){}