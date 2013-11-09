package com.karien.tacobox.screens;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.karien.taco.mapstuff.MapActions;
import com.karien.tacobox.GameEventListener;
import com.karien.tacobox.comm.MsgHandler;

public class Level {
	public final TiledMap map;
	public final MapActions acts;
	public final GameEventListener parent;
	
	public Level(GameEventListener parent, String mapPath, MsgHandler remote) {
		map = new TmxMapLoader().load(mapPath);
		acts = MapActions.procActions(map, remote);
		this.parent = parent;
	}
}
