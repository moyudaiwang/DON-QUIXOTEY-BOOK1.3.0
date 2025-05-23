package com.element.hydrogen.utils.common.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @InterfaceName IsNeeded
 * @Description TODO
 * @Author yanyu
 * @Date 2020/12/2 22:55
 * @Version 1.0
 * 自定义注解：是否需要从解析excel赋值
 * @author daochuwenziyao
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface IsNeeded {

    /**
     * 是否需要从解析excel赋值
     * @return
     *   true:需要 false:不需要
     * @see [类、类#方法、类#成员]
     */
    boolean isNeeded() default true;

}
