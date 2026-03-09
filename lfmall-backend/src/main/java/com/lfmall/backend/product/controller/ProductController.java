package com.lfmall.backend.product.controller;

import com.lfmall.backend.product.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }
    
    // 상품 목록 조회 요청
    @PostMapping("/product/list")
    public List<Map<String, Object>> productList(@RequestBody Map<String, Object> body) {
        // 카테고리 값을 꺼내기
        Integer categoryId = toInt(body.get("category_id"));
        // 성별 값을 꺼내기
        String gender = body.get("gender") == null ? null : String.valueOf(body.get("gender"));
        // 서비스로 목록 조회 전달
        return service.getProductList(categoryId, gender);
    }
    
    // 상품 상세 정보 조회 요청
    @PostMapping("/product/detail")
    public Map<String, Object> productDetail(@RequestBody Object body) {
        // 다양한 형태의 body에서 상품 ID를 뽑아 상세를 조회한다.
        return service.getProductDetail(extractProductId(body));
    }
    
    // 상품 옵션 목록 조회 요청
    @PostMapping("/product/option")
    public List<Map<String, Object>> productOption(@RequestBody Object body) {
        // 다양한 형태의 body에서 상품 ID를 뽑아 옵션을 조회한다.
        return service.getProductOptions(extractProductId(body));
    }
    
    // 여러 상품을 한 번에 조회하는 배치 요청
    @GetMapping("/product/batch")
    public Map<String, Object> productBatch(@RequestParam("ids") String ids) {
        // 파싱한 ID를 담을 리스트를 만든다.
        List<Integer> idList = new ArrayList<>();

        // ids 문자열이 비어있지 않으면 파싱한다.
        if (ids != null && !ids.isBlank()) {
            // 콤마 기준으로 나눠서 순회한다.
            for (String token : ids.split(",")) {
                // 공백을 제거한다.
                String value = token.trim();
                // 빈 값이 아니면 숫자로 변환해 담는다.
                if (!value.isEmpty()) {
                    idList.add(Integer.parseInt(value));
                }
            }
        }

        // 기존 응답 형태(success/data)를 유지한다.
        return Map.of("success", true, "data", service.getProductBatch(idList));
    }
    
    // 상품 검색 요청
    @PostMapping("/search/result")
    public Map<String, Object> searchResult(@RequestBody Map<String, Object> req) {
        // 검색 로직은 서비스에 위임한다.
        return service.searchProducts(req);
    }
    
    // 요청 본문에서 상품 ID를 추출
    private Integer extractProductId(Object body) {
        // body가 없으면 null을 반환한다.
        if (body == null) {
            return null;
        }

        // 숫자 타입이면 바로 int로 변환한다.
        if (body instanceof Number number) {
            return number.intValue();
        }

        // 문자열 타입이면 따옴표/공백 제거 후 숫자로 변환한다.
        if (body instanceof String value) {
            return Integer.parseInt(value.replace("\"", "").trim());
        }

        // Map 타입이면 product_id 우선, 없으면 id를 사용한다.
        if (body instanceof Map<?, ?> map) {
            Object idValue = map.get("product_id");
            if (idValue == null) {
                idValue = map.get("id");
            }
            return toInt(idValue);
        }

        // 그 외 타입은 문자열로 바꿔 숫자로 변환한다.
        return Integer.parseInt(String.valueOf(body));
    }
    
    // 다양한 타입의 값을 Integer로 변환
    private Integer toInt(Object value) {
        // 값이 없으면 null을 반환한다.
        if (value == null) {
            return null;
        }

        // 숫자 타입이면 int로 변환한다.
        if (value instanceof Number number) {
            return number.intValue();
        }

        // 문자열 등은 숫자로 파싱한다.
        return Integer.parseInt(String.valueOf(value));
    }
}
