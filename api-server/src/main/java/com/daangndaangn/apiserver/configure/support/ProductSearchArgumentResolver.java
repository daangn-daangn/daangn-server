package com.daangndaangn.apiserver.configure.support;

import com.daangndaangn.common.api.repository.product.query.ProductSearchOption;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class ProductSearchArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String PRODUCT_TITLE_PARAMETER = "title";
    public static final String CATEGORY_PARAMETER = "category";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return ProductSearchOption.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        String productTitleParam = defaultIfEmpty(webRequest.getParameter(PRODUCT_TITLE_PARAMETER), "");
        String categoryParam = defaultIfEmpty(webRequest.getParameter(CATEGORY_PARAMETER), "");

        return ProductSearchOption.of(productTitleParam, categoryParam);
    }
}
