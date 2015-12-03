package com.syntacticsugar.vooga.menu;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.syntacticsugar.vooga.authoring.AuthoringScreenManager;
import com.syntacticsugar.vooga.gameplayer.attribute.HealthAttribute;
import com.syntacticsugar.vooga.gameplayer.attribute.IAttribute;
import com.syntacticsugar.vooga.gameplayer.attribute.ScoreAttribute;
import com.syntacticsugar.vooga.gameplayer.event.ICollisionEvent;
import com.syntacticsugar.vooga.gameplayer.event.implementations.HealthChangeEvent;
import com.syntacticsugar.vooga.gameplayer.manager.GameManager;
import com.syntacticsugar.vooga.gameplayer.objects.GameObjectType;
import com.syntacticsugar.vooga.xml.XMLHandler;
import com.syntacticsugar.vooga.xml.data.GameData;
import com.syntacticsugar.vooga.xml.data.GlobalSettings;
import com.syntacticsugar.vooga.xml.data.LevelSettings;
import com.syntacticsugar.vooga.xml.data.MapData;
import com.syntacticsugar.vooga.xml.data.ObjectData;
import com.syntacticsugar.vooga.xml.data.SpawnerData;
import com.syntacticsugar.vooga.xml.data.TowerData;
import com.syntacticsugar.vooga.xml.data.UniverseData;
import com.syntacticsugar.vooga.xml.data.WaveData;

import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class SceneManager {
	private final double WIDTH = 600.0;
	private final double HEIGHT = 600.0;
	private final double GAME_SIZE = 600.0;
	private Stage myStage;
	
	private final double FRAME_LENGTH = 1 / 60.0;
	private Scene gameScene;
	private GameManager myGameManager;
	private AuthoringScreenManager myAuthoringEnv;
	
	public SceneManager(Stage stage) {
		// Windows 10 - Bug Fix for JavaFX
		System.setProperty("glass.accessible.force", "false");
		myStage = stage;
		myStage.setTitle("Vooga Salad");
		launchFirstMenu();
	}
	
	private void viewScene(AbstractGameMenu screen) {
		Scene scene = new Scene(screen, WIDTH, HEIGHT);
		myStage.setScene(scene);
		myStage.show();
	}
	
	public void launchFirstMenu() {
		AbstractGameMenu screen = new FirstGameMenu(this, WIDTH, HEIGHT, "Vooga Salad");
		viewScene(screen);
	}
	
	public void launchAuthoringMenu() {	
		AbstractGameMenu screen = new AuthoringGameMenu(this, WIDTH, HEIGHT, "Vooga Salad");
		viewScene(screen);
	}
	
	public void launchEngineMenu() {
		AbstractGameMenu screen = new EngineGameMenu(this, WIDTH, HEIGHT, "Vooga Salad");
		viewScene(screen);
	}
	
	public void launchEnginePauseMenu() {
		// instance of gameScene is stored upon engine launch
		AbstractGameMenu screen = new EnginePauseMenu(this, WIDTH, HEIGHT, "Vooga Salad");
		viewScene(screen);
	}
	
	public void launchAuthoringMenuFromAuthoring() {
		myAuthoringEnv.minimize();
		launchAuthoringMenu();
	}
	
	public void launchFirstMenuFromAuthoring() {
//		myAuthoringEnv.minimize();
		launchFirstMenu();
	}
	
	public void launchNewEditor() {
		myStage.hide();
		myAuthoringEnv = new AuthoringScreenManager(e -> launchFirstMenuFromAuthoring());
	}
	
	public void launchLoadEditor() {
		myStage.hide();
		// TODO load from XML here or within GameManager?
		myAuthoringEnv = new AuthoringScreenManager(e -> launchFirstMenuFromAuthoring());
	}
	
	public void launchNewEngine() {
		GameData data = makeEmptyData();
		myGameManager = new GameManager(GAME_SIZE, data);
		myGameManager.setManager(this);
		gameScene = new Scene(myGameManager.getGameView());
		myGameManager.initializeAnimation(FRAME_LENGTH);
		gameScene.setOnKeyPressed(e -> myGameManager.receiveKeyPressed(e.getCode()));
		gameScene.setOnKeyReleased(e -> myGameManager.receiveKeyReleased(e.getCode()));
		myStage.setScene(gameScene);
	}
	
	public void launchLoadEngine() {
		// TODO modify to do direct load instead of launch
		
		GameData data = null;
		XMLHandler<GameData> xml = new XMLHandler<>();
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("XML Files", "*.xml"));
		fileChooser.setTitle("Choose Map XML");
		File selectedFile = fileChooser.showOpenDialog(new Stage());
		if (selectedFile != null) {
			data = xml.read(selectedFile);
		}
		myGameManager = new GameManager(GAME_SIZE, data);
		myGameManager.setManager(this);
		gameScene = new Scene(myGameManager.getGameView(), GAME_SIZE, GAME_SIZE);
		myGameManager.initializeAnimation(FRAME_LENGTH);
		gameScene.setOnKeyPressed(e -> myGameManager.receiveKeyPressed(e.getCode()));
		gameScene.setOnKeyReleased(e -> myGameManager.receiveKeyReleased(e.getCode()));
		myStage.setScene(gameScene);
	}
	
	private GameData makeEmptyData() {
		
		Collection<ObjectData> odata = new ArrayList<>();
		
		
		String enemyPath = "enemy_monster_1.png";
		ObjectData enemyData = new ObjectData();
		Collection<IAttribute> enemyAttributes = new ArrayList<IAttribute>();
		enemyAttributes.add(new HealthAttribute(30));
		enemyAttributes.add(new ScoreAttribute(30));
//		enemyAttributes.add(new AIMovementAttribute(3));
		Map<GameObjectType, Collection<ICollisionEvent>> collisions = new HashMap<GameObjectType, Collection<ICollisionEvent>>();
		Collection<ICollisionEvent> enemyEvents = new ArrayList<ICollisionEvent>();
		enemyEvents.add(new HealthChangeEvent(-10));
		collisions.put(GameObjectType.PLAYER, enemyEvents);
		enemyData.setType(GameObjectType.ENEMY);
		enemyData.setSpawnPoint(250, 150);
		enemyData.setWidth(100);
		enemyData.setHeight(100);
		enemyData.setImagePath(enemyPath);
		enemyData.setAttributes(enemyAttributes);
		enemyData.setCollisionMap(collisions);
		
		ObjectData enemyData2 = new ObjectData();
		Collection<IAttribute> enemyAttributes2 = new ArrayList<IAttribute>();
		enemyAttributes2.add(new HealthAttribute(30));
		enemyAttributes2.add(new ScoreAttribute(40));
//		enemyAttributes.add(new AIMovementAttribute(3));
		Map<GameObjectType, Collection<ICollisionEvent>> collisions2 = new HashMap<GameObjectType, Collection<ICollisionEvent>>();
		Collection<ICollisionEvent> enemyEvents2 = new ArrayList<ICollisionEvent>();
		enemyEvents2.add(new HealthChangeEvent(-10));
		enemyData2.setType(GameObjectType.ENEMY);
		enemyData2.setSpawnPoint(350, 150);
		enemyData2.setWidth(100);
		enemyData2.setHeight(100);
		enemyData2.setImagePath(enemyPath);
		enemyData2.setAttributes(enemyAttributes2);
		enemyData2.setCollisionMap(collisions2);
		
		odata.add(enemyData);
		odata.add(enemyData2);
		WaveData wdata = new WaveData(odata);
		Collection<WaveData> sdata = new ArrayList<>();
		sdata.add(wdata);
		SpawnerData spawn = new SpawnerData(sdata);
		
		MapData map = new MapData(10, "gray.png");
		TowerData towers = new TowerData(new ArrayList<ObjectData>());
		LevelSettings lSetting = new LevelSettings(50);
		Collection<UniverseData> uni = new ArrayList<>();
		uni.add(new UniverseData(spawn, towers, map, lSetting));
		GlobalSettings settings = new GlobalSettings(1, 60);
		GameData data = new GameData(uni, settings);
		return data;
	}
	
	public void launchUnpauseEngine() {
		// should be slightly changed from loadEngine
		// TODO call myGameManager.unpause() or something like that
		myStage.setScene(gameScene);
	}
}
