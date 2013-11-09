package com.karien.tacobox.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.karien.taco.mapstuff.C;
import com.karien.taco.mapstuff.MapActions;

public class Player implements InputProcessor {

	Sprite mSprite;
	Vector2 mPos;
	final MapLayer mCollisionLayer;
	final MapLayer mActionLayer;
	final MapActions mActions;

	Vector2 mVelocity;

	float mSpeed = 2f;

	private final float TILE_WIDTH, TILE_HEIGHT;

	public Player(String spritePath, TiledMap map, MapActions actions) {
		this(spritePath, map, actions, 0, 0);
	}

	public Player(String spritePath, TiledMap map, MapActions actions, int x,
			int y) {

		MapLayers layers = map.getLayers();
		TiledMapTileLayer tiles = (TiledMapTileLayer) layers.get(C.TileLayer);
		TILE_WIDTH = tiles.getTileWidth();
		TILE_HEIGHT = tiles.getTileWidth();

		mCollisionLayer = layers.get(C.ObjectLayer);
		mActionLayer = layers.get(C.ActionLayer);
		mActions = actions;
		mSprite = new Sprite(new Texture(spritePath));
		mPos = new Vector2();
		mVelocity = new Vector2();
		setPosition(x, y);
	}

	public void setPosition(float x, float y) {
		mPos.set(x, y);
		mSprite.setPosition((int) x * TILE_WIDTH, (int) y * TILE_HEIGHT);
	}

	public int getX() {
		return (int) ((int) (mPos.x) * TILE_WIDTH);
	}

	public int getY() {
		return (int) ((int) (mPos.y) * TILE_HEIGHT);
	}

	public boolean isBlocking(MapObject obj) {
		Boolean visible = false;
		Boolean blocked = false;
		// Find object
		MapProperties props = obj.getProperties();
		// Get Properties
		visible = (Boolean) props.get(C.Visible);
		blocked = (Boolean) props.get(C.Blocked);

		return visible && blocked;
	}

	private MapObject findObj(int x, int y) {
		MapObject obj = null;
		for (MapObject object : mCollisionLayer.getObjects()) {
			MapProperties props = object.getProperties();
			if ((Integer) props.get("x") == x && (Integer) props.get("y") == y) {
				obj = object;
				break;
			}
		}
		return obj;
	}

	/**
	 * Try to move to tile at x,y
	 */
	public boolean move(int x, int y) {
		MapObject obj = findObj(x, y);

		if (obj != null && isBlocking(obj)) {
			// See if object is movable
			Boolean moveable = (Boolean) obj.getProperties().get(C.Moveable);
			if (!moveable)
				return false;
			// See if object can be moved
			int obj2X = getX() + 2 * (x - getX());
			int obj2Y = getY() + 2 * (y - getY());
			MapObject obj2 = findObj(obj2X, obj2Y);
			if (obj2 != null && isBlocking(obj2)) {
				// Can't move obj
				return false;
			}

			// Move obj
			obj.getProperties().put("x", obj2X);
			obj.getProperties().put("y", obj2Y);
			// Do exit/enter
			mActions.exit(x, y);
			mActions.enter(obj2X, obj2Y);

		}

		// Move Player
		int oldX = getX();
		int oldY = getY();

		setPosition(x, y);
		// Do exit/enter
		mActions.exit(oldX, oldY);
		mActions.enter(x, y);

		return true;
	}

	public void update(float delta) {
		// Move player
		int curX = getX();
		int curY = getY();
		float X, Y;

		if (mVelocity.x != 0) {
			X = mPos.x + mVelocity.x * delta * mSpeed;
			if ((int) X - curX != 0) {
				move((int) X, curY);
			} else {
				// Update partial position
				setPosition(X, curY);
			}
		}

		if (mVelocity.y != 0) {
			Y = mPos.y + mVelocity.y * delta * mSpeed;
			if ((int) Y - curY != 0) {
				move(curX, (int) Y);
			} else {
				// Update partial position
				setPosition(curX, Y);
			}
		}

		// When the user does an activate:
		// mActions.activate(x, y)
		// When the user enters a square:
		// mActions.enter(x, y)
		// When the user exists a square:
		// mActions.exit(x, y)

	}

	public void draw(SpriteBatch spriteBatch) {
		update(Gdx.graphics.getDeltaTime());
		mSprite.draw(spriteBatch);
	}

	public void dispose() {
		mSprite.getTexture().dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.W:
		case Keys.UP:
			// Move up
			mVelocity.y = 1;
			break;
		case Keys.A:
		case Keys.LEFT:
			// Move left
			mVelocity.x = -1;
			break;
		case Keys.S:
		case Keys.DOWN:
			// Move down
			mVelocity.y = -1;
			break;
		case Keys.D:
		case Keys.RIGHT:
			// Move right
			mVelocity.x = 1;
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.W:
		case Keys.UP:
		case Keys.S:
		case Keys.DOWN:
			// Stop vertical movement
			mVelocity.y = 0;
			break;
		case Keys.A:
		case Keys.LEFT:
		case Keys.D:
		case Keys.RIGHT:
			// Stop horizontal movement
			mVelocity.x = 0;
			break;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
