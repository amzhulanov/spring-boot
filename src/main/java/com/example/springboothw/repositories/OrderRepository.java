package com.example.springboothw.repositories;

import com.example.springboothw.entities.Order;
import com.example.springboothw.entities.OrderItem;
import com.example.springboothw.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("from Order o where o.user=:user")
    List<Order> findAllOrdersByUser(User user);


    @Modifying
    @Query("update Order o SET o.user=:user where o.phone=:phone and o.user.id=4 ")
    Integer updateAllOrder(String phone, User user);

    Order findByPhone(String phone);

}

