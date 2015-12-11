// This entire file is part of my masterpiece.
// Henry Yuen
package com.syntacticsugar.vooga.authoring;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import com.syntacticsugar.vooga.authoring.level.LevelTabManager;
import com.syntacticsugar.vooga.authoring.library.ObjectLibraryManager;
import com.syntacticsugar.vooga.authoring.objectediting.IDataClipboard;
import com.syntacticsugar.vooga.authoring.objectediting.ObjectEditor;
import com.syntacticsugar.vooga.gameplayer.objects.GameObjectType;
import com.syntacticsugar.vooga.menu.IVoogaApp;
import com.syntacticsugar.vooga.util.ResourceManager;
import com.syntacticsugar.vooga.util.gui.factory.AlertBoxFactory;
import com.syntacticsugar.vooga.util.gui.factory.MenuItemFactory;
import com.syntacticsugar.vooga.util.gui.factory.StringInputBoxFactory;
import com.syntacticsugar.vooga.util.properties.PropertiesManager;
import com.syntacticsugar.vooga.util.simplefilechooser.SimpleFileChooser;
import com.syntacticsugar.vooga.xml.XMLHandler;
import com.syntacticsugar.vooga.xml.data.GameData;
import com.syntacticsugar.vooga.xml.data.GlobalSettings;
import com.syntacticsugar.vooga.xml.data.MapData;
import com.syntacticsugar.vooga.xml.data.ObjectData;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AuthoringScreenManager implements Observer, IVoogaApp {

	private BorderPane myWindow;
	private GridPane myWindowGrid;
	private LevelTabManager myLevelEditor;
	private ObjectLibraryManager myObjectLibraryManager;
	private Stage myStage;
	private Scene myScene;
	private ObjectEditor myObjectEditor;
	private MenuItemFactory menuItemFactory;
	private IDataClipboard iObject;
	private PropertiesManager propManager = new PropertiesManager("com/syntacticsugar/vooga/resources/View");

	private double COL_CONSTRAINT_MAJOR = propManager.getDoubleProperty("COL_CONSTRAINT_MAJOR");
	private double COL_CONSTRAINT_MINOR = propManager.getDoubleProperty("COL_CONSTRAINT_MINOR");
	private double ROW_CONSTRAINT_MAJOR = propManager.getDoubleProperty("ROW_CONSTRAINT_MAJOR");
	private double ROW_CONSTRAINT_MINOR = propManager.getDoubleProperty("ROW_CONSTRAINT_MINOR");

	public AuthoringScreenManager() {
		initKeyParameters();
		initWindow();
	}

	private void initKeyParameters() {
		myObjectEditor = new ObjectEditor(() -> myObjectLibraryManager.refresh());
		iObject = myObjectEditor;
		myLevelEditor = new LevelTabManager(iObject);
		myObjectLibraryManager = new ObjectLibraryManager(myLevelEditor);
		menuItemFactory = new MenuItemFactory();
	}

	private void initWindow() {
		myWindow = new BorderPane();
		buildMenuBar();

		myWindowGrid = new GridPane();
		addGridConstraints();

		setUpMyObserver();
		tryRunObservingLink();
		setUpWindow();
		myScene = new Scene(myWindow);
		myScene.getStylesheets().add("/com/syntacticsugar/vooga/authoring/css/default.css");
		myScene.setOnKeyPressed(e -> handleKeyPress(e));
		myStage = new Stage();
		myStage.setScene(myScene);
		// myStage.setMaximized(true);
		myStage.show();
	}

	private void tryRunObservingLink() {
		try{
			myLevelEditor.getCurrentLevelEditor();
			linkObserverAndObservableObjects();
		} catch (NullPointerException e){
			AlertBoxFactory.createObject("nullLevelEditor");
			myLevelEditor = new LevelTabManager(iObject);
			linkObserverAndObservableObjects();
		}
	}

	private void setUpWindow() {
		myWindowGrid.add(myLevelEditor.getTabPane(), 0, 0, 1, 2);
		myWindowGrid.add(myObjectLibraryManager.getView(), 1, 0, 1, 1);
		myWindowGrid.add(myObjectEditor.getView(), 1, 1, 1, 1);
		myWindow.setCenter(myWindowGrid);
	}

	private void setUpMyObserver() {
		for (int i = 0; i < myLevelEditor.getLevels().size(); i++) {
			myLevelEditor.getLevels().get(i).getTowerControls().addObserver(this);
			myLevelEditor.getLevels().get(i).getSpawnerControls().addObserver(this);
		}
		for (int i = 0; i < myObjectLibraryManager.getLibraries().size(); i++) {
			myObjectLibraryManager.getLibraries().get(i).addObserver(this);
		}
	}

	private void linkObserverAndObservableObjects() {
		Tab pickedLevelTab = myLevelEditor.getTabPane().getSelectionModel().getSelectedItem();
		myObjectEditor.addObserver(
				myLevelEditor.getCurrentLevelEditor().get(pickedLevelTab).getTowerManager().getTowerView());
		myObjectEditor.addObserver(
				myLevelEditor.getCurrentLevelEditor().get(pickedLevelTab).getSpawnerManager().getCurrentView());
	}

	private void handleKeyPress(KeyEvent e) {
		if (e.isControlDown() && e.getCode().equals(KeyCode.N)) {
			addLevelRefresh();
		}
		if (e.getCode().equals(KeyCode.S)) {
			ObjectData data = new ObjectData();
			data.setImagePath(ResourceManager.getString("keyCodeDefaultPicture"));
			data.setType(GameObjectType.TOWER);
			myObjectEditor.displayData(data);
		}
	}

	public void minimize() {
		myStage.hide();
	}

	private void addLevelRefresh() {
		myLevelEditor.addNewLevel();
		setUpMyObserver();
	}

	private void buildMenuBar() {
		MenuBar menuBar = new MenuBar();
		// file menu
		Menu file = new Menu();
		file.setText("File");
		file.getItems().addAll(		
				menuItemFactory.buildMenuItem(ResourceManager.getString("new_level"),		e -> addLevelRefresh()),
				menuItemFactory.buildMenuItem(ResourceManager.getString("load_map"),		e -> loadMap()),
				menuItemFactory.buildMenuItem(ResourceManager.getString("save_map"), 		e -> saveMap()),
				menuItemFactory.buildMenuItem(ResourceManager.getString("load_objectdata"), e -> loadData()),
				menuItemFactory.buildMenuItem(ResourceManager.getString("load_game"), 		e -> loadData()),
				menuItemFactory.buildMenuItem(ResourceManager.getString("save_game"), 		e -> saveGame())
				);
		menuBar.getMenus().addAll(file);
		myWindow.setTop(menuBar);
	}

	private void saveGame() {
		GameData game = new GameData(myLevelEditor.getAllUniverseData(), new GlobalSettings());
		// File f = SimpleFileChooser.saveGame(game, myStage);
		StringInputBoxFactory msg = new StringInputBoxFactory(ResourceManager.getString("enter_filename") + " ");
		String fileName = tryObtainSavedGameName(msg);
		System.out.println(ResourceManager.getString("filename") + " " + fileName);
		String directory = ResourceManager.getString("game_data");
		System.out.println(directory);
		String path = directory + File.separator + fileName;
		// Use relative path for Unix systems
		File f = new File(path);
		// Works for both Windows and Linux
		game.setName(f.getName());
		XMLHandler<GameData> xml = new XMLHandler<>();
		xml.write(game, f);
	}

	private String tryObtainSavedGameName(StringInputBoxFactory msg) {
		String fileName = null;
		try{
			if(!msg.isValidValue()){ 
				throw new IllegalArgumentException();
			} else{
				fileName = msg.getValue();
			}
		} catch(IllegalArgumentException e){
			AlertBoxFactory.createObject(ResourceManager.getString("badInputSaveGameMessage"));
			fileName = ResourceManager.getString("badSaveGameFileName");
		}
		return fileName;
	}

	private void loadData() {
		GameData map = SimpleFileChooser.loadGame(myStage);
		myObjectLibraryManager.loadLibraries();
		myLevelEditor.addNewLevelsFromData(map.getUniverses());
	}

	private void saveMap() {
		MapData toSave = myLevelEditor.getIndividualMapData();
		try { 
			SimpleFileChooser.saveMap(toSave, myStage);
		}
		catch(NullPointerException npe){
			AlertBoxFactory.createObject("nullMapData");
		}
		catch(Exception e){
			return;
		}
	}

	private void loadMap() {
		try {
			MapData loaded = SimpleFileChooser.loadMap(myStage);
			myLevelEditor.loadMap(loaded);
		} catch (NullPointerException e) {
			AlertBoxFactory.createObject(ResourceManager.getString("load_valid_map"));
		}
	}

	private void addGridConstraints() {
		addColumnConstraints();
		addRowConstraints();
	}

	private void addColumnConstraints() {
		ColumnConstraints c1 = new ColumnConstraints();
		c1.setPercentWidth(COL_CONSTRAINT_MAJOR);
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setPercentWidth(COL_CONSTRAINT_MINOR);
		myWindowGrid.getColumnConstraints().addAll(c1, c2);
	}

	private void addRowConstraints() {
		RowConstraints r1 = new RowConstraints();
		r1.setPercentHeight(ROW_CONSTRAINT_MAJOR);
		RowConstraints r2 = new RowConstraints();
		r2.setPercentHeight(ROW_CONSTRAINT_MINOR);
		myWindowGrid.getRowConstraints().addAll(r1, r2);
	}

	@Override
	public void update(Observable o, Object arg) {
		myObjectEditor.setTypeChooserViability(false);
		myObjectEditor.setUpdateButtonVisibility(true);
		myObjectEditor.displayData((ObjectData) arg);
	}

	@Override
	public void assignCloseHandler(EventHandler<WindowEvent> onclose) {
		myStage.setOnCloseRequest(onclose);
	}
}