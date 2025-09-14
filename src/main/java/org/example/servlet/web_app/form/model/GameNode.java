package org.example.servlet.web_app.form.model;

public interface GameNode {
    String getText();
    boolean isFinalNode();
    String getId();
    GameNode getNextNode(String next);
}
