package org.example;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Order
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
@Data
public class Order {

    private Long orderId;

    private LocalDateTime createTime;

}
