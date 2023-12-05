package bistu.gbg.repository;

import bistu.gbg.entity.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author ChanvoBook
 */
public interface GiftRepository extends JpaRepository<Gift, String> {

    @Query("SELECT g FROM Gift g ORDER BY createTime desc")
    List<Gift> findAll();
}
