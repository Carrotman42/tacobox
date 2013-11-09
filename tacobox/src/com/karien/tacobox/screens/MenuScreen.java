package com.karien.tacobox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.karien.tacobox.MyTacoBox;

public class MenuScreen implements Screen {
	private final MyTacoBox parent;

	private Stage stage;
	private Skin skin;
	Label titleLabel;

	public MenuScreen(MyTacoBox callback) {
		parent = callback;
	}

	public void createUI() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		// A skin can be loaded via JSON or defined programmatically, either is
		// fine. Using a skin is optional but strongly
		// recommended solely for the convenience of getting a texture, region,
		// etc as a drawable, tinted drawable, etc.
		skin = new Skin();

		// Store the default libgdx font under the name "default".
		BitmapFont font = new BitmapFont();
		font.setScale(2f);
		skin.add("default", font);

		// Configure a LabelStyle and name it default
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("default");
		labelStyle.fontColor = Color.YELLOW;
		skin.add("default", labelStyle);

		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.font = skin.getFont("default");
		buttonStyle.fontColor = Color.YELLOW;
		skin.add("default", buttonStyle);

		// Create a table that fills the screen. Everything else will go inside
		// this table.
		Table container = new Table();
		container.debug();
		container.align(Align.left | Align.top);
		container.setFillParent(true);
		stage.addActor(container);

		// Create labels
		titleLabel = new Label("TacoBox", skin);
		titleLabel.setFontScale(3f);
		titleLabel.setAlignment(Align.center);

		// create buttons
		TextButton quitBtn = new TextButton("Quit", skin);
		quitBtn.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				throw new RuntimeException("Quit");
			}
		});

		TextButton hostBtn = new TextButton("Host", skin);
		hostBtn.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				parent.menuChoice("host");
			}
		});

		TextButton joinBtn = new TextButton("Join", skin);
		joinBtn.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				parent.menuChoice("join");
			}
		});

		container.add(titleLabel).align(Align.center).expandX();
		container.row();
		container.add(hostBtn).align(Align.center | Align.bottom).expand()
				.pad(10);
		container.row();
		container.add(joinBtn).align(Align.center).expandX().pad(10);
		container.row();
		container.add(quitBtn).align(Align.center | Align.top).expand().pad(10);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();

		// Table.drawDebug(stage); // This is optional, but enables debug lines
		// for tables.
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

	@Override
	public void show() {
		createUI();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

}
