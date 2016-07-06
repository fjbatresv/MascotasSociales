package com.jmbsystems.fjbatresv.mascotassociales.login.events;

import java.util.Map;

/**
 * Created by javie on 5/07/2016.
 */
public class LoginEvent {
    public static final int SIGNIN_SUCCESS = 0;
    public static final int SIGNIN_ERROR = 1;
    public static final int SOCIAL_SIGNIN_SUCCESS = 2;
    public static final int SOCIAL_SIGNIN_ERROR = 3;
    public static final int VALID_LOGIN = 4;
    private int type;
    private String error;
    private Map<String, Object> options;

    public LoginEvent() {
    }

    public LoginEvent(int type, String error, Map<String, Object> options) {
        this.type = type;
        this.error = error;
        this.options = options;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }
}
