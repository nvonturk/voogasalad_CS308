package objects.events;

import objects.IGameObject;

public class ObjectDespawnEvent extends AbstractEvent {

	public ObjectDespawnEvent(IGameObject obj) {
		mySource = obj;
	}
}
