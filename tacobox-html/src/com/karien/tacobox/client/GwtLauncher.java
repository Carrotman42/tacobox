package com.karien.tacobox.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.karien.tacobox.MyTacoBox;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig() {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(
				MyTacoBox.SCREEN_WIDTH, MyTacoBox.SCREEN_HEIGHT);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener() {
		return new MyTacoBox();
	}
}