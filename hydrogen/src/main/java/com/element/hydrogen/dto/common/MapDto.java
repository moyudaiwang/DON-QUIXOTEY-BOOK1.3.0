package com.element.hydrogen.dto.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName MapDto
 * @Description TODO
 * @Author yanyu
 * @Date 2024/1/9 23:24
 * @Version 1.0
 */
@Data
public class MapDto implements Serializable {

    //label
    private String label;
    //val
    private String val;

}
