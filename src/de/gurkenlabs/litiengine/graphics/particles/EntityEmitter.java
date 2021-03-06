package de.gurkenlabs.litiengine.graphics.particles;

import java.awt.geom.Point2D;

import de.gurkenlabs.litiengine.entities.IEntity;

/**
 * An abstract implementation for emitters that are bound to
 * {@link de.gurkenlabs.litiengine.entities.IEntity#getLocation()}.
 */
public abstract class EntityEmitter extends Emitter implements IEntityEmitter {

  /** The entity. */
  private final IEntity entity;

  /**
   * Instantiates a new entity emitter.
   *
   * @param entity
   *          the entity
   */
  public EntityEmitter(final IEntity entity) {
    super((int) entity.getCenter().getX(), (int) entity.getCenter().getY());
    this.entity = entity;
  }

  @Override
  public IEntity getEntity() {
    return this.entity;
  }

  @Override
  public Point2D getLocation() {
    if (this.getEntity() == null) {
      return null;
    }

    return this.getEntity().getCenter();
  }

  @Override
  public void setLocation(final Point2D location) {
  }
}
