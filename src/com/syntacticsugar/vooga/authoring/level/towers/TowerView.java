package com.syntacticsugar.vooga.authoring.level.towers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syntacticsugar.vooga.authoring.fluidmotion.FadeTransitionWizard;
import com.syntacticsugar.vooga.authoring.fluidmotion.FluidGlassBall;
import com.syntacticsugar.vooga.authoring.fluidmotion.ParallelTransitionWizard;
import com.syntacticsugar.vooga.authoring.level.IDataSelector;
import com.syntacticsugar.vooga.authoring.level.QueueBox;
import com.syntacticsugar.vooga.authoring.objectediting.IVisualElement;
import com.syntacticsugar.vooga.authoring.tooltips.ObjectTooltip;
import com.syntacticsugar.vooga.gameplayer.attribute.HealthAttribute;
import com.syntacticsugar.vooga.gameplayer.attribute.IAttribute;
import com.syntacticsugar.vooga.gameplayer.event.ICollisionEvent;
import com.syntacticsugar.vooga.gameplayer.event.implementations.HealthChangeEvent;
import com.syntacticsugar.vooga.gameplayer.objects.GameObjectType;
import com.syntacticsugar.vooga.xml.data.ObjectData;

import javafx.animation.Animation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tooltip;

public class TowerView implements IVisualElement, IDataSelector<ObjectData> {
	
	private ListView<Node> myTowerView;
	private ObservableList<Node> myObservable;
	private HashMap<Node, ObjectData> myMap;
	private Object selectedItem;

	public TowerView() {
		myTowerView = new ListView<Node>();
		myObservable = FXCollections.observableArrayList();
		myTowerView.setItems(myObservable);
		myTowerView.setOrientation(Orientation.VERTICAL);
		myTowerView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		myMap = new HashMap<Node, ObjectData>();

		//TODO: REMOVE (blank initialization)
		testCreatedObjectDataList();
	}

	// test method
	private void testCreatedObjectDataList() {
		for (int i = 1; i < 3; i++) {
			ObjectData objToAdd = new ObjectData();
			objToAdd.setImagePath(String.format(String.format("tower_%d.png", i)));
			objToAdd.setType(GameObjectType.TOWER);
			List<IAttribute> attributeList = new ArrayList<IAttribute>();
			attributeList.add(new HealthAttribute(i * 100.0));
			objToAdd.setAttributes(attributeList);
			Map<GameObjectType, Collection<ICollisionEvent>> eventMap = new HashMap<GameObjectType, Collection<ICollisionEvent>>();
			List<ICollisionEvent> eventList = new ArrayList<ICollisionEvent>();
			ICollisionEvent eventToAdd = new HealthChangeEvent(-i * 10);
			eventList.add(eventToAdd);
			eventMap.put(GameObjectType.ENEMY, eventList);
			objToAdd.setCollisionMap(eventMap);
			addData(objToAdd);
		}
	}

	@Override
	public void addData(ObjectData data) {
		Node newTower = createQueueBoxFromObjData(data);
		newTower.setOnMouseClicked(e -> selectedItem = newTower);
		myMap.put(newTower, data);
		Tooltip.install(newTower, new ObjectTooltip(myMap.get(newTower)));
		myObservable.add(newTower);
	}
	
	// Used when we initiate a save game in the authoring environment
	@Override
	public Collection<ObjectData> getData() {
		return myMap.values();
	}

	@Override
	public ObjectData getSelectedData() {
		System.out.println(myMap.get(myTowerView.getSelectionModel().getSelectedItem()));
		return myMap.get(myTowerView.getSelectionModel().getSelectedItem());
	}

	@Override
	public void removeSelectedData() {
		if (selectedItem != null) {
		     Animation fade = FadeTransitionWizard
						     	.fadeOut((Node) selectedItem, 
						     			FluidGlassBall.getFadeDuration(),
										FluidGlassBall.getFadeOpacityStart(),
										FluidGlassBall.getFadeOpacityEnd(),
										FluidGlassBall.getFadeCycleCount());
		    fade.setOnFinished(toExecuteOnFinished -> removeObjectFromList_BAREBONE());
		    fade.play();
		}
	}
	
	@Override
	public void clearData() {
		Animation towerClear = ParallelTransitionWizard
								.parallelize(convertNodeListToAnimList());
		towerClear.setOnFinished(toExecuteOnFinished->clearAll_BAREBONE());
		towerClear.play();
	}
	
	@Override
	public Node getView() {
		return myTowerView;
	}
	
	
	// *********************************************//
	
	public Node createQueueBoxFromObjData(ObjectData obj) {
		QueueBox queueBox = new QueueBox(obj);
		return queueBox.getView();
	}

	private void removeObjectFromList_BAREBONE() {
		myObservable.remove(selectedItem);
		myMap.remove(selectedItem);
	}

	private List<Animation> convertNodeListToAnimList() {
		List<Animation> animationList = new ArrayList<Animation>();
		for(Node node:myObservable){
			Animation nodeAnim = FadeTransitionWizard.fadeOut(node, 
									FluidGlassBall.getFadeDuration(),
									FluidGlassBall.getFadeOpacityStart(),
									FluidGlassBall.getFadeOpacityEnd(),
									FluidGlassBall.getFadeCycleCount());
			animationList.add(nodeAnim);
		}
		return animationList;
	}

	private void clearAll_BAREBONE() {
		myObservable.clear();
		myMap.clear();
	}

}