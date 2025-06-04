package com.alten.back.exceptions.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NotNull
@Builder
public class ErrorBody {
    private Integer code;
    private String message;
}
