package com.lfmall.backend.cart.model.dto;

import java.time.OffsetDateTime;

public class CartDto {

    /** 장바구니번호 (PK, BIGSERIAL) */
    private Long cartId;

    /** 회원번호 (FK, BIGINT) */
    private Long userId;

    /** 장바구니상태 (VARCHAR(20), NOT NULL, default 'ACTIVE') */
    private String status;

    /** 생성일 (TIMESTAMPTZ, NOT NULL, default now()) */
    private OffsetDateTime createdAt;

    /** 수정일 (TIMESTAMPTZ, NOT NULL, default now()) */
    private OffsetDateTime updatedAt;

    public CartDto() {}

    public CartDto(Long cartId, Long userId, String status,
                   OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.cartId = cartId;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return "CartDto{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}