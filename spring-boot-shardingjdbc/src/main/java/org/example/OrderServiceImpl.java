package org.example;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * OrderServiceImp
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})

public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderMapper orderMapper;

}
