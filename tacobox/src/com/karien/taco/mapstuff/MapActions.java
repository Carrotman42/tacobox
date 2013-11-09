package com.karien.taco.mapstuff;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.karien.tacobox.comm.MsgHandler;


public class MapActions {
	private final HashMap<String, MapObject> objects;
	private final HashMap<Coord, MapObject> actable;
	
	private final MsgHandler post;
	/**
	 * 
	 * @param map
	 * @param remote Remote endpoint to send data to if we get a remote action. May be null.
	 * @return
	 */
	public static MapActions procActions(TiledMap map, MsgHandler remote) {
		HashMap<String, MapObject> objects = new HashMap<String, MapObject>();
		HashMap<Coord, MapObject> actable = new HashMap<Coord, MapObject>();
		
		TiledMapTileLayer l = (TiledMapTileLayer)map.getLayers().get(C.ActionLayer);
		for (MapObject o : l.getObjects()) {
			MapProperties props = o.getProperties();

			boolean hadAny = 
					makeAction(props, C.onExit) | // Note: Can't do the short-circuiting || operator!
					makeAction(props, C.onActivate) |
					makeAction(props, C.onEnter);
			
			
			if (hadAny) {
				Object rem = props.get(C.remoteAct);
				Object local = props.get(C.localAct);
				
				if (rem == null && local == null) {
					throw new RuntimeException("Object at (" + props.get("x") + ", " + props.get("y") + ") had actions assigned to it but no target for those actions!");
				}
				
				actable.put(new Coord((Integer)props.get("x"), (Integer)props.get("y")), o);
			}
			
			setDefaults(props);
		}
		
		l = (TiledMapTileLayer)map.getLayers().get(C.ObjectLayer);
		for (MapObject o : l.getObjects()) {
			MapProperties props = o.getProperties();
			
			Object id = props.get(C.Id);
			if (id != null) {
				objects.put((String)id, o);
			}
			
			setDefaults(props);
		}
		
		return new MapActions(objects, actable, remote);
	}
	
	private MapActions(HashMap<String, MapObject> objects, HashMap<Coord, MapObject> actable, MsgHandler post) {
		this.objects = objects;
		this.actable = actable;
		this.post = post;
	}
	
	public void exit(int x, int y) {
		doCheck(x, y, C.onExit);
	}
	
	public void enter(int x, int y) {
		doCheck(x, y, C.onEnter);
	}
	
	public void activate(int x, int y) {
		doCheck(x, y, C.onActivate);
	}
	
	private void doCheck(int x, int y, String actStr) {
		MapObject obj = actable.get(new Coord(x, y));
		if (obj == null) {
			return;
		}
		
		MapProperties props = obj.getProperties();
		MapAction act = (MapAction)props.get(actStr);
		if (act != null) {
			Object dest = props.get(C.remoteAct);
			if (dest != null) {
				sendRemoteMsg(dest.toString(), act);
			} else {
				act.doit(obj);
			}
		}
	}
	
	private void sendRemoteMsg(String id, MapAction act) {
		if (post == null) {
			System.out.println("Ignored remote message: " + id + ", " + act);
		} else {
			try {
				post.postMessage(new ActionMessage(id, act));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public void checkRemoteMessage() {
		if (post == null) {
			return;
		}
		
		ActionMessage msg = post.recvAction();
		if (msg == null) {
			return;
		}
		
		msg.act.doit(objects.get(msg.id));
	}
	
	private static void setDefaults(MapProperties p) {
		setBoolDefault(p, C.Visible, C.defaultVisible);
		setBoolDefault(p, C.Blocked, C.defaultBlocked);
		setBoolDefault(p, C.Moveable, C.defaultMovable);
	}
	
	private static void setBoolDefault(MapProperties p, String name, boolean def) {
		String str = (String)p.get(name);
		if (str != null) {
			def = Boolean.parseBoolean(str);
		}
		p.put(name, def);
	}
	
	private static boolean makeAction(MapProperties props, String propName) {
		Object pp = props.get(propName);
		if (pp == null) {
			return false;
		}
		
		props.put(propName, MapAction.getAction((String)pp));
		
		return true;
	}
}



















