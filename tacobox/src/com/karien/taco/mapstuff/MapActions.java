package com.karien.taco.mapstuff;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;


public class MapActions {
	private final HashMap<Coord, Coord> acts;
	
	private MapActions(HashMap<Coord, Coord> acts) {
		this.acts = acts;
	}
	
	public Coord get(int x, int y) {
		return acts.get(new Coord(x, y));
	}
	
	public static MapActions loadActions(InputStream data, TiledMap map) {
		Scanner sc = new Scanner(data);

		HashMap<Coord, Coord> acts = new HashMap<Coord, Coord>();
		RuntimeException ex = null;
		MapLayer lay = map.getLayers().get("haveactions");
		while (sc.hasNextLine()) {
			String line = sc.nextLine().trim();
			if (line.startsWith("//") || line.length() == 0) {
				continue;
			}
			
			int colon = line.indexOf(':');
			if (colon == -1) {
				ex = new RuntimeException("Invalid line format (no colon): " + line);
				break;
			}
			
			String piece = line.substring(0, colon);
			int comma = piece.indexOf(',');
			if (comma == -1) {
				ex = new RuntimeException("Invalid line format (no left comma): " + line);
				break;
			}
			
			Coord left;
			try {
				left = new Coord(
						Integer.parseInt(piece.substring(0, comma).trim()), 
						Integer.parseInt(piece.substring(comma+1).trim())
				);
			}catch (NumberFormatException x) {
				ex = new RuntimeException("Invalid line format (bad integer): " + line);
				break;
			}
			
			piece = line.substring(colon + 1);
			comma = piece.indexOf(',');
			if (comma == -1) {
				ex = new RuntimeException("Invalid line format (no right comma): " + line);
				break;
			}
			
			Coord right;
			try {
				right = new Coord(
						Integer.parseInt(piece.substring(0, comma).trim()), 
						Integer.parseInt(piece.substring(comma+1).trim())
				);
			}catch (NumberFormatException x) {
				ex = new RuntimeException("Invalid line format (bad integer): " + line);
				break;
			}
			
			// TODO: Check to make sure that the associated map has an actionable item there
			//    helps find bugs with this action file quickly.
			acts.put(left, right);
		}
		
		sc.close();
		
		if (ex != null) {
			throw ex;
		}
		return null;
	}
}
