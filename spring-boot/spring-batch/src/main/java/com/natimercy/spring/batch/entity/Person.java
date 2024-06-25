package com.natimercy.spring.batch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 *
 * @author <a href="mailto:mercyblitz@gmail.com">natimercy</a>
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private String name;

    private int age;

    private String address;

}
