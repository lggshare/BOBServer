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

    @Override
    public void parseJSON(JSONObject json) {
        try {
            time = json.getLong("time");
            id = json.getInt("id");
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
        jsonAction.put("action", "");
        return jsonAction;
    }

    @Override
    public Long getTime() {
        return time;
    }
}
