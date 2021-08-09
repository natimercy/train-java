package hw.topevery.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * todo
 *
 * @author hq
 * @date 2021-05-21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SmsAutoConfiguration.class})
public @interface EnableSms {
}
