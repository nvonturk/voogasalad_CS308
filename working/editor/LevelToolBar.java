package editor;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;

public class LevelToolBar extends EditorToolbar{
	
	public LevelToolBar(){
		super("Level Tools");
		createAddLevelOption();
		createRemoveLevelOption();
		createChangeLevelOption();
	}
	
	private void createAddLevelOption() {
		Button add = createToolbarItem("Add");
	}

	private void createRemoveLevelOption() {
		Button remove = createToolbarItem("Remove");
	}

	private void createChangeLevelOption() {
		Button remove = createToolbarItem("Change Level");
	}

}