package com.lucasgalliani.credit_card_payments.dto;

import com.lucasgalliani.credit_card_payments.enums.TipoDePgamento;

public record FormaPagamentoDto(TipoDePgamento tipo,
                                Integer parcelas) {
}
