package simple.obj;

import java.util.Collection;

import simple.attribute.ISimpleAttribute;
import simple.event.ISimpleEvent;
import simple.gameview.ISimpleViewable;
import simple.gameview.SimpleViewableObject;
import simple.universe.ISimpleUniverse;

public interface ISimpleObject extends ISimpleAttributeContainer {

	/**
	 * Update this object within the context of the whole universe.
	 * @param universe
	 */
	public void updateSelf(ISimpleUniverse universe);
	
	/**
	 * Returns a Collection of Events associated with colliding with a specific type
	 * of object.
	 * @param type
	 * @return
	 */
	public Collection<ISimpleEvent> getEventsFromCollision(SimpleObjectType type);
	
	/**
	 * Returns the Type of this object as specified by the SimpleObjectType enum.
	 * @return
	 */
	public SimpleObjectType getType();

	
	// ISimpleAttributeContainer methods
	@Override
	public Collection<ISimpleAttribute> getAttributes();

}