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
import com.wanted.feed.user.exception.MismatchAuthCodeException;
import com.wanted.feed.user.exception.MismatchPasswordException;
import com.wanted.feed.user.exception.NotFoundAuthCodeException;
import com.wanted.feed.user.exception.NotFoundUserException;
import com.wanted.feed.user.exception.ApprovedUserException;
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
        User user = userRepository.findByUsername(username)
                                  .orElseThrow(NotFoundUserException::new);

        checkApproval(user);
        checkPassword(approvalRequestDto.getPassword(), user);
        checkAuthCode(approvalRequestDto.getCode(), username);

        user.approveUser();
    }

    private void checkApproval(User user) {
        if(user.isApproved()) {
            throw new ApprovedUserException();
        }
    }

    private void checkPassword(String confirmPassword, User user) {
        if (!passwordEncoder.matches(confirmPassword, user.getPassword())) {
            throw new MismatchPasswordException();
        }
    }

    private void checkAuthCode(String confirmCode, String username) {
        AuthCode authCode = authCodeRepository.findTopByUsernameOrderByCreatedAtDesc(username)
                                              .orElseThrow(NotFoundAuthCodeException::new);

        if (!authCode.getCode().equals(confirmCode)) {
            throw new MismatchAuthCodeException();
        }
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
