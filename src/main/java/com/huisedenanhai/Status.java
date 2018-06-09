package com.huisedenanhai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//一个包裹，包含状态数据
public class Status {
	List<Model.Ball> balls;           //当前状态下的球
    List<Model.Snack> snacks;         //当前状态下的零食
    long time;                  //游戏时间

    public Status() {
        balls = new ArrayList<>();
        snacks = new ArrayList<>();
    }

    Status(List<Model.Ball> balls, List<Model.Snack> snacks, long time)
    {
        this.balls = balls;
        this.snacks = snacks;
        this.time = time;
    }
}
