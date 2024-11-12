package com.ib.imagebord_test.entity;

import jakarta.persistence.*;

@Entity
@Table(name="reports",schema="db_imagebord")
public class entReports {
    public enum Status{IN_PROCESS,WAIT,DONE}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="text")
    private String text;

    @Column(name = "id_reply")
    private Long id_reply;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId_reply() {
        return id_reply;
    }

    public void setId_reply(Long reply_id) {
        this.id_reply = reply_id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
