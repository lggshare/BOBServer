/*
 * Created by huisedenanhai on 2018/06/04
 */

package com.huisedenanhai;

import org.json.JSONObject;

/**
 * class Action should implement this interface
 */
public interface ActionInterface {
    /**
     * Parse the json as action
     *
     * @param json
     */
    void parseJSON(JSONObject json);

    /**
     * Encode the action as json
     *
     * @return encoded Json
     */
    JSONObject encodeJSON();

    /**
     * Return the time when the action occur
     *
     * @return the time when the action occur
     */
    Long getTime();
}
