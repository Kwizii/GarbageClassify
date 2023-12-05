package bistu.gbg.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String email;
    private String password;
    private String password2;
    private String nickname;
    private String mobile;
    /**
     * 验证码
     */
//    private String captcha;
}
