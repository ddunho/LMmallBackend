package com.lfmall.backend.product.service;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Map<String, Object>> getProductList(Integer categoryId);
    Map<String, Object> getProductDetail(Integer productId);
    List<Map<String, Object>> getProductOptions(Integer productId);
    List<Map<String, Object>> getProductBatch(List<Integer> ids);
    Map<String, Object> searchProducts(Map<String, Object> req);
}
