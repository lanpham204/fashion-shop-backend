package com.shop.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SizeResponse {

    @JsonProperty("id")
    private int id;

    @JsonProperty("size")
    private String size;

}

