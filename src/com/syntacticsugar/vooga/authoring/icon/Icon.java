package com.syntacticsugar.vooga.authoring.icon;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Icon extends Pane {

	private final ObjectProperty<Double> myWidthProperty = new SimpleObjectProperty<>();
	private final ObjectProperty<Double> myHeightProperty = new SimpleObjectProperty<>();

	private ImageView myImageView;
	public ImageView getImageView() {
		return myImageView;
	}

	private String myImagePath;

	public Icon(String imagePath){
		Image img = new Image(getClass().getClassLoader().getResourceAsStream(imagePath));
		myImageView = new ImageView(img);
		myImagePath = imagePath;
		myWidthProperty.addListener(new ChangeListener<Double>() {

			@Override
			public void changed(ObservableValue<? extends Double> arg0, Double arg1, Double arg2) {
				setWidth(arg2);
			}
			
		});
		myHeightProperty.addListener(new ChangeListener<Double>() {

			@Override
			public void changed(ObservableValue<? extends Double> arg0, Double arg1, Double arg2) {
				setHeight(arg2);
			}
			
		});
		makeResizable(myImageView);
		this.getChildren().add(myImageView);
	}

	private void makeResizable(ImageView iv){
		iv.fitWidthProperty().bind(this.widthProperty());
		iv.fitHeightProperty().bind(this.heightProperty());
		iv.translateXProperty().bind(this.translateXProperty());
		iv.translateYProperty().bind(this.translateYProperty());
	}

	public ObjectProperty<Double> getWidthProperty() {
		return this.myWidthProperty;
	}
	
	public ObjectProperty<Double> getHeightProperty() {
		return this.myHeightProperty;
	}

	public void setImage(Image image) {
		myImageView.setImage(image);
	}
	
	public String getImagePath() {
		return this.myImagePath;
	}
	
}
