package frc.robot;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.JoystickPlus;

public class Controller {
    public static final double c = 0.1;
    public static final double deadzone = 0.2;
    public static final double max = 0.8;
    public static final double k = (max - c) / Math.log(2 - deadzone);

    public static final double cT = 0.08;
    public static final double deadzoneT = 0.08;
    public static final double maxT = 0.4;
    public static final double kT = (maxT - cT) / Math.log(2 - deadzoneT);

    public JoystickPlus joystick;

    public Controller(JoystickPlus joystick) {
        this.joystick = joystick;
    }

    

    public double getHorizontalMovement() {
        double x = joystick.getX();
        return Math.abs(x) >= deadzone ? k * Math.signum(x) * (Math.log(Math.abs(x) + 1 - deadzone) + c) : 0;
    }

    public double getLateralMovement() {
        double y = -joystick.getY();
        return Math.abs(y) >= deadzone ? k * Math.signum(y) * (Math.log(Math.abs(y) + 1 - deadzone) + c) : 0;
    }

    public double getRotation() {
        double t = joystick.getZ();
        return Math.abs(t) >= deadzoneT ? kT * Math.signum(t) * (Math.log(Math.abs(t) + 1 - deadzoneT) + cT) : 0;
    }

    public double getXRotation() {
        double xr = joystick.getXRotate();
        return xr;
    }

    public double getYRotation() {
        double yr = joystick.getYRotate();
        return yr;
    }

    public double getZRotation() {
        double zr = joystick.getZRotate();
        return zr;
    }

    public double getSlider() {
        double s = joystick.getSlider();
        return s;
    }

    public boolean triggerPressed() {
        return joystick.getTrigger();
    }

    public double twist() {
        double h = joystick.getTwist();
        return h;
    }

    public double throttle() {
        return joystick.getThrottle();
        
    }

    public boolean top() {
        return joystick.getTop();
    }

    //go into joystick directly and great new method
    public boolean isInputed() {
        boolean x= false;
        return x;
    }
    
    //the button was a joystick so replaced z axis
    public void setChannelforAimthingy() {
        joystick.setYChannel(4);
    }
    public void setChannelForStickButton() {
        joystick.setThrottleChannel(6);
    }

    public boolean rightShoulderPressed() {
        return joystick.getSlider()>0;
    }

    public boolean leftShoulderPressed() {
        return joystick.getYChannel() > 0;
    }
}
