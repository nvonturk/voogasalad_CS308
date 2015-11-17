package objects.player;

import objects.attributes.IPlayer;

public class MoveUpStart implements KeyInput {

	@Override
	public void run(IPlayer comp) {
		double speed = comp.getSpeed();
		comp.setYVel(-1 * speed);
	}

}
