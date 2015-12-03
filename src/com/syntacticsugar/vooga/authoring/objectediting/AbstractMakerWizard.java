package com.syntacticsugar.vooga.authoring.objectediting;

import com.syntacticsugar.vooga.util.ResourceManager;
import com.syntacticsugar.vooga.xml.data.ObjectData;

import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class AbstractMakerWizard {

	protected Stage myStage;
	protected EditingViewer myEditingViewer;
	protected ObjectData myData;
	
	public AbstractMakerWizard(EditingViewer editingViewer, ObjectData data){
		myEditingViewer = editingViewer;
		myData = data;
		myStage = new Stage();
		myStage.setTitle(ResourceManager.getString(this.getClass().getSimpleName()));
		myStage.initModality(Modality.APPLICATION_MODAL);
	}
	
}