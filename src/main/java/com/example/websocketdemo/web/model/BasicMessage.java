package com.example.websocketdemo.web.model;

import com.falconsocial.demo.szl.websocket.domain.model.MessageType;

/**
 * POJO which models messages on the REST layer
 * 
 * @author szabol
 *
 */
public class BasicMessage {

    private String content;

    private MessageType type;

    public BasicMessage() {
    }

    public BasicMessage(String content, MessageType type) {
        this.content = content;
        this.type = type;
    }

    protected void setContent(String content) {
        this.content = content;
    }

    protected void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public MessageType getType() {
        return type;
    }

}
