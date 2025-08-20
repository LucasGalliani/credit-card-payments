package com.lucasgalliani.credit_card_payments.model;

import com.lucasgalliani.credit_card_payments.enums.TipoDePgamento;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class FormaDePagamento {

    @Enumerated(EnumType.STRING)
    private TipoDePgamento tipoDePgamento;
    private Integer parcelas;
}
