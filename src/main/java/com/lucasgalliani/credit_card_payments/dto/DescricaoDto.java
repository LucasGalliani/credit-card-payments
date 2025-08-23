package com.lucasgalliani.credit_card_payments.dto;

import com.lucasgalliani.credit_card_payments.enums.StatusTransacao;

import java.time.LocalDateTime;

public record DescricaoDto(
        Double valor,
        LocalDateTime dataHora,
        String estabelecimento,
        String nsu,
        String codigoAutorizacao,
        StatusTransacao statusTransacao

) {
}
