package com.syntacticsugar.vooga.menu;

import com.syntacticsugar.vooga.authoring.AuthoringScreenManager;
import com.syntacticsugar.vooga.authoring.fluidmotion.mixandmatchmotion.PulsingFadeWizard;
import com.syntacticsugar.vooga.help.HelpManager;
import com.syntacticsugar.vooga.social.SocialCenter;
import com.syntacticsugar.vooga.util.properties.PropertiesManager;

import javafx.animation.Animation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainMenu extends AbstractMenu {
	private static PropertiesManager propManager = new PropertiesManager("com/syntacticsugar/vooga/resources/View");

	public MainMenu() {
		super(propManager.getProperty("ProgramTitle"));
	}

	@Override
	protected void initializeOptions(BorderPane pane) {
		Button launchGame = createButton("Play Game", e -> launch(new GameChooser()));
		Button launchEditor = createButton("Launch Editor", e -> launch(new AuthoringScreenManager()));
		Button launchSocial = createButton("Be Social", e -> launch(new SocialCenter()));
		Button launchHelp = createButton("Help", e -> launchHelpView("http://www.henryyuen.info"));
		PulsingFadeWizard.attachPulsingHandlers(launchGame,launchEditor, launchSocial, launchHelp);
		generateOptions(pane, launchGame, launchEditor, launchSocial, launchHelp);
	}

	private void launch(IVoogaApp app) {
		app.assignCloseHandler(e -> animatedShowStage());
		hideStage();
	}
	private void launchHelpView(String url) {
		Stage stage = new Stage();
		stage.setTitle("Web View");
        Scene scene = new Scene(new HelpManager(url),750,500);
        stage.setScene(scene);
        stage.show();
	}
}
