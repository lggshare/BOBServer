package com.huisedenanhai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ServerToModel {
    	List<ClientAcceleration> clientAcclerations = new ArrayList<>();
    	boolean connected;
    	public ServerToModel(Map<Integer, Map<Long, String> > list, boolean connected) {
    		this.connected = connected;
    		for (Integer i: list.keySet()) {
    			Map<Long, String> action = list.get(i);
    			for (Long nowTime: action.keySet()) {
    				ClientAcceleration tempAccleration = new ClientAcceleration(i,nowTime);
    				switch (action.get(nowTime)) {
					case "UP":
						tempAccleration.acc[0] = Model.Ball.ACCELERATION_VARY;
						break;
					case "DOWN":
						tempAccleration.acc[0] = -Model.Ball.ACCELERATION_VARY;
						break;
					case "RIGHT":
						tempAccleration.acc[1] = Model.Ball.ACCELERATION_VARY;
						break;
					case "LEFT":
						tempAccleration.acc[1] = -Model.Ball.ACCELERATION_VARY;
						break;
					default:
						break;
					}
    			clientAcclerations.add(tempAccleration);
    			}
    		}
    		Collections.sort(clientAcclerations);
    	}
    }