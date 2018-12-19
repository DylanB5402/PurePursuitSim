package com.company;

import grpl.pathfinder.Vec2;
import grpl.pathfinder.coupled.CoupledCausalTrajGen;
import grpl.pathfinder.coupled.CoupledChassis;
import grpl.pathfinder.coupled.CoupledState;
import grpl.pathfinder.coupled.CoupledWheelState;
import grpl.pathfinder.path.ArcParameterizer;
import grpl.pathfinder.path.Curve2d;
import grpl.pathfinder.path.HermiteFactory;
import grpl.pathfinder.path.HermiteQuintic;
import grpl.pathfinder.profile.Profile;
import grpl.pathfinder.profile.TrapezoidalProfile;
import grpl.pathfinder.transmission.DcMotor;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory.Config;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.Trajectory;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.QuickChart;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class Main {

    public static void main(String[] args) {


//        Config testConfig = new Config(Trajectory.FitMethod.HERMITE_CUBIC, Config.SAMPLES_HIGH, 0.02, 10, 1, 100);
////        double rightAngle = Pathfinder.d2r(90);
////        Waypoint[] testPoints = new Waypoint[] {
////                new Waypoint(0, 0, Pathfinder.d2r(0)),
////                new Waypoint(-5, 5, Pathfinder.d2r(0))
////        };
////        Trajectory testTraj = Pathfinder.generate(testPoints, testConfig);
////
////        Drivetrain cosmos = new Drivetrain(2, 0.02,0,0);
////        TrajectoryFollower controller = new TrajectoryFollower(testTraj, 3, false, 0.5, 0, 0.2);
////        while (!controller.isFinished()) {
////            controller.calculate(cosmos.robotX, cosmos.robotY, cosmos.angleDegrees);
////            cosmos.update(controller.getLeftVelocity(), controller.getRightVelocity());
////        }
////        System.out.println(148);
////        double[] xData = Stream.of(cosmos.xList.toArray(new Double[cosmos.xList.size()])).mapToDouble(Double::doubleValue).toArray();
////
////        double[] yData = Stream.of(cosmos.yList.toArray(new Double[cosmos.yList.size()])).mapToDouble(Double::doubleValue).toArray();
////        // Create Chart
////        XYChart chart = QuickChart.getChart("Pure pursuit", "X", "Y", "y(x)", xData, yData);
////        // Show it
////        new SwingWrapper(chart).displayChart();

        DcMotor motor = new DcMotor(12.0, (5330 * 2.0 * Math.PI / 60.0 / 12.75), 2 * 2.7, 2 * 131.0, 2 * 2.41 * 12.75);
//        0.0762 meters, 0.5 meters, 25 kg
        CoupledChassis chassis = new CoupledChassis(motor, motor, 0.0762, 0.5, 25.0);

        CoupledCausalTrajGen  gen = new CoupledCausalTrajGen(chassis);
        TrapezoidalProfile profile = new TrapezoidalProfile();
        profile.applyLimit(Profile.VELOCITY, -2, 2);
        profile.applyLimit(Profile.ACCELERATION, -1, 1);

        ArrayList<HermiteQuintic.Waypoint> waypoints = new ArrayList<>();
        waypoints.add(new HermiteQuintic.Waypoint(Vec2.cartesian(0, 0), Vec2.polar(5, Math.toRadians(0)), Vec2.cartesian(0, 0)));
        waypoints.add(new HermiteQuintic.Waypoint(Vec2.cartesian(4, 1), Vec2.polar(5, Math.toRadians(0)), Vec2.cartesian(0, 0)));

        ArrayList<Double> xList = new ArrayList<>();
        ArrayList<Double> yList = new ArrayList<>();

        TrajectoryFactory factory = new TrajectoryFactory(chassis, 0.02);
        ArrayList<CoupledState> traj = factory.generateTrajectory(waypoints, 2, 1);

        for (CoupledState pos : traj) {
//            xList.add(pos.config.position.x());
//            yList.add(pos.config.position.y());
            xList.add(pos.time);
            yList.add(pos.kinematics.velocity);
//            System.out.println(pos.kinematics.velocity);
        }

        double[] xData = Stream.of(xList.toArray(new Double[xList.size()])).mapToDouble(Double::doubleValue).toArray();

        double[] yData = Stream.of(yList.toArray(new Double[yList.size()])).mapToDouble(Double::doubleValue).toArray();
        // Create Chart
        XYChart chart = QuickChart.getChart("Pure pursuit", "X", "Y", "y(x)", xData, yData);
        // Show it
        new SwingWrapper(chart).displayChart();
    }
}
