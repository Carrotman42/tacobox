package com.karien.tacobox;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "tacobox";
		cfg.useGL20 = true;
		cfg.width = MyTacoBox.SCREEN_WIDTH;
		cfg.height = MyTacoBox.SCREEN_HEIGHT;

		new LwjglApplication(new MyTacoBox(), cfg);
	}
}
