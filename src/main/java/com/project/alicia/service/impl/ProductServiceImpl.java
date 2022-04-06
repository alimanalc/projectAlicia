package com.project.alicia.service.impl;

import com.google.gson.Gson;
import com.project.alicia.api.ApiException;
import com.project.alicia.feign.client.Product;
import com.project.alicia.model.ProductDetail;
import com.project.alicia.service.ProductService;
import feign.Feign;
import feign.FeignException;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private Product c = Feign.builder().encoder(new GsonEncoder()).decoder(new GsonDecoder()).target(Product.class, "http://localhost:3001");

    public List<String> getSimilarProducts(String productId) throws ApiException {
        List<String> res = Arrays.asList(c.getSimilarProducts(productId));
        if(res.isEmpty()){
            throw new ApiException(404, "Similar products not found");
        }
        return res;
    }

    public ProductDetail getProductDetail(String productId) throws ApiException {
        Object aux = c.getProductDetail(productId);
        if (aux == null) {
            throw new ApiException(404, String.format("Product details not found for id: %s", productId));
        }
        ProductDetail res;
        try {
            res = new Gson().fromJson(aux.toString(), ProductDetail.class);
        } catch (FeignException e) {
            throw new ApiException(404, String.format("Product details not found for id: %s", productId));
        }
        return res;
    }

}
