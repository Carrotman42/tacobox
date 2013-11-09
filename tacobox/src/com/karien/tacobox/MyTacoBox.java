package com.karien.tacobox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.karien.taco.mapstuff.LevelHelper;
import com.karien.tacobox.screens.LoadingScreen;
import com.karien.tacobox.screens.MainScreen;

public class MyTacoBox extends Game {
	private LevelHelper lvls;
	private GameState state = GameState.Title;
	
	@Override
	public void create() {	
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		switch (state) {
		case Title:
			state = GameState.LoadFirstLevel;
			break;
		case LoadFirstLevel:
			lvls = new LevelHelper(null, this);
			lvls.loadNextLevel();
			state = GameState.WaitLoadFirstLevel;
			break;
		case WaitLoadFirstLevel:
			if (lvls.isLevelLoaded()) {
				setScreen(new MainScreen(lvls.getLoadedLevel()));
				state = GameState.Level;
			} else {
				System.out.println("Loading next level");
			}
			break;
		case Level:
			//super.render();
			// nothing special other than rendering
			break;
		case LevelJustFinished:
			System.out.println("You beat the level!");
			lvls.loadNextLevel();
			setScreen(new LoadingScreen());
			state = GameState.LevelFinished;
			break;
		case LevelFinished:
			if (lvls.isLevelLoaded()) {
				state = GameState.NextLevelReady;
			} else {
				System.out.println("Still loading next level");
			}
			break;
		case NextLevelReady:
			System.out.println("Starting level!");
			setScreen(new MainScreen(lvls.getLoadedLevel()));
			state = GameState.Level;
			break;
			
		default:
			throw new RuntimeException("Invalid state!");
		}
		super.render();
	}
	
	public void goalReached() {
		state = GameState.LevelJustFinished;
	}

	public void died() {
		throw new RuntimeException("Not implemented!");
	}
	
	public void menuChoice(String action) {
		if (action.equals("start")) {
			if (state != GameState.Title) {
				throw new RuntimeException("Invalid state to call this function");
			}
			state = GameState.LoadFirstLevel;
		}
	}
}
