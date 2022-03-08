package frc.robot.controllers;


//normal controller the old boi

public class FlightJoystickController {
    public static final double c = 0.1;
    public static final double deadzone = 0.2;
    public static final double max = 0.8;
    public static final double k = (max - c) / Math.log(2 - deadzone);

    public static final double cT = 0.08;
    public static final double deadzoneT = 0.08;
    public static final double maxT = 0.4;
    public static final double kT = (maxT - cT) / Math.log(2 - deadzoneT);

    public JoystickPlus joystick;

    public FlightJoystickController(JoystickPlus joystick) {
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

    public boolean backButtonPressed() {
        return joystick.getRawButtonPressed(0);
    } //POV button for the driver stick
    
    public int getJoystickPOV() {
        return joystick.getPOV();
    }

    public boolean button5Held() {
        return joystick.getRawButtonReleased(5);
    }

    public boolean button6Pressed() {
        return joystick.getRawButtonPressed(6);
    }

    public boolean button7Pressed() {
        return joystick.getRawButtonPressed(7);
    }

    public boolean button8Pressed() {
        return joystick.getRawButtonPressed(8);
    }

}
