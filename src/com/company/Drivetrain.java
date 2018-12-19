package com.company;

import java.util.ArrayList;

public class Drivetrain {

    public double width, angleDegrees, angleRadians, leftVel, rightVel,
    leftPos, rightPos, robotX, robotY, dt, angularVel, linearVel, robotPos, time;

    public ArrayList<Double> xList, yList;

    public Drivetrain(double width, double dt, double xOffset, double yOffset) {
        this.width = width;
        this.dt = dt;
        robotX = xOffset;
        robotY = yOffset;
        angleDegrees = 0;
        angleRadians = 0;
        leftPos = 0;
        rightPos = 0;
        leftVel = 0;
        rightVel = 0;
        angularVel = 0;
        linearVel = 0;
        robotPos = 0;
        time = 0;
        xList = new ArrayList<Double>();
        yList = new ArrayList<Double>();
    }

    public void update(double leftV, double rightV) {
        rightVel = rightV;
        leftVel = leftV;
        linearVel = (leftVel + rightVel)/2;
        angularVel = -(leftVel - rightVel)/width;
        double deltaPos = linearVel * dt;
        double deltaTheta = angularVel * dt;
        robotPos += deltaPos;
        angleRadians += deltaTheta;
        angleDegrees = Math.toDegrees(angleRadians);
        rightPos += rightVel * dt;
        leftPos += leftVel * dt;
        robotX += deltaPos * Math.cos(angleRadians);
        robotY += deltaPos * Math.sin(angleRadians);
        xList.add(robotX);
        yList.add(robotY);
        time += dt;
    }

}
