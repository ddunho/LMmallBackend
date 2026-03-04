package com.lfmall.backend.product.service;

import java.util.List;
import java.util.Map;

public interface ProductService {
    // 카테고리/성별 기준 상품 목록 조회
    List<Map<String, Object>> getProductList(Integer categoryId, String gender);
    // 상품 상세 정보 조회
    Map<String, Object> getProductDetail(Integer productId);
    // 상품 옵션 정보 조회
    List<Map<String, Object>> getProductOptions(Integer productId);
    // 여러 상품 정보 일괄 조회
    List<Map<String, Object>> getProductBatch(List<Integer> ids);
    // 검색 조건 기반 상품 검색
    Map<String, Object> searchProducts(Map<String, Object> req);
}
