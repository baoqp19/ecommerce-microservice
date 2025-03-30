package com.example.product_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CategoryDto implements Serializable {
    
     @Serial
    private static final long serialVersionUID = 1L;

    private Integer categoryId;
    private String categoryTitle;
    private String imageUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL) // nếu subCategoriesDtos khác null thì hiển thị đầu ra của Json, ngược lại nếu null thì sẽ ko được hiển thị ở đầu ra của Json.
    private Set<CategoryDto> subCategoriesDtos;

    @JsonProperty("parentCategory") // tên hiển thị khi chuyển sang Json
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CategoryDto parentCategoryDto;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<ProductDto> productDtos;

}
