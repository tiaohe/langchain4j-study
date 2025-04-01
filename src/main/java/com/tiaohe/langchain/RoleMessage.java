package com.tiaohe.langchain;

import lombok.Data;

@Data
public class RoleMessage {
    private String role;
    private String message;
}
