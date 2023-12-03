package lsgwr.exam.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lsgwr.exam.dto.GiftUpdateDTO;
import lsgwr.exam.dto.GiftOrderCreateDTO;
import lsgwr.exam.entity.Gift;
import lsgwr.exam.entity.GiftOrder;
import lsgwr.exam.exception.ExamException;
import lsgwr.exam.service.GiftService;
import lsgwr.exam.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("/update")
    @ApiOperation("更新物品")
    ResultVO<Gift> updateGift(@RequestBody GiftUpdateDTO param) {
        try {
            Gift gift = giftService.updateGift(param);
            return new ResultVO<>(0, null, gift);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO<>(1, "更新异常", null);
        }
    }

    @PostMapping("/new")
    @ApiOperation("创建物品")
    ResultVO<Gift> createGift(@RequestBody GiftUpdateDTO param) {
        try {
            Gift gift = giftService.createGift(param);
            return new ResultVO<>(0, null, gift);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO<>(1, "创建异常", null);
        }
    }

    @PostMapping("/del")
    @ApiOperation("删除物品")
    ResultVO delGift(@RequestParam String giftId) {
        try {
            giftService.delById(giftId);
            return new ResultVO<>(0, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO<>(1, "删除失败", null);
        }
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
