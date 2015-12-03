package com.syntacticsugar.vooga.gameplayer.universe;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syntacticsugar.vooga.gameplayer.attribute.HealthAttribute;
import com.syntacticsugar.vooga.gameplayer.attribute.IAttribute;
import com.syntacticsugar.vooga.gameplayer.conditions.IGameCondition;
import com.syntacticsugar.vooga.gameplayer.conditions.PlayerDeathCondition;
import com.syntacticsugar.vooga.gameplayer.event.ICollisionEvent;
import com.syntacticsugar.vooga.gameplayer.event.IGameEvent;
import com.syntacticsugar.vooga.gameplayer.event.implementations.HealthChangeEvent;
import com.syntacticsugar.vooga.gameplayer.objects.GameObject;
import com.syntacticsugar.vooga.gameplayer.objects.GameObjectType;
import com.syntacticsugar.vooga.gameplayer.objects.IGameObject;
import com.syntacticsugar.vooga.gameplayer.universe.map.GameMap;
import com.syntacticsugar.vooga.gameplayer.universe.map.IGameMap;
import com.syntacticsugar.vooga.gameplayer.view.IViewAdder;
import com.syntacticsugar.vooga.gameplayer.view.IViewRemover;
import com.syntacticsugar.vooga.gameplayer.universe.spawner.ISpawner;
import com.syntacticsugar.vooga.gameplayer.universe.spawner.Spawner;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import xml.MapDataXML;
import xml.ObjectDataXML;
import xml.data.MapData;
import xml.data.ObjectData;
import xml.data.SpawnerData;
import xml.data.TowerData;
import xml.data.UniverseData;

public class GameUniverse implements IGameUniverse {

	private Collection<IGameObject> myPlayers;
	private Collection<IGameObject> myGameObjects;
	private SpawnYard mySpawnYard;
	private GraveYard myGraveYard;
	private List<IGameCondition> myConditions;
	private Collection<IGameObject> myTowers;
	private ISpawner mySpawner;
	private IGameMap myGameMap;
	private Collection<KeyCode> myCurrentInput;
	
	private IEventPoster myPoster;

	public GameUniverse(UniverseData data) {
		
		myPlayers = new ArrayList<IGameObject>();
		myGameObjects = new ArrayList<IGameObject>();
//		MapDataXML xml = new MapDataXML();
//		FileChooser fileChooser = new FileChooser();
//		fileChooser.getExtensionFilters().add(new ExtensionFilter("XML Files", "*.xml"));
//		fileChooser.setTitle("Choose Map XML");
//		File selectedFile = fileChooser.showOpenDialog(new Stage());
////		if (selectedFile != null) {
////			data = xml.loadFromFile(selectedFile);
////		}
		myGameMap = new GameMap(data.getMap());
		mySpawner = new Spawner(data.getSpawns().getWaves(), this);
		Collection<ObjectData> towerdata = data.getTowers().getTowers();
		for (ObjectData d: towerdata) {
			myTowers.add(new GameObject(d));
		}
		myGraveYard = new GraveYard(this);
		mySpawnYard = new SpawnYard(this);
		myConditions = new ArrayList<IGameCondition>();
		myConditions.add(new PlayerDeathCondition());
		myCurrentInput = new ArrayList<KeyCode>();
		myTowers = new ArrayList<IGameObject>();
		testTower();
	}
	
	private void testTower(){
		String imgPath = "tower_1.png";
		ObjectData towerData = new ObjectData();
		towerData.setImagePath(imgPath);
		Collection<IAttribute> towerAttributes = new ArrayList<IAttribute>();
		towerAttributes.add(new HealthAttribute(30));
		// towerAttributes.add(new AIMovementAttribute(3));
		Map<GameObjectType, Collection<ICollisionEvent>> collisions = new HashMap<GameObjectType, Collection<ICollisionEvent>>();
		Collection<ICollisionEvent> towerEvents = new ArrayList<ICollisionEvent>();
		towerEvents.add(new HealthChangeEvent(-10));
		towerData.setType(GameObjectType.TOWER);
		towerData.setImagePath(imgPath);
		towerData.setAttributes(towerAttributes);
		towerData.setCollisionMap(collisions);
		towerData.setWidth(100);
		towerData.setHeight(100);
		IGameObject tower = new GameObject(towerData);
		myTowers.add(tower);
		
		String imgPath1 = "tower_4.png";
		ObjectData towerData2 = new ObjectData();
		towerData.setImagePath(imgPath1);
		Collection<IAttribute> towerAttributes2 = new ArrayList<IAttribute>();
		towerAttributes2.add(new HealthAttribute(30));
		// towerAttributes.add(new AIMovementAttribute(3));
		Map<GameObjectType, Collection<ICollisionEvent>> collisions2 = new HashMap<GameObjectType, Collection<ICollisionEvent>>();
		Collection<ICollisionEvent> towerEvents2 = new ArrayList<ICollisionEvent>();
		towerEvents2.add(new HealthChangeEvent(-10));
		towerData2.setType(GameObjectType.TOWER);
		towerData2.setImagePath(imgPath);
		towerData2.setAttributes(towerAttributes2);
		towerData2.setCollisionMap(collisions2);
		towerData2.setWidth(100);
		towerData2.setHeight(100);
		IGameObject tower2 = new GameObject(towerData);
		myTowers.add(tower2);
	}

	@Override
	public void addPlayer(IGameObject player) {
		if (player.getType().equals(GameObjectType.PLAYER)) {
			myPlayers.add(player);
		}
	}

	@Override
	public Collection<IGameObject> getPlayers() {
		return Collections.unmodifiableCollection(myPlayers);
	}

	@Override
	public Collection<IGameObject> getGameObjects() {
		return Collections.unmodifiableCollection(myGameObjects);
	}

	@Override
	public void addGameObject(IGameObject toAdd) {
		System.out.println("ADD");
		myGameObjects.add(toAdd);
	}

	@Override
	public void receiveKeyPress(KeyCode code) {
		if (!myCurrentInput.contains(code)) {
			myCurrentInput.add(code);

		}
	}

	@Override
	public void receiveKeyRelease(KeyCode code) {

		if (myCurrentInput.contains(code)) {
			myCurrentInput.remove(code);

		}
	}

	@Override
	public void receiveMouseEvent(MouseEvent mouseEvent) {
		// TODO handle mouse input??????
	}

	@Override
	public Collection<KeyCode> getCurrentKeyInput() {
		return Collections.unmodifiableCollection(myCurrentInput);
	}

	@Override
	public void addToSpawnYard(IGameObject toAdd) {
		mySpawnYard.addToYard(toAdd);
	}

	@Override
	public void addToGraveYard(IGameObject toRemove) {
		myGraveYard.addToYard(toRemove);
	}

	@Override
	public void removeFromUniverse(IViewRemover remover) {
		myGraveYard.alterUniverse(remover);

	}

	@Override
	public void addToUniverse(IViewAdder adder) {
		mySpawnYard.alterUniverse(adder);
	}

	@Override
	public IGameMap getMap() {
		return this.myGameMap;
	}

	@Override
	public Collection<IGameCondition> getConditions() {
		return Collections.unmodifiableCollection(myConditions);
	}

	@Override
	public void removeGameObject(IGameObject obj) {
		System.out.println("HERE");
		myGameObjects.remove(obj);
	}

	@Override
	public SpawnYard getSpawnYard() {
		return mySpawnYard;
	}

	@Override
	public GraveYard getGraveYard() {
		return myGraveYard;
	}

	@Override
	public ISpawner getSpawner() {
		return mySpawner;
	}

	@Override
	public void postEvent(IGameEvent event) {
		myPoster.postEvent(event);
	}

	@Override
	public void saveGame() {
		SpawnerData spawn = mySpawner.saveGame();
		TowerData towers = saveTowers();
		MapData map = new MapData(myGameMap);
		UniverseData data = new UniverseData(spawn, towers, map);
		
	}
	
	private TowerData saveTowers() {
		Collection<ObjectData> data = new ArrayList<>();
		for (IGameObject o: myTowers) {
			data.add(new ObjectData(o));
		}
		return new TowerData(data);
	}

	@Override
	public Collection<ObjectData> getAvailableTowers() {
		List<ObjectData> towerData = new ArrayList<ObjectData>();
		for(IGameObject tower: myTowers){
			towerData.add(new ObjectData(tower));
		}
		return towerData;
	}
}
