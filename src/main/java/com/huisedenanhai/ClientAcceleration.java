package com.huisedenanhai;

/**
  * 客户端传来的加速度
  */
public class ClientAcceleration implements Comparable<Object>{
	int ID;
    long time;
    double[] acc;
    public ClientAcceleration() {
    	ID = 0;
    	time = 0;
    	this.acc = new double[2];
	}
    ClientAcceleration(int ID, long time)
    {
            this.ID = ID;
            this.time = time;
            this.acc = new double[2];
    }
    ClientAcceleration(int ID, long time, double[] acc)
    {
        this.ID = ID;
        this.time = time;
        this.acc = acc;
    }
    public int compareTo(Object o) {
    	ClientAcceleration s = (ClientAcceleration) o;
    	if (this.time < s.time)
    		return 1;
    	else if (this.time > s.time)
    		return -1;
    	return 0;
    }
}
