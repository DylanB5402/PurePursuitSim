package com.company;


import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;

public class TrajectoryUtils {

    public static Segment getClosestSegment(double x, double y, Trajectory trajectory, int index, int range) {
        double min = 1000000;
        int segIndex = 0;
        int counter = index - range;
        int max = index + range;
        if (max > trajectory.length() - 1) {
            max = trajectory.length() - 1;
        }
        Segment seg;
        Segment closestSeg = trajectory.get(0);
        double dist;
        while (counter != max) {
            if (counter < 0) {
                counter = 0;
            }
            seg = trajectory.get(counter);
            dist = NerdyMath.distanceFormula(x, y, seg.x, seg.y);
            if (dist < min) {
                min = dist;
                closestSeg = seg;
            }
            counter += 1;
        }
        return closestSeg;
    }

    public static Segment getCloserSegment(double x, double y, Segment seg1, Segment seg2) {
        double dist1 = NerdyMath.distanceFormula(x, y, seg2.x, seg2.y);
        double dist2 = NerdyMath.distanceFormula(x, y, seg1.x, seg1.y);
        if (dist1 <= dist2) {
            return seg1;
        }
        else {
            return seg2;
        }
    }


}

