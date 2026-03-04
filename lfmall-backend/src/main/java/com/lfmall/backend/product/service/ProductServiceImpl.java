package com.lfmall.backend.product.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.lfmall.backend.product.mapper.ProductMapper;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;

    public ProductServiceImpl(ProductMapper mapper) {
        this.mapper = mapper;
    }

    // 카테고리/성별 필터로 상품 목록을 조회하고 화면용 정보로 보강
    @Override
    public List<Map<String, Object>> getProductList(Integer categoryId, String gender) {
        List<Map<String, Object>> baseList = mapper.selectProductListByCategory(categoryId, normalizeGender(gender));
        List<Map<String, Object>> out = new ArrayList<>();
        for (Map<String, Object> base : baseList) {
            out.add(enrichProduct(base));
        }
        return out;
    }

    // 단일 상품의 기본 정보와 상세 이미지를 조회
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

    // 상품 옵션(색상/사이즈/재고) 목록 조회
    @Override
    public List<Map<String, Object>> getProductOptions(Integer productId) {
        return mapper.selectOptionsByProductId(productId);
    }

    // 여러 상품을 ID 목록으로 일괄 조회 후 화면용 정보로 보강
    @Override
    public List<Map<String, Object>> getProductBatch(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<Map<String, Object>> baseList = mapper.selectProductBaseByIds(ids);
        List<Map<String, Object>> out = new ArrayList<>();
        for (Map<String, Object> base : baseList) {
            out.add(enrichProduct(base));
        }
        return out;
    }

    // 검색 조건을 해석해 상품을 조회하고 응답 포맷으로 변환
    @Override
    public Map<String, Object> searchProducts(Map<String, Object> req) {
        String searchtxt = asString(req.get("searchtxt"));
        String searchtype = asString(req.get("searchtype"));
        List<String> color = asStringList(req.get("color"));
        List<String> size = asStringList(req.get("size"));
        List<String> brandName = asStringList(req.get("brand_name"));
        List<String> addSearch = asStringList(req.get("addSearch"));

        List<Map<String, Object>> baseList = mapper.searchProducts(
            searchtxt, searchtype, color, size, brandName, addSearch
        );

        List<Map<String, Object>> data = new ArrayList<>();
        for (Map<String, Object> base : baseList) {
            data.add(enrichProduct(base));
        }

        return Map.of(
            "success", true,
            "total", data.size(),
            "data", data
        );
    }

    // 조회된 상품 기본 데이터에 이미지/옵션 등 부가 정보를 결합
    private Map<String, Object> enrichProduct(Map<String, Object> base) {
        Integer productId = toInt(base.get("product_id"));

        Map<String, Object> out = new LinkedHashMap<>(base);
        out.put("brand_name", "LFmall");
        out.put("delivery_state", "Y");
        out.put("is_note", "X");
        out.put("free_delivery", "Y");
        out.put("img_names", mapper.selectImageNamesByProductId(productId));
        out.put("color", mapper.selectColorsByProductId(productId));
        out.put("size", mapper.selectSizesByProductId(productId));
        return out;
    }

    // Object 값을 Integer로 안전하게 변환
    private Integer toInt(Object v) {
        if (v == null) return null;
        if (v instanceof Number) return ((Number) v).intValue();
        return Integer.parseInt(String.valueOf(v));
    }

    // Object 값을 문자열로 변환하고 null은 빈 문자열로 처리
    private String asString(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

    // 입력된 성별 값을 male/female 기준값으로 정규화
    private String normalizeGender(String gender) {
        if (gender == null) return null;
        String g = gender.trim().toLowerCase();
        if (g.isEmpty()) return null;

        if (g.equals("male") || g.equals("m") || g.equals("남성") || g.equals("남자")) {
            return "male";
        }
        if (g.equals("female") || g.equals("f") || g.equals("여성") || g.equals("여자")) {
            return "female";
        }
        return null;
    }

    // Object 값을 문자열 리스트로 변환
    @SuppressWarnings("unchecked")
    private List<String> asStringList(Object v) {
        if (v == null) return Collections.emptyList();
        if (v instanceof List<?>) {
            List<String> out = new ArrayList<>();
            for (Object o : (List<Object>) v) out.add(String.valueOf(o));
            return out;
        }
        return Collections.emptyList();
    }
}
