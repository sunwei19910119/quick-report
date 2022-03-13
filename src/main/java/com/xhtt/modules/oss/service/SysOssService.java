/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.xhtt.modules.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhtt.common.utils.PageUtils;
import com.xhtt.modules.oss.entity.SysOssEntity;
import com.xhtt.modules.sys.entity.SysUserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 文件上传
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-25 12:13:26
 */
public interface SysOssService extends IService<SysOssEntity> {

    PageUtils queryPage(Map<String, Object> params);

    String uploadFile(MultipartFile file, boolean watermark , SysUserEntity user) throws IOException;
}
