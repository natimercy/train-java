package org.example;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Order
 *
 * @author <a href="mailto:natimercy93@gmail.com">natimercy</a>
 * @version 1.0.0
 */
@Data
@TableName("t_order_0")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long orderId;

    private LocalDateTime createTime;

}
