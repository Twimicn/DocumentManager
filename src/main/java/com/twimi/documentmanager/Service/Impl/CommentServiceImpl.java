package com.twimi.documentmanager.Service.Impl;

import com.twimi.documentmanager.Dao.CommentDao;
import com.twimi.documentmanager.Dao.ProposalDao;
import com.twimi.documentmanager.Model.Comment;
import com.twimi.documentmanager.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao commentDao;

    @Autowired
    ProposalDao proposalDao;

    @Override
    public List<Comment> getCommentsByPid(int pid) {
        return commentDao.getCommentsByPid(pid);
    }

    @Override
    public int comment(int pid, int uid, String content, String opinion) {
        Comment comment = commentDao.getCommentByPidAndUid(pid, uid);
        if (comment == null) {
            comment = new Comment(pid, uid, content);
            int r = commentDao.insert(comment);
            if (r > 0) {
                if("support".equals(opinion)){
                    proposalDao.support(pid);
                }else{
                    proposalDao.reject(pid);
                }
                return comment.getCid();
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }
}
