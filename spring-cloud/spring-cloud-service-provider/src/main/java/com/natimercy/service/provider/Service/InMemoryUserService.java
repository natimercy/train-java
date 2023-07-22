package com.natimercy.service.provider.Service;

import com.natimercy.service.provider.entity.User;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * InMemoryUserService
 *
 * @author <a href="mailto:mercyblitz@gmail.com">natimercy</a>
 * @since 1.0.0
 */
@Service
public class InMemoryUserService implements UserService {

    private final RestTemplate rest;

    private final CircuitBreakerFactory cbFactory;

    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    public InMemoryUserService(RestTemplate rest, CircuitBreakerFactory cbFactory) {
        this.rest = rest;
        this.cbFactory = cbFactory;
    }

    @Override
    public boolean save(User user) {
        return users.put(user.getId(), user) == null;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    public String slow() {
        return cbFactory.create("slow")
                .run(() -> rest.getForObject("/slow", String.class), throwable -> "fallback");
    }

}
