package com.karien.tacobox.comm;

import java.io.IOException;

import com.karien.taco.mapstuff.ActionMessage;

public interface MsgPoster {
	void postMessage(ActionMessage msg) throws IOException;
}
