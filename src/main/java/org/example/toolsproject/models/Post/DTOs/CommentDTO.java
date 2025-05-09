package org.example.toolsproject.models.Post.DTOs;

public class CommentDTO {
    private String Content;
    private int id;
    public CommentDTO(int id, String content) {
        this.Content = content;
        this.id = id;
    }
    public String getContent() {
        return Content;
    }
    public CommentDTO() {}
    public CommentDTO(String content) {
        this.Content = content;
    }



    public void setContent(String content) {
        this.Content = content;
    }
}
