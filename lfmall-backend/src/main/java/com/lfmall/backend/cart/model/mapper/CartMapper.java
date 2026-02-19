package com.lfmall.backend.cart.model.mapper;

import com.lfmall.backend.cart.model.dto.CartDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.OffsetDateTime;

@Mapper
public interface CartMapper {

    /** cartId로 장바구니 조회 */
    CartDto selectCartById(@Param("cartId") Long cartId);

    /** userId의 ACTIVE 장바구니 조회 (없으면 null) */
    CartDto selectActiveCartByUserId(@Param("userId") Long userId);

    /**
     * 장바구니 생성
     * - BIGSERIAL이면 XML에서 INSERT ... RETURNING cart_id 로 cartId 세팅 추천
     */
    int insertCart(CartDto cartDto);

    /** 장바구니 수정 (status, updatedAt 등) */
    int updateCart(CartDto cartDto);

    /**
     * 소프트 삭제(권장): status 변경 + updatedAt 갱신
     * - 예: status = 'DELETED'
     */
    int softDeleteCart(@Param("cartId") Long cartId,
                       @Param("status") String status,
                       @Param("updatedAt") OffsetDateTime updatedAt);
}