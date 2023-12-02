/***********************************************************
 * @Description : 用户接口
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-17 08:02
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package lsgwr.exam.service;

import lsgwr.exam.dto.GiftOrderCreateDTO;
import lsgwr.exam.entity.Gift;
import lsgwr.exam.entity.GiftOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GiftService {

    List<Gift> findAll();

    void createOrder(String userId,GiftOrderCreateDTO param);

    Page<GiftOrder> getOrders(String userId, Pageable pageable);
}
