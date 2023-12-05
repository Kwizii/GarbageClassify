package bistu.gbg.dto;

import lombok.Data;

@Data
public class PredictionDTO {
    private String cls;
    private Double conf;
}
