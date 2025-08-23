    package com.lucasgalliani.credit_card_payments.dto;

    public record TransacaoDto(
            Long id,
            String cartao,
            DescricaoDto descricao,
            FormaPagamentoDto formaPagamento) {
    }