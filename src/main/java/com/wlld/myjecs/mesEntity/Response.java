package com.wlld.myjecs.mesEntity;

import com.wlld.myjecs.sqlEntity.Admin;
import com.wlld.myjecs.sqlEntity.KeywordType;
import com.wlld.myjecs.sqlEntity.MyTree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @param
 * @DATA
 * @Author LiDaPeng
 * @Description
 */
@Getter
@Setter
@ApiModel
public class Response {
    @ApiModelProperty(value = "错误码", example = "1")
    private int error;
    @ApiModelProperty(value = "错误信息", example = "通讯异常")
    private String errorMessage;
    @ApiModelProperty(value = "语义识别结果")
    private Shop shop;
    @ApiModelProperty(value = "身份1是管理员 2是打标员")
    private int role;
    @ApiModelProperty(value = "返回的请求类型", example = "1")
    private int responseType;
    @ApiModelProperty(value = "当前所有分类")
    private List<MyTree> myTrees;
    @ApiModelProperty(value = "当前所有关键词")
    private List<KeywordType> keywordTypeList;
    @ApiModelProperty(value = "当前所有待通过账号")
    private List<Admin> admins;
    @ApiModelProperty(value = "返回id", example = "2")
    private int resultID;
    @ApiModelProperty(value = "返回今日标注语句")
    private List<AdminSentence> adminSentenceList;
    @ApiModelProperty(value = "回复语句")
    private String answer;
}
