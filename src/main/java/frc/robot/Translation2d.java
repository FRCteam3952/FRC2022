package frc.robot;
public class Translation2d{
    protected final double x_;
    protected final double y_;
    public Translation2d(){
      x_ = 0;
      y_ = 0;
    }
    public Translation2d(double x, double y) {
      x_ = x;
      y_ = y;
  }
  public double x() {
    return x_;
}

public double y() {
    return y_;
}
  }