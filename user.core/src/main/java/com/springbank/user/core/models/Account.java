package com.springbank.user.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Size(min = 2, message = "Username must be at least 2 characters long")
    private String userName;
    @Size(min = 7, message = "Password must be at least 7 characters long")
    private String password;
    @NotNull(message = "Please provide at least one role")
    private List<Role> roles;
}
