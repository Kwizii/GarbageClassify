package bistu.gbg.repository;

import bistu.gbg.entity.GiftOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author ChanvoBook
 */
public interface GiftOrderRepository extends JpaRepository<GiftOrder, String>, JpaSpecificationExecutor<GiftOrder> {

}
