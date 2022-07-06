package com.mygdx.spacegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import static com.mygdx.spacegame.GameScreen.spaceShip;

public class Asteroid
{
    ShapeRenderer largeAsteroid;
    float asteroidX = 300;
    float asteroidY = 150;
    float circleRadius = 20;
    float xSpeed = 4;
    float ySpeed = 3;
    boolean largeAsteroidHit = false;
    boolean asteroidHitShip = false;
    Random random;

    public Asteroid()
    {
        random = new Random();
        largeAsteroid = new ShapeRenderer();
        asteroidX = random.nextInt(Gdx.graphics.getWidth() / 2) + asteroidX;
        asteroidY = random.nextInt(Gdx.graphics.getHeight() / 2) + asteroidY;
    }

    public void asteroidRender()
    {
        largeAsteroid.begin(ShapeRenderer.ShapeType.Filled);
        largeAsteroid.setColor(0, 1, 0, 1);
        largeAsteroid.rect(asteroidX, asteroidY, 25, 25);
        largeAsteroid.end();

        asteroidX += xSpeed;
        asteroidY += ySpeed;

        if (asteroidX < 0 || asteroidX > Gdx.graphics.getWidth()) {
            xSpeed *= -1;
        }

        if (asteroidY < 0 || asteroidY > Gdx.graphics.getHeight()) {
            ySpeed *= -1;
        }

        if (Vector2.dst(spaceShip.spaceShipPositionX, spaceShip.spaceShipPositionY, asteroidX, asteroidY) < circleRadius)
        {
            asteroidHitShip = true;
        }

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                int renderY = Gdx.graphics.getHeight() - y;
                if (Vector2.dst(asteroidX, asteroidY, x, renderY) < circleRadius) {
                    largeAsteroidHit = true;
                }
                return true;
            }
        });
    }

    public float getAsteroidX()
    {
        return asteroidX;
    }

    public void setAsteroidX(float asteroidX)
    {
        this.asteroidX = asteroidX;
    }

    public float getAsteroidY()
    {
        return asteroidY;
    }

    public void setAsteroidY(float asteroidY)
    {
        this.asteroidY = asteroidY;
    }

    public float getCircleRadius()
    {
        return circleRadius;
    }

    public void setCircleRadius(float circleRadius)
    {
        this.circleRadius = circleRadius;
    }

    public float getxSpeed()
    {
        return xSpeed;
    }

    public void setxSpeed(float xSpeed)
    {
        this.xSpeed = xSpeed;
    }

    public float getySpeed()
    {
        return ySpeed;
    }

    public void setySpeed(float ySpeed)
    {
        this.ySpeed = ySpeed;
    }

    public boolean isLargeAsteroidHit()
    {
        return largeAsteroidHit;
    }

    public boolean isAsteroidHitShip()
    {
        return asteroidHitShip;
    }

    public void setAsteroidHitShip(boolean asteroidHitShip)
    {
        this.asteroidHitShip = asteroidHitShip;
    }

    public void setLargeAsteroidHit(boolean largeAsteroidHit)
    {
        this.largeAsteroidHit = largeAsteroidHit;
    }
    public void asteroidDispose()
    {
        largeAsteroid.dispose();
    }
}
