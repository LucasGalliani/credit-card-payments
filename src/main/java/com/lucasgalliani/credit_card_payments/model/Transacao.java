package com.lucasgalliani.credit_card_payments.model;

import com.lucasgalliani.credit_card_payments.enums.StatusTransacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_transacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cartao;
    private String estabelecimento;
    private LocalDateTime dataHora;
    private Double valor;
    private String nsu;
    private String codigoAutorizacao;
    @Enumerated(EnumType.STRING)
    private StatusTransacao status;
    @Embedded
    private FormaDePagamento formaDePagamento;



}
