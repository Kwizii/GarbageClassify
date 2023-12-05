package bistu.gbg.utils;

import bistu.gbg.vo.ResultVO;

public class ResultVOUtil {

    public static ResultVO success(Integer code, String msg, Object object) {
        return new ResultVO(code, msg, object);
    }

    public static ResultVO success(Object object) {
        return new ResultVO(0, "成功", object);
    }

    public static ResultVO success() {
        return new ResultVO(0, "成功", null);
    }


    public static ResultVO error(Integer code, String msg) {
        return new ResultVO(code, msg, null);
    }
}
