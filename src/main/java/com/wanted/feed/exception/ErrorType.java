package com.wanted.feed.exception;

import com.wanted.feed.user.exception.DuplicateUserException;
import com.wanted.feed.user.exception.MismatchAuthCodeException;
import com.wanted.feed.user.exception.MismatchPasswordException;
import com.wanted.feed.user.exception.NotFoundAuthCodeException;
import com.wanted.feed.user.exception.NotFoundUsernameException;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    U001("U001", "에러 메시지를 담습니다.", WantedException.class, HttpStatus.NOT_FOUND),
    U002("U002", "이미 승인된 계정이거나 없는 계정입니다.", NotFoundUsernameException.class, HttpStatus.NOT_FOUND),
    U003("U003", "중복된 계정입니다.", DuplicateUserException.class, HttpStatus.CONFLICT),
    U004("U004", "잘못된 비밀번호입니다.", MismatchPasswordException.class, HttpStatus.NOT_FOUND),
    U005("U005", "아직 인증코드를 받지 않으셨습니다.", NotFoundAuthCodeException.class, HttpStatus.NOT_FOUND),
    U006("U006", "잘못된 인증코드입니다.", MismatchAuthCodeException.class, HttpStatus.NOT_FOUND);


    private final String code;
    private final String message;
    private final Class<? extends WantedException> classType;
    private final HttpStatus httpStatus;
    private static final List<ErrorType> errorTypes = Arrays.stream(ErrorType.values()).toList();

    public static ErrorType of(Class<? extends WantedException> classType) {
        return errorTypes.stream()
                         .filter(it -> it.classType.equals(classType))
                         .findFirst()
                         .orElseThrow(RuntimeException::new);
    }
}
