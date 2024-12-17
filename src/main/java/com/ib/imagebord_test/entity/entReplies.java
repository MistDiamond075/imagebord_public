package com.ib.imagebord_test.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="replies",schema="db_imagebord")
@NamedQuery(name="replies.getAll",query = "select c from entReplies c")
public class entReplies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="thread_id")
    private entThread threadid;

    @Column(name="text",columnDefinition = "TEXT")
    private String text;

    @Column(name="date")
    private String date;

    @Column(name="ipreplying")
    private String ipreplying;

    @Column(name="receiver",columnDefinition = "JSON")
    private String receiver;

    @Column(name="status")
    private String status;

    @Column(name="number")
    private Long number;

    @Column(name = "is_op")
    private Boolean op;

    @Column(name="repliers",columnDefinition = "JSON")
    private String repliers;

    @Column(name="ip")
    private String ip;

    @Column(name="postername")
    private String postername;

    @OneToMany(mappedBy = "repliesid",cascade = CascadeType.REMOVE)
    private List<entPostfiles> postfiles;

    @Transient
    private List<String> img_paths;

    @Transient
    private List<String> repliers_list;

    @Transient
    private List<String> receiver_list;

    public entReplies() {
    }

    public entReplies(Long id, entThread threadid, String text, String date, String ipreplying, String receiver, String status, Long number, Boolean op, String repliers, String ip) {
        this.id = id;
        this.threadid = threadid;
        this.text = text;
        this.date = date;
        this.ipreplying = ipreplying;
        this.receiver = receiver;
        this.status = status;
        this.number=number;
        this.op=op;
        this.repliers=repliers;
        this.ip=ip;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public entThread getThreadid() {
        return threadid;
    }

    public void setThreadid(entThread thread_id) {
        this.threadid = thread_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIpreplying() {
        return ipreplying;
    }

    public void setIpreplying(String ipreplying) {
        this.ipreplying = ipreplying;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Boolean isOp() {
        return op;
    }

    public void setOp(Boolean op) {
        this.op = op;
    }

    public List<String> getImg_paths() {
        return img_paths;
    }

    public void setImg_paths(List<String> img_paths) {
        this.img_paths = img_paths;
    }

    public String getRepliers() {
        return repliers;
    }

    public void setRepliers(String repliers) {
        this.repliers = repliers;
    }

    public List<String> getRepliers_list() {
        return repliers_list;
    }

    public void setRepliers_list(List<String> repliers_list) {
        this.repliers_list = repliers_list;
    }

    public List<String> getReceiver_list() {
        return receiver_list;
    }

    public void setReceiver_list(List<String> receiver_list) {
        this.receiver_list = receiver_list;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPostername() {
        return postername;
    }

    public void setPostername(String is_admin) {
        this.postername = is_admin;
    }
}
