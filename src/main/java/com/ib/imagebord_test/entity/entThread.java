package com.ib.imagebord_test.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="Thread",schema = "db_imagebord")
@NamedQuery(name="Thread.getAll",query="select c from entThread c")
public class entThread {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="bord_id")
    private entBords bordid;

    @Column(name="fposttext")
    private String fposttext;

    @Column(name="date")
    private String date;

    @Column(name="ipcreator")
    private String ipcreator;

    @Column(name="postcount")
    private Integer postcount;

    @Column(name="cap")
    private Integer cap;

    @Column(name="pinned")
    private Boolean pinned;

    @Column(name="locked")
    private Boolean locked;

    @OneToMany(mappedBy = "threadid",cascade = CascadeType.REMOVE)
    private List<entReplies> replies;

    @Transient
    private entReplies firstReply;

    @Transient
    private List<entReplies> lastReplies;

    public entThread() {
    }

    public entThread(Long id, entBords bordid, String fposttext, String date, String ipcreator, Integer postcount, Integer cap) {
        this.id = id;
        this.bordid = bordid;
        this.fposttext = fposttext;
        this.date = date;
        this.ipcreator = ipcreator;
        this.postcount = postcount;
        this.cap=cap;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFposttext() {
        return fposttext;
    }

    public void setFposttext(String fposttext) {
        this.fposttext = fposttext;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIpcreator() {
        return ipcreator;
    }

    public void setIpcreator(String ipcreator) {
        this.ipcreator = ipcreator;
    }

    public entBords getBordid() {
        return bordid;
    }

    public void setBordid(entBords bordid) {
        this.bordid = bordid;
    }

    public Integer getPostcount() {
        return postcount;
    }

    public void setPostcount(Integer postcount) {
        this.postcount = postcount;
    }

    public Integer getCap() {
        return cap;
    }

    public void setCap(Integer cap) {
        this.cap = cap;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public entReplies getFirstReply() {
        return firstReply;
    }

    public void setFirstReply(entReplies firstReply) {
        this.firstReply = firstReply;
    }

    public List<entReplies> getLastReplies() {
        return lastReplies;
    }

    public void setLastReplies(List<entReplies> lastReplies) {
        this.lastReplies = lastReplies;
    }
}
