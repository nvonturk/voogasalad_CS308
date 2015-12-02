package authoring.icons.implementations;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public abstract class AbstractIcon extends Pane {
	
	private static final double BORDER_WIDTH = 1.0;
	private static final Border BORDER = new Border(new BorderStroke(
			Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(BORDER_WIDTH)));

	private ImageView myImageView;
	
	public AbstractIcon(String imagePath, double dimension){
		Image img = new Image(getClass().getClassLoader().getResourceAsStream(imagePath));
		myImageView = new ImageView(img);
		setIconDimensions(dimension);
		this.getChildren().add(myImageView);
		this.setBorder(BORDER);
	}
	
	private void setIconDimensions(double size){
		myImageView.fitWidthProperty().bind(this.widthProperty().subtract(2*BORDER_WIDTH));
		myImageView.fitHeightProperty().bind(this.heightProperty().subtract(1*BORDER_WIDTH));
		myImageView.translateXProperty().bind(this.translateXProperty().add(1));
		myImageView.translateYProperty().bind(this.translateYProperty().add(0));
		this.setSize(size);
	}
	
	public void setSize(double size) {
		this.setWidth(size);
		this.setHeight(size);
	}
	
	public void setImage(Image image) {
		myImageView.setImage(image);
	}
	
}
