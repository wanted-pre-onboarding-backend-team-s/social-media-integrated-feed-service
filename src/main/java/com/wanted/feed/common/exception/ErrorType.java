package com.wanted.feed.common.exception;

import com.wanted.feed.feign.exception.SnsShareFeedFailException;
import com.wanted.feed.feed.exception.DateRangeExceeds30Days;
import com.wanted.feed.feed.exception.DateRangeExceeds7Days;
import com.wanted.feed.feed.exception.FeedNotFoundException;
import com.wanted.feed.user.exception.ApprovedUserException;
import com.wanted.feed.feign.exception.SnsContentIdNotNullException;
import com.wanted.feed.feign.exception.SnsLikeFeedFailException;
import com.wanted.feed.feign.exception.SnsNotSupportException;
import com.wanted.feed.feed.exception.like.LikeFeedIdNotNullException;
import com.wanted.feed.feed.exception.like.LikeUserIdNotNullException;
import com.wanted.feed.feed.exception.share.ShareFeedIdNotNullException;
import com.wanted.feed.feed.exception.share.ShareUserIdNotNullException;
import com.wanted.feed.feed.exception.EndDateInvalidException;
import com.wanted.feed.feed.exception.StartDateInvalidException;
import com.wanted.feed.feed.exception.StartIsAfterEndException;
import com.wanted.feed.feed.exception.TypeInvalidException;
import com.wanted.feed.feed.exception.TypeNullException;
import com.wanted.feed.feed.exception.ValueInvalidException;
import com.wanted.feed.user.exception.DuplicateUserException;
import com.wanted.feed.user.exception.ExpiredTokenException;
import com.wanted.feed.user.exception.InvalidTokenException;
import com.wanted.feed.user.exception.InvalidTypeOfTokenException;
import com.wanted.feed.user.exception.NotFoundUserException;
import com.wanted.feed.user.exception.NullTokenException;
import com.wanted.feed.user.exception.MismatchAuthCodeException;
import com.wanted.feed.user.exception.MismatchPasswordException;
import com.wanted.feed.user.exception.NotFoundAuthCodeException;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    U001("U001", "계정을 찾을 수 없습니다.", NotFoundUserException.class, HttpStatus.BAD_REQUEST),
    U002("U002", "중복된 계정입니다.", DuplicateUserException.class, HttpStatus.CONFLICT),
    U003("U003", "잘못된 비밀번호입니다.", MismatchPasswordException.class, HttpStatus.NOT_FOUND),
    U004("U004", "아직 인증코드를 받지 않으셨습니다.", NotFoundAuthCodeException.class, HttpStatus.NOT_FOUND),
    U005("U005", "잘못된 인증코드입니다.", MismatchAuthCodeException.class, HttpStatus.NOT_FOUND),
    U006("U006", "이미 승인된 계정입니다.", ApprovedUserException.class, HttpStatus.NOT_FOUND),

    F001("F001", "피드가 존재하지 않습니다.", FeedNotFoundException.class, HttpStatus.NOT_FOUND),

    L001("L001", "FeedId는 Null 이 될 수 없습니다.", LikeFeedIdNotNullException.class, HttpStatus.INTERNAL_SERVER_ERROR),
    L002("L002", "UserId는 Null 이 될 수 없습니다.", LikeUserIdNotNullException.class, HttpStatus.INTERNAL_SERVER_ERROR),

    S001("S001", "지원하지 않는 소셜미디어 피드 입니다.", SnsNotSupportException.class, HttpStatus.INTERNAL_SERVER_ERROR),
    S002("S002", "contendId 는 필수 입니다.", SnsContentIdNotNullException.class, HttpStatus.INTERNAL_SERVER_ERROR),
    S003("S003", "외부 SNS 서비스의 문제가 발생하였습니다.", SnsLikeFeedFailException.class, HttpStatus.INTERNAL_SERVER_ERROR),
    S004("S004", "외부 SNS 서비스의 문제가 발생하였습니다.", SnsShareFeedFailException.class, HttpStatus.INTERNAL_SERVER_ERROR),

    T001("T001", "토큰이 입력되지 않았습니다.", NullTokenException.class, HttpStatus.BAD_REQUEST),
    T002("T002", "유효하지 않은 토큰 타입 입니다.", InvalidTypeOfTokenException.class, HttpStatus.BAD_REQUEST),
    T003("T003", "만료된 토큰 입니다.", ExpiredTokenException.class, HttpStatus.BAD_REQUEST),
    T004("T004", "유효하지 않은 토큰 입니다.", InvalidTokenException.class, HttpStatus.BAD_REQUEST),

    H001("H001", "FeedId는 Null 이 될 수 없습니다.", ShareFeedIdNotNullException.class, HttpStatus.INTERNAL_SERVER_ERROR),
    H002("H002", "UserId는 Null 이 될 수 없습니다.", ShareUserIdNotNullException.class, HttpStatus.INTERNAL_SERVER_ERROR),

    ST002("ST002", "타입이 포함되어야 합니다.", TypeNullException.class, HttpStatus.BAD_REQUEST),
    ST003("ST003", "타입은 date, hour 중 하나여야 합니다.", TypeInvalidException.class, HttpStatus.BAD_REQUEST),
    ST004("ST004", "시작일은 yyyy-mm-dd 형식이어야 합니다.", StartDateInvalidException.class, HttpStatus.BAD_REQUEST),
    ST005("ST005", "종료일은 yyyy-mm-dd 형식이어야 합니다.", EndDateInvalidException.class, HttpStatus.BAD_REQUEST),
    ST006("ST006", "올바르지 않은 결과요소가 포함되어 있습니다.", ValueInvalidException.class, HttpStatus.BAD_REQUEST),
    ST007("ST007", "시작일이 종료일보다 앞섭니다.", StartIsAfterEndException.class, HttpStatus.BAD_REQUEST),
    ST008("ST008", "조회일자 범위가 30일을 초과합니다.", DateRangeExceeds30Days.class, HttpStatus.BAD_REQUEST),
    ST009("ST009", "조회일자 범위가 7일을 초과합니다.", DateRangeExceeds7Days.class, HttpStatus.BAD_REQUEST);

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
