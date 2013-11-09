package com.karien.tacobox.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
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

	// public boolean isBlocked(int x, int y) {
	// MapProperties props = mCollisionLayer.getCell(x, y).getTile()
	// .getProperties();
	// Boolean visible = (Boolean) props.get(C.Visible);
	// Boolean blocked = (Boolean) props.get(C.Blocked);
	// return visible && blocked;
	// }

	public void update(float delta) {
		//Move player
		if (mVelocity.x != 0) {
			// TODO: check collision
			setPosition(mPos.x + mVelocity.x * delta * mSpeed, mPos.y);
		}

		if (mVelocity.y != 0) {
			// TODO: check collision
			setPosition(mPos.x, mPos.y + mVelocity.y * delta * mSpeed);
		}
		
		// When the user does an activate: 
		//   mActions.activate(x, y)
		// When the user enters a square:
		//   mActions.enter(x, y)
		// When the user exists a square:
		//   mActions.exit(x, y)
		
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
