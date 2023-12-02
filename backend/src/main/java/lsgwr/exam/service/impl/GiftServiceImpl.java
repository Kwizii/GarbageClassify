package lsgwr.exam.service.impl;

import cn.hutool.core.util.IdUtil;
import lsgwr.exam.dto.GiftOrderCreateDTO;
import lsgwr.exam.entity.Gift;
import lsgwr.exam.entity.GiftOrder;
import lsgwr.exam.enums.ResultEnum;
import lsgwr.exam.exception.ExamException;
import lsgwr.exam.repository.GiftOrderRepository;
import lsgwr.exam.repository.GiftRepository;
import lsgwr.exam.service.GiftService;
import lsgwr.exam.service.UserService;
import lsgwr.exam.vo.UserVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @author ChanvoBook
 */
@Service
@Transactional
public class GiftServiceImpl implements GiftService {

    final GiftRepository giftRepository;
    final GiftOrderRepository giftOrderRepository;
    final UserService userService;

    public GiftServiceImpl(GiftRepository giftRepository, GiftOrderRepository giftOrderRepository, UserService userService) {
        this.giftRepository = giftRepository;
        this.giftOrderRepository = giftOrderRepository;
        this.userService = userService;
    }


    @Override
    public List<Gift> findAll() {
        return giftRepository.findAll();
    }

    @Override
    public void createOrder(String userId, GiftOrderCreateDTO param) {
        Gift gift = giftRepository.getOne(param.getGiftId());
        UserVo userInfo = userService.getUserInfo(userId);
        if (userInfo.getScore() < gift.getGiftPrice()) {
            throw new ExamException(ResultEnum.NO_ENOUGH_SCORE);
        }
        GiftOrder giftOrder = new GiftOrder();
        giftOrder.setGiftId(param.getGiftId());
        giftOrder.setGiftAvatar(gift.getGiftAvatar());
        giftOrder.setGiftDescription(gift.getGiftDescription());
        giftOrder.setGiftPrice(gift.getGiftPrice());
        giftOrder.setGiftAvatar(gift.getGiftAvatar());
        giftOrder.setGiftName(gift.getGiftName());
        giftOrder.setOrderPhone(param.getOrderPhone());
        giftOrder.setOrderContact(param.getOrderContact());
        giftOrder.setOrderAddress(param.getOrderAddress());
        giftOrder.setCreateTime(new Date());
        giftOrder.setUpdateTime(new Date());
        giftOrder.setUserId(userId);
        giftOrder.setGiftOrderId(IdUtil.simpleUUID());
        userService.updateScore(userId, -giftOrder.getGiftPrice());
        giftOrderRepository.save(giftOrder);
    }

    @Override
    public Page<GiftOrder> getOrders(String userId, Pageable pageable) {
        return giftOrderRepository.findAll(userIdEqualsOne(userId), pageable);
    }

    public static Specification<GiftOrder> userIdEqualsOne(String userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId"), userId);
    }
}
