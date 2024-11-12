package com.ib.imagebord_test.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="bords",schema="db_imagebord")
@NamedQuery(name="bords.getAll",query = "select c from entBords c")
public class entBords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="postcount")
    private Long postcount;

    @Column(name="fullname")
    private String fullname;

    @Column(name="activethreads")
    private Integer activethreads;

    @Column(name="description")
    private String description;

    @Column(name="displayedname")
    private String displayedname;

    public entBords() {
    }

    public Long getPostcount() {
        return postcount;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPostcount(Long postcount) {
        this.postcount = postcount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getActiveThreads() {
        return activethreads;
    }

    public void setActiveThreads(Integer active_threads) {
        this.activethreads = active_threads;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayedname() {
        return displayedname;
    }

    public void setDisplayedname(String displayedname) {
        this.displayedname = displayedname;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        entBords entBords = (entBords) o;
        return Objects.equals(id, entBords.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}
