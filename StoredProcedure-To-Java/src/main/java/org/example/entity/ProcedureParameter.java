package org.example.entity;

import lombok.Data;

/**
 * 存储过程属性封装
 *
 * @author hq
 * @date 2020-12-08
 */
@Data
public class ProcedureParameter {

    private String specificSchema;

    private String specificName;

    private String parameterMode;

    private String parameterName;

    private String dataType;

    private Object value;

}
