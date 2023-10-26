package com.wanted.feed.user.service;

import com.wanted.feed.exception.ErrorType;
import com.wanted.feed.exception.WantedException;
import com.wanted.feed.user.domain.AuthCode;
import com.wanted.feed.user.domain.AuthCodeRepository;
import com.wanted.feed.user.domain.User;
import com.wanted.feed.user.domain.UserRepository;
import com.wanted.feed.user.dto.JoinRequestDto;
import com.wanted.feed.user.dto.JoinResponseDto;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final AuthCodeRepository authCodeRepository;
    private final PasswordEncoder passwordEncoder;

    public JoinService(UserRepository userRepository,
            AuthCodeRepository authCodeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authCodeRepository = authCodeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public JoinResponseDto join(JoinRequestDto joinRequest) {
        validateDuplicateUsername(joinRequest.getUsername());

        User user = userRepository.save(User.builder()
                                            .username(joinRequest.getUsername())
                                            .email(joinRequest.getEmail())
                                            .password(passwordEncoder.encode(joinRequest.getPassword()))
                                            .build());

        String randomCode = createCode();
        saveCode(joinRequest, randomCode);

        return new JoinResponseDto(user.getUsername(), randomCode);
    }

    private void validateDuplicateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new WantedException(ErrorType.CONFLICT_USERNAME);
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
            throw new WantedException(ErrorType.INTERNAL_SERVER_ERROR);
        }
    }
}
