package com.lfmall.backend.cart.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lfmall.backend.cart.model.dto.CartDto;
@Mapper
public interface CartMapper {

    List<CartDto> selectCartsByMemberId(@Param("memberId") Long memberId);

    Integer selectCartQuantity(@Param("memberId") Long memberId,
                              @Param("stockId") Long stockId);

    Long selectCartIdByMemberAndStock(@Param("memberId") Long memberId,
                                      @Param("stockId") Long stockId);

    int insertCart(@Param("memberId") Long memberId,
                   @Param("stockId") Long stockId,
                   @Param("quantity") Integer quantity);

    int updateCartQuantityById(@Param("cartId") Long cartId,
                               @Param("quantity") Integer quantity);

    int updateCartQuantityByMemberAndStock(@Param("memberId") Long memberId,
                                           @Param("stockId") Long stockId,
                                           @Param("quantity") Integer quantity);

    int updateCartStockAndQuantity(@Param("cartId") Long cartId,
                                   @Param("memberId") Long memberId,
                                   @Param("newStockId") Long newStockId,
                                   @Param("quantity") Integer quantity);

    int deleteCartItems(@Param("memberId") Long memberId,
                        @Param("cartIds") List<Long> cartIds);
}