package org.example.servlet.web_app.form.model;

import java.util.Map;

public class Node implements GameNode{
    private final String text;
    private final String id;
    private final Map<String,GameNode> choicesMap;

    public Node(String id, String text, Map<String, GameNode> choicesMap) {
        this.text = text;
        this.id = id;
        this.choicesMap = choicesMap;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public boolean isFinalNode() {
        return false;
    }

    public Map<String, GameNode> getChoicesMap() {
        return choicesMap;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public GameNode getNextNode(String choice) {
        return choicesMap.get(choice);
    }
}
