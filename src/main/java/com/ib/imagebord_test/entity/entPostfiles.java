package com.ib.imagebord_test.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "postfiles",schema = "db_imagebord")
public class entPostfiles {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="path")
    private String path;

    @Column(name="type")
    private String type;

    @ManyToOne
    @JoinColumn(name="replies_id")
    private entReplies repliesid;


    public entPostfiles() {
    }

    public entPostfiles(Long id, String path, String type, entReplies repliesid) {
        this.id = id;
        this.path = path;
        this.type = type;
        this.repliesid = repliesid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public entReplies getReply_id() {
        return repliesid;
    }

    public void setReply_id(entReplies repliesid) {
        this.repliesid = repliesid;
    }
}
