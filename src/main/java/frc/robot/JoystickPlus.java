// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.Joystick;

/**
 * This class contains additional methods to the Joysick class that access additional axis values
 */
public class JoystickPlus extends Joystick {
  public static final byte kDefaultXRotateChannel = 3;
  public static final byte kDefaultYRotateChannel = 4;
  public static final byte kDefaultZRotateChannel = 5;
  public static final byte kDefaultSliderChannel = 6;

  /** Represents an analog axis on a joystick. */
  public enum AxisType {
    kX(0),
    kY(1),
    kZ(2),
    kTwist(3),
    kThrottle(4),
    kXRotate(5),
    kYRotate(6),
    kZRotate(7),
    kSlider(8);

    public final int value;

    AxisType(int value) {
      this.value = value;
    }
  }

    public enum ButtonType {
        kTrigger(1);
    
        public final int value;
    
        ButtonType(int value) {
          this.value = value;
        }
      }

  private final byte[] m_axes = new byte[AxisType.values().length];
  

  /**
   * Construct an instance of a joystick.
   *
   * @param port The port index on the Driver Station that the joystick is plugged into.
   */
  public JoystickPlus(final int port) {
    super(port);

    m_axes[AxisType.kXRotate.value] = kDefaultXRotateChannel;
    m_axes[AxisType.kYRotate.value] = kDefaultYRotateChannel;
    m_axes[AxisType.kZRotate.value] = kDefaultZRotateChannel;
    m_axes[AxisType.kSlider.value] = kDefaultSliderChannel;

    HAL.report(tResourceType.kResourceType_Joystick, port + 1);
  }

  /**
   * Set the channel associated with the X Rotation axis.
   *
   * @param channel The channel to set the axis to.
   */
  public void setXRotateChannel(int channel) {
    m_axes[AxisType.kXRotate.value] = (byte) channel;
  }

  /**
   * Set the channel associated with the Y Rotation axis.
   *
   * @param channel The channel to set the axis to.
   */
  public void setYRotateChannel(int channel) {
    m_axes[AxisType.kYRotate.value] = (byte) channel;
  }

  /**
   * Set the channel associated with the Z Rotation axis.
   *
   * @param channel The channel to set the axis to.
   */
  public void setZRotateChannel(int channel) {
    m_axes[AxisType.kZRotate.value] = (byte) channel;
  }

  /**
   * Set the channel associated with the Slider axis.
   *
   * @param channel The channel to set the axis to.
   */
  public void setSliderChannel(int channel) {
    m_axes[AxisType.kSlider.value] = (byte) channel;
  }

  /**
   * Get the channel currently associated with the X Rotation axis.
   *
   * @param channel The channel to set the axis to.
   */
 
  public int getXRotateChannel() {
    return m_axes[AxisType.kXRotate.value];
  }

  /**
   * Get the channel currently associated with the Y Rotation axis.
   *
   * @return The channel for the axis.
   */
  public int getYRotateChannel() {
    return m_axes[AxisType.kYRotate.value];
  }

  /**
   * Get the channel currently associated with the Z Rotation axis.
   *
   * @return The channel for the axis.
   */
  public int getZRotateChannel() {
    return m_axes[AxisType.kZRotate.value];
  }

  /**
   * Get the channel currently associated with the Slider axis.
   *
   * @return The channel for the axis.
   */
  public int getSliderChannel() {
    return m_axes[AxisType.kSlider.value];
  }

  /**
   * Get the X Rotation value of the joystick. This depends on the mapping of the joystick connected to the
   * current port.
   *
   * @return The X Rotate value of the joystick.
   */
  public final double getXRotate() {
    return getRawAxis(m_axes[AxisType.kXRotate.value]);
  }

  /**
   * Get the Y Rotate value of the joystick. This depends on the mapping of the joystick connected to the
   * current port.
   *
   * @return The Y Rotate value of the joystick.
   */
  public final double getYRotate() {
    return getRawAxis(m_axes[AxisType.kYRotate.value]);
  }

  /**
   *  Get the Z Rotation value of the joystick. This depends on the mapping of the joystick connected to the
   *  current port.
   *
   * @return The Z Rotation value of the joystick.
   */
  public double getZRotate() {
    return getRawAxis(m_axes[AxisType.kZRotate.value]);
  }

  /**
   * Get the slider value of the current joystick. This depends on the mapping of the joystick
   * connected to the current port.
   *
   * @return The Slider value of the joystick.
   */
  public double getSlider() {
    return getRawAxis(m_axes[AxisType.kSlider.value]);
  }


  /**
   *    * Set the channel associated with the X Rotation axis.
   *
   * @param channel The channel to set the axis to.
   */
  public boolean getAiming() {
    return getRawButton(ButtonType.kTrigger.value);
  }

}
