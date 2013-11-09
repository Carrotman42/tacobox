package com.karien.taco.mapstuff;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.karien.tacobox.comm.MsgHandler;


public class MapActions {
	private final HashMap<MapObject, Integer> remoteActs;
	private final HashMap<MapObject, Integer> localActs;
	private final HashMap<Integer, MapObject> actions;
	
	private final MsgHandler post;
	/**
	 * 
	 * @param map
	 * @param remote Remote endpoint to send data to if we get a remote action. May be null.
	 * @return
	 */
	public static MapActions procActions(TiledMap map, MsgHandler remote) {
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
			
			setDefaults(props);
		}
		
		l = (TiledMapTileLayer)map.getLayers().get(C.ObjectLayer);
		for (MapObject o : l.getObjects()) {
			MapProperties props = o.getProperties();
			
			String id = (String)props.get(C.Id);
			if (id != null) {
				String action = (String)props.get(C.action);
				if (action != null) {
					props.put(C.action, MapAction.getAction(action));
					actions.put(Integer.parseInt(id), o);
				} else {
					System.out.println("Warning: found object with id without action. id=" + id);
				}
			}
			
			setDefaults(props);
		}
		
		return new MapActions(remoteActs, localActs, actions, remote);
	}
	
	private MapActions(HashMap<MapObject, Integer> remote, HashMap<MapObject, Integer> local, HashMap<Integer, MapObject> actions, MsgHandler post) {
		this.remoteActs = remote;
		this.localActs = local;
		this.actions = actions;
		this.post = post;
	}
	
	public void checkAction(int x, int y) {
		// May be innefficient to constantly iterate through these. If that is true, I'll implement a map of the current
		// objects and whatnot.
		
		for (Map.Entry<MapObject, Integer> o : remoteActs.entrySet()) {
			MapProperties p = o.getKey().getProperties();
			int nx = p.get("x", int.class);
			int ny = p.get("y", int.class);
			if (x == nx && y == ny) {
				sendRemoteMsg(o.getValue());
				return;
			}
		}
		

		for (Map.Entry<MapObject, Integer> o : localActs.entrySet()) {
			MapProperties p = o.getKey().getProperties();
			int nx = p.get("x", int.class);
			int ny = p.get("y", int.class);
			if (x == nx && y == ny) {
				procAction(o.getValue());
				return;
			}
		}
	}
	
	private void sendRemoteMsg(Integer msg) {
		if (post == null) {
			System.out.println("Ignored remote message: " + msg);
		} else {
			try {
				post.postMessage(ActionMessage.fromString(msg.toString()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private void procAction(Integer id) {
		MapObject ob = actions.get(id);
		if (ob == null) {
			throw new RuntimeException("Got id " + id + " as an action but didn't see an object mapping to that id");
		}
		MapAction action = (MapAction)ob.getProperties().get(C.action);
		if (action == null) {
			throw new RuntimeException("Got id " + id + " as an action. I found that object but didn't find an action on it");
		}
		
		action.doit(ob);
	}
	
	public void checkRemoteMessage() {
		if (post == null) {
			return;
		}
		
		ActionMessage msg = post.recvAction();
		if (msg == null) {
			return;
		}
		
		procAction(msg.id);
	}
	
	private static void setDefaults(MapProperties p) {
		setBoolDefault(p, C.Visible, C.defaultVisible);
		setBoolDefault(p, C.Blocked, C.defaultBlocked);
	}
	
	private static void setBoolDefault(MapProperties p, String name, boolean def) {
		String str = (String)p.get(name);
		if (str != null) {
			def = Boolean.parseBoolean(str);
		}
		p.put(name, def);
	}
}



















