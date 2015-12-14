package com.syntacticsugar.vooga.gameplayer.conditions.implementation;

import com.syntacticsugar.vooga.authoring.parameters.EditableClass;
import com.syntacticsugar.vooga.authoring.parameters.EditableField;
import com.syntacticsugar.vooga.authoring.parameters.InputParser;
import com.syntacticsugar.vooga.authoring.parameters.InputTypeException;
import com.syntacticsugar.vooga.gameplayer.conditions.AbstractCondition;
import com.syntacticsugar.vooga.gameplayer.event.GameEventType;
import com.syntacticsugar.vooga.gameplayer.event.IGameEvent;
import com.syntacticsugar.vooga.gameplayer.event.implementations.LevelChangeEvent;
import com.syntacticsugar.vooga.gameplayer.event.implementations.ScoreUpdateEvent;

@EditableClass(className = "Score Threshold Needed")
public class ScoreCondition extends AbstractCondition {

	private int myScoreThreshold;

	public ScoreCondition() {
		super();
	}

	@Override
	protected void setDefaults() {
		this.myScoreThreshold = 100;
	}

	@Override
	public void onEvent(IGameEvent e) {
		if (e.getEventType().equals(GameEventType.ScoreUpdate)) {
			try {
				ScoreUpdateEvent event = (ScoreUpdateEvent) e;
				if (event.getScore() >= myScoreThreshold) {
					postEvent(new LevelChangeEvent(GameEventType.Winning));
				}
			} catch (ClassCastException ce) {

			}
		}
	}

	/** EDIT TAGS **/
	/** *************************************** **/

	@EditableField(inputLabel = "Score Threshold", defaultVal = "100")
	private void setScoreThreshold(String arg) {
		try {
			this.myScoreThreshold = InputParser.parseAsInt(arg);
		} catch (InputTypeException e) {
		}
	}

}
