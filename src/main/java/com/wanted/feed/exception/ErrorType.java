package com.wanted.feed.exception;

import com.wanted.feed.user.exception.DuplicateUserException;
import com.wanted.feed.user.exception.InvalidAuthenticationException;
import com.wanted.feed.user.exception.NotFoundUserException;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    U001("U001", "에러 메시지를 담습니다.", WantedException.class, HttpStatus.NOT_FOUND),
    U002("U002", "사용자를 찾을 수 없습니다.", NotFoundUserException.class, HttpStatus.BAD_REQUEST),
    J001("J001", "중복된 계정입니다.", DuplicateUserException.class, HttpStatus.CONFLICT),
    T001("T001", "유효한 토큰을 입력해주세요.", InvalidAuthenticationException.class, HttpStatus.BAD_REQUEST);

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
