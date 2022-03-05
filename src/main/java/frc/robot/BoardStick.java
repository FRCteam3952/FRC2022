// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.POVButton;

/**
 * This class contains additional methods to the Joysick class that access additional axis values
 */
public class BoardStick extends Joystick {
  public static final byte kDefaultXRotateChannel = 3;
  public static final byte kDefaultYRotateChannel = 4;
  public static final byte kDefaultZRotateChannel = 5;
  public static final byte kDefaultSliderChannel = 6;

  /** Represents an analog axis on a joystick. */
  public enum AxisType {
    kX(0),
    kY(1);

    public final int value;

    AxisType(int value) {
      this.value = value;
    }

    public enum PowType{
        up(1),
        upRight(2),
        right(3),
        downRight(4),
        downLeft(5),
        left(6),
        upLeft(7);

        public final int value;

        PowType(int value) {
            this.value=value;
        }
    }

//button for aiming declaration
    public enum ButtonType {
        kTrigger(1);
    
        public final int value;
    
        ButtonType(int value) {
          this.value = value;
        }
      }
  }

  private final byte[] m_axes = new byte[AxisType.values().length];
  

  /**
   * Construct an instance of a joystick.
   *
   * @param port The port index on the Driver Station that the joystick is plugged into.
   */
  public BoardStick(final int port) {
    super(port);

    HAL.report(tResourceType.kResourceType_Joystick, port + 1);
  }

  /**
   *    * Set the channel associated with the X Rotation axis.
   *
   * @param channel The channel to set the axis to.
   */
  public boolean getAiming() {
    return getRawButton(ButtonType.kTrigger.value);
  }

  public boolean getLocate()
  {
      return true;
  }

  /**
   *    * Set the channel associated with the X Rotation axis.
   *
   * @param channel The channel to set the axis to.
   */
  public double getXstuff() {
    return getRawAxis(m_axes[AxisType.kX.value]);
  }

    /**
   *    * Set the channel associated with the X Rotation axis.
   *
   * @param channel The channel to set the axis to.
   */
  public double getYstuff() {
    return getRawAxis(m_axes[AxisType.kY.value]);
  }

}