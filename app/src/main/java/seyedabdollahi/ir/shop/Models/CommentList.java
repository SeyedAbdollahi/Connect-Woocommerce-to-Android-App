package seyedabdollahi.ir.shop.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommentList implements Serializable {

    private List<Comment> commentList;

    public CommentList() {
        commentList = new ArrayList<Comment>();
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public void add(Comment comment){
        commentList.add(comment);
    }

    public void clear(){
        commentList.clear();
    }


}
