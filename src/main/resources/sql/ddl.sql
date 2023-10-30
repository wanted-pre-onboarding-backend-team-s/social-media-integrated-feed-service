-- 테이블 순서는 관계를 고려하여 한 번에 실행해도 에러가 발생하지 않게 정렬되었습니다.

-- feeds Table Create SQL
-- 테이블 생성 SQL - feeds
CREATE TABLE feeds
(
    `id`           BIGINT         NOT NULL    AUTO_INCREMENT,
    `type`         VARCHAR(50)    NOT NULL    COMMENT 'SNS종류(''facebook'', ''twitter'', ''instagram'' ''threads'')',
    `title`        VARCHAR(50)    NOT NULL    COMMENT '게시물 제목',
    `content`      TEXT           NOT NULL    COMMENT '게시물 내용',
    `view_count`   INT            NOT NULL    DEFAULT 0 COMMENT '조회 수',
    `updated_at`   DATETIME       NOT NULL    DEFAULT NOW() COMMENT '수정 일',
    `created_at`   DATETIME       NOT NULL    DEFAULT NOW() COMMENT '생성 일',
    `content_id`   VARCHAR(50)    NOT NULL    COMMENT 'sns content id',
    `like_count`   INT            NOT NULL    DEFAULT 0 COMMENT '좋아요 수',
    `share_count`  INT            NOT NULL    DEFAULT 0 COMMENT '공유 수',
    PRIMARY KEY (id)
);


-- hashtags Table Create SQL
-- 테이블 생성 SQL - hashtags
CREATE TABLE hashtags
(
    `id`    BIGINT         NOT NULL    AUTO_INCREMENT,
    `name`  VARCHAR(50)    NOT NULL,
    PRIMARY KEY (id)
);


-- users Table Create SQL
-- 테이블 생성 SQL - users
CREATE TABLE users
(
    `id`        BIGINT          NOT NULL    AUTO_INCREMENT COMMENT '식별자',
    `username`  VARCHAR(50)     NOT NULL    COMMENT '계정 (ui)',
    `password`  VARCHAR(255)    NOT NULL    COMMENT '비밀번호',
    `email`     VARCHAR(255)    NOT NULL    COMMENT '이메일 (ui)',
    `approved`  TINYINT         NOT NULL    DEFAULT 0 COMMENT '승인여부',
    PRIMARY KEY (id)
);


-- auth_codes Table Create SQL
-- 테이블 생성 SQL - auth_codes
CREATE TABLE auth_codes
(
    `id`          BIGINT         NOT NULL    AUTO_INCREMENT COMMENT '식별자',
    `code`        VARCHAR(10)    NOT NULL    COMMENT '인증코드(xxxxx)',
    `username`    VARCHAR(50)    NOT NULL    COMMENT '이메일',
    `created_at`  DATETIME       NOT NULL    DEFAULT NOW() COMMENT '생성일',
    PRIMARY KEY (id)
);


-- feeds_hashtags Table Create SQL
-- 테이블 생성 SQL - feeds_hashtags
CREATE TABLE feeds_hashtags
(
    `id`          BIGINT    NOT NULL    AUTO_INCREMENT,
    `hashtag_id`  BIGINT    NOT NULL    COMMENT '해쉬태그 id',
    `feed_id`     BIGINT    NOT NULL    COMMENT '피드게시물 id',
    PRIMARY KEY (id)
);

-- Foreign Key 설정 SQL - feeds_hashtags(hashtag_id) -> hashtags(id)
ALTER TABLE feeds_hashtags
    ADD CONSTRAINT FK_feeds_hashtags_hashtag_id_hashtags_id FOREIGN KEY (hashtag_id)
        REFERENCES hashtags (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - feeds_hashtags(hashtag_id)
-- ALTER TABLE feeds_hashtags
-- DROP FOREIGN KEY FK_feeds_hashtags_hashtag_id_hashtags_id;

-- Foreign Key 설정 SQL - feeds_hashtags(feed_id) -> feeds(id)
ALTER TABLE feeds_hashtags
    ADD CONSTRAINT FK_feeds_hashtags_feed_id_feeds_id FOREIGN KEY (feed_id)
        REFERENCES feeds (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - feeds_hashtags(feed_id)
-- ALTER TABLE feeds_hashtags
-- DROP FOREIGN KEY FK_feeds_hashtags_feed_id_feeds_id;


-- likes Table Create SQL
-- 테이블 생성 SQL - likes
CREATE TABLE likes
(
    `id`          BIGINT      NOT NULL    AUTO_INCREMENT,
    `feed_id`     BIGINT      NOT NULL    COMMENT '피드 게시물 id',
    `user_id`     BIGINT      NOT NULL    COMMENT '유저 id',
    `created_at`  DATETIME    NOT NULL    DEFAULT NOW() COMMENT '생성 일',
    PRIMARY KEY (id)
);

-- Foreign Key 설정 SQL - likes(feed_id) -> feeds(id)
ALTER TABLE likes
    ADD CONSTRAINT FK_likes_feed_id_feeds_id FOREIGN KEY (feed_id)
        REFERENCES feeds (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - likes(feed_id)
-- ALTER TABLE likes
-- DROP FOREIGN KEY FK_likes_feed_id_feeds_id;

-- Foreign Key 설정 SQL - likes(user_id) -> users(id)
ALTER TABLE likes
    ADD CONSTRAINT FK_likes_user_id_users_id FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - likes(user_id)
-- ALTER TABLE likes
-- DROP FOREIGN KEY FK_likes_user_id_users_id;


-- shares Table Create SQL
-- 테이블 생성 SQL - shares
CREATE TABLE shares
(
    `i`           BIGINT      NOT NULL    AUTO_INCREMENT,
    `feed_id`     BIGINT      NOT NULL    COMMENT '피드 게시물 Id',
    `user_id`     BIGINT      NOT NULL    COMMENT '유저 id',
    `created_at`  DATETIME    NOT NULL    DEFAULT NOW() COMMENT '생성 일',
    PRIMARY KEY (i)
);

-- Foreign Key 설정 SQL - shares(feed_id) -> feeds(id)
ALTER TABLE shares
    ADD CONSTRAINT FK_shares_feed_id_feeds_id FOREIGN KEY (feed_id)
        REFERENCES feeds (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - shares(feed_id)
-- ALTER TABLE shares
-- DROP FOREIGN KEY FK_shares_feed_id_feeds_id;

-- Foreign Key 설정 SQL - shares(user_id) -> users(id)
ALTER TABLE shares
    ADD CONSTRAINT FK_shares_user_id_users_id FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - shares(user_id)
-- ALTER TABLE shares
-- DROP FOREIGN KEY FK_shares_user_id_users_id;


