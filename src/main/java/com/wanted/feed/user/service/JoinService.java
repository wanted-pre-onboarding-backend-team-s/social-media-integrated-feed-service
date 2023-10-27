package com.wanted.feed.user.service;

import com.wanted.feed.exception.WantedException;
import com.wanted.feed.user.domain.AuthCode;
import com.wanted.feed.user.domain.AuthCodeRepository;
import com.wanted.feed.user.domain.User;
import com.wanted.feed.user.domain.UserRepository;
import com.wanted.feed.user.dto.ApprovalRequestDto;
import com.wanted.feed.user.dto.JoinRequestDto;
import com.wanted.feed.user.dto.JoinResponseDto;
import com.wanted.feed.user.exception.DuplicateUserException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final AuthCodeRepository authCodeRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JoinResponseDto join(JoinRequestDto joinRequest) {
        validateDuplicateUsername(joinRequest.getUsername());

        User user = userRepository.save(joinRequest.toEntity());
        user.changePassword(passwordEncoder.encode(joinRequest.getPassword()));

        String randomCode = createCode();
        saveCode(joinRequest, randomCode);

        return new JoinResponseDto(user.getUsername(), randomCode);
    }

    @Transactional
    public void approve(ApprovalRequestDto approvalRequestDto) {
        String username = approvalRequestDto.getUsername();
        User user = userRepository.findByUsernameAndApproved(username, false)
                                  .orElseThrow(WantedException::new);

        if (!passwordEncoder.matches(approvalRequestDto.getPassword(), user.getPassword())) {
            throw new WantedException();
        }

        AuthCode authCode = authCodeRepository.findTopByUsernameOrderByCreatedAtDesc(username)
                                              .orElseThrow(WantedException::new);

        if (!authCode.getCode().equals(approvalRequestDto.getCode())) {
            throw new WantedException();
        }

        user.approveUser();
    }

    private void validateDuplicateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUserException();
        }
    }

    private void saveCode(JoinRequestDto joinRequest, String randomCode) {
        authCodeRepository.save(AuthCode.builder()
                                        .username(joinRequest.getUsername())
                                        .code(randomCode)
                                        .build());
    }

    private String createCode() {
        int length = 6;

        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new WantedException();
        }
    }
}
