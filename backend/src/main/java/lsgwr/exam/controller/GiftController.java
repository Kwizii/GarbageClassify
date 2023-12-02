package lsgwr.exam.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lsgwr.exam.dto.GiftOrderCreateDTO;
import lsgwr.exam.entity.Gift;
import lsgwr.exam.entity.GiftOrder;
import lsgwr.exam.enums.ResultEnum;
import lsgwr.exam.exception.ExamException;
import lsgwr.exam.service.GiftService;
import lsgwr.exam.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * @author ChanvoBook
 */
@RestController
@Api(tags = "Shop APIs")
@RequestMapping("/api/gift")
public class GiftController {

    @Autowired
    private GiftService giftService;

    @GetMapping("/list")
    @ApiOperation("获取物品列表")
    ResultVO<List<Gift>> getGiftList() {
        return new ResultVO<>(0, "", giftService.findAll());
    }

    @PostMapping("/order/new")
    @ApiOperation("创建物品订单")
    ResultVO createGiftOrder(@RequestBody GiftOrderCreateDTO param, HttpServletRequest request) {
        String userId = (String) request.getAttribute("user_id");
        try {
            giftService.createOrder(userId, param);
            return new ResultVO<>(0, null, null);
        } catch (ExamException e) {
            e.printStackTrace();
            return new ResultVO(e.getCode(), e.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO(1, "订单创建失败", null);
        }
    }

    @GetMapping("/order/list")
    @ApiOperation("获取订单")
    ResultVO<Page<GiftOrder>> getGiftOrders(HttpServletRequest request,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        String userId = (String) request.getAttribute("user_id");
        try {
            Page<GiftOrder> orders = giftService.getOrders(userId, PageRequest.of(page, size, Sort.by("createTime").descending()));
            return new ResultVO<>(0, null, orders);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO<>(1, "获取订单失败", null);
        }
    }

}
