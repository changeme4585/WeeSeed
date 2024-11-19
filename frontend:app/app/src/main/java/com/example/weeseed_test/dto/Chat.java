package com.example.weeseed_test.dto;

public class Chat {
    private String message;
    private boolean isChatBot;

    public Chat(String message, boolean isChatBot){
        this.message = message;
        this.isChatBot = isChatBot;
    }


    public String getMessage() {
        return message;
    }

    public boolean isChatBot() {
        return isChatBot;
    }

    public void setChatBot(boolean chatBot) {
        this.isChatBot = chatBot;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
