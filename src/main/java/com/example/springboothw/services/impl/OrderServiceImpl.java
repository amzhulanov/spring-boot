package com.example.springboothw.services.impl;

import com.example.springboothw.entities.Address;
import com.example.springboothw.entities.Order;
import com.example.springboothw.entities.User;
import com.example.springboothw.repositories.OrderRepository;
import com.example.springboothw.services.OrderService;
import com.example.springboothw.services.UserService;
import com.example.springboothw.utils.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private UserService userService;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository,UserService userService) {
        this.orderRepository = orderRepository;
        this.userService=userService;
    }

    public Order save(Order order) {

        return orderRepository.save(order);
    }

    public Order save(Principal principal, Map<String,String> params, Cart cart){
        User user;
        Address address = new Address();
        String phone;
        if (principal==null){
            address.setCity("buyOneClick");
            address.setStreet("buyOneClick");
            address.setHouse("buyOneClick");
            user=userService.findByPhone("buyOneClick");
            phone=params.get("phoneOneClick");
        }else {
            address.setCity(params.get("city"));
            address.setStreet(params.get("street"));
            address.setHouse(params.get("house"));
            user = userService.findByPhone(principal.getName());
            phone=params.get("phone");
        }
        return orderRepository.save(new Order(user, cart,address,phone));
    }

    public List<Order> findAllOrdersByUser(User user){
        return orderRepository.findAllOrdersByUser(user);
    }

    public BigDecimal costOrders(List<Order> orders){
        BigDecimal cost=new BigDecimal(0);
        for (Order order: orders) {
            cost=cost.add(order.getCost());
        }
        return cost;
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * Добавляю во все записи заказов user_id, если у них совпадает номер телефона и user_id==null
     *
     */
    @Override
    public Integer checkOrders(User user) {

        return  orderRepository.updateAllOrder(user.getPhone(),user);

    }
}
