package bistu.gbg.service.impl;

import bistu.gbg.enums.ResultEnum;
import bistu.gbg.vo.UserVo;
import cn.hutool.core.util.IdUtil;
import bistu.gbg.dto.GiftOrderCreateDTO;
import bistu.gbg.dto.GiftUpdateDTO;
import bistu.gbg.entity.Gift;
import bistu.gbg.entity.GiftOrder;
import bistu.gbg.exception.ExamException;
import bistu.gbg.repository.GiftOrderRepository;
import bistu.gbg.repository.GiftRepository;
import bistu.gbg.service.GiftService;
import bistu.gbg.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Gift createGift(GiftUpdateDTO param) {
        Gift gift = new Gift(IdUtil.simpleUUID(),
                param.getGiftName(),
                param.getGiftAvatar(),
                param.getGiftDescription(),
                param.getGiftPrice(),
                new Date(),
                new Date());
        return giftRepository.save(gift);
    }

    @Override
    public Gift updateGift(GiftUpdateDTO param) {
        Gift gift = giftRepository.getOne(param.getGiftId());
        gift.setUpdateTime(new Date());
        gift.setGiftName(param.getGiftName());
        gift.setGiftDescription(param.getGiftDescription());
        gift.setGiftAvatar(param.getGiftAvatar());
        gift.setGiftPrice(param.getGiftPrice());
        return giftRepository.save(gift);
    }

    @Override
    public void delById(String giftId) {
        giftRepository.deleteById(giftId);
    }

    public static Specification<GiftOrder> userIdEqualsOne(String userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId"), userId);
    }
}
