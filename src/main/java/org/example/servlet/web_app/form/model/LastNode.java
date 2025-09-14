package org.example.servlet.web_app.form.model;

public class LastNode implements GameNode{
    private final String text;
    private final String id;
    private final boolean isWinning;

    public LastNode(String id, String text, boolean isWinning) {
        this.text = text;
        this.id = id;
        this.isWinning = isWinning;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public boolean isFinalNode() {
        return true;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public GameNode getNextNode(String next) {
        return null;
    }

    public boolean isWinning() {
        return isWinning;
    }
}
