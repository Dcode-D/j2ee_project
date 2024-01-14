package com.gmail.j2ee.ecommerce.dto.order;

import com.gmail.j2ee.ecommerce.dto.perfume.PerfumeResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    private Long id;
    private Long amount;
    private Long quantity;
    private PerfumeResponse perfume;
}
