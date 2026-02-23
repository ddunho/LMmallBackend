package com.lfmall.backend.cart.model.dto;

import java.time.OffsetDateTime;

public class CartItemDto {

    /** 상품목록번호 (PK, BIGSERIAL) */
    private Long cartItemId;

    /** 장바구니번호 (FK, BIGINT) */
    private Long cartId;

    /** 상품번호 (FK, BIGINT) */
    private Long productId;

    /** 옵션번호 (FK, BIGINT) */
    private Long optionId;

    /** 수량 (INT, NOT NULL, CHECK (quantity > 0)) */
    private Integer quantity;

    /** 구매가격 (INT, NOT NULL) - 담을 당시 가격(스냅샷) */
    private Integer unitPrice;

    /** 선택된주문 (BOOLEAN, NOT NULL, default false) */
    private Boolean isSelected;

    /** 생성일 (TIMESTAMPTZ, NOT NULL, default now()) */
    private OffsetDateTime createdAt;

    /** 수정일 (TIMESTAMPTZ, NOT NULL, default now()) */
    private OffsetDateTime updatedAt;

    public CartItemDto() {}

    public CartItemDto(Long cartItemId, Long cartId, Long productId, Long optionId,
                       Integer quantity, Integer unitPrice, Boolean isSelected,
                       OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.isSelected = isSelected;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean selected) {
        isSelected = selected;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "CartItemDto{" +
                "cartItemId=" + cartItemId +
                ", cartId=" + cartId +
                ", productId=" + productId +
                ", optionId=" + optionId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", isSelected=" + isSelected +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
