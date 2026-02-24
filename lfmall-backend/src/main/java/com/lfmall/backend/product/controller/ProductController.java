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

    @PostMapping("/product/list")
    public List<Map<String, Object>> productList(@RequestBody Map<String, Object> body) {
        Integer categoryId = toInt(body.get("category_id"));
        String gender = body.get("gender") == null ? null : String.valueOf(body.get("gender"));
        return service.getProductList(categoryId, gender);
    }

    @PostMapping("/product/detail")
    public Map<String, Object> productDetail(@RequestBody Object body) {
        Integer productId = extractId(body);
        return service.getProductDetail(productId);
    }

    @PostMapping("/product/option")
    public List<Map<String, Object>> productOption(@RequestBody Object body) {
        Integer productId = extractId(body);
        return service.getProductOptions(productId);
    }

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

    @PostMapping("/search/result")
    public Map<String, Object> searchResult(@RequestBody Map<String, Object> req) {
        return service.searchProducts(req);
    }

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

    private Integer toInt(Object v) {
        if (v == null) return null;
        if (v instanceof Number) return ((Number) v).intValue();
        return Integer.parseInt(String.valueOf(v));
    }
}
