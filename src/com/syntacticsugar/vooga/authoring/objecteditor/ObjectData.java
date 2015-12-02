package com.syntacticsugar.vooga.authoring.objecteditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.syntacticsugar.vooga.gameplayer.attribute.IAttribute;
import com.syntacticsugar.vooga.gameplayer.event.ICollisionEvent;
import com.syntacticsugar.vooga.gameplayer.objects.GameObjectType;

public class ObjectData {

	private GameObjectType myType;
	private String myName;
	private String myImagePath;
	private Collection<IAttribute> myAttributes;
	private Map<GameObjectType, Collection<ICollisionEvent>> myCollisionMap;
	
	public ObjectData() {
		myAttributes = new ArrayList<IAttribute>();
		myCollisionMap = new HashMap<GameObjectType, Collection<ICollisionEvent>>();
	}
	
	public GameObjectType getType() {
		return this.myType;
	}
	
	public String getImagePath() {
		return this.myImagePath;
	}
	
	public String getObjectName(){
		return this.myName;
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
	
	public void setImagePath(String myImagePath) {
		this.myImagePath = myImagePath;
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
