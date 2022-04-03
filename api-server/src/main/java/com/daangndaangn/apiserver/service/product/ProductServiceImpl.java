package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.apiserver.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl {

    private final ProductRepository productRepository;
}
