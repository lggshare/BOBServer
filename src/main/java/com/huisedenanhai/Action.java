package com.huisedenanhai;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The temporary implementation of action
 * This class need future modify
 */
public class Action implements JSONSerializable {

    private long time;
    private int id;
    private String action; // in {"UP", "DOWN", "RIGHT", "LEFT"}

    public Action() {}

    public Action(long time, int id, String action) {
        this.time = time;
        this.id = id;
        this.action = action;
    }

    @Override
    public void parseJSON(JSONObject json) {
        try {
            time = json.getLong("time");
            id = json.getInt("id");
            action = json.getString("action");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public JSONObject encodeJSON() {
        JSONObject jsonAction = new JSONObject();
        jsonAction.put("method", "action");
        jsonAction.put("time", time);
        jsonAction.put("id", id);
        jsonAction.put("action", action);
        return jsonAction;
    }

    public Long getTime() {
        return time;
    }

    public int getID() { return id; }

    public String getAction() { return action; }
}
