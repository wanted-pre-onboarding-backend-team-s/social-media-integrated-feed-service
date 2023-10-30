package com.wanted.feed.feign;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wanted.feed.feign.exception.SnsNotSupportException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("SnsType Enum 테스트")
class SnsTypeTest {

    @ParameterizedTest
    @MethodSource("snsTypeProvider")
    @DisplayName("snsType 으로 해당되는 SnsType 을 찾는다. - 성공")
    void findType(String snsType, SnsType expected) {
        SnsType findType = SnsType.findType(snsType);
        assertThat(findType).isEqualTo(expected);
    }

    @Test
    @DisplayName("지원하지 않는 sns 타입이 올 경우 예외를 던진다. - 실패")
    void throwExceptionWhenNotSupportedSnsType() {
        assertThatThrownBy(() -> SnsType.findType("kakao"))
                .isInstanceOf(SnsNotSupportException.class);
    }

    static Stream<Arguments> snsTypeProvider() {
        return Stream.of(
                Arguments.of("twitter", SnsType.TWITTER),
                Arguments.of("facebook", SnsType.FACEBOOK),
                Arguments.of("instagram", SnsType.INSTAGRAM),
                Arguments.of("threads", SnsType.THREADS)
        );
    }
}
