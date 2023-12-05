package bistu.gbg.dto;

import lombok.Data;

@Data
public class ModelEndResponseDTO<T> {
    private String message;
    private Integer code;
    private T data;
}
