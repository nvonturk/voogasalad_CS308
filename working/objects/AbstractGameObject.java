package objects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import environment.EventPoster;
import environment.GameEnvironment;
import javafx.geometry.Point2D;
import objects.events.AbstractEvent;
import objects.events.CollisionEvent;
import objects.events.EEventType;
import objects.events.IEvent;
import objects.events.ObjectDespawnEvent;
import view.BoundingBox;

public abstract class AbstractGameObject implements IGameObject{
	
	//Members
	
	//Unique, corresponds to a view object
	int myID;
	
	//Maybe this is a list if objects can have multiple types?
	EObjectType myType;

	BoundingBox myBoundingBox;
	Vector<Double> myVelocity;
	
	Boolean toBeDestroyed;
	
	Map<EObjectType, List<IEvent>> myCollisionEvents;
	
	//This holds all the attributes and items an object has
	List<IChild> myChildren;
	
	EventPoster myEventPoster;
	
	//Methods

	public AbstractGameObject(Point2D p, double w, double h, GameEnvironment g) {
		myBoundingBox = new BoundingBox(p, w, h);
		
		setVelocity(0.0, 0.0);
		
		myEventPoster = (EventPoster) g;
		myChildren = new LinkedList<>();
		toBeDestroyed = false;
		
		
		myCollisionEvents = new HashMap<>();
	}
	
	public void setVelocity(double x, double y) {
		Vector<Double> vel = new Vector<>();
		vel.add(x);
		vel.add(y);
		myVelocity = vel;
	}
	
	public EObjectType getType() {
		return myType;
	}
	
	public Point2D getLocation() {
		return myBoundingBox.getPoint();
	}
	
	public void setLocation(Point2D loc) {
		myBoundingBox.setPoint(loc.getX(), loc.getY());
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
	
	public void move() {
		double x = myBoundingBox.getPoint().getX();
		double y = myBoundingBox.getPoint().getY();
		myBoundingBox.setPoint(x + myVelocity.get(0), y + myVelocity.get(1));
	}
	
	public void setVelocity(Vector<Double> vel) {
		myVelocity = vel;
	}
	
	public void update() {
		if (toBeDestroyed) {
			destroySelf();
		}
		
		for (IChild c: myChildren) {
			c.update();
		}
		
		move();
		System.out.println(myBoundingBox.getPoint());
	}
	
	private void destroySelf() {
		ObjectDespawnEvent event = new ObjectDespawnEvent(this);
		myEventPoster.postEvent(event);
	}
	
	public void onEvent(IEvent e) {
		
		if (e.getTarget() != null && e.getTarget().equals(this) && e.getType().equals(EEventType.ObjectKillEvent)) {
			toBeDestroyed = true;
		}
		
		sendEventToChildren(e);
	}
	
	public List<IChild> getChildren() {
		return myChildren;
	}
	
	public BoundingBox getBoundingBox(){
		return myBoundingBox;
	}

}
