package org.example;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
