package com.company;

import grpl.pathfinder.coupled.CoupledCausalTrajGen;
import grpl.pathfinder.coupled.CoupledChassis;
import grpl.pathfinder.coupled.CoupledState;
import grpl.pathfinder.path.ArcParameterizer;
import grpl.pathfinder.path.Curve2d;
import grpl.pathfinder.path.HermiteFactory;
import grpl.pathfinder.path.HermiteQuintic;
import grpl.pathfinder.profile.Profile;
import grpl.pathfinder.profile.TrapezoidalProfile;

import java.util.ArrayList;
import java.util.List;

public class TrajectoryFactory {

    private CoupledChassis m_chasis;
    private ArcParameterizer m_param;
    private CoupledCausalTrajGen m_gen;
    private double m_dt;


    public TrajectoryFactory(CoupledChassis chasis, double dt) {
        m_chasis = chasis;
        m_param = new ArcParameterizer();
        m_param.configure(0.01, 0.01);
        m_gen = new CoupledCausalTrajGen(m_chasis);
        m_dt = dt;
    }

    public ArrayList<CoupledState> generateTrajectory(ArrayList<HermiteQuintic.Waypoint> waypoints, double maxVel, double maxAccel) {
        TrapezoidalProfile profile = new TrapezoidalProfile();

//        doesn't actual work at the moment, waiting for jaci to impl
        profile.applyLimit(Profile.ACCELERATION, -maxAccel, maxAccel);
        profile.applyLimit(Profile.VELOCITY, -maxVel, maxVel);

        List<? extends Curve2d> curves = m_param.parameterize(HermiteFactory.generateQuintic(waypoints));
        CoupledState lastState = new CoupledState();
        CoupledState state = new CoupledState();
        m_gen.configure(curves, profile);
        ArrayList<CoupledState> traj = new ArrayList<>();
        for (double t = 0; !state.finished; t += m_dt) {
            state = m_gen.generate(lastState, t);
            traj.add(state);
            lastState = state;
        }
        return traj;
    }
}
