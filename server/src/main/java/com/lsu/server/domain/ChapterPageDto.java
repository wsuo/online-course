package com.lsu.server.domain;

import com.lsu.server.dto.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 大章的扩展类
 *
 * @Author wang suo
 * @Date 2020/10/14 0014 15:41
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class ChapterPageDto<T> extends PageDto<T> {
    private String courseId;
}
