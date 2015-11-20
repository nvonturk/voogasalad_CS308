package com.syntacticsugar.vooga.attribute.movement.algs;

import com.syntacticsugar.vooga.attribute.movement.IMover;

public class MoveLeft implements IMovementSetter {

	@Override
	public void setMovement(IMover mover) {
		double speed = mover.getSpeed();
		mover.setXVelocity(-1.0 * speed);
	}

}
