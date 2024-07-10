package org.example;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.Order;

import java.util.List;

/**
 * OrderService
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
public interface OrderService extends IService<Order> {

    List<Order> getOrderList(Order order);

}
