package com.mygdx.spacegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;


public class GameScreen extends ScreenAdapter {

    SpaceGame game;
    static SpaceShip spaceShip;
    static Asteroid[] asteroid;
    boolean asteroidsOff = false;
    static boolean playerIsDead = false;
    static boolean playerWasHit = false;
    static boolean playerIsInvincible = false;
    public GameScreen(SpaceGame game) {
        this.game = game;
    }
    static int asteroidsHit = 0;

    @Override
    public void show() {
        spaceShip = new SpaceShip();
        asteroid = new Asteroid[SpaceGame.gameManager.getStages()];
        for (int i = 0; i < SpaceGame.gameManager.getStages(); i++)
        {
            asteroid[i] = new Asteroid();
        }
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
                if (asteroid[i].isShowAsteroid() == true)
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
        game.font.draw(game.batch, "Health:" + spaceShip.getHealth(), Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * .25f);
        game.font.draw(game.batch, "AsteroidsHit:" + asteroidsHit, Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * .15f);

        game.batch.end();

        for (int i = 0; i < SpaceGame.gameManager.getStages(); i++)
        {
            if (asteroid[i].isShowAsteroid() == false)
            {
                asteroid[i].setAsteroidX(-1000);
                asteroid[i].setAsteroidY(-1000);
            }
        }

        for (int i = 0; i < SpaceGame.gameManager.getStages(); i++)
        {
            if (asteroid[i].isLargeAsteroidHit() == true)
            {
                asteroidsHit++;
                asteroid[i].setLargeAsteroidHit(false);
                asteroid[i].setShowAsteroid(false);
                asteroid[i].setLargeAsteroidHit(false);
            }
            if (asteroidsHit >= SpaceGame.gameManager.getStages())
            {
                game.setScreen(new EndScreen(game));
            }
        }

        for (int k = 0; k < asteroid.length; k++)
        {
            if (asteroid[k].isAsteroidHitShip() == true)
            {
                spaceShip.setHealth(spaceShip.getHealth() - 1);
                asteroid[k].setAsteroidHitShip(false);
                asteroid[k].setShowAsteroid(false);
                asteroidsHit++;
            }
            if (spaceShip.getHealth() == 0 && playerIsInvincible == false)
            {
                playerIsDead = true;
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
                    asteroid[j].setShowAsteroid(false);
                }
            }
        }

    }

    public static boolean isPlayerIsDead()
    {
        return playerIsDead;
    }

    public static void setPlayerIsDead(boolean playerIsDead)
    {
        GameScreen.playerIsDead = playerIsDead;
    }

    public static boolean isPlayerWasHit()
    {
        return playerWasHit;
    }

    public static void setPlayerWasHit(boolean playerWasHit)
    {
        GameScreen.playerWasHit = playerWasHit;
    }

    public static boolean isPlayerIsInvincible()
    {
        return playerIsInvincible;
    }

    public static void setPlayerIsInvincible(boolean playerIsInvincible)
    {
        GameScreen.playerIsInvincible = playerIsInvincible;
    }

    public static int getAsteroidsHit()
    {
        return asteroidsHit;
    }

    public static void setAsteroidsHit(int asteroidsHit)
    {
        GameScreen.asteroidsHit = asteroidsHit;
    }

    @Override
    public void hide()
    {
        spaceShip.spaceShipDispose();
        Gdx.input.setInputProcessor(null);
    }
}