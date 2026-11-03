package com.example.seminar.integration;

import com.example.seminar.domain.Account;
import com.example.seminar.exception.AccountNotFoundException;
import com.example.seminar.repository.AccountRepository;
import com.example.seminar.service.TransferService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TransferServiceIntegrationTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    void cleanup() {
        accountRepository.deleteAll();
    }

    // 수업 실습 4
    @Test
    @DisplayName("실제 DB에서 정상적인 계좌 이체가 이루어진다")
    void transferSuccess() {
        // Given
        Account sender = accountRepository.save(
                new Account("John", new BigDecimal("1000"))
        );
        Account receiver = accountRepository.save(
                new Account("Jane", new BigDecimal("1000"))
        );

        Long senderId = sender.getId();
        Long receiverId = receiver.getId();

        // When
        transferService.transferMoney(
                senderId, receiverId, new BigDecimal("100")
        );

        // Then
        Account updatedSender = accountRepository.findById(senderId)
                .orElseThrow();
        Account updatedReceiver = accountRepository.findById(receiverId)
                .orElseThrow();

        assertThat(updatedSender.getAmount())
                .isEqualByComparingTo("900");
        assertThat(updatedReceiver.getAmount())
                .isEqualByComparingTo("1100");
    }

    // 수업 실습 5
    @Test
    @DisplayName("발신인 계좌가 없으면 예외가 발생한다")
    void senderNotFound() {
        // Given
        Long missingSenderId = 999L;

        assertThat(accountRepository.existsById(missingSenderId))
                .isFalse();

        // When & Then
        assertThatThrownBy(() ->
                transferService.transferMoney(
                        missingSenderId, 2L, new BigDecimal("100")
                )
        )
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Sender");
    }

    // 과제 3
    @Test
    @DisplayName("수취인 계좌가 없으면 잔액이 변경되지 않는다")
    void receiverNotFound() {
        // Given
        Account sender = accountRepository.save(
                new Account("John", new BigDecimal("1000"))
        );

        Long senderId = sender.getId();
        Long missingReceiverId = 999L;

        assertThat(accountRepository.existsById(missingReceiverId))
                .isFalse();

        // When & Then
        assertThatThrownBy(() ->
                transferService.transferMoney(
                        senderId, missingReceiverId, new BigDecimal("100")
                )
        )
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Receiver");

        Account unchangedSender = accountRepository.findById(senderId)
                .orElseThrow();

        assertThat(unchangedSender.getAmount())
                .isEqualByComparingTo("1000");
    }
}