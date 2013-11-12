package com.karien.tacobox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.karien.taco.mapstuff.C;
import com.karien.taco.mapstuff.MapActions;
import com.karien.tacobox.MyTacoBox;
import com.karien.tacobox.entities.Player;

public class MainScreen implements Screen, GestureListener {

	private final TiledMap map;
	private final MapActions acts;
	private final MyTacoBox callback;

	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private int screenWidth;
	private int screenHeight;
	private float lastZoomDistance;

	Player mPlayer;

	public MainScreen(Level lvl) {
		this.map = lvl.map;
		this.acts = lvl.acts;
		this.callback = lvl.parent;
	}

	@Override
	public void render(float delta) {
		camera.position.lerp(new Vector3(mPlayer.getX() * mPlayer.TILE_WIDTH,
				mPlayer.getY() * mPlayer.TILE_HEIGHT, 0), 0.5f);
		camera.update();

		renderer.setView(camera);

		renderer.getSpriteBatch().begin();
		renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(
				C.TileLayer));
		drawObjects(C.ActionLayer);
		mPlayer.draw(renderer.getSpriteBatch());
		drawObjects(C.ObjectLayer);
		renderer.getSpriteBatch().end();

		if (mPlayer.getX() == (Integer) map.getProperties().get("goalX")
				&& mPlayer.getY() == (Integer) map.getProperties().get("goalY")) {
			callback.goalReached();
		}

		acts.checkRemoteMessage();
	}

	public void drawObjects(String layerID) {
		for (MapObject obj : map.getLayers().get(layerID).getObjects()) {
			MapProperties props = obj.getProperties();
			if ((Boolean) props.get(C.Visible)) {
				int gid = (Integer) props.get("gid");
				TiledMapTile tile = map.getTileSets().getTile(gid);
				renderer.getSpriteBatch().draw(tile.getTextureRegion(),
						(Integer) props.get("x"), (Integer) props.get("y"));
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		screenWidth = width;
		screenHeight = height;
		camera.viewportWidth = width;
		camera.viewportHeight = height;
	}

	@Override
	public void show() {
		renderer = new OrthogonalTiledMapRenderer(map);
		camera = new OrthographicCamera();

		mPlayer = new Player(new String[] { "man_back.png", "man_front.png",
				"man_right.png", "man_left.png" }, map, acts, (Integer) map
				.getProperties().get(C.SpawnX), (Integer) map.getProperties()
				.get(C.SpawnY));

		camera.position.set(mPlayer.getX() * mPlayer.TILE_WIDTH, mPlayer.getY()
				* mPlayer.TILE_HEIGHT, 0);

		Gdx.input.setInputProcessor(new InputMultiplexer(new GestureDetector(
				this), mPlayer, new GestureDetector(mPlayer)));
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		mPlayer.dispose();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		Vector3 touchPt = new Vector3(x, y, 0);
		camera.unproject(touchPt);
		Vector2 tileTouch = new Vector2((int) (touchPt.x / mPlayer.TILE_WIDTH),
				(int) (touchPt.y / mPlayer.TILE_HEIGHT));

		System.out.println(String.format(
				"touched Screen: (%f, %f) World: (%f,%f) Tiles: (%f,%f)", x, y,
				touchPt.x, touchPt.y, tileTouch.x, tileTouch.y));

		// translate touch into movement
		Vector2 dir = new Vector2();
		if (mPlayer.getX() - tileTouch.x > 1) {
			dir.x = -1;
		} else if (tileTouch.x - mPlayer.getX() > 1) {
			dir.x = 1;
		}

		if (mPlayer.getY() - tileTouch.y > 1) {
			dir.y = 1;
		} else if (tileTouch.y - mPlayer.getY() > 1) {
			dir.y = -1;
		}

		if (dir.y != 0) {
			if (dir.y == 1) {
				mPlayer.keyDown(Keys.DOWN);
			} else {
				mPlayer.keyDown(Keys.UP);
			}
		}

		if (dir.x != 0) {
			if (dir.x == 1) {
				mPlayer.keyDown(Keys.RIGHT);
			} else {
				mPlayer.keyDown(Keys.LEFT);
			}
		}

		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		if (Math.abs(initialDistance - distance) < 20) {
			lastZoomDistance = distance;
		}

		if (lastZoomDistance != distance) {
			camera.zoom -= (distance - lastZoomDistance) / 1000f;
			if (camera.zoom < 0) {
				camera.zoom = -camera.zoom;
			}
		}

		lastZoomDistance = distance;
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

}
