package authoring.level;

import com.syntacticsugar.vooga.util.ResourceManager;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import xml.data.TileData;

public class TileInfoTooltip extends CustomTooltip {

	private TileData myTileData;
	private Label myType;
	private Label myDestinationMarker;
	private ImageView currentImageView;
	
	public TileInfoTooltip(TileData tileData) {
		super();
		myTileData = tileData;
	}

	@Override
	protected Node buildContentNode() {
		GridPane infoGrid = new GridPane();
		myType = new Label();
		myType.setFont(new Font(6));
		myDestinationMarker = new Label();
		myDestinationMarker.setFont(new Font(6));
		currentImageView = new ImageView();
		currentImageView.setFitWidth(40);
		currentImageView.setFitHeight(40);
		infoGrid.setHgap(8);
		infoGrid.setAlignment(Pos.CENTER);
		infoGrid.add(myType, 0, 0, 1, 1);
		infoGrid.add(myDestinationMarker, 0, 1, 1, 1);
		infoGrid.add(currentImageView, 1, 0, 1, 2);
		return infoGrid;
	}	
	
	@Override
	protected void updateContent() {
		currentImageView.setImage(new Image(ResourceManager.getResource(this, myTileData.getImagePath())));
		myType.setText(myTileData.getImplementation().toString());
		myDestinationMarker.setText(String.format("%s %s", "Destination?", myTileData.isDestination().toString().toUpperCase()));
	}
}
