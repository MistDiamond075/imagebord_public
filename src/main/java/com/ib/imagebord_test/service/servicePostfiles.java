package com.ib.imagebord_test.service;

import com.ib.imagebord_test.configuration_app.confPropsPaths;
import com.ib.imagebord_test.entity.entPostfiles;
import com.ib.imagebord_test.entity.entReplies;
import com.ib.imagebord_test.repository.repPostfiles;
import com.ib.imagebord_test.repository.repReplies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class servicePostfiles {
    private final repPostfiles rPostfiles;
    private final repReplies rReplies;
    private final confPropsPaths apppaths;

    @Autowired
    public servicePostfiles(repPostfiles rPostfiles, repReplies rReplies, confPropsPaths apppaths) {
        this.rPostfiles = rPostfiles;
        this.rReplies = rReplies;
        this.apppaths = apppaths;
    }

    public Long getMaxId(){
        return rPostfiles.findTopByOrderByIdDesc().orElseThrow(()->new RuntimeException("max id not found in postfiles"));
    }

    public entPostfiles getPostfileById(Long id){
        return rPostfiles.findById(id).orElseThrow(()->new RuntimeException("postfile not found when getting by id"));
    }

    public List<entPostfiles> getAllPostfiles(){
        return (List<entPostfiles>) rPostfiles.findAll();
    }

    public List<entPostfiles> getAllPostfilesByReplyId(Long id){
        entReplies reply=rReplies.findById(id).orElseThrow(()->new RuntimeException("reply not found when getting files by reply id"));
        return rPostfiles.findAllByRepliesidId(reply.getId());
    }

    public entPostfiles addPostfile(entPostfiles postfile){
        rPostfiles.save(postfile);
        return postfile;
    }
}
