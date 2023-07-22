package com.natimercy.service.provider.Service;

import com.natimercy.service.provider.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * TODO
 *
 * @author <a href="mailto:mercyblitz@gmail.com">natimercy</a>
 * @since 1.0.0
 */
@RestController
@RequestMapping("/user")
public interface UserService {

    @PostMapping("/save")
    boolean save(@RequestBody User user);

    @GetMapping("/getAllUsers")
    Collection<User> getAllUsers();

}
