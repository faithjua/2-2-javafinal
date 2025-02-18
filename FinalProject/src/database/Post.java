package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Post {
    private int postNo;
    private String title;
    private String writer;
    private String regDate;
    private String updDate;
    private String content;
    private byte[] image;
    
    public Post(String title, String writer, String content, byte[]image) {
    	this.title = title;
    	this.writer = writer;
    	this.content = content;
    	this.image = image;
    }

    // Getters and setters
    public int getPostNo() {
        return postNo;
    }

    public void setPostNo(int postNo) {
        this.postNo = postNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    /*
    public String getUpdDate() {
        return updDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }
    */

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    
    public void loadImage(int recipeId, Database db) {
    	this.image = db.getRecipeImage(recipeId);
    }
}
