package com.lfmall.backend.product.service;

import com.lfmall.backend.product.mapper.ProductMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;

    public ProductServiceImpl(ProductMapper mapper) {
        this.mapper = mapper;
    }

    // 카테고리/성별 조건으로 상품 목록을 조회
    @Override
    public List<Map<String, Object>> getProductList(Integer categoryId, String gender) {
        List<Map<String, Object>> baseList = mapper.selectProductListByCategory(categoryId, normalizeGender(gender));
        return enrichProducts(baseList);
    }

    // 단일 상품의 상세 정보를 조회한다.
    @Override
    public Map<String, Object> getProductDetail(Integer productId) {
        Map<String, Object> base = mapper.selectProductBaseById(productId);
        if (base == null) {
            return Map.of("product", Collections.emptyMap(), "imgList", Collections.emptyList());
        }

        Map<String, Object> product = enrichProduct(base);
        product.put("comment", "");
        product.put("path", "menu/" + product.get("category_id"));

        List<Map<String, Object>> imgList = mapper.selectDetailImagesByProductId(productId);
        return Map.of("product", product, "imgList", imgList);
    }

    // 단일 상품의 옵션 목록을 조회
    @Override
    public List<Map<String, Object>> getProductOptions(Integer productId) {
        return mapper.selectOptionsByProductId(productId);
    }

    // 여러 상품을 ID 목록으로 일괄 조회
    @Override
    public List<Map<String, Object>> getProductBatch(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> baseList = mapper.selectProductBaseByIds(ids);
        return enrichProducts(baseList);
    }

    // 검색 조건으로 상품 목록을 조회
    @Override
    public Map<String, Object> searchProducts(Map<String, Object> req) {
        // 요청값을 검색 파라미터로 변환
        String searchtxt = toStringValue(req.get("searchtxt"));
        String searchtype = toStringValue(req.get("searchtype"));
        List<String> color = toStringList(req.get("color"));
        List<String> size = toStringList(req.get("size"));
        List<String> brandName = toStringList(req.get("brand_name"));
        List<String> addSearch = toStringList(req.get("addSearch"));

        // 조건에 맞는 상품 기본 목록을 조회
        List<Map<String, Object>> baseList = mapper.searchProducts(
            searchtxt,
            searchtype,
            color,
            size,
            brandName,
            addSearch
        );

        // 공통 필드를 붙여 최종 데이터를 만들기
        List<Map<String, Object>> data = enrichProducts(baseList);

        // 기존 응답 포맷을 그대로 반환
        return Map.of(
            "success", true,
            "total", data.size(),
            "data", data
        );
    }

    private List<Map<String, Object>> enrichProducts(List<Map<String, Object>> baseList) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> base : baseList) {
            result.add(enrichProduct(base));
        }
        return result;
    }

    private Map<String, Object> enrichProduct(Map<String, Object> base) {
        Integer productId = toInt(base.get("product_id"));

        Map<String, Object> product = new LinkedHashMap<>(base);
        product.put("brand_name", "LFmall");
        product.put("delivery_state", "Y");
        product.put("is_note", "X");
        product.put("free_delivery", "Y");
        product.put("img_names", mapper.selectImageNamesByProductId(productId));
        product.put("color", mapper.selectColorsByProductId(productId));
        product.put("size", mapper.selectSizesByProductId(productId));
        return product;
    }

    private Integer toInt(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.intValue();
        }

        return Integer.parseInt(String.valueOf(value));
    }

    private String toStringValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private String normalizeGender(String gender) {
        if (gender == null) {
            return null;
        }

        String value = gender.trim().toLowerCase();
        if (value.isEmpty()) {
            return null;
        }

        if (
            value.equals("male")
                || value.equals("m")
                || value.equals("\uB0A8\uC131")
                || value.equals("\uB0A8\uC790")
        ) {
            return "male";
        }

        if (
            value.equals("female")
                || value.equals("f")
                || value.equals("\uC5EC\uC131")
                || value.equals("\uC5EC\uC790")
        ) {
            return "female";
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private List<String> toStringList(Object value) {
        if (!(value instanceof List<?> list)) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        for (Object item : (List<Object>) list) {
            result.add(String.valueOf(item));
        }
        return result;
    }
}
