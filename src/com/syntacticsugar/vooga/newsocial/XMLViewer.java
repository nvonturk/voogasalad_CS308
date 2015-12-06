package com.syntacticsugar.vooga.newsocial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.syntacticsugar.vooga.authoring.objectediting.IVisualElement;
import com.syntacticsugar.vooga.newsocial.IDataViewUpdater;
import com.syntacticsugar.vooga.util.ResourceManager;
import com.syntacticsugar.vooga.util.gui.factory.GUIFactory;
import com.syntacticsugar.vooga.util.webconnect.WebConnector;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class XMLViewer implements IVisualElement {

	private VBox myView;
	private ListView<Node> myListView;
	private int mySelectedItemID = Integer.MIN_VALUE;
	private IDataViewUpdater myUpdater;

	public XMLViewer(IDataViewUpdater updater) {
		myUpdater = updater;
		myView = makeView();
	}

	private VBox makeView(){
		myListView = new ListView<Node>();
		myView = new VBox();
		myView.getChildren().addAll(makeTitleNode(makeButtonStrip()), myListView);
		return myView;
	}
	
	private Node makeTitleNode(Node buttons){
		HBox title = GUIFactory.buildTitleNode("Game Name | Author Name");
		return GUIFactory.buildAnchorPane(title, buttons);
	}
	
	private Node makeButtonStrip(){
		Button download = GUIFactory.buildButton("Download", e->{}, 100.00, null);
		Button upload = GUIFactory.buildButton("Upload", e->{}, 100.00, null);
		Button refresh = GUIFactory.buildButton("Refresh", e-> refresh(), 100.0, null);
		HBox buttonStrip = new HBox();
		buttonStrip.getChildren().addAll(refresh, download, upload);
		return buttonStrip;
	}
	
	private void addElementToList(Node element){
		myListView.getItems().add(element);
	}
	
	private void populateListFromDatabase() {
		JSONObject XMLs = WebConnector.getXMLs();
		JSONArray array;
		try {
			array = XMLs.getJSONArray("xmls");
			for (int i = 0; i < array.length(); i++) {
				JSONObject current = (JSONObject) array.get(i);
				addElementToList(makeListElement(current.getString("gamename"), current.getString("author"),
						(int) current.get("id")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void refresh() {
		clearList();
		populateListFromDatabase();
	}
	
	private void clearList(){
		myListView.getItems().clear();
	}

	private Node makeListElement(String itemName, String itemData, int itemID) {
		HBox listElement = new HBox();
		HBox game = GUIFactory.buildTitleNode(itemName);
		HBox author = GUIFactory.buildTitleNode(itemData);
		game.setPrefWidth(300);
		listElement.getChildren().addAll(game, author);

		listElement.setOnMouseClicked(e -> {
			mySelectedItemID = itemID;
			try {
				myUpdater.updateDataViewer(itemID);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		return listElement;
	}

	
	public int getCurrentlySelected() {
		return mySelectedItemID;
	}

	@Override
	public Node getView() {
		return myView;
	}

}