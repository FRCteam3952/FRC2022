package frc.robot;
public class TargetInfo {
    protected double x = 1.0;
    protected double y;
    protected double z;
    protected double skew;

    public TargetInfo(double y, double z) {
        this.y = y;
        this.z = z;
    }

    public void setSkew(double skew) {
        this.skew = skew;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getSkew() {
        return skew;
    }
}