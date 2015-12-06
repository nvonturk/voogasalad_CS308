package com.syntacticsugar.vooga.authoring.level.map;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

import com.syntacticsugar.vooga.authoring.dragdrop.DragDropManager;
import com.syntacticsugar.vooga.authoring.fluidmotion.FadeTransitionWizard;
import com.syntacticsugar.vooga.authoring.fluidmotion.FluidGlassBall;
import com.syntacticsugar.vooga.authoring.fluidmotion.mixandmatchmotion.PulsingFadeWizard;
import com.syntacticsugar.vooga.authoring.icon.IconPane;
import com.syntacticsugar.vooga.authoring.icon.ImageFileFilter;
import com.syntacticsugar.vooga.authoring.objectediting.IVisualElement;
import com.syntacticsugar.vooga.util.ResourceManager;
import com.syntacticsugar.vooga.util.filechooser.FileChooserUtil;
import com.syntacticsugar.vooga.util.gui.factory.AlertBoxFactory;
import com.syntacticsugar.vooga.util.gui.factory.GUIFactory;
import com.syntacticsugar.vooga.xml.data.TileData;
import com.syntacticsugar.vooga.xml.data.TileImplementation;

import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MapControls extends Observable implements IVisualElement {

	private VBox myVBox;
	private TitledPane myViewPane;
	private Button selectAll;
	private Button clearAll;
	private ComboBox<TileImplementation> typeChooser;
	private Label destinationWrapper;
	private CheckBox destinationChooser;
	private Button addNewImage;
	private Button applyChanges;
	private TileImplementation mySelectedType;
	private IconPane myIconPane;
	private ImageView previewTile = new ImageView(); 

	public ImageView getPreviewTile() {
		return previewTile;
	}

	public void setPreviewTile(ImageView previewTile) {
		this.previewTile = previewTile;
	}

	public MapControls(IMapDisplay mapEditor) {
		myIconPane = new IconPane();
		previewTile.setFitWidth(100);
		previewTile.setFitHeight(100);
		myIconPane.addPreviewListener((o,s1,s2)-> updatePreview());
		typeChooser = buildTileTypeChooser();
		selectAll = 
				GUIFactory.buildButton("Select All", 
						e -> mapEditor.selectAllTiles(),
						null, null);
		clearAll = 
				GUIFactory.buildButton("Clear All", 
						e -> mapEditor.clearDisplay(),
						null, null);
		destinationChooser = new CheckBox();
		destinationChooser.setAllowIndeterminate(false);
		destinationWrapper = new Label("AI Destination: ");
		destinationWrapper.setGraphic(destinationChooser);
		destinationWrapper.setContentDisplay(ContentDisplay.RIGHT); //You can choose RIGHT,LEFT,TOP,BOTTOM
		addNewImage = 
				GUIFactory.buildButton("Add New Image", 
						e -> createNewImage(),
						null, null);
		applyChanges = 
				GUIFactory.buildButton("Apply", 
						e -> mapEditor.displayData(buildTileFromSelections()), 
						null, null);
		// Build control container view
		buildView();
		// Let the IconPane expand to fill the contents of the controls
		VBox.setVgrow(myIconPane.getView(), Priority.ALWAYS);
	}

	private void initPreviewDragHandler(TileData tileData) {
		previewTile.setOnDragDetected(event -> DragDropManager.createClipboard(
												tileData,
												previewTile,
												event));
	}

	@Override
	public Node getView() {
		return myViewPane;
	}

	private TileData buildTileFromSelections() {
		if (mySelectedType == null) {
			AlertBoxFactory.createObject("Please select a tile type.");
			return null;
		}
		if (myIconPane.getSelectedImagePath() == null) {
			AlertBoxFactory.createObject("Please select a tile image.");
			return null;
		}
		TileData td = initTileData();
		return td;
	}

	private void buildView() {
		AnchorPane top = GUIFactory.buildAnchorPane(selectAll, clearAll);
		AnchorPane middle = GUIFactory.buildAnchorPane(typeChooser, destinationWrapper);
		AnchorPane bottom = GUIFactory.buildAnchorPane(addNewImage, applyChanges);
		myVBox = new VBox();
		myVBox.setSpacing(10);
		myVBox.setPadding(new Insets(10));
		myVBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		myVBox.getChildren()
		.addAll(top, middle, myIconPane.getView(), bottom,previewTile);
		myVBox.setAlignment(Pos.CENTER);
		myViewPane = GUIFactory.buildTitledPane("Map Controls", myVBox);
	}

	private void updatePreview() {
		TileData td = initTileData();
		initPreviewDragHandler(td);
		previewTile.setImage(new Image(getClass().getClassLoader().getResourceAsStream(myIconPane.getSelectedImagePath())));
		Animation anim = PulsingFadeWizard
								.applyEffect(previewTile);
		previewTile.setOnMouseEntered(e->anim.play());
		previewTile.setOnMouseExited(e->{
			previewTile.setOpacity(1);
			anim.stop();
		});
	}

	private TileData initTileData() {
		TileData td = new TileData(myIconPane.getSelectedImagePath());
		td.setDestination(destinationChooser.isSelected());
		td.setImplementation(mySelectedType);
		return td;
	}

	private ComboBox<TileImplementation> buildTileTypeChooser() {
		ComboBox<TileImplementation> box = new ComboBox<TileImplementation>();
		box.setPromptText("Tile Type");
		box.getItems().addAll(TileImplementation.values());
		box.valueProperty().addListener((o, s1, s2) -> updateSelectedType(s2));
		return box;
	}

	private void updateSelectedType(TileImplementation type) {
		mySelectedType = type;
		showImageOptions(type);
	}

	private void showImageOptions(TileImplementation type) {
		File imgDirectory = new File(
				ResourceManager.getString(String.format("%s%s", mySelectedType, "_images")));
		myIconPane.showDirectoryContents(imgDirectory, e -> convertImageFiles(imgDirectory));
	}

	private Collection<String> convertImageFiles(File directory) {
		File[] files = directory.listFiles(new ImageFileFilter());
		Collection<String> imagePaths = new ArrayList<String>();
		for (int i=0; i<files.length; i++) {
			imagePaths.add(files[i].getName());
		}
		return imagePaths;
	}

	private void createNewImage() {
		if (mySelectedType == null) {
			AlertBoxFactory.createObject("Select a tile type.");
			return;
		}
		FileChooserUtil.loadFile("Add Image File", new ExtensionFilter("Image Files", "*.jpeg", "*.gif", "*.png"), null,
				selectedFile -> {
					try {
						String path = ResourceManager.getString(String.format("%s%s", mySelectedType, "_images"));
						Files.copy(selectedFile.toPath(), (new File(
								path + "/" + mySelectedType.toString().toLowerCase() + "_" + selectedFile.getName()))
										.toPath(),
								StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						AlertBoxFactory.createObject("Image already exists. Please select another.");
					}
				});
		showImageOptions(mySelectedType);
	}

}
