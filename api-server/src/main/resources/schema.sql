SET foreign_key_checks = 0;

DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS product_images CASCADE;
DROP TABLE IF EXISTS favorite_products CASCADE;
DROP TABLE IF EXISTS sale_reviews CASCADE;
DROP TABLE IF EXISTS manner_evaluations CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;


CREATE TABLE users
(
    id                  bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    oauth_id            bigint          NOT NULL COMMENT 'oauth id',
    nickname            varchar(20)     DEFAULT NULL COMMENT '닉네임',
    location            varchar(50)     DEFAULT NULL COMMENT '지역',
    profile_url         varchar(500)    DEFAULT NULL COMMENT '프로필 사진',
    manner              double          NOT NULL DEFAULT 36.5 COMMENT '매너온도',
    created_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    PRIMARY KEY (id),
    KEY users_idx_oauth (oauth_id),
    KEY users_idx_nickname (nickname)
) COMMENT '유저 테이블';


CREATE TABLE categories
(
    id                  bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    name                varchar(20)     NOT NULL COMMENT '카테고리명',
    created_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    PRIMARY KEY (id),
    KEY categories_idx_name (name)
) COMMENT '물품 카테고리 테이블';


CREATE TABLE products
(
    id                  bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    price               bigint          NOT NULL COMMENT '가격',
    title               varchar(100)    NOT NULL COMMENT '제목',
    description         varchar(500)    NOT NULL COMMENT '상세 내용',
    location            varchar(50)     NOT NULL COMMENT '지역',
    product_state       int             NOT NULL COMMENT '물품상태 (삭제, 예약중, 판매중, 거래완료, 숨기기)',
    thumb_nail_image    varchar(500)    DEFAULT NULL COMMENT '대표이미지 url',
    refresh_cnt         int             NOT NULL DEFAULT 0 COMMENT '새로 고침(끌어올리기) 횟수',
    view_cnt            int             NOT NULL DEFAULT 0 COMMENT '조회수',
    created_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    seller_id           bigint          DEFAULT NULL COMMENT '판매자 id',
    buyer_id            bigint          DEFAULT NULL COMMENT '구매자 id',
    category_id         bigint          DEFAULT NULL COMMENT '카테고리 id',
    PRIMARY KEY (id),
    KEY products_idx_seller (seller_id),
    KEY products_idx_buyer (buyer_id),
    KEY products_idx_location_category (location, category_id),
    KEY products_idx_location_created_at (location, created_at),
    CONSTRAINT fk_products_to_seller FOREIGN KEY (seller_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_products_to_buyer FOREIGN KEY (buyer_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_products_to_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE SET NULL ON UPDATE CASCADE
) COMMENT '물품 테이블';


CREATE TABLE product_images
(
    id                  bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    created_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    image_url           varchar(500)    NOT NULL COMMENT '물품사진 url',
    product_id          bigint          DEFAULT NULL COMMENT '물품 id',
    PRIMARY KEY (id),
    KEY product_images_idx_product (product_id),
    CONSTRAINT fk_product_images_to_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '물품 사진 테이블';


CREATE TABLE favorite_products
(
    id                  bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    is_valid            boolean         NOT NULL COMMENT '찜상태 유효여부',
    created_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    user_id             bigint          DEFAULT NULL COMMENT '사용자 id',
    product_id          bigint          DEFAULT NULL COMMENT '물품 id',
    PRIMARY KEY (id),
    KEY favorite_products_idx_user (user_id),
    KEY favorite_products_idx_product (product_id),
    KEY favorite_products_idx_product_user (product_id, user_id),
    CONSTRAINT fk_favorite_products_to_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_favorite_products_to_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '찜하기 테이블';


CREATE TABLE sale_reviews
(
    id                  bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    sale_review_type    int             NOT NULL COMMENT '후기 타입 (판매자 후기, 구매자 후기)',
    content             varchar(500)    NOT NULL COMMENT '내용',
    created_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    product_id          bigint          DEFAULT NULL COMMENT '물품 id',
    reviewer_id         bigint          DEFAULT NULL COMMENT '리뷰 작성자 id',
    reviewee_id         bigint          DEFAULT NULL COMMENT '리뷰 작성 대상자 id',
    PRIMARY KEY (id),
    KEY sale_reviews_idx_reviewee_type (reviewee_id, sale_review_type),
    KEY sale_reviews_idx_product_reviewer_type (product_id, reviewer_id, sale_review_type),
    CONSTRAINT fk_sale_reviews_to_reviewer FOREIGN KEY (reviewer_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_sale_reviews_to_reviewee FOREIGN KEY (reviewee_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_sale_reviews_to_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE SET NULL ON UPDATE CASCADE
) COMMENT '거래후기 테이블';


CREATE TABLE manner_evaluations
(
    id                  bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    user_id             bigint          DEFAULT NULL COMMENT '평가받는 사람 id',
    evaluator_id        bigint          DEFAULT NULL COMMENT '평가자 id',
    score               int             NOT NULL COMMENT '매너평가점수 (-10 ~ 10)',
    created_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    PRIMARY KEY (id),
    KEY manner_evaluations_idx_user (user_id),
    KEY manner_evaluations_idx_evaluator (evaluator_id),
    CONSTRAINT fk_manner_evaluations_to_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_manner_evaluations_to_evaluator FOREIGN KEY (evaluator_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '매너평가 테이블';

CREATE TABLE notifications
(
    id                  bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    user_id             bigint          NOT NULL COMMENT '알림받는 사용자 id',
    notification_type   int             NOT NULL COMMENT '알림유형 (삭제, 예약중, 판매중, 거래완료, 숨기기)',
    identifier          varchar(50)     NOT NULL COMMENT '식별자',
    is_read             boolean         NOT NULL COMMENT '알림 메시지 조회여부',
    is_valid            boolean         NOT NULL COMMENT '알림 삭제여부',
    created_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    PRIMARY KEY (id),
    KEY notifications_idx_user (user_id),
    CONSTRAINT fk_notifications_to_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '알림 테이블';

SET foreign_key_checks = 1;