package bistu.gbg.service;

import bistu.gbg.dto.GiftOrderCreateDTO;
import bistu.gbg.dto.GiftUpdateDTO;
import bistu.gbg.entity.Gift;
import bistu.gbg.entity.GiftOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GiftService {

    List<Gift> findAll();

    void createOrder(String userId,GiftOrderCreateDTO param);

    Page<GiftOrder> getOrders(String userId, Pageable pageable);

    Gift createGift(GiftUpdateDTO param);

    Gift updateGift(GiftUpdateDTO param);

    void delById(String giftId);
}
