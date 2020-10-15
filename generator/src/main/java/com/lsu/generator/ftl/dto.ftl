package com.lsu.server.dto;

<#list typeSet as type>
<#if type=='Date'>
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
</#if>
import lombok.Data;
import lombok.ToString;
<#if type=='BigDecimal'>
import java.math.BigDecimal;
</#if>
</#list>

@Data
@ToString
public class ${Domain}Dto {

    <#list fieldList as field>
    /**
     * ${field.comment}
     */
    <#if field.javaType=='Date'>
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    </#if>
    private ${field.javaType} ${field.nameHump};

    </#list>
}