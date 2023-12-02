package lsgwr.exam.repository;

import lsgwr.exam.entity.GiftOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author ChanvoBook
 */
public interface GiftOrderRepository extends JpaRepository<GiftOrder, String>, JpaSpecificationExecutor<GiftOrder> {

}
