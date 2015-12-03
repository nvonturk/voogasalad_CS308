package authoring.objectediting;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class Resizer {
    public interface OnResizeEventListener{
    	void onResize(Node node, double x, double y, double h, double w);
    }
    
    private static final OnResizeEventListener resizeListener = new OnResizeEventListener(){
    	@Override
    	public void onResize(Node node, double x, double y, double h, double w){
    		setNodeSize(node, x, y, h, w);
    	}
    };
    
    private static void setNodeSize(Node node, double x, double y, double h, double w){
    	node.setLayoutX(x);
    	node.setLayoutX(y);
    	// Set width and height here
    	
    }
    
    
    /**
     * The margin around the control that a user can click in to start resizing
     * the region.
     */
    private static final int RESIZE_MARGIN = 5;
    private final Region region;
    private double y;
    private boolean initMinHeight;
    private boolean dragging;

    private Resizer(Region region) {
        this.region = region;
    }

    public static void makeResizable(Region region) {
        final Resizer resizer = new Resizer(region);
        
        region.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mousePressed(event);
            }});
        region.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseDragged(event);
            }});
        region.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseOver(event);
            }});
        region.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseReleased(event);
            }});
    }

    protected void mouseReleased(MouseEvent event) {
        dragging = false;
        region.setCursor(Cursor.DEFAULT);
    }

    protected void mouseOver(MouseEvent event) {
        if(isInDraggableZone(event) || dragging) {
            region.setCursor(Cursor.S_RESIZE);
        }
        else {
            region.setCursor(Cursor.DEFAULT);
        }
    }

    protected boolean isInDraggableZone(MouseEvent event) {
        return event.getY() > (region.getHeight() - RESIZE_MARGIN);
    }

    protected void mouseDragged(MouseEvent event) {
        if(!dragging) {
            return;
        }
        
        double mousey = event.getY();
        
        double newHeight = region.getMinHeight() + (mousey - y);

        region.setMinHeight(newHeight);
        
        y = mousey;
    }

    protected void mousePressed(MouseEvent event) {
        
        // ignore clicks outside of the draggable margin
        if(!isInDraggableZone(event)) {
            return;
        }
        
        dragging = true;
        
        // make sure that the minimum height is set to the current height once,
        // setting a min height that is smaller than the current height will
        // have no effect
        if (!initMinHeight) {
            region.setMinHeight(region.getHeight());
            initMinHeight = true;
        }
        
        y = event.getY();
    }
}