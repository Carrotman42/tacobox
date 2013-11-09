package com.karien.tacobox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.karien.taco.mapstuff.C;
import com.karien.tacobox.entities.Player;

public class MainScreen implements Screen {

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;

	Player mPlayer;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.position.lerp(new Vector3(mPlayer.getX(), mPlayer.getY(), 0),
				0.5f);
		camera.update();

		renderer.setView(camera);

		renderer.getSpriteBatch().begin();
		renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(
				C.TileLayer));
		for (MapObject obj : map.getLayers().get(C.ActionLayer).getObjects()) {
			int gid = (Integer) obj.getProperties().get("gid");
			TiledMapTile tile = map.getTileSets().getTile(gid);
			renderer.getSpriteBatch().draw(tile.getTextureRegion(),
					(Integer) obj.getProperties().get("x"),
					(Integer) obj.getProperties().get("y"));
		}
		mPlayer.draw(renderer.getSpriteBatch());
		for (MapObject obj : map.getLayers().get(C.ObjectLayer).getObjects()) {
			int gid = (Integer) obj.getProperties().get("gid");
			TiledMapTile tile = map.getTileSets().getTile(gid);
			renderer.getSpriteBatch().draw(tile.getTextureRegion(),
					(Integer) obj.getProperties().get("x"),
					(Integer) obj.getProperties().get("y"));
		}
		renderer.getSpriteBatch().end();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
	}

	@Override
	public void show() {
		map = new TmxMapLoader().load("maps/lightTest.tmx");
		renderer = new OrthogonalTiledMapRenderer(map);
		camera = new OrthographicCamera();

		mPlayer = new Player("player.png", map);

		Gdx.input.setInputProcessor(mPlayer);
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		mPlayer.dispose();
	}

}
