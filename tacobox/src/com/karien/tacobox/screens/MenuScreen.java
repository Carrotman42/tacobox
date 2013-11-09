package com.karien.tacobox.screens;

import com.badlogic.gdx.Screen;
import com.karien.tacobox.MyTacoBox;

public class MenuScreen implements Screen {
	private final MyTacoBox parent;
	public MenuScreen(MyTacoBox callback) {
		parent = callback;
	}

	@Override
	public void render(float delta) {
		// Automatically start level until we get a menu set up.
		parent.menuChoice("start");
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
