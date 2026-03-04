package com.lfmall.backend.product.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {

    // 카테고리/성별 기준 상품 목록 조회 쿼리
    List<Map<String, Object>> selectProductListByCategory(
        @Param("categoryId") Integer categoryId,
        @Param("gender") String gender
    );

    // 상품 기본 정보 단건 조회 쿼리
    Map<String, Object> selectProductBaseById(@Param("productId") Integer productId);

    // 상품 기본 정보 다건 조회 쿼리
    List<Map<String, Object>> selectProductBaseByIds(@Param("ids") List<Integer> ids);

    // 상품 썸네일/대표 이미지 경로 조회 쿼리
    List<String> selectImageNamesByProductId(@Param("productId") Integer productId);

    // 상품 상세 이미지 목록 조회 쿼리
    List<Map<String, Object>> selectDetailImagesByProductId(@Param("productId") Integer productId);

    // 상품 색상 목록 조회 쿼리
    List<String> selectColorsByProductId(@Param("productId") Integer productId);

    // 상품 사이즈 목록 조회 쿼리
    List<String> selectSizesByProductId(@Param("productId") Integer productId);

    // 상품 옵션/재고 정보 조회 쿼리
    List<Map<String, Object>> selectOptionsByProductId(@Param("productId") Integer productId);

    // 검색 조건 기반 상품 검색 쿼리
    List<Map<String, Object>> searchProducts(
        @Param("searchtxt") String searchtxt,
        @Param("searchtype") String searchtype,
        @Param("color") List<String> color,
        @Param("size") List<String> size,
        @Param("brandName") List<String> brandName,
        @Param("addSearch") List<String> addSearch
    );
}
