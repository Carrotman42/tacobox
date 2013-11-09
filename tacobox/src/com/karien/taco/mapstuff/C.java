package com.karien.taco.mapstuff;

/**
 * A set of constants to talk to the map so that they are just in one place.
 *
 */
public interface C {
	String Blocked = "blocked";
	String Visible = "visible";
	
	boolean defaultBlocked = true;
	boolean defaultVisible = true;
	boolean defaultMovable = false;
	
	String Id = "id";
	String remoteAct = "remoteAct";
	String localAct = "localAct";
	String onExit = "onExit";
	String onActivate = "onActivate";
	String onEnter = "onActivate";
	
	String Disappear = "disappear";
	String Appear = "appear";
	String Toggle = "toggle";
	String Moveable = "moveable";
	
	// Layers
	int TileLayer = 0;
	int ActionLayer = 2;
	int ObjectLayer = 1;
}
