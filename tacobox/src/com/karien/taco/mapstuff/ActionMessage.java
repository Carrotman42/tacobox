package com.karien.taco.mapstuff;

/**
 * Abstracted version of a message that is sent between clients regarding actions.
 *
 */
public class ActionMessage {
	public final Integer id;
	
	public ActionMessage(Integer id) {
		this.id = id;
	}

	public static ActionMessage fromString(String line) {
		return new ActionMessage(Integer.parseInt(line));
	}
}
