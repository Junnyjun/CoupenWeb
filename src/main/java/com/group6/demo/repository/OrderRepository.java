package com.group6.demo.repository;

import com.group6.demo.entity.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    List<Orders> findByMemberAccount(String memberAccount);

    Optional<Orders> findByMemberId(Long id);

}
