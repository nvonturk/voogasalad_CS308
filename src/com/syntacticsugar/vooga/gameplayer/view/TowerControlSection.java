package com.syntacticsugar.vooga.gameplayer.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import com.syntacticsugar.vooga.authoring.icon.Icon;
import com.syntacticsugar.vooga.gameplayer.objects.GameObject;
import com.syntacticsugar.vooga.gameplayer.universe.IUniverseView;
import com.syntacticsugar.vooga.xml.data.ObjectData;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TowerControlSection extends Observable implements Observer {

	private VBox myContent;
	private ScrollPane towerList;
	private ObjectData currentSelection;
	private IUniverseView myUniverse;
	private Collection<Icon> myTowerIcons;

	public TowerControlSection() {
		myContent = new VBox();
		Label title = new Label("Tower Controls");
		title.getStyleClass().add("title");
		myContent.getChildren().add(title);
		towerList = new ScrollPane(myContent);
	}

	public void initialize(Collection<ObjectData> availableTowers, IUniverseView universe) {
		testDragAndDrop(availableTowers);
		myUniverse = universe;
	}

	private void testDragAndDrop(Collection<ObjectData> availableTowers) {
		myTowerIcons = new ArrayList<>();
		for (ObjectData towerObject : availableTowers) {
			Icon tower = new Icon(towerObject.getImagePath());
			tower.setPrefHeight(100);
			tower.setPrefWidth(100);
			tower.setOnMouseClicked(e -> selectedTower(tower, towerObject));
			addTowerInfo(tower, towerObject);
			myTowerIcons.add(tower);
		}
	}

	private void addTowerInfo(Icon towerIcon, ObjectData towerData) {
		HBox towerInfo = new HBox(10);
		VBox towerValues = new VBox(5);
		towerValues.getChildren().addAll(new Label("Tower       "));
		towerInfo.getChildren().addAll(towerIcon, towerValues);
		myContent.getChildren().add(towerInfo);
	}

	private void selectedTower(Icon tower, ObjectData towerObject) {
		if (towerObject.equals(currentSelection)) {
			tower.setStyle("-fx-background-color: transparent");
			setChanged();
			notifyObservers(false);
			currentSelection = null;
		} else {
			for (Icon towerIcon : myTowerIcons) {
				towerIcon.setStyle("-fx-background-color: transparent");
			}
			tower.setStyle("-fx-background-color: red");
			currentSelection = towerObject;
			setChanged();
			notifyObservers(true);
		}

	}

	public ScrollPane getContent() {
		return towerList;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		TowerData data = (TowerData) arg1;
		Point2D coordinates = data.getCoordinates();
		currentSelection.setSpawnPoint(coordinates.getX(), coordinates.getY());
		currentSelection.setDirection(data.getDirection());
		myUniverse.addToSpawnYard(new GameObject(currentSelection));
	}
}
