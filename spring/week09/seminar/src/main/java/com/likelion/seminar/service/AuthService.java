package com.likelion.seminar.service;

import com.likelion.seminar.entity.Member;
import com.likelion.seminar.jwt.JwtTokenProvider;
import com.likelion.seminar.repository.MemberRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenResponse login(String username, String password) {
        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "아이디와 비밀번호를 입력해 주세요."
            );
        }

        Member member = memberRepository.findForUpdate(username)
                .orElseThrow(() ->
                        unauthorized("아이디 또는 비밀번호가 잘못되었습니다.")
                );

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw unauthorized("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        return issueTokens(member);
    }

    @Transactional
    public TokenResponse refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw unauthorized("Refresh Token이 필요합니다.");
        }

        String username;

        try {
            username = jwtTokenProvider.verifyAndGetUsername(
                    refreshToken,
                    "refresh"
            );
        } catch (JwtException | IllegalArgumentException e) {
            throw unauthorized("유효하지 않거나 만료된 Refresh Token입니다.");
        }

        Member member = memberRepository.findForUpdate(username)
                .orElseThrow(() ->
                        unauthorized("존재하지 않는 회원입니다.")
                );

        String savedHash = member.getRefreshTokenHash();
        String requestedHash = hash(refreshToken);

        if (savedHash == null || !savedHash.equals(requestedHash)) {
            throw unauthorized("이미 교체되었거나 사용할 수 없는 Refresh Token입니다.");
        }

        return issueTokens(member);
    }

    private TokenResponse issueTokens(Member member) {
        String accessToken = jwtTokenProvider.createAccessToken(
                member.getUsername()
        );

        String refreshToken = jwtTokenProvider.createRefreshToken(
                member.getUsername()
        );

        member.changeRefreshTokenHash(hash(refreshToken));

        return new TokenResponse(
                "Bearer",
                accessToken,
                refreshToken
        );
    }

    private String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("토큰 해시 생성에 실패했습니다.", e);
        }
    }

    private ResponseStatusException unauthorized(String message) {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                message
        );
    }

    public record TokenResponse(
            String tokenType,
            String accessToken,
            String refreshToken
    ) {
    }
}
