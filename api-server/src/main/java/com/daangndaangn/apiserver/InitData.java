package com.daangndaangn.apiserver;

import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.apiserver.service.chatroom.ChatRoomService;
import com.daangndaangn.apiserver.service.favorite.FavoriteProductService;
import com.daangndaangn.apiserver.service.manner.MannerService;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.salereview.SaleReviewService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.review.SaleReviewType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Profile("local")
@Component
@RequiredArgsConstructor
public class InitData {

    private final InitDataService initDataService;

    @PostConstruct
    public void init() {
        log.info("start initTestSetting");
        try {
//            initDataService.initCategories();
//            initDataService.initUsers();
            initDataService.initUsersWithProducts();
//        initDataService.initManners();
//        initDataService.initProducts();
//        initDataService.initFavoriteProducts();
//        initDataService.initChatRooms();
        } catch (Exception ignored) {
        }

        log.info("end initTestSetting");
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitDataService {

        private static final String[] CATEGORIES =
            new String[]{"디지털기기","생활가전","가구/인테리어","유아동","생활/가공식품","유아도서","스포츠/레저","여성잡화","여성의류",
                    "남성패션/잡화","게임/취미","뷰티/미용","반려동물용품","도서/티켓/음반","식물","기타 중고물품"};

        private final UserService userService;
        private final MannerService mannerService;
        private final CategoryService categoryService;
        private final ProductService productService;
        private final FavoriteProductService favoriteProductService;
        private final ChatRoomService chatRoomService;

        public void initUsers() throws ExecutionException, InterruptedException {
            userService.create(12L, "test001.jpg")
                .thenApply(userId -> {
                    userService.update(userId, "테스트 닉네임1", "노원구 상계동");
                    return null;
                }).thenAccept(result -> log.info("initUsers"));

            userService.create(34L, "test002.jpg")
                .thenApply(userId -> {
                    userService.update(userId, "테스트닉네임2", "노원구 상계동");
                    return null;
                }).thenAccept(result -> log.info("initUsers"));

            userService.create(56L, "test003.jpg")
                .thenApply(userId -> {
                    userService.update(userId, "테스트닉네임3", "노원구 상계동");
                    return null;
                }).thenAccept(result -> log.info("initUsers"));

            userService.create(78L, "test004.jpg")
                .thenApply(userId -> {
                    userService.update(userId, "테스트닉네임4", "노원구 상계동");
                    return null;
                }).thenAccept(result -> log.info("initUsers"));

            userService.create(910L, "test005.jpg")
                .thenApply(userId -> {
                    userService.update(userId, "테스트닉네임5", "노원구 상계동");
                    return null;
                }).thenAccept(result -> log.info("initUsers"));
        }

        public void initUsersWithProducts() throws ExecutionException, InterruptedException {
            userService.create(12L, "test001.jpg")
                    .thenApply(userId -> {
                        userService.update(userId, "테스트 닉네임1", "노원구 상계동");
                        productService.create(userId,
                                1L,
                                "mockProduct 팝니다1",
                                10000L,
                                "mockProduct1 description",
                                null);

                        productService.create(userId,
                                2L,
                                "mockProduct 팝니다2",
                                20000L,
                                "mockProduct2 description",
                                null);

                        productService.create(userId,
                                3L,
                                "mockProduct 팝니다3",
                                30000L,
                                "mockProduct3 description",
                                List.of("test001.jpg","test002.jpeg","test003.png"));

                        productService.create(userId,
                                4L,
                                "mockProduct 팝니다4",
                                40000L,
                                "mockProduct4 description",null);

                        productService.create(userId,
                                5L,
                                "mockProduct 팝니다5",
                                50000L,
                                "mockProduct5 description",null);
                        return null;
                    }).thenAccept(result -> log.info("initUsers"));

            userService.create(34L, "test002.jpg")
                    .thenApply(userId -> {
                        userService.update(userId, "테스트닉네임2", "노원구 상계동");
                        productService.create(userId,
                                2L,
                                "mockProduct 팝니다6",
                                110000L,
                                "mockProduct6 description",null);

                        productService.create(userId,
                                2L,
                                "mockProduct 팝니다7",
                                120000L,
                                "mockProduct7 description",null);

                        productService.create(userId,
                                3L,
                                "mockProduct 팝니다8",
                                130000L,
                                "mockProduct8 description",null);

                        productService.create(userId,
                                4L,
                                "mockProduct 팝니다9",
                                140000L,
                                "mockProduct9 description",null);
                        return null;
                    }).thenAccept(result -> log.info("initUsers"));

            userService.create(56L, "test003.jpg")
                    .thenApply(userId -> {
                        userService.update(userId, "테스트닉네임3", "노원구 상계동");
                        productService.create(userId,
                                5L,
                                "mockProduct 팝니다10",
                                150000L,
                                "mockProduct10 description",null);

                        productService.create(userId,
                                5L,
                                "mockProduct 팝니다11",
                                160000L,
                                "mockProduct11 description",
                                List.of("test020.jpg","test021.jpeg","test022.png"));

                        productService.create(userId,
                                5L,
                                "mockProduct 팝니다12",
                                170000L,
                                "mockProduct12 description",
                                List.of("test010.jpg","test011.jpeg","test012.png"));
                        return null;
                    }).thenAccept(result -> log.info("initUsers"));

            userService.create(78L, "test004.jpg")
                    .thenApply(userId -> {
                        userService.update(userId, "테스트닉네임4", "노원구 상계동");
                        return null;
                    }).thenAccept(result -> log.info("initUsers"));

            userService.create(910L, "test005.jpg")
                    .thenApply(userId -> {
                        userService.update(userId, "테스트닉네임5", "노원구 상계동");
                        return null;
                    }).thenAccept(result -> log.info("initUsers"));
        }

        public void initManners() {
            mannerService.createManner(1L, 2L, -5);
            mannerService.createManner(1L, 2L, -4);
            mannerService.createManner(1L, 2L, -3);
            mannerService.createManner(1L, 2L, -2);
            mannerService.createManner(1L, 2L, -1);
            mannerService.createManner(1L, 3L, 1);
            mannerService.createManner(1L, 3L, 2);
            mannerService.createManner(1L, 3L, 3);
            mannerService.createManner(1L, 3L, 4);
            mannerService.createManner(1L, 3L, 5);
            mannerService.createManner(1L, 4L, 5);
            mannerService.createManner(1L, 4L, -5);
        }

        public void initCategories() {
            for (String category : CATEGORIES) {
                categoryService.create(category);
            }
        }

        public void initProducts() {
            productService.create(1L,
                    1L,
                    "mockProduct 팝니다1",
                    10000L,
                    "mockProduct1 description",
                    null);

            productService.create(1L,
                    2L,
                    "mockProduct 팝니다2",
                    20000L,
                    "mockProduct2 description",
                    null);

            productService.create(1L,
                    3L,
                    "mockProduct 팝니다3",
                    30000L,
                    "mockProduct3 description",
                    List.of("test001.jpg","test002.jpeg","test003.png"));

            productService.create(1L,
                    4L,
                    "mockProduct 팝니다4",
                    40000L,
                    "mockProduct4 description",null);

            productService.create(1L,
                    5L,
                    "mockProduct 팝니다5",
                    50000L,
                    "mockProduct5 description",null);

            productService.create(2L,
                    2L,
                    "mockProduct 팝니다6",
                    110000L,
                    "mockProduct6 description",null);

            productService.create(2L,
                    2L,
                    "mockProduct 팝니다7",
                    120000L,
                    "mockProduct7 description",null);

            productService.create(2L,
                    3L,
                    "mockProduct 팝니다8",
                    130000L,
                    "mockProduct8 description",null);

            productService.create(2L,
                    4L,
                    "mockProduct 팝니다9",
                    140000L,
                    "mockProduct9 description",null);

            productService.create(3L,
                    5L,
                    "mockProduct 팝니다10",
                    150000L,
                    "mockProduct10 description",null);

            productService.create(3L,
                    5L,
                    "mockProduct 팝니다11",
                    160000L,
                    "mockProduct11 description",
                    List.of("test020.jpg","test021.jpeg","test022.png"));

            productService.create(3L,
                    5L,
                    "mockProduct 팝니다12",
                    170000L,
                    "mockProduct12 description",
                    List.of("test010.jpg","test011.jpeg","test012.png"));
        }

        public void initFavoriteProducts() {
            favoriteProductService.create(1L,2L);
            favoriteProductService.create(1L,3L);
            favoriteProductService.create(1L,4L);
            favoriteProductService.create(1L,5L);

            favoriteProductService.create(2L,3L);
            favoriteProductService.create(2L,4L);
            favoriteProductService.create(2L,5L);
        }

        public void initChatRooms() {
            chatRoomService.create(1L, List.of(1L, 5L));
            chatRoomService.create(2L, List.of(1L, 4L));
        }
    }

}
