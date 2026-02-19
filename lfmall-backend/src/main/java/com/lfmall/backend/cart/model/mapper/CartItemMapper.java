package com.lfmall.backend.cart.model.mapper;

import com.lfmall.backend.cart.model.dto.CartItemDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper
public interface CartItemMapper {

    /** cartId 기준 아이템 목록 조회 */
    List<CartItemDto> selectItemsByCartId(@Param("cartId") Long cartId);

    /** cartItemId 단건 조회 */
    CartItemDto selectItemById(@Param("cartItemId") Long cartItemId);

    /**
     * 같은 상품/옵션이 이미 담겨있는지 조회 (업서트용)
     * - optionId는 null일 수 있으므로 XML에서 IS NOT DISTINCT FROM 또는 분기 처리 필요
     */
    CartItemDto selectSameItem(@Param("cartId") Long cartId,
                               @Param("productId") Long productId,
                               @Param("optionId") Long optionId);

    /**
     * 아이템 추가
     * - BIGSERIAL이면 XML에서 INSERT ... RETURNING cart_item_id 로 cartItemId 세팅 추천
     */
    int insertItem(CartItemDto itemDto);

    /**
     * 아이템 수정 (quantity/optionId/unitPrice/isSelected/updatedAt 등)
     * - DTO 분리 없이 하니, XML에서 null 필드 업데이트 정책을 잘 정해야 함
     *   (서비스에서 null 보정 후 호출하는 방식이면 단순 UPDATE로 가능)
     */
    int updateItem(CartItemDto itemDto);

    /** 선택여부만 수정 */
    int updateSelected(@Param("cartItemId") Long cartItemId,
                       @Param("isSelected") Boolean isSelected,
                       @Param("updatedAt") OffsetDateTime updatedAt);

    /** cartItemId 삭제 */
    int deleteItem(@Param("cartItemId") Long cartItemId);

    /** cartId 기준 전체 삭제(비우기) */
    int deleteByCartId(@Param("cartId") Long cartId);

    /** cartId에서 선택된 항목만 삭제 */
    int deleteSelectedByCartId(@Param("cartId") Long cartId);
}