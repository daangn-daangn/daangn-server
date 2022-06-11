package com.daangndaangn.apiserver;

import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.apiserver.service.favorite.FavoriteProductService;
import com.daangndaangn.apiserver.service.manner.MannerService;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

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
        initDataService.updateUsers();
        initDataService.initCategories();
        initDataService.initProducts();
        initDataService.initFavoriteProducts();
//        initDataService.initManners();
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
        private final MannerService mannerService;

        public void initUsers() {
            userService.create(12L, null);
            userService.create(34L, null);
            userService.create(56L, null);
            userService.create(78L, null);
            userService.create(910L, null);
        }

        public void updateUsers() {
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
                    "mockProduct1 description",
                    null);

            productService.create(1L,
                    2L,
                    "mockProduct 팝니다2",
                    "mockProduct2",
                    20000L,
                    "mockProduct2 description",
                    null);

            productService.create(1L,
                    3L,
                    "mockProduct 팝니다3",
                    "mockProduct3",
                    30000L,
                    "mockProduct3 description",
                    List.of("test001.jpg","test002.jpeg","test003.png"));

            productService.create(1L,
                    4L,
                    "mockProduct 팝니다4",
                    "mockProduct4",
                    40000L,
                    "mockProduct4 description",null);

            productService.create(1L,
                    5L,
                    "mockProduct 팝니다5",
                    "mockProduct5",
                    50000L,
                    "mockProduct5 description",null);

            productService.create(2L,
                    2L,
                    "mockProduct 팝니다6",
                    "mockProduct6",
                    110000L,
                    "mockProduct6 description",null);

            productService.create(2L,
                    2L,
                    "mockProduct 팝니다7",
                    "mockProduct7",
                    120000L,
                    "mockProduct7 description",null);

            productService.create(2L,
                    3L,
                    "mockProduct 팝니다8",
                    "mockProduct8",
                    130000L,
                    "mockProduct8 description",null);

            productService.create(2L,
                    4L,
                    "mockProduct 팝니다9",
                    "mockProduct9",
                    140000L,
                    "mockProduct9 description",null);

            productService.create(3L,
                    5L,
                    "mockProduct 팝니다10",
                    "mockProduct10",
                    150000L,
                    "mockProduct10 description",null);

            productService.create(3L,
                    5L,
                    "mockProduct 팝니다11",
                    "mockProduct11",
                    160000L,
                    "mockProduct11 description",
                    List.of("test020.jpg","test021.jpeg","test022.png"));

            productService.create(3L,
                    5L,
                    "mockProduct 팝니다12",
                    "mockProduct12",
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
    }

}
