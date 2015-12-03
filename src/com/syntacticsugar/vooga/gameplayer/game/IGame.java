package com.syntacticsugar.vooga.gameplayer.game;

import com.syntacticsugar.vooga.gameplayer.universe.IGameUniverse;
import com.syntacticsugar.vooga.xml.data.UniverseData;

public interface IGame {
	
	public IGameUniverse nextLevel();
	
	public IGameUniverse getLevel(int i);
	
	public void saveGame(UniverseData d);

}
