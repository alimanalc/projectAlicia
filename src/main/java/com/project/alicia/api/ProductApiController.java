package com.project.alicia.api;

import com.project.alicia.feign.client.Product;
import com.project.alicia.model.ProductDetail;
import com.project.alicia.model.SimilarProducts;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.alicia.service.ProductService;
import com.project.alicia.service.impl.ProductServiceImpl;
import feign.Feign;
import feign.FeignException;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-04-04T19:11:12.622+02:00[Europe/Paris]")
@RestController
public class ProductApiController implements ProductApi {

    private static final Logger log = LoggerFactory.getLogger(ProductApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private ProductService productService;

    @org.springframework.beans.factory.annotation.Autowired
    public ProductApiController(ObjectMapper objectMapper, HttpServletRequest request, ProductService productService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.productService = productService;
    }

    @GetMapping("/product/{productId}/similar")
    public ResponseEntity getProductSimilar(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("productId") String productId) {
        try {
            List<String> similarProductsId = productService.getSimilarProducts(productId);
            SimilarProducts response = new SimilarProducts();
            for(String similarProductId: similarProductsId){
                response.add(productService.getProductDetail(similarProductId));
            }
            return ResponseEntity.ok(response);
        } catch (ApiException ae) {
            log.error("An error ocurred in the request", ae);
            return new ResponseEntity<SimilarProducts>(HttpStatus.NOT_FOUND);
        } catch (FeignException f) {
            HttpStatus status = null;
            switch (f.status()){
                case 404:
                    status = HttpStatus.NOT_FOUND;
                    break;
                default:
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            log.error("Couldn't serialize response for content type application/json", f);
            return new ResponseEntity<SimilarProducts>(status);
        }catch (Exception e) {
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<SimilarProducts>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
