package com.karien.taco.mapstuff;

/**
 * A set of constants to talk to the map so that they are just in one place.
 *
 */
public interface C {
	// Common
	String Id = "id";
	
	// Object properties
	String Blocked = "blocked";
	String Visible = "visible";
	String Moveable = "moveable";
	
	boolean defaultBlocked = true;
	boolean defaultVisible = true;
	boolean defaultMovable = false;
	
	// Action layer properties
	String remoteAct = "remoteAct";
	String localAct = "localAct";
	String onExit = "onExit";
	String onActivate = "onActivate";
	String onEnter = "onEnter";
	
	// Actions (for onExit/onActivate/onEnter)
	String Disappear = "disappear";
	String Appear = "appear";
	String Toggle = "toggle";
	
	// Layers
	int TileLayer = 0;
	int ActionLayer = 2;
	int ObjectLayer = 1;
	
	// Map properties
	String SpawnX = "spawnX";
	String SpawnY = "spawnY";
}
