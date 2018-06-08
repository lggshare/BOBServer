package com.huisedenanhai;

import org.json.JSONObject;

public class SyncronizationMessage implements JSONSerializable {
    @Override
    public void parseJSON(JSONObject json) {

    }

    @Override
    public JSONObject encodeJSON() {
        return new JSONObject();
    }
}
