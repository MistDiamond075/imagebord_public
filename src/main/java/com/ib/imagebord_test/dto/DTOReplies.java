package com.ib.imagebord_test.dto;

public class DTOReplies {
    private String text;

    private Long number;

    private Integer reply_count;

    private String name;

    private String time;

    private Integer thread_id;

    private Integer receiver;

   private String docpath;

    public DTOReplies(String text, Long number, Integer reply_count, String name, String time, Integer thread_id, Integer receiver, String docpath) {
        this.text = text;
        this.number = number;
        this.reply_count = reply_count;
        this.name = name;
        this.time = time;
        this.thread_id = thread_id;
        this.receiver = receiver;
        this.docpath = docpath;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Integer getReply_count() {
        return reply_count;
    }

    public void setReply_count(Integer reply_count) {
        this.reply_count = reply_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getThread_id() {
        return thread_id;
    }

    public void setThread_id(Integer thread_id) {
        this.thread_id = thread_id;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    public String getDocpath() {
        return docpath;
    }

    public void setDocpath(String docpath) {
        this.docpath = docpath;
    }
}
