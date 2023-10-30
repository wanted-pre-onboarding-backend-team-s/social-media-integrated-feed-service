package com.wanted.feed.feed.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wanted.feed.common.config.security.SecurityConfig;
import com.wanted.feed.common.util.TokenProvider;
import com.wanted.feed.feed.dto.StatRequestParamDto;
import com.wanted.feed.feed.dto.StatResponseDto;
import com.wanted.feed.feed.service.FeedService;
import com.wanted.feed.user.domain.UserRepository;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StatController.class)
@Import(SecurityConfig.class)
@WithMockUser()
@MockBean(JpaMetamodelMappingContext.class)
class StatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FeedService feedService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expired-ms}")
    private String expiredMs;

    private static final Long USER_ID = 1L;

    private String accessToken;

    @BeforeEach
    void setUp() {
        given(userRepository.existsById(any(Long.class)))
                .willReturn(true);
    }

    @DisplayName("GET /stats")
    @Nested
    class GetStats {

        @BeforeEach
        void setUp() {
            accessToken = TokenProvider.createJwt(USER_ID, secretKey, Long.valueOf(expiredMs))
                    .getToken();
        }

        @DisplayName("반환 JSON 검증")
        @Nested
        class ResponseData {

            @DisplayName("각 날짜 별 통계 조회 결과 JSON 반환")
            @Test
            void dateTimeToStatResponseDto() throws Exception {
                Map<String, StatResponseDto> dateToStatResponseDto = new LinkedHashMap<>();
                dateToStatResponseDto.put("2023-10-11", StatResponseDto.builder()
                        .count(2L)
                        .viewCount(124L)
                        .likeCount(30L)
                        .shareCount(12L)
                        .build()
                );
                dateToStatResponseDto.put("2023-10-12", StatResponseDto.builder()
                        .count(5L)
                        .viewCount(473L)
                        .likeCount(126L)
                        .shareCount(75L)
                        .build()
                );
                given(feedService.getFeedStats(eq(USER_ID), any(StatRequestParamDto.class)))
                        .willReturn(dateToStatResponseDto);

                mockMvc.perform(get("/stats")
                                .header("Authorization", "Bearer " + accessToken)
                                .param("hashtag", "wanted")
                                .param("type", "date")
                                .param("start", "2023-10-11")
                                .param("end", "2023-10-12")
                                .param("value", "count")
                                .param("value", "view_count")
                                .param("value", "like_count")
                                .param("value", "share_count"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("""
                                "2023-10-11":{"count":2,"viewCount":124,"likeCount":30,"shareCount":12}""")))
                        .andExpect(content().string(containsString("""
                                "2023-10-12":{"count":5,"viewCount":473,"likeCount":126,"shareCount":75}""")));
            }
        }

        @DisplayName("RequestParam 정상")
        @Nested
        class ValidRequestParams {

            @BeforeEach
            void setUp() {
                given(feedService.getFeedStats(eq(USER_ID), any(StatRequestParamDto.class)))
                        .willReturn(Map.of());
            }

            @DisplayName("모든 RequestParam 주어지는 경우에는 예외 발생하지 않음")
            @Test
            void givenAllRequestParams() throws Exception {
                mockMvc.perform(get("/stats")
                                .header("Authorization", "Bearer " + accessToken)
                                .param("hashtag", "wanted")
                                .param("type", "hour")
                                .param("start", "2023-10-15")
                                .param("end", "2023-10-20")
                                .param("value", "count"))
                        .andExpect(status().isOk());
            }

            @DisplayName("start 주어지지 않는 경우에는 예외 발생하지 않음")
            @Test
            void exceptStart() throws Exception {
                mockMvc.perform(get("/stats")
                                .header("Authorization", "Bearer " + accessToken)
                                .param("hashtag", "wanted")
                                .param("type", "date")
                                .param("end", "2023-10-20")
                                .param("value", "count"))
                        .andExpect(status().isOk());
            }

            @DisplayName("end 주어지지 않는 경우에는 예외 발생하지 않음")
            @Test
            void exceptEnd() throws Exception {
                mockMvc.perform(get("/stats")
                                .header("Authorization", "Bearer " + accessToken)
                                .param("hashtag", "wanted")
                                .param("type", "date")
                                .param("start", "2023-10-15")
                                .param("value", "count"))
                        .andExpect(status().isOk());
            }

            @DisplayName("value 주어지지 않는 경우에는 예외 발생하지 않음")
            @Test
            void exceptValue() throws Exception {
                mockMvc.perform(get("/stats")
                                .header("Authorization", "Bearer " + accessToken)
                                .param("hashtag", "wanted")
                                .param("type", "date")
                                .param("start", "2023-10-15")
                                .param("end", "2023-10-20"))
                        .andExpect(status().isOk());

                mockMvc.perform(get("/stats")
                                .header("Authorization", "Bearer " + accessToken)
                                .param("hashtag", "wanted")
                                .param("type", "date")
                                .param("start", "2023-10-15")
                                .param("end", "2023-10-20")
                                .param("value", "")
                                .param("value", "    "))
                        .andExpect(status().isOk());
            }
        }

        @DisplayName("잘못된 RequestParam 존재")
        @Nested
        class InvalidRequestParams {

            @DisplayName("type 주어지지 않는 경우 예외 발생")
            @Test
            void exceptType() throws Exception {
                mockMvc.perform(get("/stats")
                                .header("Authorization", "Bearer " + accessToken)
                                .param("hashtag", "wanted")
                                .param("start", "2023-10-15")
                                .param("end", "2023-10-20")
                                .param("value", "count"))
                        .andExpect(status().isBadRequest());

                verify(feedService, never())
                        .getFeedStats(any(Long.class), any(StatRequestParamDto.class));
            }

            @DisplayName("type이 date, hour 중 하나가 아닌 경우 예외 발생")
            @Test
            void invalidType() throws Exception {
                mockMvc.perform(get("/stats")
                                .header("Authorization", "Bearer " + accessToken)
                                .param("hashtag", "wanted")
                                .param("type", "millisecond")
                                .param("start", "2023-10-15")
                                .param("end", "2023-10-20")
                                .param("value", "count"))
                        .andExpect(status().isBadRequest());

                verify(feedService, never())
                        .getFeedStats(any(Long.class), any(StatRequestParamDto.class));
            }

            @DisplayName("start가 yyyy-mm-dd 형식이 아닌 경우 예외 발생")
            @Test
            void invalidStart() throws Exception {
                mockMvc.perform(get("/stats")
                                .header("Authorization", "Bearer " + accessToken)
                                .param("hashtag", "wanted")
                                .param("type", "hour")
                                .param("start", "23-10-15")
                                .param("end", "2023-10-20")
                                .param("value", "count"))
                        .andExpect(status().isBadRequest());

                verify(feedService, never())
                        .getFeedStats(any(Long.class), any(StatRequestParamDto.class));
            }

            @DisplayName("end가 yyyy-mm-dd 형식이 아닌 경우 예외 발생")
            @Test
            void invalidEnd() throws Exception {
                mockMvc.perform(get("/stats")
                                .header("Authorization", "Bearer " + accessToken)
                                .param("hashtag", "wanted")
                                .param("type", "hour")
                                .param("start", "2023-10-15")
                                .param("end", "20th, Oct. 2023")
                                .param("value", "count"))
                        .andExpect(status().isBadRequest());

                verify(feedService, never())
                        .getFeedStats(any(Long.class), any(StatRequestParamDto.class));
            }

            @DisplayName("value에 count, view_count, like_count, share_count가 아닌 "
                    + "내용이 포함된 경우 예외 발생")
            @Test
            void invalidValue() throws Exception {
                mockMvc.perform(get("/stats")
                                .header("Authorization", "Bearer " + accessToken)
                                .param("hashtag", "wanted")
                                .param("type", "hour")
                                .param("start", "2023-10-15")
                                .param("end", "2023-10-20")
                                .param("value", "count")
                                .param("value", "view_count")
                                .param("value", "dislike_count"))
                        .andExpect(status().isBadRequest());

                verify(feedService, never())
                        .getFeedStats(any(Long.class), any(StatRequestParamDto.class));
            }
        }
    }
}
