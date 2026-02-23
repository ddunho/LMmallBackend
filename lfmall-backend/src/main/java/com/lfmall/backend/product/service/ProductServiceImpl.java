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

    @Override
    public List<Map<String, Object>> getProductList(Integer categoryId) {
        List<Map<String, Object>> baseList = mapper.selectProductListByCategory(categoryId);
        List<Map<String, Object>> out = new ArrayList<>();
        for (Map<String, Object> base : baseList) {
            out.add(enrichProduct(base));
        }
        return out;
    }

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

    @Override
    public List<Map<String, Object>> getProductOptions(Integer productId) {
        return mapper.selectOptionsByProductId(productId);
    }

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

    private Integer toInt(Object v) {
        if (v == null) return null;
        if (v instanceof Number) return ((Number) v).intValue();
        return Integer.parseInt(String.valueOf(v));
    }

    private String asString(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

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
