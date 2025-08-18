package com.sinse.broadcastcapp.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ResponseConnect {
    private String responseType;
    private Set<String> data;
}
