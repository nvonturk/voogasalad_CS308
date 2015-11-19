package simple.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import simple.attribute.SimpleControlAttribute;
import simple.attribute.SimpleHealthAttribute;
import simple.conditions.ISimpleCondition;
import simple.conditions.SimpleConditions;
import simple.eng.SimpleEngine;
import simple.event.SimpleHealthChangeEvent;
import simple.obj.ISimpleObject;
import simple.obj.SimpleObject;
import simple.obj.SimpleObjectType;
import simple.universe.ISimpleUniverse;
import simple.universe.SimpleUniverse;
import simple.utilities.GameInformation;
import view.ViewController;

public class SimpleGameManager implements ISimpleGameManager {

	private ISimpleUniverse myUniverse;
	// private ISimpleViewController myViewController;
	private List<ISimpleCondition> myConditions;
	private GameInformation myInformation;
	private ViewController myViewController;

	public SimpleGameManager() {
		myUniverse = new SimpleUniverse();
		myConditions = new ArrayList<ISimpleCondition>();
		myViewController = new ViewController(600.0);
		
		ISimpleObject player = new SimpleObject(SimpleObjectType.PLAYER, new Point2D(0,0),10,10);
		player.addAttribute(new SimpleControlAttribute(player));
		player.addAttribute(new SimpleHealthAttribute(10, player));
		
		ISimpleObject enemy = new SimpleObject(SimpleObjectType.ENEMY, new Point2D(20,20),30,30);
		enemy.addCollisionBinding(SimpleObjectType.PLAYER, new SimpleHealthChangeEvent(10));
		
		myUniverse.addGameObject(player);
		myUniverse.addGameObject(enemy);
		
		SimpleEngine.frameUpdate(myUniverse);
		
	}
	
	@Override
	public void updateGame() {
		SimpleEngine.frameUpdate(myUniverse);
		checkConditions();
		updateStats();

	}

	private void updateStats() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkConditions() {
		Collection<ISimpleObject> unmodifiableUniverse = myUniverse.getGameObjects();

		for (ISimpleCondition condition : myConditions) {
			if(condition.checkObject(unmodifiableUniverse)){
				switchLevel(condition.returnType());
			}
		}

	}

	@Override
	public void switchLevel(SimpleConditions type) {
		if (type.equals(SimpleConditions.WINNING)) {
			// go forward
		} else if (type.equals(SimpleConditions.LOSING)) {
			// go backward?
		}

	}
	
	public void receiveKeyPressed(KeyCode code){
		myUniverse.receiveKeyPress(code);
	}
	
	public void receiveKeyReleased(KeyCode code){
		myUniverse.receiveKeyRelease(code);
	}

	public Pane getGameView() {
		return myViewController.getGameView();
	}

}
