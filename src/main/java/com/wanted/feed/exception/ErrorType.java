package com.wanted.feed.exception;

import com.wanted.feed.user.exception.DuplicateUserException;
import com.wanted.feed.user.exception.InvalidTokenException;
import com.wanted.feed.user.exception.InvalidTypeOfTokenException;
import com.wanted.feed.user.exception.NotFoundUserException;
import com.wanted.feed.user.exception.NullTokenException;
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
    T001("T001", "유효하지 않은 토큰 타입 입니다.", InvalidTypeOfTokenException.class, HttpStatus.BAD_REQUEST),
    T002("T002", "토큰이 입력되지 않았습니다.", NullTokenException.class, HttpStatus.BAD_REQUEST),
    T003("T003", "유효하지 않은 토큰 입니다.", InvalidTokenException.class, HttpStatus.BAD_REQUEST);

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
