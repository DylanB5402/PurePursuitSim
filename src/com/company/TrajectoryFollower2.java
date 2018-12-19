package com.company;

import grpl.pathfinder.coupled.CoupledState;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrajectoryFollower2 {

    private ArrayList<CoupledState> m_trajectory;
    private CoupledState m_robotSegment, m_lookaheadSegment;
    private Boolean m_goingForwards;

    private double m_robotX, m_robotY, m_targetAngle, m_velocity,
            m_error, m_leftDesiredVel, m_rightDesiredVel, m_kP, m_kD, m_turn, m_dT, m_lastError;
    private int m_lookaheadIndex, m_robotIndex, m_lookahead;



    public TrajectoryFollower2(ArrayList<CoupledState> traj, int lookahead, boolean goingForwards, double kP, double kD, double dT) {
        m_trajectory = traj;
        m_lookahead = lookahead;
        m_goingForwards = goingForwards;
        m_robotIndex = 0;
        m_kP = kP;
        m_kD = kD;
        m_dT = dT;
        m_lookaheadIndex = 0;
        m_lastError = 0;

    }

    public void calculate(double robotX, double robotY, double robotTheta) {
        m_robotX = robotX;
        m_robotY = robotY;
        m_robotSegment = getClosestSegment(m_robotX, m_robotY, m_trajectory, m_robotIndex, 5);
        m_robotIndex = m_trajectory.indexOf(m_robotSegment);
        m_lookaheadIndex = m_robotIndex + m_lookahead;
        if (m_lookaheadIndex > m_trajectory.size() - 1) {
            m_lookaheadIndex = m_trajectory.size() - 1;
        }
        m_lookaheadSegment = m_trajectory.get(m_lookaheadIndex);
        m_targetAngle = Math.toDegrees(m_lookaheadSegment.config.heading);
        m_velocity = m_robotSegment.kinematics.velocity;
        if (!m_goingForwards) {
            m_velocity = -m_velocity;
//            robotTheta += 180;
        }
        m_error = Pathfinder.boundHalfDegrees(m_targetAngle - robotTheta);
        m_turn = m_error * m_kP + (m_error - m_lastError)/m_dT * m_kD;
        m_leftDesiredVel = m_velocity - m_turn;
        m_rightDesiredVel = m_velocity + m_turn;
    }

    public boolean isFinished() {
        return m_trajectory.size()-3 == m_robotIndex;
    }

    public double getLeftVelocity() {
        return m_leftDesiredVel;
    }

    public double getRightVelocity() {
        return m_rightDesiredVel;
    }

    public static CoupledState getClosestSegment(double x, double y, ArrayList<CoupledState> trajectory, int index, int range) {
        double min = 1000000;
        int segIndex = 0;
        int counter = index - range;
        int max = index + range;
        if (max > trajectory.size() - 1) {
            max = trajectory.size() - 1;
        }
        CoupledState seg;
        CoupledState closestSeg = trajectory.get(0);
        double dist;
        while (counter != max) {
            if (counter < 0) {
                counter = 0;
            }
            seg = trajectory.get(counter);
            dist = NerdyMath.distanceFormula(x, y, seg.config.position.x(), seg.config.position.y());
            if (dist < min) {
                min = dist;
                closestSeg = seg;
            }
            counter += 1;
        }
        return closestSeg;
    }
}