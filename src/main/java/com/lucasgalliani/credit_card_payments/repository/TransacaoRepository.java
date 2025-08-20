package com.lucasgalliani.credit_card_payments.repository;

import com.lucasgalliani.credit_card_payments.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}
