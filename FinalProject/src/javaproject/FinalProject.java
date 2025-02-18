package javaproject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import database.*;
import java.util.List;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FinalProject extends JFrame {
    private Container c = getContentPane();

    Database database = new Database();
    String userName;
    
    public FinalProject() {
        setTitle("Recipe Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c.setLayout(new FlowLayout()); // Set FlowLayout for the main container

        // Start with the login page
        showLoginPage();

        setSize(800, 800);
        setVisible(true);
    }
    
    private void showLoginPage() {
        // Clear container and add LoginPage
        c.removeAll();
        c.add(new LogInPage());
        c.revalidate(); // Refresh the container
        c.repaint();
    }

    private void showRecipePage() {
        // Clear container and add RecipePage
        c.removeAll();
        c.add(new RecipePage());
        c.revalidate(); // Refresh the container
        c.repaint();
    }
    
    private void showMainPage() {
    	c.removeAll();
        c.add(new MainPage());
        c.revalidate(); // Refresh the container
        c.repaint();
    }
    
    private void showCreatePanel() {
    	c.removeAll();
    	c.add(new CreatePostPanel());
    	c.revalidate();
    	c.repaint();
    }

    //로그인 페이지 구현한 페널
    class LogInPage extends JPanel {
        private JTextField loginID = new JTextField(20);
        private JPasswordField loginPW = new JPasswordField(20);
        private JButton signupBtn = new JButton("회원가입");
        private JButton loginBtn = new JButton("로그인");

        LogInPage() {
        	this.setLayout(new GridLayout(3, 2, 5, 5));
            add(new JLabel("아이디: "));
            add(loginID);
            add(new JLabel("비밀번호: "));
            add(loginPW);
            add(signupBtn);
            add(loginBtn);

            /*
            signinBtn.addActionListener(new ActionListener() {
            	
            });
            */
            
            loginBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String uid = loginID.getText();
                	String upw = new String(loginPW.getPassword());
                    //칸이 비어있는지 확인
                    if (uid.equals("") || upw.equals("")) {
                        JOptionPane.showMessageDialog(null, "아이디와 비밀번호를 입력하세요.", "Message", JOptionPane.WARNING_MESSAGE);
                        return;
                    } else if (database.loginCheck(uid, upw)) {
                    	JOptionPane.showMessageDialog(null, "Welcome " + uid, "Login Success", JOptionPane.INFORMATION_MESSAGE);
                    	userName = uid;
                    	showMainPage();
                	    //showRecipePage();
                    } else {
                    	JOptionPane.showMessageDialog(null, "Invalid ID or Password", "Login Failed", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            
            signupBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String uid = loginID.getText(); // Assume signupID is the JTextField for ID input
                    String upw = new String(loginPW.getPassword()); // Assume signupPW is the JPasswordField for password input

                    // Check if inputs are valid
                    if (uid.equals("") || upw.equals("")) {
                        JOptionPane.showMessageDialog(null, "아이디와 비밀번호를 입력하세요.", "Message", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    boolean isSignedUp = database.signupCheck(uid, upw);
                    
                    if (isSignedUp) {
                        JOptionPane.showMessageDialog(null, "Signup successful! You can now log in.", "Signup Status", JOptionPane.INFORMATION_MESSAGE);
                        showLoginPage(); 
                    } else {
                        JOptionPane.showMessageDialog(null, "ID already exists or signup failed. Try again.", "Signup Status", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
        }
    }
    
    //메인 페이지 구현 패널
    class MainPage extends JPanel {
    	
    	MainPage() {
    		setLayout(new BorderLayout());
    		
    		JPanel topBar = new JPanel(new BorderLayout());
    		topBar.setPreferredSize(new Dimension(600, 50));
    		topBar.setBackground(new Color(255, 213, 0));
    		topBar.setOpaque(true);
    		
    		JLabel titleLabel = new JLabel("레시피 모음집<3", JLabel.CENTER);
    		titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
    		topBar.add(titleLabel, BorderLayout.CENTER);
    		
    		JPanel sidePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    		sidePanel.setOpaque(false);
    		
    		JLabel loggedinID = new JLabel(userName);
    		loggedinID.setFont(new Font("Arial", Font.PLAIN, 14));
    		//loggedinID.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
    		sidePanel.add(loggedinID);
    		
    		JButton logoutBtn = new JButton("log out");
    		logoutBtn.setFont(new Font("Arial", Font.PLAIN, 14));
    		logoutBtn.addActionListener(e -> {
    			JOptionPane.showMessageDialog(null, "로그아웃 성공", "Logout", JOptionPane.INFORMATION_MESSAGE);
    			userName = null;
    			showLoginPage();
    		});
    		sidePanel.add(logoutBtn);
    		
    		topBar.add(sidePanel, BorderLayout.EAST);
    		add(topBar, BorderLayout.NORTH);
    		
    		JTabbedPane pane = createTabbedPane();
    		add(pane, BorderLayout.CENTER);
    		
    		JButton addPostButton = new JButton("글쓰기");
            addPostButton.addActionListener(e -> showCreatePanel());
            add(addPostButton, BorderLayout.SOUTH);
    		
    	}
    }
    
    JTabbedPane createTabbedPane() {
    	JTabbedPane tabbedPane = new JTabbedPane();

    	RecipePage recipePage = new RecipePage();
    	recipePage.setPreferredSize(new Dimension(700, 500)); // Fixed size for RecipePage

    	MyPage myPage = new MyPage();
    	myPage.setPreferredSize(new Dimension(700, 500)); // Same fixed size for MyPage

    	tabbedPane.addTab("Recipes", new JScrollPane(recipePage));
    	tabbedPane.addTab("My Page", new JScrollPane(myPage));
    	
    	return tabbedPane;
    }

    //레시피 페이지 구현한 패널
    class RecipePage extends JPanel {

        RecipePage() {
            setLayout(new GridLayout(0, 3, 10, 10)); // Set layout for the recipe page
            
            List<Post> posts = database.getAllPosts();
            for (Post post : posts) {
            	add(createEachPost(post));
            }
            
            JPanel postsContainer = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns, dynamic rows
            postsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JScrollPane scrollPane = new JScrollPane(postsContainer);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setPreferredSize(new Dimension(800, 600));
        }
        
        private JPanel createEachPost(Post post) {
        	JPanel panel = new JPanel(new BorderLayout());
        	
        	panel.setPreferredSize(new Dimension(260, 160));

        	JLabel imageLabel = new JLabel();
        	byte[] imageBytes = post.getImage();
        	if (imageBytes != null) {
                try {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                    BufferedImage image = ImageIO.read(inputStream);

                    if (image != null) {
                        Image scaledImage = image.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaledImage));
                    } else {
                        imageLabel.setText("Invalid image format");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    imageLabel.setText("Error loading image");
                }
            } else {
                imageLabel.setText("No image available");
            }
            //imageLabel.setIcon(new ImageIcon(post.getImage())); // Assuming `getImage` returns a valid file path or URL
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Title (Clickable)
            JButton titleButton = new JButton(post.getTitle());
            titleButton.addActionListener(e -> showPostDetails(post));

            panel.add(imageLabel, BorderLayout.CENTER);
            panel.add(titleButton, BorderLayout.SOUTH);

            return panel;
        }
        
        private void showPostDetails(Post post) {
            // Create a panel to hold the details
            JPanel detailsPanel = new JPanel(new BorderLayout());
            detailsPanel.setPreferredSize(new Dimension(400, 300));

            // Title and metadata
            String details = "Title: " + post.getTitle() + "\n" +
                             "Date: " + post.getRegDate() + "\n" +
                             "작성자: " + post.getWriter() + "\n\n" +
                             post.getContent();
                             
            JTextArea detailsTextArea = new JTextArea(details);
            detailsTextArea.setLineWrap(true);
            detailsTextArea.setWrapStyleWord(true);
            detailsTextArea.setEditable(false); // Make it non-editable
            detailsTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JScrollPane textScrollPane = new JScrollPane(detailsTextArea);
            textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            // Add the image if available
            JLabel imageLabel = new JLabel();
            byte[] imageBytes = post.getImage();
            if (imageBytes != null) {
                try {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                    BufferedImage image = ImageIO.read(inputStream);
                    Image scaledImage = image.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } catch (Exception e) {
                    e.printStackTrace();
                    imageLabel.setText("Image not available");
                }
            } else {
                imageLabel.setText("No image available.");
            }

            // Add components to the panel
            detailsPanel.add(imageLabel, BorderLayout.NORTH);
            detailsPanel.add(textScrollPane, BorderLayout.CENTER);

            // Show the dialog
            JOptionPane.showMessageDialog(this, detailsPanel, "Recipe Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    class MyPage extends JPanel {
    	MyPage() {
    		setLayout(new GridLayout(0, 3, 10, 10)); // Set layout for the recipe page
            
            List<Post> posts = database.getPostsByUser(userName);
            for (Post post : posts) {
            	add(createEachPost(post));
            }
    	}
    	
    	private JPanel createEachPost(Post post) {
        	JPanel panel = new JPanel(new BorderLayout());
        	panel.setPreferredSize(new Dimension(260, 160));

        	
        	JLabel imageLabel = new JLabel();
        	byte[] imageBytes = post.getImage();
        	if (imageBytes != null) {
        		try {
        			ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        			BufferedImage image = ImageIO.read(inputStream);
        			Image scaledImage = image.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
        			imageLabel.setIcon(new ImageIcon(scaledImage));
        		} catch (Exception e) {
        			e.printStackTrace();
        			imageLabel.setText("Image not available");
        		}
        	} else {
        		imageLabel.setText("No image available");
        	}
            //imageLabel.setIcon(new ImageIcon(post.getImage())); // Assuming `getImage` returns a valid file path or URL
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Title (Clickable)
            JButton titleButton = new JButton(post.getTitle());
            titleButton.addActionListener(e -> showPostDetails(post));
            
            panel.add(imageLabel, BorderLayout.CENTER);
            panel.add(titleButton, BorderLayout.SOUTH);

            return panel;
        }
    	
    	private void showPostDetails(Post post) {
            // Create a panel to hold the details
            JPanel detailsPanel = new JPanel(new BorderLayout());
            //detailsPanel.setPreferredSize(new Dimension(400, 300));

            // Title and metadata
            String details = "Title: " + post.getTitle() + "\n" +
                    "Date: " + post.getRegDate() + "\n" +
                    "작성자: " + post.getWriter() + "\n\n" +
                    post.getContent();
            
            JTextArea detailsTextArea = new JTextArea(details);
            detailsTextArea.setLineWrap(true);
            detailsTextArea.setWrapStyleWord(true);
            detailsTextArea.setEditable(false); // Make it non-editable
            detailsTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JScrollPane textScrollPane = new JScrollPane(detailsTextArea);
            textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            // Add the image if available
            JLabel imageLabel = new JLabel();
            byte[] imageBytes = post.getImage();
            if (imageBytes != null) {
                try {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                    BufferedImage image = ImageIO.read(inputStream);
                    Image scaledImage = image.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } catch (Exception e) {
                    e.printStackTrace();
                    imageLabel.setText("Image not available");
                }
            } else {
                imageLabel.setText("No image available.");
            }

            // Add components to the panel
            detailsPanel.add(imageLabel, BorderLayout.NORTH);
            detailsPanel.add(textScrollPane, BorderLayout.CENTER);
            
            JScrollPane scrollPane = new JScrollPane(detailsPanel);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            //JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton deleteButton = new JButton("삭제");
            
            // Add delete button functionality
            deleteButton.addActionListener(e -> {
                int confirmation = JOptionPane.showConfirmDialog(null, 
                    "Are you sure you want to delete this post?", 
                    "Confirm Deletion", 
                    JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    boolean deleted = database.deletePost(post.getTitle()); // Delete from DB
                    if (deleted) {
                        JOptionPane.showMessageDialog(null, "Post deleted successfully.");
                        // Optionally, close the dialog after successful deletion
                        ((Window) SwingUtilities.getWindowAncestor(deleteButton)).dispose();
                        showMainPage();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete the post.");
                    }
                }
            });

            // Show the dialog
            JOptionPane.showMessageDialog(this, 
            		new Object[] {scrollPane, deleteButton}, 
            		"Recipe Details", 
            		JOptionPane.INFORMATION_MESSAGE);
        }
    	
    }
    
    class CreatePostPanel extends JPanel {
    	private JTextField titleField = new JTextField(50);
    	private JTextArea contentArea = new JTextArea(5, 20);
        private byte[] imageBytes;

        CreatePostPanel() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Title
            gbc.gridx = 0; gbc.gridy = 0;
            add(new JLabel("Title:"), gbc);
            gbc.gridx = 1;
            add(titleField, gbc);

            // Content
            gbc.gridx = 0; gbc.gridy = 1;
            add(new JLabel("Content:"), gbc);
            gbc.gridx = 1;
            contentArea.setLineWrap(true);
            contentArea.setWrapStyleWord(true);
            add(new JScrollPane(contentArea), gbc);

            // Image Upload
            gbc.gridx = 0; gbc.gridy = 2;
            add(new JLabel("Image:"), gbc);
            
            JButton uploadButton = new JButton("Upload Image");
            uploadButton.addActionListener(e -> uploadImage());
            
            gbc.gridx = 1;
            add(uploadButton, gbc);

            // Post Button
            JButton postButton = new JButton("Post");
            postButton.addActionListener(e -> savePost());
            gbc.gridx = 1; gbc.gridy = 3;
            add(postButton, gbc);
            
            //Home Button
            JButton homeButton = new JButton("뒤로가기");
            homeButton.addActionListener(e -> showMainPage());
            gbc.gridx = 1; gbc.gridy = 4;
            add(homeButton, gbc);
        }
        
        private void savePost() {
            String title = titleField.getText();
            String content = contentArea.getText();
            

            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title and Content cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Post newPost = new Post(title, userName, content, imageBytes);
            boolean success = database.savePost(newPost); // Save post using your Database class

            if (success) {
                JOptionPane.showMessageDialog(this, "Post added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                titleField.setText("");
                contentArea.setText("");
                imageBytes = null;
                showMainPage();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save post.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        private void uploadImage() {
        	JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fileChooser.getSelectedFile();
                    imageBytes = Files.readAllBytes(file.toPath());
                    JOptionPane.showMessageDialog(this, "Image uploaded successfully!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to upload image.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    	
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FinalProject::new);
    }
}

