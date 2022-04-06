package com.project.alicia.service;

import com.project.alicia.api.ApiException;
import com.project.alicia.model.ProductDetail;

import java.util.List;

public interface ProductService {

    List<String> getSimilarProducts(String productId) throws ApiException;
    ProductDetail getProductDetail(String productId) throws ApiException;
}
