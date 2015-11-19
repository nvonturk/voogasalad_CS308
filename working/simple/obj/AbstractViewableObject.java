package simple.obj;

import javafx.geometry.Point2D;

public abstract class AbstractViewableObject implements IViewableObject{
	
	private SimpleBoundingBox myBoundingBox;
	private String myPath;
	private int myID;
	
	public AbstractViewableObject(Point2D point, double width, double height, String path, int id){
		myBoundingBox = new SimpleBoundingBox(point, width, height);
		myPath = path;
		myID = id;
	}
	
	@Override
	public SimpleBoundingBox getBoundingBox(){
		return myBoundingBox;
	}
	
	@Override
	public String getPath(){
		return myPath;
	}

	@Override 
	public int returnID(){
		return myID;
	}
	
}
