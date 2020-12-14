package com.markly;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.markly.entity.SysUser;
import com.markly.service.SysUserService;
import com.markly.service.impl.SysUserServiceImpl;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

/**
 * n
 *
 * @author hq
 * @date 2020-11-04
 */
public class LocalTimeDemo {

    private String test;

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String FIX_CONDITION = "where 1 = 1 and c_name like '%%%s%%'";

    public static void main(String[] args) {


    }

    public static void getFields(Class<? extends SysUserService> targetClass) {
        Field[] declaredFields = targetClass.getDeclaredFields();
        System.out.println(Arrays.toString(declaredFields));
    }

    /**
     *
     * 发顺丰
     *
     * @param obj fff
     * @param targetEnum fff
     * @param fileList fffff
     */
    public static void setFileValue(Object obj, Class<?> targetEnum, List<?> fileList) {
        System.out.println(targetEnum);
        Assert.isTrue(targetEnum.isEnum(), "targetEnum is not enumeration class");

        Method values = ReflectionUtils.findMethod(targetEnum, "values");
        Object[] invoke = (Object[]) ReflectionUtils.invokeMethod(values, null);
        System.out.println(invoke);

        Stream.of(1).forEach(fileEnum -> {
            List<?> files = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(files)) {
                // Field field = ReflectionUtils.findField(obj.getClass(), fileEnum.getFiledName());
                Field field = null;
                if (field != null) {
                    Class<?> type = field.getType();
                    if (Collection.class.isAssignableFrom(type)) {
                        ReflectionUtils.setField(field, obj, files);
                    } else if (Map.class == type || type.isArray()) {
                        return;
                    } else {
                        ReflectionUtils.setField(field, obj, files.get(0));
                    }
                }
            }
        });
    }

    private static class TestFiles {

        private List<SysUser> test1;

        private SysUser test2;

        private SysUser[] test3;

        public List<SysUser> getTest1() {
            return test1;
        }

        public void setTest1(List<SysUser> test1) {
            this.test1 = test1;
        }

        public SysUser getTest2() {
            return test2;
        }

        public void setTest2(SysUser test2) {
            this.test2 = test2;
        }
    }
}
