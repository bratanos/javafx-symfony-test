package com.innertrack.controller;

import com.innertrack.model.Comment;
import com.innertrack.service.CommentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.Duration;
//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CommunityController {
    @FXML
    private TreeView<Comment> comments;

    @FXML
    private TextField newComment;

    private final CommentService commentService = new CommentService();
    private final int UserID = 1;
    private TreeItem<Comment> root = new TreeItem<Comment>();


    private String formatTimeAgo(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return seconds + " seconds ago";
        }
        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + " minutes ago";
        }
        long hours = minutes / 60;
        if (hours < 24) {
            return hours + " hours ago";
        }
        long days = hours / 24;
        if (days < 30) {
            return days + " days ago";
        }
        long months = days / 30;
        if (months < 12) {
            return months + " months ago";
        }
        long years = months / 12;
        return years + " years ago";
    }



    @FXML
    public void initialize() {
        comments.setCellFactory(tv -> new TreeCell<>(){
            @Override
            protected void updateItem(Comment item, boolean empty){
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {

                    /*
                    en tant que user:
                    Service{
                    interface, user
                    }
                    outil{
                    mydatabase.java}
                    ressource{
                    dashboard fedi
                    interface loadin}
                    test{
                        main
                    }
                    model{
                        entity
                    }
                    controlleur{

                     */


                    /*
                    new VBox(
                            new HBox(
                                    new Label(item.getEmail()),
                                    new Label(formatTimeAgo(item.getCreation()))
                            ),
                            new Label(item.getComment()),
                            new Label("Likes: " + item.getLikes())
                    );
                     */

                    /*
                    HBox hbox = new HBox(
                            new Label(item.getComment())
                    );

                    Label userLabel = new Label(item.getEmail());
                    Label commentLabel = new Label(item.getComment());


                    Label date = new Label(formatTimeAgo(item.getCreation()));
                    Label likesLabel = new Label("Likes: " + item.getLikes());

                    hbox.getChildren().addAll(userLabel, commentLabel);

                    HBox hbox2 = new HBox();
                    */

                    //root.getChildren().addAll(userLabel, commentLabel, date, likesLabel);



                    TextField replyField = new TextField();
                    replyField.setPromptText("Send reply...");
                    replyField.setOnAction(event -> {
                        String reply = replyField.getText();
                        int id = commentService.addReply(new Comment(UserID, reply, item.getId()));
                        //System.out.println(id);
                        replyField.clear();
                    });


                    HBox hbox = new HBox(
                            new Label(item.getEmail()),
                            new Label(formatTimeAgo(item.getCreation()))
                    );

                    if(UserID == item.getUser()) {
                        Button deleteButton = new Button("Delete");
                        deleteButton.setOnAction(event -> {
                            commentService.delete(item.getId());
                            TreeItem<Comment> treeItem = getTreeItem();
                            TreeItem<Comment> parent = treeItem.getParent();
                            if (parent != null) {
                                parent.getChildren().remove(treeItem);
                            }
                        });
                        //vbox.getChildren().add(deleteButton);
                        hbox.getChildren().add(deleteButton);
                    }


                    TextField commentField = new TextField();
                    if(UserID == item.getUser()) {
                        commentField.setEditable(true);

                        commentField.setOnAction(event -> {
                            Comment c = new Comment();
                            c.setId(item.getId());
                            c.setComment(commentField.getText());
                            commentService.update(c);
                        });
                    } else {
                        commentField.setEditable(false);
                    }

                    commentField.setText(item.getComment());

                    VBox vbox = new VBox(
                            hbox,
                            commentField,
                            new Label("Likes: " + item.getLikes()),
                            replyField
                    );

                    setGraphic(vbox);
                }
            }
        });

        //root = new TreeItem<Comment>();

        List<Comment> list = commentService.getRaw();

        for(Comment comment : list) {
            root.getChildren().add(createItem(comment));
        }

        comments.setRoot(root);
        comments.setShowRoot(false);
    }


    private TreeItem<Comment> createItem(Comment comment) {
        TreeItem<Comment> item = new TreeItem<Comment>(comment);

        item.getChildren().add(new TreeItem<Comment>(null));

        item.addEventHandler(TreeItem.<Comment>branchExpandedEvent(), e -> {
            TreeItem<Comment> expandedItem = e.getTreeItem();
            if(expandedItem.getChildren().size() == 1 && expandedItem.getChildren().get(0).getValue() == null) {
                expandedItem.getChildren().clear();
                List<Comment> replies = commentService.getReplies((Comment)expandedItem.getValue());
                for(Comment reply : replies) {
                    expandedItem.getChildren().add(createItem(reply));
                }
            }
        });

        return item;
    }

    @FXML
    private void storeNewComment() {
        int id = commentService.addComment(new Comment(UserID, newComment.getText()));
        //System.out.println(id);
        newComment.clear();
    }

}
