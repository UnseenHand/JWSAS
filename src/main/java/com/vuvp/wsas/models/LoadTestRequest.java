package com.vuvp.wsas.models;

public class LoadTestRequest {
    private String url;
    private String requestMethod;
    private String users;
    private String timeToLoadAllUsers;
    private String loopCount;

    // getters and setters
    public String getUrl(){
        return url;
    }

    public String getRequestMethod(){
        return requestMethod;
    }

    public int getUsers(){
        return Integer.parseInt(users);
    }

    public int getTimeToLoadAllUsers(){
        return Integer.parseInt(timeToLoadAllUsers);
    }

    public int getLoopCount(){
        return Integer.parseInt(loopCount);
    }
}
