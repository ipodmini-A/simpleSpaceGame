package com.mygdx.spacegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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
	
	@Override
	public void dispose ()
	{

		batch.dispose();
		shapeRenderer.dispose();
		font.dispose();
	}
}