package frc.robot.subsystems;



import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.TargetInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import frc.robot.Translation2d;
import frc.robot.LimelightConstants;
/**
 * Subsystem for interacting with the Limelight 2
 */
public class Limelight extends SubsystemBase {
    public final static int kDefaultPipeline = 0;
    public final static int kSortTopPipeline = 1;

 

    private NetworkTable mNetworkTable;

    public Limelight(LimelightConstants constants) {
        mConstants = constants;
        mNetworkTable = NetworkTableInstance.getDefault().getTable(constants.kTableName);
    }


    public static class PeriodicIO {
        // INPUTS
        public double latency;
        public int givenLedMode;
        public int givenPipeline;
        public double xOffset;
        public double yOffset;
        public double area;

        // OUTPUTS
        public int ledMode = 1; // 0 - use pipeline mode, 1 - off, 2 - blink, 3 - on
        public int camMode = 0; // 0 - vision processing, 1 - driver camera
        public int pipeline = 0; // 0 - 9
        public int stream = 2; // sets stream layout if another webcam is attached
        public int snapshot = 0; // 0 - stop snapshots, 1 - 2 Hz
    }

    private LimelightConstants mConstants = null;
    private PeriodicIO mPeriodicIO = new PeriodicIO();
    private boolean mOutputsHaveChanged = true;
    private double[] mZeroArray = new double[]{0, 0, 0, 0, 0, 0, 0, 0};
    private List<TargetInfo> mTargets = new ArrayList<>();
    private boolean mSeesTarget = false;

 

    public double getLensHeight() {
        return mConstants.kHeight;
    }


    public synchronized void readPeriodicInputs() {
     
        mPeriodicIO.givenLedMode = (int) mNetworkTable.getEntry("ledMode").getDouble(1.0);
        mPeriodicIO.givenPipeline = (int) mNetworkTable.getEntry("pipeline").getDouble(0);
        mPeriodicIO.xOffset = mNetworkTable.getEntry("tx").getDouble(0.0);
        mPeriodicIO.yOffset = mNetworkTable.getEntry("ty").getDouble(0.0);
        mPeriodicIO.area = mNetworkTable.getEntry("ta").getDouble(0.0);
        mSeesTarget = mNetworkTable.getEntry("tv").getDouble(0) == 1.0;
    }

    public synchronized void writePeriodicOutputs() {
        if (mPeriodicIO.givenLedMode != mPeriodicIO.ledMode ||
                mPeriodicIO.givenPipeline != mPeriodicIO.pipeline) {
            System.out.println("Table has changed from expected, retrigger!!");
            mOutputsHaveChanged = true;
        }
        if (mOutputsHaveChanged) {
            mNetworkTable.getEntry("ledMode").setNumber(mPeriodicIO.ledMode);
            mNetworkTable.getEntry("camMode").setNumber(mPeriodicIO.camMode);
            mNetworkTable.getEntry("pipeline").setNumber(mPeriodicIO.pipeline);
            mNetworkTable.getEntry("stream").setNumber(mPeriodicIO.stream);
            mNetworkTable.getEntry("snapshot").setNumber(mPeriodicIO.snapshot);

            mOutputsHaveChanged = false;
        }
    }


    public synchronized void outputTelemetry() {
        SmartDashboard.putBoolean(mConstants.kName + ": Has Target", mSeesTarget);
        SmartDashboard.putNumber(mConstants.kName + ": Pipeline Latency (ms)", mPeriodicIO.latency);
    }

    public enum LedMode {
        PIPELINE, OFF, BLINK, ON
    }

    public synchronized void setLed(LedMode mode) {
        if (mode.ordinal() != mPeriodicIO.ledMode) {
            mPeriodicIO.ledMode = mode.ordinal();
            mOutputsHaveChanged = true;
        }
    }

    public synchronized void setPipeline(int mode) {
        if (mode != mPeriodicIO.pipeline) {
            mPeriodicIO.pipeline = mode;
            System.out.println(mPeriodicIO.pipeline + ", " + mode);
            mOutputsHaveChanged = true;
        }
    }

    public synchronized void triggerOutputs() {
        mOutputsHaveChanged = true;
    }

    public synchronized int getPipeline() {
        return mPeriodicIO.pipeline;
    }

    public synchronized boolean seesTarget() {
        return mSeesTarget;
    }




    /**
     * @return two targets that make up one hatch/port or null if less than two targets are found
     */
    public synchronized List<TargetInfo> getTarget() {
        List<TargetInfo> targets = getRawTargetInfos();
        if (seesTarget() && targets != null) {
            return targets;
        }
        return null;
    }

    public static final double kEpsilon = 1e-12;
    // limelight
    public static final double kHorizontalFOV = 59.6; // degrees
    public static final double kVerticalFOV = 49.7; // degrees
    public static final double kVPW = 2.0 * Math.tan(Math.toRadians(kHorizontalFOV / 2.0));
    public static final double kVPH = 2.0 * Math.tan(Math.toRadians(kVerticalFOV / 2.0));
    public static final double kImageCaptureLatency = 11.0 / 1000.0; // seconds

    private synchronized List<TargetInfo> getRawTargetInfos() {
        List<double[]> corners = getTopCorners();
        if (corners == null) {
            return null;
        }

        double slope = 1.0;
        if (Math.abs(corners.get(1)[0] - corners.get(0)[0]) > kEpsilon) {
            slope = (corners.get(1)[1] - corners.get(0)[1]) /
                    (corners.get(1)[0] - corners.get(0)[0]);
        }

        mTargets.clear();
        for (int i = 0; i < 2; ++i) {
            // Average of y and z;
            double y_pixels = corners.get(i)[0];
            double z_pixels = corners.get(i)[1];

            // Redefine to robot frame of reference.
            double nY = -((y_pixels - 160.0) / 160.0);
            double nZ = -((z_pixels - 120.0) / 120.0);

            double y = kVPW / 2 * nY;
            double z = kVPH / 2 * nZ;

            TargetInfo target = new TargetInfo(y, z);
            target.setSkew(slope);
            mTargets.add(target);
        }

        return mTargets;
    }

    /**
     * Returns raw top-left and top-right corners
     *
     * @return list of corners: index 0 - top left, index 1 - top right
     */
    private List<double[]> getTopCorners() {
        double[] xCorners = mNetworkTable.getEntry("tcornx").getDoubleArray(mZeroArray);
        double[] yCorners = mNetworkTable.getEntry("tcorny").getDoubleArray(mZeroArray);
        mSeesTarget = mNetworkTable.getEntry("tv").getDouble(0) == 1.0;

        // something went wrong
        if (!mSeesTarget ||
                Arrays.equals(xCorners, mZeroArray) || Arrays.equals(yCorners, mZeroArray)
                || xCorners.length != 8 || yCorners.length != 8) {
            return null;
        }

        return extractTopCornersFromBoundingBoxes(xCorners, yCorners);
    }

  

    private static final Comparator<Translation2d> xSort = Comparator.comparingDouble(Translation2d::x);
    private static final Comparator<Translation2d> ySort = Comparator.comparingDouble(Translation2d::y);

    /**
     * Returns raw top-left and top-right corners
     *
     * @return list of corners: index 0 - top left, index 1 - top right
     */
    public static List<double[]> extractTopCornersFromBoundingBoxes(double[] xCorners, double[] yCorners) {
        List<Translation2d> corners = new ArrayList<>();
        for (int i = 0; i < xCorners.length; i++) {
            corners.add(new Translation2d(xCorners[i], yCorners[i]));
        }

        corners.sort(xSort);

        List<Translation2d> left = corners.subList(0, 4);
        List<Translation2d> right = corners.subList(4, 8);

        left.sort(ySort);
        right.sort(ySort);

        List<Translation2d> leftTop = left.subList(0, 2);
        List<Translation2d> rightTop = right.subList(0, 2);

        leftTop.sort(xSort);
        rightTop.sort(xSort);

        Translation2d leftCorner = leftTop.get(0);
        Translation2d rightCorner = rightTop.get(1);

        return List.of(new double[]{leftCorner.x(), leftCorner.y()}, new double[]{rightCorner.x(), rightCorner.y()});
    }

    public double getLatency() {
        return mPeriodicIO.latency;
    }
    
}