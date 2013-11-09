package com.karien.taco.mapstuff;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

public enum MapAction {
	Appear {
		@Override
		void doit(MapObject obj) {
			obj.getProperties().put(C.Visisble, true);
		}
	}, Disapear {
		@Override
		void doit(MapObject obj) {
			obj.getProperties().put(C.Visisble, false);
		}
	}, Toggle {
		@Override
		void doit(MapObject obj) {
			MapProperties p = obj.getProperties();
			p.put(C.Visisble, p.get(C.Visisble, boolean.class));
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
