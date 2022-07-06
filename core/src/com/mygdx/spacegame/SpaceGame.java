package com.mygdx.spacegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.lang.Math;

public class SpaceGame extends Game
{
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	BitmapFont font;
	static GameManager gameManager;

	@Override
	public void create ()
	{
		gameManager = new GameManager();
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();

		setScreen(new TitleScreen(this));
	}

	/*@Override
	public void render ()
	{

		Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spaceShip.spaceShipUpdate();
	}*/
	
	@Override
	public void dispose ()
	{

		batch.dispose();
		shapeRenderer.dispose();
		font.dispose();
	}
}