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

    private String action; // 由"U", "D", "R", "L"组成的字符串。不区分大小写。 
    /**
     * 补充：
     * 每种字母只能出现一次，否则会出现bug。
     * 如果用户同时按下了多个键，可以单字母叠加，不论次序。
     * 如“UL”和“LU”均代表同时按下了上、左键。
     * 服务器的实现里，可以正确处理含“LR”“UD”的组合。
     */

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
