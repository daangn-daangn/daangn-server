DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS product_images CASCADE;
DROP TABLE IF EXISTS favorite_products CASCADE;
DROP TABLE IF EXISTS sale_reviews CASCADE;


CREATE TABLE users
(
    id                  bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    oauth_id            bigint          NOT NULL COMMENT 'oauth id',
    nickname            varchar(20)     DEFAULT NULL COMMENT '닉네임',
    location            varchar(50)     DEFAULT NULL COMMENT '지역',
    profile_url         varchar(500)    DEFAULT NULL COMMENT '프로필 사진',
    manner              double          NOT NULL DEFAULT 36.5 COMMENT '매너온도',
    created_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at          datetime        DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    PRIMARY KEY (id),
    KEY users_idx_oauth_id (oauth_id),
    KEY users_idx_nickname (nickname)
) COMMENT '유저 테이블';


CREATE TABLE categories
(
    id                  bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    name                varchar(20)     NOT NULL COMMENT '카테고리명',
    created_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at          datetime        DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    PRIMARY KEY (id),
    KEY categories_idx_name (name)
) COMMENT '물품 카테고리 테이블';


CREATE TABLE products
(
    id                  bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    name                varchar(50)     NOT NULL COMMENT '물품명',
    price               bigint          NOT NULL COMMENT '가격',
    title               varchar(100)    NOT NULL COMMENT '제목',
    description         varchar(500)    NOT NULL COMMENT '상세 내용',
    chatting_count      bigint          NOT NULL COMMENT '채팅 수',
    location            varchar(50)     NOT NULL COMMENT '지역',
    state               int             NOT NULL COMMENT '물품상태 (삭제, 예약중, 판매중, 판매완료)',
    thumb_nail_image    varchar(500)    DEFAULT NULL COMMENT '대표이미지 url',
    created_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at          datetime        DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    seller_id           bigint          DEFAULT NULL COMMENT '판매자 id',
    buyer_id            bigint          DEFAULT NULL COMMENT '구매자 id',
    category_id         bigint          DEFAULT NULL COMMENT '카테고리 id',
    PRIMARY KEY (id),
    KEY products_idx_seller_location (seller_id, location),
    KEY products_idx_buyer_location (buyer_id, location),
    CONSTRAINT fk_products_to_seller FOREIGN KEY (seller_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_products_to_buyer FOREIGN KEY (buyer_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_products_to_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE SET NULL ON UPDATE CASCADE
) COMMENT '물품 테이블';


CREATE TABLE product_images
(
    id                  bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    created_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at          datetime        DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
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
    updated_at          datetime        DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    user_id             bigint          DEFAULT NULL COMMENT '사용자 id',
    product_id          bigint          DEFAULT NULL COMMENT '물품 id',
    PRIMARY KEY (id),
    KEY favorite_products_idx_user (user_id),
    KEY favorite_products_idx_product_user (product_id, user_id),
    CONSTRAINT fk_favorite_products_to_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_favorite_products_to_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '찜하기 테이블';


CREATE TABLE sale_reviews
(
    id                  bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    content             varchar(500)    NOT NULL COMMENT '내용',
    created_at          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at          datetime        DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    seller_id           bigint          DEFAULT NULL COMMENT '판매자 id',
    buyer_id            bigint          DEFAULT NULL COMMENT '구매자 id',
    PRIMARY KEY (id),
    KEY sale_reviews_idx_seller (seller_id),
    KEY sale_reviews_idx_buyer (buyer_id),
    CONSTRAINT fk_sale_reviews_to_seller FOREIGN KEY (seller_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_sale_reviews_to_buyer FOREIGN KEY (buyer_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE
) COMMENT '거래후기 테이블';