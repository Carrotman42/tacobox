package com.karien.tacobox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.karien.taco.mapstuff.C;
import com.karien.taco.mapstuff.MapActions;
import com.karien.tacobox.MyTacoBox;
import com.karien.tacobox.entities.Player;

public class MainScreen implements Screen {

	private final TiledMap map;
	private final MapActions acts;
	private final MyTacoBox callback;
	
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;

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
		
		if (mPlayer.getX() == (Integer)map.getProperties().get("goalX") && mPlayer.getY() == (Integer)map.getProperties().get("goalY")){
			callback.goalReached();
		}
		
	}

	public void drawObjects(int layerID) {
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
		camera.viewportWidth = width;
		camera.viewportHeight = height;
	}

	@Override
	public void show() {
		renderer = new OrthogonalTiledMapRenderer(map);
		camera = new OrthographicCamera();

		mPlayer = new Player("player.png", map, acts, (Integer) map
				.getProperties().get(C.SpawnX), (Integer) map.getProperties()
				.get(C.SpawnY));

		camera.position.set(mPlayer.getX() * mPlayer.TILE_WIDTH, mPlayer.getY()
				* mPlayer.TILE_HEIGHT, 0);

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
