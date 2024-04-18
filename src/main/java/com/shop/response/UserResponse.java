package com.shop.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.models.Role;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private int id;
    private String email;
    private String password;
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("is_active")
    private boolean isActive;
    @JsonProperty("role_id")
    private int roleId;
}
