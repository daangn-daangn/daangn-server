package com.daangndaangn.apiserver;

import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.apiserver.service.favorite.FavoriteProductService;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Slf4j
@Profile("local")
@Component
@RequiredArgsConstructor
public class InitData {

    private final InitDataService initDataService;

    @PostConstruct
    public void init() {
        log.info("start initTestSetting");
        initDataService.initUsers();
        initDataService.initCategories();
        initDataService.initProducts();
        initDataService.initFavoriteProducts();
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
        private final CategoryService categoryService;
        private final ProductService productService;
        private final FavoriteProductService favoriteProductService;

        public void initUsers() {
            userService.join(12L, null);
            userService.join(34L, null);
            userService.join(56L, null);
            userService.join(78L, null);
            userService.join(910L, null);

            userService.update(1L, "테스트닉네임1", "노원구 상계동");
            userService.update(2L, "테스트닉네임2", "노원구 상계동");
            userService.update(3L, "테스트닉네임3", "노원구 상계동");
            userService.update(4L, "테스트닉네임4", "노원구 상계동");
            userService.update(5L, "테스트닉네임5", "노원구 상계동");
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
                    "mockProduct1",
                    10000L,
                    "mockProduct1 description");

            productService.create(1L,
                    2L,
                    "mockProduct 팝니다2",
                    "mockProduct2",
                    20000L,
                    "mockProduct2 description");

            productService.create(1L,
                    3L,
                    "mockProduct 팝니다3",
                    "mockProduct3",
                    30000L,
                    "mockProduct3 description");

            productService.create(1L,
                    4L,
                    "mockProduct 팝니다4",
                    "mockProduct4",
                    40000L,
                    "mockProduct4 description");

            productService.create(1L,
                    5L,
                    "mockProduct 팝니다5",
                    "mockProduct5",
                    50000L,
                    "mockProduct5 description");

            productService.create(2L,
                    2L,
                    "mockProduct 팝니다6",
                    "mockProduct6",
                    110000L,
                    "mockProduct6 description");

            productService.create(2L,
                    2L,
                    "mockProduct 팝니다7",
                    "mockProduct7",
                    120000L,
                    "mockProduct7 description");

            productService.create(2L,
                    3L,
                    "mockProduct 팝니다8",
                    "mockProduct8",
                    130000L,
                    "mockProduct8 description");

            productService.create(2L,
                    4L,
                    "mockProduct 팝니다9",
                    "mockProduct9",
                    140000L,
                    "mockProduct9 description");

            productService.create(3L,
                    5L,
                    "mockProduct 팝니다10",
                    "mockProduct10",
                    150000L,
                    "mockProduct10 description");

            productService.create(3L,
                    5L,
                    "mockProduct 팝니다11",
                    "mockProduct11",
                    160000L,
                    "mockProduct11 description");

            productService.create(3L,
                    5L,
                    "mockProduct 팝니다12",
                    "mockProduct12",
                    170000L,
                    "mockProduct12 description");
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
    }

}
