package com.wanted.feed.exception;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    U001("U001", "에러 메시지를 담습니다.", WantedException.class, HttpStatus.NOT_FOUND),

    CONFLICT_USERNAME("409", "중복된 계정입니다.", WantedException.class, HttpStatus.CONFLICT),
    INTERNAL_SERVER_ERROR("500", "정의되지 않은 에러가 발생했습니다.", WantedException.class, HttpStatus.INTERNAL_SERVER_ERROR);

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
