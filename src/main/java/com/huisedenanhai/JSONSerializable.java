/*
 * Created by huisedenanhai on 2018/06/04
 */

package com.huisedenanhai;

import org.json.JSONObject;

/**
 * The class that can parse and encode JSON
 */
public interface JSONSerializable {
    /**
     * Parse the json as object
     *
     * @param json
     */
    void parseJSON(JSONObject json);

    /**
     * Encode the object as json
     *
     * @return encoded Json
     */
    JSONObject encodeJSON();
}