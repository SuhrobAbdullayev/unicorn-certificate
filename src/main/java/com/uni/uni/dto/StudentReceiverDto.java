package com.uni.uni.dto;

import lombok.Builder;

@Builder
public record StudentReceiverDto(
        String firstName,
        String lastName,
        String course
) {
}
