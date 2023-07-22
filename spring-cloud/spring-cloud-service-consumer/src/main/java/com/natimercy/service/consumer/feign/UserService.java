package com.natimercy.service.consumer.feign;

import com.natimercy.service.consumer.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

/**
 * TODO
 *
 * @author <a href="mailto:mercyblitz@gmail.com">natimercy</a>
 * @since 1.0.0
 */
@FeignClient(name = "${feign.name}")
public interface UserService {

    @GetMapping("/user/getAllUsers")
    Collection<User> getAllUsers();

}
