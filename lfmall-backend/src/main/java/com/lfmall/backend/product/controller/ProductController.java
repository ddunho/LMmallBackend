package com.lfmall.backend.product.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

import com.lfmall.backend.product.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // 상품 목록 조회 요청 처리
    @PostMapping("/product/list")
    public List<Map<String, Object>> productList(@RequestBody Map<String, Object> body) {
        Integer categoryId = toInt(body.get("category_id"));
        String gender = body.get("gender") == null ? null : String.valueOf(body.get("gender"));
        return service.getProductList(categoryId, gender);
    }

    // 상품 상세 정보 조회 요청 처리
    @PostMapping("/product/detail")
    public Map<String, Object> productDetail(@RequestBody Object body) {
        Integer productId = extractId(body);
        return service.getProductDetail(productId);
    }

    // 상품 옵션 목록 조회 요청 처리
    @PostMapping("/product/option")
    public List<Map<String, Object>> productOption(@RequestBody Object body) {
        Integer productId = extractId(body);
        return service.getProductOptions(productId);
    }

    // 여러 상품을 한 번에 조회하는 배치 요청 처리
    @GetMapping("/product/batch")
    public Map<String, Object> productBatch(@RequestParam("ids") String ids) {
        List<Integer> idList = new ArrayList<>();
        if (ids != null && !ids.isBlank()) {
            String[] arr = ids.split(",");
            for (String s : arr) {
                if (!s.isBlank()) idList.add(Integer.parseInt(s.trim()));
            }
        }
        return Map.of("success", true, "data", service.getProductBatch(idList));
    }

    // 상품 검색 요청 처리
    @PostMapping("/search/result")
    public Map<String, Object> searchResult(@RequestBody Map<String, Object> req) {
        return service.searchProducts(req);
    }

    // 요청 본문에서 상품 ID를 유연하게 추출
    private Integer extractId(Object body) {
        if (body == null) return null;
        if (body instanceof Number) return ((Number) body).intValue();
        if (body instanceof String) return Integer.parseInt(((String) body).replace("\"", "").trim());
        if (body instanceof Map<?, ?> map) {
            Object v = map.get("product_id");
            if (v == null) v = map.get("id");
            return toInt(v);
        }
        return Integer.parseInt(String.valueOf(body));
    }

    // 다양한 타입의 값을 Integer로 변환
    private Integer toInt(Object v) {
        if (v == null) return null;
        if (v instanceof Number) return ((Number) v).intValue();
        return Integer.parseInt(String.valueOf(v));
    }
}
