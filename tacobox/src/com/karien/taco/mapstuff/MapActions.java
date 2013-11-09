package com.karien.taco.mapstuff;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.maps.MapLayer;
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
	 * @param remote
	 *            Remote endpoint to send data to if we get a remote action. May
	 *            be null.
	 * @return
	 */
	public static MapActions procActions(TiledMap map, MsgHandler remote) {
		// Mutate the spawn/x/y/goalx/y so that (0,0) is bottom left
		MapProperties p = map.getProperties();

		String sgx = (String) p.get(C.GoalX);
		String sgy = (String) p.get(C.GoalY);
		String ssx = (String) p.get(C.SpawnX);
		String ssy = (String) p.get(C.SpawnY);
		
		if (sgx == null || sgy == null || ssx == null || ssy == null) {
			throw new RuntimeException("Didn't set goal/spawn correctly!");
		}
		
		int height = ((TiledMapTileLayer)map.getLayers().get(C.TileLayer)).getHeight();

		p.put(C.GoalX, Integer.parseInt(sgx));
		p.put(C.GoalY, height - Integer.parseInt(sgy));
		p.put(C.SpawnX, Integer.parseInt(ssx));
		p.put(C.SpawnY, height - Integer.parseInt(ssy));
		
		// Now process the action objects
		HashMap<String, MapObject> objects = new HashMap<String, MapObject>();
		HashMap<Coord, MapObject> actable = new HashMap<Coord, MapObject>();

		MapLayer l = map.getLayers().get(C.ActionLayer);
		for (MapObject o : l.getObjects()) {
			MapProperties props = o.getProperties();

			boolean hadAny = makeAction(props, C.onExit)
					| // Note: Can't do the short-circuiting || operator!
					makeAction(props, C.onActivate)
					| makeAction(props, C.onEnter);

			if (hadAny) {
				actable.put(
						new Coord((Integer) props.get("x"), (Integer) props
								.get("y")), o);
			}
			
			Object id = props.get(C.Id);
			if (id != null) {
				objects.put((String) id, o);
			}

			setDefaults(props);
		}

		l = map.getLayers().get(C.ObjectLayer);
		for (MapObject o : l.getObjects()) {
			MapProperties props = o.getProperties();

			Object id = props.get(C.Id);
			if (id != null) {
				objects.put((String) id, o);
			}

			setDefaults(props);
		}

		return new MapActions(objects, actable, remote);
	}

	private MapActions(HashMap<String, MapObject> objects,
			HashMap<Coord, MapObject> actable, MsgHandler post) {
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
		if (!(Boolean)props.get(C.Visible)) {
			return;
		}
		
		
		ActionAction act = (ActionAction) props.get(actStr);
		if (act != null) {
			if (act.remote) {
				sendRemoteMsg(act.targetId, act.act);
			} else {
				MapObject dest = objects.get(act.targetId);
				act.act.doit(dest);
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
		setBoolDefault(p, C.Moveable, C.defaultMoveable);
	}

	private static void setBoolDefault(MapProperties p, String name, boolean def) {
		String str = (String) p.get(name);
		if (str != null) {
			def = Boolean.parseBoolean(str);
		}
		p.put(name, def);
	}

	private static boolean makeAction(MapProperties props, String propName) {
		String pp = (String)props.get(propName);
		if (pp == null) {
			return false;
		}
		
		char location = pp.charAt(0);
		int colon = pp.indexOf(':');
		if (location != 'r' && location != 'l' || colon == -1) {
			throw new RuntimeException("Bad action string: " + pp);
		}
		
		props.put(propName, new ActionAction(location == 'r', pp.substring(1, colon), MapAction.valueOf(pp.substring(colon+1))));

		return true;
	}
}
