package org.natimercy.generator.entity;

import lombok.Data;

/**
 * 存储过程属性封装
 *
 * @author hq
 * @date 2020-12-08
 */
@Data
public class ProcedureParameter {

    /**
     * 过程名称
     */
    private String procedure;

    private String specificSchema;

    private String specificName;

    private String parameterMode;

    /**
     * 参数名称
     */
    private String parameterName;

    /**
     * 参数名称
     */
    private String dataType;

    private Object value;

}
