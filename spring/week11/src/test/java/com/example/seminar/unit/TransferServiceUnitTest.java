package com.example.seminar.unit;

import com.example.seminar.domain.Account;
import com.example.seminar.exception.AccountNotFoundException;
import com.example.seminar.exception.InvalidTransferException;
import com.example.seminar.repository.AccountRepository;
import com.example.seminar.service.TransferService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class TransferServiceUnitTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferService transferService;

    // 수업 실습 1
    @Test
    @DisplayName("정상적인 계좌 이체에 성공한다")
    void transferSuccess() {
        // Given
        Account sender = new Account(
                1L, "John", new BigDecimal("1000")
        );
        Account receiver = new Account(
                2L, "Jane", new BigDecimal("1000")
        );

        given(accountRepository.findById(1L))
                .willReturn(Optional.of(sender));
        given(accountRepository.findById(2L))
                .willReturn(Optional.of(receiver));

        // When
        transferService.transferMoney(
                1L, 2L, new BigDecimal("100")
        );

        // Then
        assertThat(sender.getAmount())
                .isEqualByComparingTo("900");
        assertThat(receiver.getAmount())
                .isEqualByComparingTo("1100");

        verify(accountRepository).findById(1L);
        verify(accountRepository).findById(2L);
    }

    // 수업 실습 2
    @Test
    @DisplayName("발신인 계좌가 없으면 예외가 발생한다")
    void senderNotFound() {
        // Given
        given(accountRepository.findById(999L))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() ->
                transferService.transferMoney(
                        999L, 2L, new BigDecimal("100")
                )
        )
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Sender");

        verify(accountRepository).findById(999L);
        verify(accountRepository, never()).findById(2L);
    }

    // 수업 실습 3
    @Test
    @DisplayName("수취인 계좌가 없으면 예외가 발생한다")
    void receiverNotFound() {
        // Given
        Account sender = new Account(
                1L, "John", new BigDecimal("1000")
        );

        given(accountRepository.findById(1L))
                .willReturn(Optional.of(sender));
        given(accountRepository.findById(999L))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() ->
                transferService.transferMoney(
                        1L, 999L, new BigDecimal("100")
                )
        )
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Receiver");

        assertThat(sender.getAmount())
                .isEqualByComparingTo("1000");

        verify(accountRepository).findById(1L);
        verify(accountRepository).findById(999L);
    }

    // 과제 1
    @Test
    @DisplayName("잔액이 부족하면 이체에 실패한다")
    void insufficientBalance() {
        // Given
        Account sender = new Account(
                1L, "John", new BigDecimal("100")
        );
        Account receiver = new Account(
                2L, "Jane", new BigDecimal("1000")
        );

        given(accountRepository.findById(1L))
                .willReturn(Optional.of(sender));
        given(accountRepository.findById(2L))
                .willReturn(Optional.of(receiver));

        // When & Then
        assertThatThrownBy(() ->
                transferService.transferMoney(
                        1L, 2L, new BigDecimal("500")
                )
        )
                .isInstanceOf(InvalidTransferException.class)
                .hasMessageContaining("Insufficient balance");

        assertThat(sender.getAmount())
                .isEqualByComparingTo("100");
        assertThat(receiver.getAmount())
                .isEqualByComparingTo("1000");

        verify(accountRepository).findById(1L);
        verify(accountRepository).findById(2L);
    }

    // 과제 2
    @Test
    @DisplayName("음수 금액은 이체할 수 없다")
    void negativeAmount() {
        // When & Then
        assertThatThrownBy(() ->
                transferService.transferMoney(
                        1L, 2L, new BigDecimal("-100")
                )
        )
                .isInstanceOf(InvalidTransferException.class)
                .hasMessageContaining("positive");

        verifyNoInteractions(accountRepository);
    }
}