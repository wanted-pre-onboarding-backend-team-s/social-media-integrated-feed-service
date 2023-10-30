package com.wanted.feed.feed.domain;

import static com.wanted.feed.feed.domain.QFeed.feed;
import static com.wanted.feed.feed.domain.QFeedToHashtag.feedToHashtag;
import static com.wanted.feed.feed.domain.QHashtag.hashtag;
import static com.wanted.feed.feed.domain.enums.SortDirectionType.ASC;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.feed.feed.domain.enums.FeedOrderByType;
import com.wanted.feed.feed.domain.enums.FeedSearchByType;
import com.wanted.feed.feed.domain.enums.SortDirectionType;
import com.wanted.feed.feed.dto.SearchFeedRequestDto;
import com.wanted.feed.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Feed> findFeedListBySearch(User loginUser, SearchFeedRequestDto filter,
        Pageable pageable) {
        List<Feed> contents = jpaQueryFactory
            .selectFrom(feed)
            .where(
                hashTagEq(loginUser, filter.getHashtag()),
                searchEq(filter.getSearch(), filter.getSearchBy()),
                typeEq(filter.getType())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderBy(filter.getOrderBy(), filter.getSortDirection()))
            .fetch();

        JPAQuery<Feed> countQuery = jpaQueryFactory
            .selectFrom(feed)
            .where(
                hashTagEq(loginUser, filter.getHashtag()),
                searchEq(filter.getSearch(), filter.getSearchBy()),
                typeEq(filter.getType())
            );

        return PageableExecutionUtils.getPage(contents, pageable, () -> countQuery.fetch().size());
    }

    private OrderSpecifier<?>[] getOrderBy(FeedOrderByType orderByType,
        SortDirectionType sortDirectionType) {
        Order order =
            (Objects.nonNull(sortDirectionType) && ASC.equals(sortDirectionType)) ? Order.ASC
                : Order.DESC;

        List<OrderSpecifier<?>> orderSpecifierList = new ArrayList<>();

        // orderByType이 Null일 경우 기본 정렬 값
        if (Objects.isNull(orderByType)) {
            orderSpecifierList.add(new OrderSpecifier<>(Order.DESC, feed.createdAt).nullsLast());
            return orderSpecifierList.toArray(OrderSpecifier<?>[]::new);
        }

        switch (orderByType) {
            case CREATED_AT ->
                orderSpecifierList.add(new OrderSpecifier<>(order, feed.createdAt).nullsLast());
            case UPDATED_AT ->
                orderSpecifierList.add(new OrderSpecifier<>(order, feed.updatedAt).nullsLast());
            case VIEW_COUNT ->
                orderSpecifierList.add(new OrderSpecifier<>(order, feed.viewCount).nullsLast());
            case LIKE_COUNT ->
                orderSpecifierList.add(new OrderSpecifier<>(order, feed.likeCount).nullsLast());
            case SHARE_COUNT ->
                orderSpecifierList.add(new OrderSpecifier<>(order, feed.shareCount).nullsLast());
        }

        orderSpecifierList.add(new OrderSpecifier<>(Order.DESC, feed.createdAt).nullsLast());
        return orderSpecifierList.toArray(OrderSpecifier<?>[]::new);
    }

    private BooleanExpression searchEq(String search, FeedSearchByType searchBy) {
        if (StringUtils.isNullOrEmpty(search)) {
            return Expressions.TRUE;
        }

        if (Objects.isNull(searchBy)) {
            return feed.title.contains(search)
                .or(feed.content.contains(search));
        }

        return switch (searchBy) {
            case TITLE -> feed.title.contains(search);
            case CONTENT -> feed.content.contains(search);
            case TITLE_AND_CONTENT -> feed.title.contains(search)
                .or(feed.content.contains(search));
        };
    }

    private BooleanExpression typeEq(String type) {
        if (StringUtils.isNullOrEmpty(type)) {
            return Expressions.TRUE;
        }

        return feed.type.eq(type);
    }

    private BooleanExpression hashTagEq(User loginUser, String hashTag) {
        String searchHashTag =
            StringUtils.isNullOrEmpty(hashTag) ? loginUser.getUsername() : hashTag;

        JPQLQuery<Long> subQuery = JPAExpressions.select(feedToHashtag.feedId)
            .from(feedToHashtag)
            .innerJoin(hashtag).on(hashtag.id.eq(feedToHashtag.hashtagId))
            .where(hashtag.name.eq(searchHashTag));

        return feed.id.in(subQuery);
    }

}
