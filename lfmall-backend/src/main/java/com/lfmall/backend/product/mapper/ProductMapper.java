package com.lfmall.backend.product.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {

    List<Map<String, Object>> selectProductListByCategory(@Param("categoryId") Integer categoryId);

    Map<String, Object> selectProductBaseById(@Param("productId") Integer productId);

    List<Map<String, Object>> selectProductBaseByIds(@Param("ids") List<Integer> ids);

    List<String> selectImageNamesByProductId(@Param("productId") Integer productId);

    List<Map<String, Object>> selectDetailImagesByProductId(@Param("productId") Integer productId);

    List<String> selectColorsByProductId(@Param("productId") Integer productId);

    List<String> selectSizesByProductId(@Param("productId") Integer productId);

    List<Map<String, Object>> selectOptionsByProductId(@Param("productId") Integer productId);

    List<Map<String, Object>> searchProducts(
        @Param("searchtxt") String searchtxt,
        @Param("searchtype") String searchtype,
        @Param("color") List<String> color,
        @Param("size") List<String> size,
        @Param("brandName") List<String> brandName,
        @Param("addSearch") List<String> addSearch
    );
}
