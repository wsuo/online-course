package com.lsu.server.domain;

import com.lsu.server.dto.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 小结查询条件类
 *
 * @Author wang suo
 * @Date 2020/10/14 0014 16:21
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class SectionPageDto<T> extends PageDto<T> {
    private String courseId;
    private String chapterId;
}
