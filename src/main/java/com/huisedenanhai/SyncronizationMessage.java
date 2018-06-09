package com.huisedenanhai;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SyncronizationMessage implements JSONSerializable {
    private Status status;

    public SyncronizationMessage() {}

    public SyncronizationMessage(Status status) {
        this.status = status;
    }

    @Override
    public void parseJSON(JSONObject json) {
        try {
            List<Model.Ball> balls = new ArrayList<>();
            JSONArray jBallArray = json.getJSONArray("ball");
            for (int i = 0; i < jBallArray.length(); i++) {
                JSONObject ballJSON = jBallArray.getJSONObject(i);
                Model.Ball ball = new Model.Ball(
                        ballJSON.getInt("ID"),
                        ballJSON.getDouble("x"),
                        ballJSON.getDouble("y"),
                        ballJSON.getDouble("vx"),
                        ballJSON.getDouble("vy"),
                        ballJSON.getDouble("ax"),
                        ballJSON.getDouble("ay"),
                        (Color)ballJSON.get("color"),
                        ballJSON.getDouble("size")
                );
                balls.add(ball);
            }

            List<Model.Snack> snacks = new ArrayList<>();
            JSONArray jSnackArray = json.getJSONArray("snack");
            for (int i = 0; i < jSnackArray.length(); i++) {
                JSONObject snackJSON = jSnackArray.getJSONObject(i);
                Model.Snack snack = new Model.Snack(
                        snackJSON.getInt("x"),
                        snackJSON.getInt("y"),
                        (Color)snackJSON.get("color"),
                        snackJSON.getLong("time"),
                        snackJSON.getInt("index")
                );
                snacks.add(snack);
            }

            long time = json.getLong("time");
            status = new Status(balls, snacks, time);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public JSONObject encodeJSON() {
        JSONObject jsonMessage = new JSONObject();

        JSONArray jBallArray = new JSONArray();
        for (Model.Ball ball: status.balls) {
            JSONObject ballJSON = new JSONObject();
            ballJSON.put("ID", ball.ID);
            ballJSON.put("x", ball.x);
            ballJSON.put("y", ball.y);
            ballJSON.put("vx", ball.vx);
            ballJSON.put("vy", ball.vy);
            ballJSON.put("ax", ball.ax);
            ballJSON.put("ay", ball.ay);
            ballJSON.put("color", ball.color);
            ballJSON.put("size", ball.size);
            jBallArray.put(ballJSON);
        }
        jsonMessage.put("ball", jBallArray);

        JSONArray jSnackArray = new JSONArray();
        for (Model.Snack snack: status.snacks) {
            JSONObject snackJSON = new JSONObject();
            snackJSON.put("x", snack.x);
            snackJSON.put("y", snack.y);
            snackJSON.put("color", snack.color);
            snackJSON.put("time", snack.time);
            snackJSON.put("index", snack.index);
            jSnackArray.put(snackJSON);
        }
        jsonMessage.put("snack", jSnackArray);

        jsonMessage.put("time", status.time);
        return jsonMessage;
    }

    public Status getStatus() {
        return status;
    }
}
