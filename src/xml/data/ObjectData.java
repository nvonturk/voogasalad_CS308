package xml.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.syntacticsugar.vooga.gameplayer.attribute.IAttribute;
import com.syntacticsugar.vooga.gameplayer.event.ICollisionEvent;
import com.syntacticsugar.vooga.gameplayer.objects.GameObjectType;
import com.syntacticsugar.vooga.gameplayer.objects.IGameObject;

import javafx.geometry.Point2D;

public class ObjectData {
	
	private String myName;
	
	private GameObjectType myType;
	private Point2D mySpawnPoint;
	private double width;
	private double height;
	private String myImagePath;
	private Collection<IAttribute> myAttributes;
	private Map<GameObjectType, Collection<ICollisionEvent>> myCollisionMap;
	
	public ObjectData() {
		mySpawnPoint = new Point2D(0.0, 0.0);
		myAttributes = new ArrayList<>();
		myCollisionMap = new HashMap<GameObjectType, Collection<ICollisionEvent>>();
	}
	
	public ObjectData(IGameObject obj) {
		mySpawnPoint = obj.getBoundingBox().getPoint();
		width = obj.getBoundingBox().getWidth();
		height = obj.getBoundingBox().getHeight();
		myAttributes = obj.getAttributes().values();
		myCollisionMap = obj.getCollisionMap();
	}
	
	public GameObjectType getType() {
		return this.myType;
	}
	
	public Point2D getSpawnPoint() {
		return this.mySpawnPoint;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public String getImagePath() {
		return this.myImagePath;
	}
	
	public Collection<IAttribute> getAttributes() {
		return this.myAttributes;
	}
	
	public Map<GameObjectType, Collection<ICollisionEvent>> getCollisionMap() {
		return this.myCollisionMap;
	}
	
	public void setType(GameObjectType type) {
		this.myType = type;
	}
	
	public void setSpawnPoint(double x, double y) {
		this.mySpawnPoint = new Point2D(x, y);
	}
	
	public void setWidth(double w) {
		width = w;
	}
	
	public void setHeight(double h) {
		height = h;
	}
	
	public void setImagePath(String myImagePath) {
		this.myImagePath = myImagePath;
	}
	
	public void setObjectName(String name) {
		this.myName = name;
	}
	
	public String getObjectName() {
		return myName;
	}

	public void setAttributes(Collection<IAttribute> attributes) {
		this.myAttributes.clear();
		this.myAttributes.addAll(attributes);
	}

	public void setCollisionMap(Map<GameObjectType, Collection<ICollisionEvent>> collisionMap) {
		this.myCollisionMap.clear();
		this.myCollisionMap.putAll(collisionMap);
	}
}
