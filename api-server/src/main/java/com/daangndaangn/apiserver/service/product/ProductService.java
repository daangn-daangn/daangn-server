package com.daangndaangn.apiserver.service.product;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ProductService {

    void createProduct(String title, Long category, Long price, String content, List<String> imgUrl);

}
