package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.apiserver.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public void createProduct(String title, Long category, Long price, String content, List<String> imgUrl) {

    }
}
