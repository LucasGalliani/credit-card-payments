package com.lucasgalliani.credit_card_payments.controller;

import com.lucasgalliani.credit_card_payments.dto.TransacaoDto;
import com.lucasgalliani.credit_card_payments.model.Transacao;
import com.lucasgalliani.credit_card_payments.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping("/pagamentos")
    public ResponseEntity<Transacao> pagar(@RequestBody TransacaoDto transacaoDto) {
        return ResponseEntity.ok(transacaoService.processarPagamento(transacaoDto));
    }

    @PostMapping("/estornos/{id}")
    public ResponseEntity<Transacao> estorno(@PathVariable Long id) {
        return ResponseEntity.ok(transacaoService.estornarPagamento(id));
    }

    @GetMapping("/transacoes")
    public ResponseEntity<List<Transacao>> listar() {
        return ResponseEntity.ok(transacaoService.listarTodos());
    }

    @GetMapping("/transacoes/{id}")
    public ResponseEntity<Transacao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transacaoService.buscarPorId(id));
    }

}
