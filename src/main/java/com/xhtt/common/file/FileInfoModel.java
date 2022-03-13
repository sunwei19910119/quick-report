package com.xhtt.common.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片信息
 *
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoModel {

    /**
     * 系统随机的名称
     */
    private String name;
    /**
     * 原始名称
     */
    private String fileOrignalName;

}
