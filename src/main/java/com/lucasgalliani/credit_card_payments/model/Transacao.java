package com.lucasgalliani.credit_card_payments.model;

import com.lucasgalliani.credit_card_payments.enums.StatusTransacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_transacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Transacao {

    private Long id;
    private String cartao;
    private String estabelecimento;
    private LocalDate dataHora;
    private String nsu;
    private String codigoAutorizacao;
    @Enumerated(EnumType.STRING)
    private StatusTransacao status;
    @Embedded
    private FormaDePagamento formaDePagamento;



}
