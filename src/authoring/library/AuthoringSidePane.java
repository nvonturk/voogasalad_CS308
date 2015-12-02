package authoring.library;

import java.io.File;

import com.syntacticsugar.vooga.gameplayer.attribute.IAttribute;
import com.syntacticsugar.vooga.gameplayer.event.ICollisionEvent;
import com.syntacticsugar.vooga.gameplayer.objects.GameObjectType;

import authoring.objectediting.ObjectEditor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.VBox;
import xml.data.ObjectData;

public class AuthoringSidePane {

	private VBox myEditorView;
	private TabPane myLibraryTabs;
	private ObjectEditor myLibraryEditor;
	private ObjectData myData;
	private ObjectLibrary myEnemyLibrary;
	private ObjectLibrary myTowerLibrary;
	private ObjectLibrary myPlayerLibrary;
	private ObjectLibrary myItemLibrary;

	public AuthoringSidePane(File GameDirectory) {
		myEditorView = new VBox();
		initializeLibraryTabs(GameDirectory);
		myLibraryTabs = new TabPane();
		populateTabPane();
		myLibraryTabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		myData = new ObjectData();
		myLibraryEditor = new ObjectEditor(GameObjectType.ENEMY);
		myLibraryTabs.getSelectionModel().selectedItemProperty().addListener((e,ov,nv) -> {
			myLibraryEditor.setTypeChosen(GameObjectType.valueOf(nv.getText().toUpperCase()));
		});
		myEditorView.getChildren().addAll(myLibraryEditor.getView(),createBtn());
		myData.setType(GameObjectType.valueOf(myLibraryTabs.getSelectionModel().getSelectedItem().getText().toUpperCase()));
	}

	private Node createBtn() {
		Button btn = new Button("Create");
		btn.setOnAction(e -> createObjectData());
		return btn;
	}

	private void createObjectData() {
		myData.setType(GameObjectType.valueOf(myLibraryTabs.getSelectionModel().getSelectedItem().getText().toUpperCase()));
		myData.setAttributes(myLibraryEditor.getAttributes());
		myData.setCollisionMap(myLibraryEditor.getCollisions());
		
		System.out.println("**********************Created ObjectData******************");
		System.out.println(myData.getType());
		for (IAttribute i: myData.getAttributes()) {
			System.out.println(i.getClass().getSimpleName());
		}
		for (GameObjectType g: myData.getCollisionMap().keySet()) {
			for (ICollisionEvent i: myData.getCollisionMap().get(g)) {
				System.out.println(String.format("%s->%s",g,i.getClass().getSimpleName()));
			}
		}
	}

	private void initializeLibraryTabs(File GameDirectory) {
		myEnemyLibrary = new EnemyLibrary(GameObjectType.ENEMY);
		myTowerLibrary = new TowerLibrary(GameObjectType.TOWER);
		myPlayerLibrary = new PlayerLibrary(GameObjectType.PLAYER);
		myItemLibrary = new ItemLibrary(GameObjectType.ITEM);
	}

	private void populateTabPane() {
		Tab myEnemyTab = new Tab("Enemy");
		myEnemyTab.setContent(myEnemyLibrary.getContent());
		Tab myTowerTab = new Tab("Tower");
		myTowerTab.setContent(myTowerLibrary.getContent());
		Tab myPlayerTab = new Tab("Player");
		myPlayerTab.setContent(myPlayerLibrary.getContent());
		Tab myItemTab = new Tab("Item");
		myItemTab.setContent(myItemLibrary.getContent());
		myLibraryTabs.getTabs().addAll(myEnemyTab, myTowerTab, myPlayerTab, myItemTab);
	}
	
	public Node getLibrary() {
		return myLibraryTabs;
	}
	
	public Node getEditor() {
		return myEditorView;
	}
	
}
