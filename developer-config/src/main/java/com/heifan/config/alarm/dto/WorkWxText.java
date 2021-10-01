package com.heifan.config.alarm.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author z201.coding@gmail.com
 * @date 2020-11-09
 **/
@Getter
@Setter
public class WorkWxText {

    String msgtype = "markdown";

    WorkWxTxtText markdown;

    String[] mentioned_mobile_list;

    public WorkWxText(String markdown, String[] mentioned_mobile_list) {
        this.markdown = new WorkWxTxtText(markdown);
        this.mentioned_mobile_list = mentioned_mobile_list;
    }

}
