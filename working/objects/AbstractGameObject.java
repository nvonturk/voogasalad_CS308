package objects;

import java.awt.Point;
import java.util.EventListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import environment.GameEnvironment;
import javafx.util.Pair;
import objects.events.AbstractEvent;
import objects.events.CollisionEvent;
import objects.events.ICollisionListener;
import objects.events.IEvent;

public abstract class AbstractGameObject extends Observable implements IGameObject, ICollisionListener{
	
	//Members
	
	//Unique, corresponds to a view object
	int myID;
	
	//Maybe this is a list if objects can have multiple types?
	EObjectType myType;

	Point myLocation;
	Pair myBBox;
	
	Boolean toBeDestroyed;
	
	Map<EObjectType, List<IEvent>> myCollisionEvents;
	
	//This holds all the components and items an object has
	List<IChild> myChildren;
	
	//Methods

	public AbstractGameObject(Point p, GameEnvironment g) {
		myLocation = p;
		this.addObserver(g);
		
		myCollisionEvents = new HashMap<>();
	}
	
	public EObjectType getType() {
		return myType;
	}
	
	public void addCollisionEvent(EObjectType type, AbstractEvent e) {
		if (myCollisionEvents.keySet().contains(type)) {
			myCollisionEvents.get(type).add(e);
		}
		else {
			myCollisionEvents.put(type, new LinkedList<>());
			myCollisionEvents.get(type).add(e);
		}
	}
	
	public void onCollision(CollisionEvent e) {
		
		//Figure out if this event is for us (and which object is us)
		IGameObject obj;		
		if (e.getSource().equals(this)) {
			obj = e.getTarget();
		}
		else if (e.getTarget().equals(this)){
			obj = e.getSource();
		}
		else {
			return;
		}
		
		//Send every event for the right type of object
		for (EObjectType type : myCollisionEvents.keySet()) {
			if (obj.getType().equals(type)){
				List<IEvent> eventList = myCollisionEvents.get(type);
				for (IEvent event: eventList) {
					obj.sendEventToChildren(event);
				}
			}
		}
	}
	
	public void sendEventToChildren(IEvent e){
		for (IChild c: myChildren) {
			c.receiveEvent(e);
		}
	}
	
	public void setToDestroy(){
		toBeDestroyed = true;
	}
	
	public void move(Point loc) {
		myLocation = loc;
	}
	
	public void update() {
		if (toBeDestroyed) {
			//TODO: Write this later
		}
		
		for (IChild c: myChildren) {
			c.update();
		}
	}

}