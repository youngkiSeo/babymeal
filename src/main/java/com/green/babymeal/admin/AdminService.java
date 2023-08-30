package com.green.babymeal.admin;

import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.common.repository.OrderlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private OrderlistRepository orderlistRepository;


    public Page<OrderlistEntity> allOrder(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return orderlistRepository.findByCreatedAtBetween(startDate, endDate, pageable);
    }

    public List<OrderlistEntity> selOrder(Long orderCode) {
        return orderlistRepository.findOrderById(orderCode);
    }
}
