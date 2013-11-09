package com.karien.taco.mapstuff;

import java.util.HashMap;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.karien.tacobox.comm.MsgPoster;


public class MapActions {
	public final HashMap<MapObject, Integer> remoteActs;
	public final HashMap<MapObject, Integer> localActs;
	public final HashMap<Integer, MapObject> actions;
	
	/**
	 * 
	 * @param map
	 * @param remote Remote endpoint to send data to if we get a remote action. May be null.
	 * @return
	 */
	public static MapActions procActions(TiledMap map, MsgPoster remote) {
		HashMap<MapObject, Integer> remoteActs = new HashMap<MapObject, Integer>();
		HashMap<MapObject, Integer> localActs = new HashMap<MapObject, Integer>();
		HashMap<Integer, MapObject> actions = new HashMap<Integer, MapObject>();
		
		TiledMapTileLayer l = (TiledMapTileLayer)map.getLayers().get(C.ActionLayer);
		
		for (MapObject o : l.getObjects()) {
			MapProperties props = o.getProperties();
			
			String act = (String)props.get(C.remoteAct);
			if (act != null) {
				remoteActs.put(o, Integer.parseInt(act));
			}
			
			act = (String)props.get(C.localAct);
			if (act != null) {
				localActs.put(o, Integer.parseInt(act));
			}
		}
		
		l = (TiledMapTileLayer)map.getLayers().get(C.ObjectLayer);
		for (MapObject o : l.getObjects()) {
			MapProperties props = o.getProperties();
			
			String id = (String)props.get(C.Id);
			if (id != null) {
				actions.put(Integer.parseInt(id), o);
			}
		}
		
		return new MapActions(remoteActs, localActs, actions);
	}
	
	private MapActions(HashMap<MapObject, Integer> remote, HashMap<MapObject, Integer> local, HashMap<Integer, MapObject> actions) {
		this.remoteActs = remote;
		this.localActs = local;
		this.actions = actions;
	}
	
	public void doAction(MapObject obj) {
		
	}
	
	
	public void procRemoteMessage(ActionMessage id) {
		
	}
}
