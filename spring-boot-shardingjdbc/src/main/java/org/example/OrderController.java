package org.example;

import lombok.RequiredArgsConstructor;
import org.example.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OrderController
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
@RestController
@RequestMapping(value = "/order")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OrderController {

    private final OrderService orderService;

    @GetMapping(value = "/getOrderList")
    public List<Order> getOrderList() {
        return orderService.getOrderList(null);
    }

    @PostMapping(value = "/save")
    public boolean save(@RequestBody Order order) {
        return orderService.save(order);
    }

}
