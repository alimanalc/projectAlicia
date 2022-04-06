package com.project.alicia.feign.client;

import com.project.alicia.model.ProductDetail;
import feign.Param;
import feign.RequestLine;

public interface Product {

    @RequestLine("GET /product/{productId}/similarids")
    String[] getSimilarProducts(@Param("productId") String productId);

    @RequestLine("GET /product/{productId}")
    Object getProductDetail(@Param("productId") String productId);
}
