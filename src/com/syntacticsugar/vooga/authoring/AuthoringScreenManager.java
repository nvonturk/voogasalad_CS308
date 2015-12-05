package com.syntacticsugar.vooga.authoring;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.syntacticsugar.vooga.authoring.level.LevelTabManager;
import com.syntacticsugar.vooga.authoring.library.ObjectLibraryManager;
import com.syntacticsugar.vooga.authoring.objectediting.ObjectEditor;
import com.syntacticsugar.vooga.gameplayer.objects.GameObjectType;
import com.syntacticsugar.vooga.menu.IVoogaApp;
import com.syntacticsugar.vooga.util.ResourceManager;
import com.syntacticsugar.vooga.xml.XMLHandler;
import com.syntacticsugar.vooga.xml.data.GameData;
import com.syntacticsugar.vooga.xml.data.GlobalSettings;
import com.syntacticsugar.vooga.xml.data.LevelSettings;
import com.syntacticsugar.vooga.xml.data.MapData;
import com.syntacticsugar.vooga.xml.data.ObjectData;
import com.syntacticsugar.vooga.xml.data.SpawnerData;
import com.syntacticsugar.vooga.xml.data.TowerData;
import com.syntacticsugar.vooga.xml.data.UniverseData;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AuthoringScreenManager implements Observer, IVoogaApp {

	private BorderPane myWindow;
	private GridPane myWindowGrid;

	private LevelTabManager myLevelEditor;
	private ObjectLibraryManager myObjectLibraryManager;
	private Stage myStage;
	private Scene myScene;
	// private ObjectLibrary myObjectLibrary;
	private ObjectEditor myObjectEditor;

	public AuthoringScreenManager() {
		myLevelEditor = new LevelTabManager();
		myObjectLibraryManager = new ObjectLibraryManager();
		myObjectEditor = new ObjectEditor(() -> myObjectLibraryManager.refresh());
		initWindow();
	}

	// private void initObjectLibrary() {
	// myObjectLibrary = new ObjectLibrary(null);
	// myObjectManager = new AuthoringSidePane(null);
	// }

	private void initWindow() {
		myWindow = new BorderPane();
		buildMenuBar();

		myWindowGrid = new GridPane();
		// myWindowGrid.setGridLinesVisible(true);
		addGridConstraints();

		setUpObserver();
		myWindowGrid.add(myLevelEditor.getTabPane(), 0, 0, 1, 2);
		myWindowGrid.add(myObjectLibraryManager.getTabPane(), 1, 0, 1, 1);
		myWindowGrid.add(myObjectEditor.getView(), 1, 1, 1, 1);
		myWindow.setCenter(myWindowGrid);

		myScene = new Scene(myWindow);
		myScene.setOnKeyPressed(e -> handleKeyPress(e));
		myStage = new Stage();
		myStage.setScene(myScene);
		// myStage.setMaximized(true);
		myStage.show();
	}

	private void setUpObserver() {
		for (int i = 0; i < myLevelEditor.getLevels().size(); i++) {
			myLevelEditor.getLevels().get(i).getTowerControls().addObserver(this);
			myLevelEditor.getLevels().get(i).getSpawnerControls().addObserver(this);

		}

		for (int i = 0; i < myObjectLibraryManager.getLibraries().size(); i++) {
			myObjectLibraryManager.getLibraries().get(i).addObserver(this);

		}
	}

	private void handleKeyPress(KeyEvent e) {
		if (e.isControlDown() && e.getCode().equals(KeyCode.N)) {
			addLevelRefresh();
		}
		if (e.getCode().equals(KeyCode.S)) {
			ObjectData data = new ObjectData();
			data.setImagePath("enemy_moster_1.png");
			data.setType(GameObjectType.TOWER);
			myObjectEditor.displayData(data);
		}
	}

	public void minimize() {
		myStage.hide();
	}

	private void addLevelRefresh() {
		myLevelEditor.addNewLevel();
		setUpObserver();
	}

	private void buildMenuBar() {
		MenuBar menuBar = new MenuBar();
		// file menu
		Menu file = new Menu();
		file.setText("File");
		MenuItem newLevel = new MenuItem();
		newLevel.setText("New Level");
		// newLevel.setOnAction(e -> myLevelEditor.addNewLevel());
		newLevel.setOnAction(e -> addLevelRefresh());

		MenuItem loadMap = new MenuItem();
		loadMap.setText("Load map");
		loadMap.setOnAction(e -> loadMap());

		MenuItem saveMap = new MenuItem();
		saveMap.setText("Save map");
		saveMap.setOnAction(e -> saveMap());

		MenuItem loadData = new MenuItem();
		loadData.setText("Load ObjectData");
		loadData.setOnAction(e -> loadData());

		MenuItem saveGame = new MenuItem();
		saveGame.setText("Save Game");
		saveGame.setOnAction(e -> saveGame());

		file.getItems().addAll(newLevel, loadMap, saveMap, loadData, saveGame);

		// menu menu
		// Menu menu = new Menu();
		// menu.setText("Menu");
		// return to main menu
		// return to authoring menu
		// MenuItem authoringMenu = new MenuItem();
		// authoringMenu.setText("Authoring Menu");
		// authoringMenu.setOnAction(e ->
		// sceneManager.launchAuthoringMenuFromAuthoring());
		// menu.getItems().addAll(mainMenu, authoringMenu);

		menuBar.getMenus().addAll(file);
		myWindow.setTop(menuBar);
	}

	private void saveGame() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Game File");
		File selectedFile = fileChooser.showSaveDialog(new Stage());
		if (selectedFile != null) {
			Map<Integer, SpawnerData> spawnerSave = myLevelEditor.getSpawnerQueues();
			Map<Integer, TowerData> towerSave = myLevelEditor.getTowerLists();
			Map<Integer, MapData> mapSave = myLevelEditor.getMapData();
			Map<Integer, LevelSettings> conditionsSave = myLevelEditor.getConditionsList();

			ArrayList<UniverseData> levelData = new ArrayList<UniverseData>();
			if ((spawnerSave.size() == towerSave.size()) && (mapSave.size() == conditionsSave.size())) {
				for (int i : towerSave.keySet()) {
					UniverseData universe = new UniverseData(spawnerSave.get(i), towerSave.get(i), mapSave.get(i),
							conditionsSave.get(i));
//					UniverseData universe = new UniverseData(null, towerSave.get(i), null,
//							null);
					levelData.add(universe);
				}
			}
			// need to change later with global settings
			GameData game = new GameData(levelData, new GlobalSettings());
			XMLHandler<GameData> xml = new XMLHandler<>();
//			XMLHandler<TowerData> xml = new XMLHandler<>();
//			System.out.println(towerSave.get(49));
//			xml.write(towerSave.get(49), selectedFile);
			xml.write(game, selectedFile);
//			long i = selectedFile.length();
		}

	}

	private void loadData() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
		fileChooser.setInitialDirectory(new File(ResourceManager.getString("data")));
		File selectedFile = fileChooser.showOpenDialog(new Stage());
		if (selectedFile != null) {
			XMLHandler<ObjectData> xml = new XMLHandler<>();
			ObjectData toload = xml.read(selectedFile);
			myObjectEditor.displayData(toload);
		}
	}

	private void loadMap() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
		File selectedFile = fileChooser.showOpenDialog(new Stage());
		if (selectedFile != null) {
			XMLHandler<MapData> xml = new XMLHandler<>();
			MapData toload = xml.read(selectedFile);
			myLevelEditor.loadMap(toload);
		}
	}

	private void saveMap() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Resource File");
		File selectedFile = fileChooser.showSaveDialog(new Stage());
		if (selectedFile != null) {
			XMLHandler<MapData> xml = new XMLHandler<>();
			MapData toSave = myLevelEditor.getIndividualMapData();
			xml.write(toSave, selectedFile);
		}
	}

	private void addGridConstraints() {
		addColumnConstraints();
		addRowConstraints();
	}

	private void addColumnConstraints() {
		ColumnConstraints c1 = new ColumnConstraints();
		c1.setPercentWidth(75.0);
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setPercentWidth(25.0);
		myWindowGrid.getColumnConstraints().addAll(c1, c2);
	}

	private void addRowConstraints() {
		RowConstraints r1 = new RowConstraints();
		r1.setPercentHeight(50);
		RowConstraints r2 = new RowConstraints();
		r2.setPercentHeight(50);
		myWindowGrid.getRowConstraints().addAll(r1, r2);
	}

	@Override
	public void update(Observable o, Object arg) {
		myObjectEditor.setTypeChooserViability(false);
		myObjectEditor.setUpdateButtonViability(false);
		myObjectEditor.displayData((ObjectData) arg);

	}

	@Override
	public void assignCloseHandler(EventHandler<WindowEvent> onclose) {
		myStage.setOnCloseRequest(onclose);
	}

}