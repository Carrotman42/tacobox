package com.karien.tacobox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.karien.taco.mapstuff.LevelHelper;
import com.karien.tacobox.comm.MultiplayerComm;
import com.karien.tacobox.screens.LoadingScreen;
import com.karien.tacobox.screens.MainScreen;
import com.karien.tacobox.screens.MenuScreen;

public class MyTacoBox extends Game {
	private LevelHelper lvls;
	private GameState state = GameState.Title;

	private MultiplayerComm multi;

	@Override
	public void create() {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		switch (state) {
		case Title:
			setScreen(new MenuScreen(this));
			state = GameState.WaitForAction;
			break;
		case WaitForAction:
			// state = GameState.LoadFirstLevel;
			break;
		case LoadFirstLevel:
			lvls = new LevelHelper(multi, this);
			lvls.loadNextLevel();
			setScreen(new LoadingScreen());
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
			// super.render();
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

	public void menuChoice(String action, String... params) {
		if (action.equals("host")) {
			if (state != GameState.WaitForAction) {
				throw new RuntimeException(
						"Invalid state to call this function");
			}
			multi = hostMultiplayer();
			state = GameState.LoadFirstLevel;
		} else if (action.equals("join")) {
			if (state != GameState.WaitForAction) {
				throw new RuntimeException(
						"Invalid state to call this function: " + state);
			}
			multi = joinMultiplayer(params[0]);
			state = GameState.LoadFirstLevel;
		}
	}

	private int port = 4608;

	private MultiplayerComm hostMultiplayer() {
		System.out.println("Hosting multiplayer");
		try {
			return MultiplayerComm.connect(port);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Joins a multi-player game at <var>ipAddr</var>
	 * 
	 * @param ipAddr
	 *            - The IP Address of the host, if null connects to localhost
	 * @return a multi-player session
	 */
	private MultiplayerComm joinMultiplayer(String ipAddr) {
		if (ipAddr == null) {
			ipAddr = "127.0.0.1";
		}
		System.out.println("Joining multiplayer at " + ipAddr);
		try {
			return MultiplayerComm.connect(ipAddr, port);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
