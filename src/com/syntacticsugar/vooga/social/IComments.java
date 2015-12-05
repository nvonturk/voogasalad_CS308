package com.syntacticsugar.vooga.social;

import org.json.JSONObject;

public interface IComments {

	public String getSerializedComments(String gameName);
		
	public String getGameName();
	
	public int getGameID();
	
	public void updateData();
	
	
}
