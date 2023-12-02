/***********************************************************
 * @Description : 注册接口参数
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-16 23:40
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package lsgwr.exam.dto;

import lombok.Data;

@Data
public class PredictionDTO {
    private String cls;
    private Double conf;
}
