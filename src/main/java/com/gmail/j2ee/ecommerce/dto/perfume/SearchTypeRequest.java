package com.gmail.j2ee.ecommerce.dto.perfume;

import com.gmail.j2ee.ecommerce.enums.SearchPerfume;
import lombok.Data;

@Data
public class SearchTypeRequest {
    private SearchPerfume searchType;
    private String text;
}
