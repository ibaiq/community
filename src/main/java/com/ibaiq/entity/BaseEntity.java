package com.ibaiq.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author 十三
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginTime;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    public LocalDate getBeginTime() {
        return beginTime;
    }

    @JsonProperty
    public void setBeginTime(LocalDate beginTime) {
        this.beginTime = beginTime;
    }

    @JsonIgnore
    public LocalDate getEndTime() {
        return endTime;
    }

    @JsonProperty
    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

}
