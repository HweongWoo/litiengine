/***************************************************************
 * Copyright (c) 2014 - 2015 , gurkenlabs, All rights reserved *
 ***************************************************************/
package de.gurkenlabs.litiengine.input;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import de.gurkenlabs.litiengine.IUpdateable;

// TODO: Auto-generated Javadoc
/**
 * The Interface IMouse.
 */
public interface IMouse extends IUpdateable, MouseListener, MouseMotionListener, MouseWheelListener {

  /**
   * Gets the render location.
   *
   * @return the render location
   */
  public Point2D getLocation();
  
  public Point2D getMapLocation();

  /**
   * Checks if is pressed.
   *
   * @return true, if is pressed
   */
  public boolean isPressed();

  /**
   * Register mouse listener.
   *
   * @param listener
   *          the listener
   */
  public void registerMouseListener(MouseListener listener);

  /**
   * Register mouse motion listener.
   *
   * @param listener
   *          the listener
   */
  public void registerMouseMotionListener(MouseMotionListener listener);

  /**
   * Register mouse wheel listener.
   *
   * @param listener
   *          the listener
   */
  public void registerMouseWheelListener(MouseWheelListener listener);

  /**
   * Unregister mouse listener.
   *
   * @param listener
   *          the listener
   */
  public void unregisterMouseListener(MouseListener listener);

  /**
   * Unregister mouse motion listener.
   *
   * @param listener
   *          the listener
   */
  public void unregisterMouseMotionListener(MouseMotionListener listener);

  /**
   * Unregister mouse wheel listener.
   *
   * @param listener
   *          the listener
   */
  public void unregisterMouseWheelListener(MouseWheelListener listener);
}
