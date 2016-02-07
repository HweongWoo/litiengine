/***************************************************************
 * Copyright (c) 2014 - 2015 , gurkenlabs, All rights reserved *
 ***************************************************************/
package de.gurkenlabs.litiengine.abilities;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import de.gurkenlabs.litiengine.IGameLoop;
import de.gurkenlabs.litiengine.abilities.effects.EffectArgument;
import de.gurkenlabs.litiengine.abilities.effects.IEffect;
import de.gurkenlabs.litiengine.annotation.AbilityInfo;
import de.gurkenlabs.litiengine.entities.IMovableCombatEntity;

/**
 * The Class Ability.
 */
@AbilityInfo
public abstract class Ability {
  private final List<Consumer<AbilityExecution>> abilityCastConsumer;

  /** The ability type. */
  private final String name;
  /** The tooltip. */
  private final String description;

  /** The attributes. */
  private final AbilityAttributes attributes;

  /** The current execution. */
  private AbilityExecution currentExecution;

  /** The effects. */
  private final List<IEffect> effects;

  /** The executing mob. */
  private final IMovableCombatEntity executor;

  /** The multi target. */
  private final boolean multiTarget;

  /**
   * Instantiates a new ability.
   *
   * @param executingMob
   *          the executing mob
   */
  protected Ability(final IMovableCombatEntity executor) {
    this.abilityCastConsumer = new CopyOnWriteArrayList<>();
    this.effects = new CopyOnWriteArrayList<>();

    final AbilityInfo info = this.getClass().getAnnotation(AbilityInfo.class);
    this.attributes = new AbilityAttributes(info);
    this.executor = executor;
    this.name = info.name();
    this.multiTarget = info.multiTarget();
    this.description = info.description();
  }

  public void addEffect(final IEffect effect) {
    this.getEffects().add(effect);
  }

  /**
   * Calculate impact area.
   *
   * @return the shape
   */
  public Shape calculateImpactArea() {
    final int impact = this.getAttributes().getImpact().getCurrentValue();
    final int impactAngle = this.getAttributes().getImpactAngle().getCurrentValue();
    final double arcX = this.getExecutor().getCollisionBox().getCenterX() - impact / 2;
    final double arcY = this.getExecutor().getCollisionBox().getCenterY() - impact / 2;
    final double start = this.getExecutor().getFacingAngle() - impactAngle / 2 - 90;

    return new Arc2D.Double(arcX, arcY, impact, impact, start, impactAngle, Arc2D.PIE);
  }

  /**
   * Can cast.
   *
   * @return true, if successful
   */
  public boolean canCast(final IGameLoop gameLoop) {
    return this.getCurrentExecution() == null || this.getCurrentExecution().getExecutionTicks() == 0 || gameLoop.getDeltaTime(this.getCurrentExecution().getExecutionTicks()) >= this.getAttributes().getCooldown().getCurrentValue();
  }

  /**
   * Cast.
   */
  public AbilityExecution cast(final IGameLoop gameLoop) {
    if (!this.canCast(gameLoop)) {
      return null;
    }
    this.currentExecution = new AbilityExecution(gameLoop, this);

    for (final Consumer<AbilityExecution> castConsumer : this.abilityCastConsumer) {
      castConsumer.accept(this.currentExecution);
    }

    return this.getCurrentExecution();
  }

  /**
   * Gets the attributes.
   *
   * @return the attributes
   */
  public AbilityAttributes getAttributes() {
    return this.attributes;
  }

  /**
   * Gets the cooldown in seconds.
   *
   * @return the cooldown in seconds
   */
  public float getCooldownInSeconds() {
    return (float) (this.getAttributes().getCooldown().getCurrentValue() / 1000.0);
  }

  /**
   * Gets the current execution.
   *
   * @return the current execution
   */
  public AbilityExecution getCurrentExecution() {
    return this.currentExecution;
  }

  public String getDescription() {
    return this.description;
  }

  /**
   * Gets the executing mob.
   *
   * @return the executing mob
   */
  public IMovableCombatEntity getExecutor() {
    return this.executor;
  }

  public String getName() {
    return this.name;
  }

  /**
   * Gets the remaining cooldown in seconds.
   *
   * @return the remaining cooldown in seconds
   */
  public float getRemainingCooldownInSeconds(final IGameLoop loop) {
    // calculate cooldown in seconds
    return (float) (!this.canCast(loop) ? (this.getAttributes().getCooldown().getCurrentValue() - loop.getDeltaTime(this.getCurrentExecution().getExecutionTicks())) / 1000.0 : 0);
  }

  /**
   * Checks if is multi target.
   *
   * @return true, if is multi target
   */
  public boolean isMultiTarget() {
    return this.multiTarget;
  }

  public void onCast(final Consumer<AbilityExecution> castConsumer) {
    if (!this.abilityCastConsumer.contains(castConsumer)) {
      this.abilityCastConsumer.add(castConsumer);
    }
  }

  public void onEffectApplied(final Consumer<EffectArgument> consumer) {
    for (final IEffect effect : this.getEffects()) {
      // registers to all effects and their follow up effects recursively
      this.onEffectApplied(effect, consumer);
    }
  }

  public void onEffectCeased(final Consumer<EffectArgument> consumer) {
    for (final IEffect effect : this.getEffects()) {
      // registers to all effects and their follow up effects recursively
      this.onEffectCeased(effect, consumer);
    }
  }

  private void onEffectApplied(final IEffect effect, final Consumer<EffectArgument> consumer) {
    effect.onEffectApplied(consumer);

    for (final IEffect followUp : effect.getFollowUpEffects()) {
      this.onEffectApplied(followUp, consumer);
    }
  }

  private void onEffectCeased(final IEffect effect, final Consumer<EffectArgument> consumer) {
    effect.onEffectCeased(consumer);

    for (final IEffect followUp : effect.getFollowUpEffects()) {
      this.onEffectCeased(followUp, consumer);
    }
  }

  /**
   * Gets the effects.
   *
   * @return the effects
   */
  protected List<IEffect> getEffects() {
    return this.effects;
  }
}