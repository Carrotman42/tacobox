package com.karien.taco.mapstuff;

/**
 * A set of constants to talk to the map so that they are just in one place.
 *
 */
public interface C {
	String Blocked = "blocked";
	String Visisble = "visible";
	
	boolean defaultBlocked = true;
	boolean defaultVisible = true;
	
	String Id = "id";
	String remoteAct = "remoteAct";
	String localAct = "localAct";
	
	String Disappear = "disappear";
	String Appear = "appear";
	String Toggle = "toggle";
}
