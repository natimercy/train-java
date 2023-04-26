package org.natimercy.generator.enums;

/**
 * JavaType
 *
 * @author qian.he
 * @since 2023-04-26
 * @version 1.0.0
 */
public enum JavaType {

    STRING("", String.class.getName()),

    INTEGER(-1, Integer.class.getName(), int.class.getName()),

    LONG(-1L, Long.class.getName(), long.class.getName()),

    FLOAT(-1f, Float.class.getName(), float.class.getName()),

    DOUBLE(-1, Double.class.getName(), double.class.getName());

    private Object defaultValue;

    private String[] simpleClasses;

    JavaType(Object defaultValue, String... simpleClasses) {
        this.defaultValue = defaultValue;
        this.simpleClasses = simpleClasses;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public String[] getSimpleClasses() {
        return simpleClasses;
    }
}
