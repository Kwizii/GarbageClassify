package lsgwr.exam.dto;

import lombok.Data;

@Data
public class GiftUpdateDTO {
    private String giftId;
    private String giftName;
    private String giftAvatar;
    private String giftDescription;
    private Integer giftPrice;
}
