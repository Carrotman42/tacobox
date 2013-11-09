package com.karien.taco.mapstuff;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

public enum MapAction {
	Appear {
		@Override
		void doit(MapObject obj) {
			obj.getProperties().put(C.Visible, true);
		}
	}, Disapear {
		@Override
		void doit(MapObject obj) {
			obj.getProperties().put(C.Visible, false);
		}
	}, Toggle {
		@Override
		void doit(MapObject obj) {
			MapProperties p = obj.getProperties();
			p.put(C.Visible, p.get(C.Visible, boolean.class));
		}
	};
	
	abstract void doit(MapObject obj);
	
	static MapAction getAction(String str) {
		if (str.equals(C.Appear)) {
			return Appear;
		} if (str.equals(C.Disappear)) {
			return Disapear;
		} if (str.equals(C.Toggle)) {
			return Toggle;
		}
		throw new RuntimeException("Unknown action: " + str);
	}
}
