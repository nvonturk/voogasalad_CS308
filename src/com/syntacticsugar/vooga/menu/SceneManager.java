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
import com.syntacticsugar.vooga.social.SocialCenter;
import com.syntacticsugar.vooga.util.properties.PropertiesManager;
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
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class SceneManager {

	private double WIDTH;
	private double HEIGHT;
	private double GAME_SIZE;
	private String TITLE; // = "Vooga Salad";
	private double FRAME_LENGTH;

	private PropertiesManager myPropertiesManager;
	private Stage myStage;

	public SceneManager(Stage stage) {
		this();
		// Windows 10 - Bug Fix for JavaFX
		System.setProperty("glass.accessible.force", "false");
		myStage = stage;
		myStage.setTitle(TITLE);
		launchFirstMenu();
	}

	private SceneManager() {
		myPropertiesManager = new PropertiesManager("com/syntacticsugar/vooga/resources/View");
		WIDTH = myPropertiesManager.getDoubleProperty("DefaultWidth");
		HEIGHT = myPropertiesManager.getDoubleProperty("DefaultHeight");
		GAME_SIZE = myPropertiesManager.getDoubleProperty("DefaultGameSize");
		TITLE = myPropertiesManager.getProperty("WindowTitle");
		FRAME_LENGTH = 1.0 / myPropertiesManager.getDoubleProperty("FrameLength");
	}

	private void viewScene(AbstractGameMenu screen) {
		Scene scene = new Scene(screen, WIDTH, HEIGHT);
		myStage.setScene(scene);
		myStage.show();
	}

	public void launchFirstMenu() {
		AbstractGameMenu screen = new FirstGameMenu(this, WIDTH, HEIGHT, TITLE);
		viewScene(screen);
	}

	public void launchAuthoringMenu() {
		AbstractGameMenu screen = new AuthoringGameMenu(this, WIDTH, HEIGHT, TITLE);
		viewScene(screen);
	}

	public void launchEngineMenu() {
		AbstractGameMenu screen = new EngineGameMenu(this, WIDTH, HEIGHT, TITLE);
		viewScene(screen);
	}

	public void launchSocialCenter() {
		myStage.hide();
		new SocialCenter(e -> launchFirstMenu());
	}

	public void launchNewEditor() {
		myStage.hide();
		new AuthoringScreenManager(e -> launchFirstMenu());
	}

	public void launchLoadEditor() {
		// TODO load from XML here or within GameManager?
		myStage.hide();
		new AuthoringScreenManager(e -> launchFirstMenu());
	}

	public void launchNewEngine() {
		GameData data = makeEmptyData();
		myStage.hide();
		new GameManager(e -> launchFirstMenu(), GAME_SIZE, data, FRAME_LENGTH);
	}

	public void launchLoadEngine() {
		// TODO modify to do direct load instead of launch
		GameData data = loadData();
		new GameManager(e -> launchFirstMenu(), GAME_SIZE, data, FRAME_LENGTH);
		myStage.hide();
	}

	private GameData loadData() {
		GameData data = null;
		XMLHandler<GameData> xml = new XMLHandler<>();
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("XML Files", "*.xml"));
		fileChooser.setTitle("Choose Map XML");
		File selectedFile = fileChooser.showOpenDialog(new Stage());
		if (selectedFile != null) {
			data = xml.read(selectedFile);
		}
		return data;
	}

	private GameData makeEmptyData() {

		Collection<ObjectData> odata = new ArrayList<>();
		String enemyPath = "enemy_monster_1.png";
		ObjectData enemyData = new ObjectData();
		Collection<IAttribute> enemyAttributes = new ArrayList<IAttribute>();
		enemyAttributes.add(new HealthAttribute(30.0));
		enemyAttributes.add(new ScoreAttribute(30));
		// enemyAttributes.add(new AIMovementAttribute(3));
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
		enemyAttributes2.add(new HealthAttribute(30.0));
		enemyAttributes2.add(new ScoreAttribute(40));
		// enemyAttributes.add(new AIMovementAttribute(3));
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

		MapData map = new MapData(10, "scenery_grass_2.png");

		ArrayList<ObjectData> towers = new ArrayList<>();
		String imgPath = "tower_1.png";
		ObjectData towerData = new ObjectData();
		towerData.setImagePath(imgPath);
		Collection<IAttribute> towerAttributes = new ArrayList<IAttribute>();
		towerAttributes.add(new HealthAttribute(30.0));
		// towerAttributes.add(new AIMovementAttribute(3));
		Map<GameObjectType, Collection<ICollisionEvent>> collisionst = new HashMap<GameObjectType, Collection<ICollisionEvent>>();
		Collection<ICollisionEvent> towerEvents = new ArrayList<ICollisionEvent>();
		towerEvents.add(new HealthChangeEvent(-10));
		towerData.setType(GameObjectType.ENEMY);
		towerData.setImagePath(imgPath);
		towerData.setAttributes(towerAttributes);
		towerData.setCollisionMap(collisionst);
		towerData.setWidth(100);
		towerData.setHeight(100);
		towers.add(towerData);

		String imgPath1 = "tower_4.png";
		ObjectData towerData2 = new ObjectData();
		towerData2.setImagePath(imgPath1);
		Collection<IAttribute> towerAttributes2 = new ArrayList<IAttribute>();
		towerAttributes2.add(new HealthAttribute(30.0));
		// towerAttributes.add(new AIMovementAttribute(3));
		Map<GameObjectType, Collection<ICollisionEvent>> collisionst2 = new HashMap<GameObjectType, Collection<ICollisionEvent>>();
		Collection<ICollisionEvent> towerEvents2 = new ArrayList<ICollisionEvent>();
		towerEvents2.add(new HealthChangeEvent(-10));
		towerData2.setType(GameObjectType.TOWER);
		towerData2.setImagePath(imgPath);
		towerData2.setAttributes(towerAttributes2);
		towerData2.setCollisionMap(collisionst2);
		towerData2.setWidth(100);
		towerData2.setHeight(100);
		towers.add(towerData2);

		TowerData td = new TowerData(towers);

		LevelSettings lSetting = new LevelSettings(50);
		Collection<UniverseData> uni = new ArrayList<>();
		uni.add(new UniverseData(spawn, td, map, lSetting));
		GlobalSettings settings = new GlobalSettings(1, 60);
		GameData data = new GameData(uni, settings);

		return data;
	}

}
