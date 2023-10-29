package com.wanted.feed.exception;

import com.wanted.feed.feed.exception.FeedNotFoundException;
import com.wanted.feed.exception.client.SnsContentIdNotNullException;
import com.wanted.feed.exception.client.SnsLikeFeedFailException;
import com.wanted.feed.exception.client.SnsNotSupportException;
import com.wanted.feed.exception.feed.like.LikeFeedIdNotNullException;
import com.wanted.feed.exception.feed.like.LikeUserIdNotNullException;
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
    J001("J001", "중복된 계정입니다.", DuplicateUserException.class, HttpStatus.CONFLICT),
    F001("F001", "피드가 존재하지 않습니다.", FeedNotFoundException.class, HttpStatus.NOT_FOUND),
    L001("L001", "FeedId는 Null 이 될 수 없습니다.", LikeFeedIdNotNullException.class, HttpStatus.INTERNAL_SERVER_ERROR),
    L002("L002", "UserId는 Null 이 될 수 없습니다.", LikeUserIdNotNullException.class, HttpStatus.INTERNAL_SERVER_ERROR),
    S001("S001", "지원하지 않는 소셜미디어 피드 입니다.", SnsNotSupportException.class, HttpStatus.INTERNAL_SERVER_ERROR),
    S002("S002", "contendId 는 필수 입니다.", SnsContentIdNotNullException.class, HttpStatus.INTERNAL_SERVER_ERROR),
    S003("S003", "외부 SNS 서비스의 문제가 발생하였습니다.", SnsLikeFeedFailException.class, HttpStatus.INTERNAL_SERVER_ERROR),
    U002("U002", "사용자를 찾을 수 없습니다.", NotFoundUserException.class, HttpStatus.BAD_REQUEST),
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
