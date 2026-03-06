package com.lfmall.backend.cart.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lfmall.backend.cart.model.dto.CartDto;
@Mapper
public interface CartMapper {

    List<Map<String, Object>> selectCartsByMemberId(@Param("memberId") Long memberId);
    
    int selectCartCountByMemberId(@Param("memberId") Long memberId);
    List<Map<String, Object>> selectOptionsByProductIds(List<Integer> productIds);
    Integer selectCartQuantity(@Param("memberId") Long memberId,
                              @Param("stockId") Long stockId);

    Long selectCartIdByMemberAndStock(@Param("memberId") Long memberId,
                                      @Param("stockId") Long stockId);

    int insertCart(@Param("memberId") Long memberId,
                   @Param("stockId") Long stockId,
                   @Param("optionId") Long optionId,
                   @Param("quantity") Integer quantity);

    /**향후 updateOption으로 통합요망*/
    int updateCartQuantityById(@Param("cartId") Long cartId,
                               @Param("quantity") Integer quantity);
    /**향후 updateOption으로 통합요망*/
    int updateCartQuantityByMemberAndStock(@Param("memberId") Long memberId,
                                           @Param("stockId") Long stockId,
                                           @Param("optionId") Long optionId,
                                           @Param("quantity") Integer quantity);
    /**향후 updateOption으로 통합요망*/
    int updateCartStockAndQuantity(@Param("cartId") Long cartId,
                                   @Param("memberId") Long memberId,
                                   @Param("newStockId") Long newStockId,
                                   @Param("quantity") Integer quantity);

    int updateOption(@Param("cartId") Long cartId,
            @Param("memberId") Long memberId,
            @Param("quantity") Integer quantity,
            @Param("optionId") Long optionId);
    
    int deleteCartItems(@Param("memberId") Long memberId,
                        @Param("cartIds") List<Long> cartIds);
}
