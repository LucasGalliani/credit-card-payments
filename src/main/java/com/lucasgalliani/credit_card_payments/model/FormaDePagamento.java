package com.lucasgalliani.credit_card_payments.model;

import com.lucasgalliani.credit_card_payments.enums.TipoDePgamento;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormaDePagamento {

    @Enumerated(EnumType.STRING)
    private TipoDePgamento tipoDePgamento;
    private Integer parcelas;
}
