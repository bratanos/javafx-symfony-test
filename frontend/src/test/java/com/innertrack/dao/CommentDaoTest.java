package com.innertrack.dao;

import com.innertrack.model.Comment;
import com.innertrack.service.CommentService;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentDaoTest {

    private static CommentService commentService = new CommentService();
    private static int id;
    private static int reply;
    final int user = 1;
    final String comment = "Test comment";
    final String comment2 = "Test comment2";

    @BeforeAll
    static void setUp() {
        System.out.println("setUp");
    }

    @AfterAll
    static void tearDown() {
        System.out.println("tearDown");
        commentService.delete(id);
        commentService.delete(reply);
    }

    @Test
    @Order(1)
    void addComment() {
        System.out.println("addComment");
        Comment c = new Comment(user, comment);
        id = commentService.addComment(c);
        assertTrue(id >= 0);
        Comment c2 = commentService.read(id);
        assertTrue(c2.getId() == id && c2.getUser() == user && c2.getComment().equals(comment));
    }

    @Test
    @Order(2)
    void addReply() {
        System.out.println("addReply");
        Comment c = new Comment(user, comment2, id);
        reply =  commentService.addReply(c);
        assertTrue(reply >= 1);
        Comment c2 = commentService.read(reply);
        assertTrue(c2.getId() == reply && c2.getUser() == user && c2.getComment().equals(comment2));
    }

    @Test
    void getRaw() {
        System.out.println("getRaw");
        List<Comment> list = commentService.getRaw();
        assertTrue(list.size() >= 1);
        Comment c1 = list.get(list.size() - 1);
        assertTrue(c1.getId() == id && c1.getUser() == user && c1.getComment().equals(comment));
    }

    @Test
    void getReplies() {
        System.out.println("getReplies");
        Comment c = new Comment();
        c.setId(id);
        List<Comment> list = commentService.getReplies(c);
        assertTrue(list.size() >= 1);
        Comment c1 = list.get(list.size() - 1);
        assertTrue(c1.getId() == reply && c1.getUser() == user && c1.getComment().equals(comment2));
    }
}