package com.wanted.feed.exception;

import com.wanted.feed.feed.exception.FeedNotFoundException;
import com.wanted.feed.exception.client.SnsContentIdNotNullException;
import com.wanted.feed.exception.client.SnsLikeFeedFailException;
import com.wanted.feed.exception.client.SnsNotSupportException;
import com.wanted.feed.exception.feed.like.LikeFeedIdNotNullException;
import com.wanted.feed.exception.feed.like.LikeUserIdNotNullException;
import com.wanted.feed.user.exception.DuplicateUserException;
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
  
    L001("L001", "FeedId는 Null 이 될 수 없습니다.", LikeFeedIdNotNullException.class,
            HttpStatus.INTERNAL_SERVER_ERROR),
    L002("L002", "UserId는 Null 이 될 수 없습니다.", LikeUserIdNotNullException.class,
            HttpStatus.INTERNAL_SERVER_ERROR),

    S001("S001", "지원하지 않는 소셜미디어 피드 입니다.", SnsNotSupportException.class,
            HttpStatus.INTERNAL_SERVER_ERROR),
    S002("S002", "contendId 는 필수 입니다.", SnsContentIdNotNullException.class,
            HttpStatus.INTERNAL_SERVER_ERROR),

    S003("S003", "외부 SNS 서비스의 문제가 발생하였습니다.", SnsLikeFeedFailException.class,
            HttpStatus.INTERNAL_SERVER_ERROR);
  
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
