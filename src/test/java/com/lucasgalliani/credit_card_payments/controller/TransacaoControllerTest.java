package com.lucasgalliani.credit_card_payments.controller;

import com.lucasgalliani.credit_card_payments.dto.DescricaoDto;
import com.lucasgalliani.credit_card_payments.dto.FormaPagamentoDto;
import com.lucasgalliani.credit_card_payments.dto.TransacaoDto;
import com.lucasgalliani.credit_card_payments.dto.TransacaoResponseDto;
import com.lucasgalliani.credit_card_payments.enums.StatusTransacao;
import com.lucasgalliani.credit_card_payments.enums.TipoDePgamento;
import com.lucasgalliani.credit_card_payments.infra.exception.EntityNotFoundException;
import com.lucasgalliani.credit_card_payments.infra.exception.TransacaoNaoAutorizadaException;
import com.lucasgalliani.credit_card_payments.model.FormaDePagamento;
import com.lucasgalliani.credit_card_payments.model.Transacao;
import com.lucasgalliani.credit_card_payments.repository.TransacaoRepository;
import com.lucasgalliani.credit_card_payments.service.TransacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransacaoControllerTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveAutorizarPagamentoQuandoValorMaiorQueZero() {

        DescricaoDto descricao = new DescricaoDto(
                100.0,
                LocalDateTime.now(),
                "Loja Teste",
                "NSU123",
                "COD123",
                null
        );
        FormaPagamentoDto forma = new FormaPagamentoDto(TipoDePgamento.PARCELADO_EMISSOR, 1);
        TransacaoDto dto = new TransacaoDto(1L, "123456789", descricao, forma);


        Transacao transacaoSalva = new Transacao();
        transacaoSalva.setValor(100.0);
        transacaoSalva.setStatus(StatusTransacao.AUTORIZADO);


        FormaDePagamento formaPagamento = new FormaDePagamento();
        formaPagamento.setTipoDePgamento(TipoDePgamento.PARCELADO_EMISSOR);
        formaPagamento.setParcelas(1);
        transacaoSalva.setFormaDePagamento(formaPagamento);

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacaoSalva);


        TransacaoResponseDto response = transacaoService.processarPagamento(dto);


        assertNotNull(response);
        assertEquals(StatusTransacao.AUTORIZADO, response.transacao().descricao().statusTransacao());
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    void deveNegarPagamentoQuandoValorNuloOuZero() {

        DescricaoDto descricao = new DescricaoDto(
                0.0,
                LocalDateTime.now(),
                "Loja Teste",
                "NSU123",
                "COD123",
                null
        );
        FormaPagamentoDto forma = new FormaPagamentoDto(TipoDePgamento.AVISTA, 1);
        TransacaoDto dto = new TransacaoDto(1L, "123456789", descricao, forma);

        Transacao transacaoSalva = new Transacao();
        transacaoSalva.setValor(0.0);
        transacaoSalva.setStatus(StatusTransacao.NEGADO);

        // Preenche a forma de pagamento para evitar NPE
        FormaDePagamento formaPagamento = new FormaDePagamento();
        formaPagamento.setTipoDePgamento(TipoDePgamento.AVISTA);
        formaPagamento.setParcelas(1);
        transacaoSalva.setFormaDePagamento(formaPagamento);

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacaoSalva);


        TransacaoResponseDto response = transacaoService.processarPagamento(dto);


        assertNotNull(response);
        assertEquals(StatusTransacao.NEGADO, response.transacao().descricao().statusTransacao());
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    void deveEstornarPagamentoAutorizado() {
        Long id = 1L;
        Transacao transacao = new Transacao();
        transacao.setValor(100.0);
        transacao.setStatus(StatusTransacao.AUTORIZADO);

        FormaDePagamento forma = new FormaDePagamento();
        forma.setTipoDePgamento(null);
        forma.setParcelas(1);
        transacao.setFormaDePagamento(forma);

        when(transacaoRepository.findById(id)).thenReturn(Optional.of(transacao));
        when(transacaoRepository.save(transacao)).thenReturn(transacao);


        TransacaoResponseDto response = transacaoService.estornarPagamento(id);


        assertNotNull(response);
        assertEquals(StatusTransacao.CANCELADO, response.transacao().descricao().statusTransacao());
        verify(transacaoRepository, times(1)).findById(id);
        verify(transacaoRepository, times(1)).save(transacao);
    }

    @Test
    void deveLancarExceptionQuandoTransacaoNaoAutorizada() {

        Long id = 2L;
        Transacao transacao = new Transacao();
        transacao.setValor(50.0);
        transacao.setStatus(StatusTransacao.NEGADO);

        when(transacaoRepository.findById(id)).thenReturn(Optional.of(transacao));


        TransacaoNaoAutorizadaException exception = assertThrows(
                TransacaoNaoAutorizadaException.class,
                () -> transacaoService.estornarPagamento(id)
        );

        assertEquals("Só é possível estornar transações autorizadas!", exception.getMessage());
        verify(transacaoRepository, times(1)).findById(id);
        verify(transacaoRepository, never()).save(any());
    }

    @Test
    void deveListarTodasAsTransacoes() {

        Transacao t1 = new Transacao();
        t1.setValor(100.0);
        FormaDePagamento f1 = new FormaDePagamento();
        f1.setParcelas(1);
        t1.setFormaDePagamento(f1);

        Transacao t2 = new Transacao();
        t2.setValor(50.0);
        FormaDePagamento f2 = new FormaDePagamento();
        f2.setParcelas(2);
        t2.setFormaDePagamento(f2);

        when(transacaoRepository.findAll()).thenReturn(Arrays.asList(t1, t2));


        List<TransacaoResponseDto> lista = transacaoService.listarTodos();


        assertNotNull(lista);
        assertEquals(2, lista.size());
        verify(transacaoRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarTransacaoPorIdQuandoExistir() {
        // Arrange
        Long id = 1L;
        Transacao transacao = new Transacao();
        transacao.setValor(100.0);
        FormaDePagamento forma = new FormaDePagamento();
        forma.setParcelas(1);
        transacao.setFormaDePagamento(forma);

        when(transacaoRepository.findById(id)).thenReturn(Optional.of(transacao));


        TransacaoResponseDto response = transacaoService.buscarPorId(id);


        assertNotNull(response);
        verify(transacaoRepository, times(1)).findById(id);
    }

    @Test
    void deveLancarExceptionQuandoTransacaoNaoExistir() {

        Long id = 2L;
        when(transacaoRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> transacaoService.buscarPorId(id));

        assertEquals("Transação não encontrada!", exception.getMessage());
        verify(transacaoRepository, times(1)).findById(id);
    }

}
