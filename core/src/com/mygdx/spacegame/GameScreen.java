package com.mygdx.spacegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;


public class GameScreen extends ScreenAdapter {

    SpaceGame game;
    static SpaceShip spaceShip;
    static Asteroid[] asteroid;
    boolean asteroidsOff = false;
    static boolean playerWasHit = false;
    public GameScreen(SpaceGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        spaceShip = new SpaceShip();
        asteroid = new Asteroid[5];
        asteroid[0] = new Asteroid();
        asteroid[1] = new Asteroid();
        asteroid[2] = new Asteroid();
        asteroid[3] = new Asteroid();
        asteroid[4] = new Asteroid();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spaceShip.spaceShipUpdate();

        if (Gdx.input.isKeyPressed(Input.Keys.P))
        {
            asteroidsOff = !asteroidsOff;
        }

        if (!asteroidsOff)
        {
            for (int i = 0; i < asteroid.length; i++)
            {
                if (asteroid[i].isLargeAsteroidHit() == false)
                {
                    asteroid[i].asteroidRender();
                }
            }
        }

        game.batch.begin();
        game.font.draw(game.batch, "Debug:", Gdx.graphics.getWidth() * .05f, Gdx.graphics.getHeight() * .95f);
        game.font.draw(game.batch, "Ship X Position:" + spaceShip.getSpaceShipPositionX(), Gdx.graphics.getWidth() * .05f, Gdx.graphics.getHeight() * .85f);
        game.font.draw(game.batch, "Ship Y Position:" + spaceShip.getSpaceShipPositionY(), Gdx.graphics.getWidth() * .05f, Gdx.graphics.getHeight() * .75f);
        game.font.draw(game.batch, "Ship Direction:" + spaceShip.getShipDirection(), Gdx.graphics.getWidth() * .05f, Gdx.graphics.getHeight() * .65f);
        game.font.draw(game.batch, "Bullet X Position" + spaceShip.getBulletXPosition(), Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * .55f);
        game.font.draw(game.batch, "Bullet Y Position" + spaceShip.getBulletYPosition(), Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * .45f);
        game.font.draw(game.batch, "Time Elapsed:" + game.gameManager.getElapsedTime(), Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * .35f);

        game.batch.end();

        if (asteroid[0].isLargeAsteroidHit() == true && asteroid[1].isLargeAsteroidHit() == true && asteroid[2].isLargeAsteroidHit() == true
        && asteroid[3].isLargeAsteroidHit() == true && asteroid[4].isLargeAsteroidHit() == true)
        {
            game.setScreen(new EndScreen(game));
        }

        for (int k = 0; k < asteroid.length; k++)
        {
            if (asteroid[k].isAsteroidHitShip() == true)
            {
                playerWasHit = true;
                game.setScreen(new EndScreen(game));
            }
        }

        for (int i = 0; i < SpaceShip.bullets.size; i++)
        {
            for (int j = 0; j < asteroid.length; j++)
            {
                if (Vector2.dst(SpaceShip.bullets.get(i).position.x, SpaceShip.bullets.get(i).position.y, asteroid[j].asteroidX, asteroid[j].asteroidY) < 10)
                {
                    asteroid[j].setLargeAsteroidHit(true);
                }
            }
        }

    }

    public static boolean isPlayerWasHit()
    {
        return playerWasHit;
    }

    public static void setPlayerWasHit(boolean playerWasHit)
    {
        GameScreen.playerWasHit = playerWasHit;
    }

    @Override
    public void hide()
    {
        spaceShip.spaceShipDispose();
        Gdx.input.setInputProcessor(null);
    }
}