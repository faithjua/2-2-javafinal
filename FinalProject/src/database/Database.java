package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
	Connection con = null;
	PreparedStatement pstmt = null;
	private ResultSet result; 
	
	String url="jdbc:mysql://localhost/recipeapp?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false";
	String id="root";
	String password="4021";
	
	public Database() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, id, password);
			System.out.println("database connecting success!");
		} catch (ClassNotFoundException e) {
			System.out.println("cannot find driver!");
		} catch(SQLException e) {
			System.out.println("connection failed!!: " + e.toString());
		}
	}
	
	//로그인 가능 여부 확인 함수
	public boolean loginCheck(String userId, String userPassword) {
		
		String query = "SELECT password FROM user WHERE id = ?";
		
		try {
			//SQL 쿼리 준비
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, userId);
			result = pstmt.executeQuery();
			
			//데이타베이스에 id가 있는지 체크
			if (result.next()) {
				String dbPassword = result.getString("password");
				
				if (userPassword.equals(dbPassword)) {
					System.out.println("Login succesful!");
					return true;
				} else {
					System.out.println("Login failed: Incorrect password.");
					return false;
				}
			} else {
				System.out.println("Login failed: User ID not found.");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Database query error: " + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (result != null) result.close();
				if (pstmt != null) pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//회원가입 가능 여부 확인 함수
	public boolean signupCheck(String id, String password) {
	    try {
	        // Query to check if the ID already exists
	        String checkQuery = "SELECT id FROM user WHERE id = ?";
	        PreparedStatement checkStmt = con.prepareStatement(checkQuery);
	        checkStmt.setString(1, id);
	        ResultSet result = checkStmt.executeQuery();

	        if (result.next()) {
	            // ID already exists
	            System.out.println("ID already exists");
	            return false;
	        } else {
	            // Insert new ID and password
	            String insertQuery = "INSERT INTO user (id, password) VALUES (?, ?)";
	            PreparedStatement insertStmt = con.prepareStatement(insertQuery);
	            insertStmt.setString(1, id);
	            insertStmt.setString(2, password);
	            int rowsInserted = insertStmt.executeUpdate();

	            if (rowsInserted > 0) {
	                System.out.println("Signup successful");
	                return true;
	            } else {
	                System.out.println("Failed to add user to the database");
	                return false;
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Database error: " + e.getMessage());
	        return false;
	    }
	}
	
	public List<Post> getAllPosts() {
	    List<Post> posts = new ArrayList<>();
	    String query = "SELECT * FROM post";

	    try {
	        pstmt = con.prepareStatement(query);
	        result = pstmt.executeQuery();

	        while (result.next()) {
	        	Post post = new Post(null, null, null, null);
	        	post.setPostNo(result.getInt("post_no"));
                post.setTitle(result.getString("title"));
                post.setWriter(result.getString("writer"));
                post.setRegDate(result.getString("reg_date"));
                //post.setUpdDate(result.getString("upd_date"));
                post.setContent(result.getString("content"));
                post.setImage(result.getBytes("image"));
                posts.add(post);
	        }
	    } catch (SQLException e) {
	        System.out.println("Database error: " + e.getMessage());
	    } finally {
	        try {
	            if (result != null) result.close();
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return posts;
	}
	
	public byte[] getRecipeImage(int recipeId) {
		String query = "SELECT image FROM post WHERE post_no = ?";
		
		try (PreparedStatement statement = con.prepareStatement(query)) {
			statement.setInt(1, recipeId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getBytes("image");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean savePost(Post post) {
	    String sql = "INSERT INTO Post (title, writer, content, image) VALUES (?, ?, ?, ?)";
	    try (PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setString(1, post.getTitle());
	        stmt.setString(2, post.getWriter());
	        stmt.setString(3, post.getContent());
	        stmt.setBytes(4, post.getImage());
	        stmt.executeUpdate();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public List<Post> getPostsByUser(String username) {
	    List<Post> userPosts = new ArrayList<>();
	    String sql = "SELECT * FROM post WHERE writer = ?";
	    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
	        pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            Post post = new Post(null, null, null, null);
	            //post.setPostNo(rs.getInt("postNo"));
	            post.setTitle(rs.getString("title"));
	            post.setWriter(rs.getString("writer"));
	            post.setRegDate(rs.getString("reg_date"));
	            //post.setUpdDate(rs.getString("upd_date"));
	            post.setContent(rs.getString("content"));
	            post.setImage(rs.getBytes("image"));
	            userPosts.add(post);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return userPosts;
	}
	
	public boolean deletePost(String post_title) {
	    String query = "DELETE FROM post WHERE title = ?";
	    try (PreparedStatement stmt = con.prepareStatement(query)) {
	        stmt.setString(1, post_title);
	        int rowsAffected = stmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
