package com.mygdx.spacegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class SpaceShip
{
    ShapeRenderer ship;
    ShapeRenderer pointer;
    int health;
    float pointerPositionX;
    float pointerPositionY;
    float spaceShipPositionX;
    float spaceShipPositionY;
    float spaceShipMomentumXEast;
    float spaceShipMomentumXWest;
    float spaceShipMomentumYNorth;
    float spaceShipMomentumYSouth;
    float spaceShipSpeed = 0.02f;
    float northExponent = 0.02f;
    float eastExponent = 0.02f;
    float southExponent = 0.02f;
    float westExponent = 0.02f;
    float spaceShipSpeedCap = 6.00f;
    Sprite shipSprite;
    SpriteBatch batch;
    Texture texture;
    Texture bulletTexture;
    int shipDirection;
    int invincibleTime;
    float bulletXPosition;
    float bulletYPosition;
    float bulletXSpeed = 3;
    float bulletYSpeed = 4;
    float shipShootRate = 4 * Gdx.graphics.getDeltaTime();
    Vector2 position;
    long lastDropTime;
    static Array<Bullet> bullets;

    public SpaceShip()
    {
        batch = new SpriteBatch();
        texture = new Texture("spaceShip.png");
        bulletTexture = new Texture("testCake.png");
        ship = new ShapeRenderer();
        shipSprite = new Sprite(texture);
        pointer = new ShapeRenderer();
        position = new Vector2();
        spaceShipPositionX = 200;
        spaceShipPositionY = 100;
        health = 3;
        bullets = new Array<Bullet>();
    }

    public void spaceShipUpdate()
    {

        //ship.begin(ShapeRenderer.ShapeType.Filled);
        //ship.setColor(0, 1, 0, 1);
        //ship.rect(spaceShipPositionX, spaceShipPositionY, 50, 50);

        spaceShipMovement();
        shipShooting();
        spaceShipBoundaries();
        pointerPosition();

        //Draws the pointer. Used for debugging at the moment.
        pointer.begin(ShapeRenderer.ShapeType.Filled);
        pointer.setColor(1, 0, 0, 1);
        pointer.circle(pointerPositionX, pointerPositionY, 5);
        pointer.end();

        //Draws the ship. If this gets too large, this can be moved into a separate function.

        batch.begin();
        shipSprite.draw(batch);
        shipSprite.setSize(50, 50);
        shipSprite.setPosition(spaceShipPositionX, spaceShipPositionY);
        shipSprite.setOrigin(25, 25);
        batch.end();

        for(Iterator<Bullet> itr = bullets.iterator(); itr.hasNext(); )
        {
            Bullet b = itr.next();

            b.update();

            if(b.position.y > Gdx.graphics.getHeight())
            {
                itr.remove();
            }
        }

    }
    //This needs to be linked to time. Use Gdx.graphics.getDeltaTime(); in the correct position when the time comes.
    //This function controls movement. Using a exponent, when a key is pressed it should gradually increase speed
    //once the key is released, it should lose speed.
    //The speed is capped using spaceShipSpeedCap
    //Each direction has its own movement exponent
    //There possibly will be a global variable titled spaceShipSpeedAdditive possibly.
    public void spaceShipMovement()
    {
        //Movement via touch is only here for debugging.
        if (Gdx.input.isTouched())
        {
            setSpaceShipPositionX(Gdx.input.getX());
            setSpaceShipPositionY(Gdx.graphics.getHeight() - Gdx.input.getY());
        }

        //When a direction is pressed, the first gate it passes through is to check to see if the speed cap is already hit
        //If the ship is under the speed cap, it passes through an exponent which should give the ship a smooth feeling sense of movement.
        //Once the exponent is calculated, the ship is translated by the variable.
        //If no button is pressed, the else statement reduces the momentum of the ship in that specific direction.
        if(Gdx.input.isKeyPressed(Input.Keys.W))
        {
            if (spaceShipMomentumYNorth <= spaceShipSpeedCap)
            {
                spaceShipMomentumYNorth = (float) (Math.pow(2, northExponent)) - (spaceShipMomentumYSouth);
                northExponent = northExponent + 0.1f;
            }
            shipSprite.translateY(spaceShipMomentumYNorth);
            shipDirection = 0;
            shipSprite.setRotation(0);
        }else
        {
            if((spaceShipMomentumYNorth) <= 0.2f && (spaceShipMomentumYNorth) >= -0.2f)
            {
                spaceShipMomentumYNorth = 0;
            }else if (spaceShipMomentumYNorth < -0.2)
            {
                spaceShipMomentumYNorth += 0.2f;
            }else
            {
                spaceShipMomentumYNorth -= 0.1f;
            }
            shipSprite.translateY(spaceShipMomentumYNorth);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S))
        {
            if (spaceShipMomentumYSouth <= spaceShipSpeedCap)
            {
                spaceShipMomentumYSouth = (float) (Math.pow(2, southExponent)) - spaceShipMomentumYNorth;
                southExponent = southExponent + 0.1f;
            }
            shipSprite.translateY(-spaceShipMomentumYSouth);
            shipDirection = 2;
            shipSprite.setRotation(180);
        }else
        {
            if((spaceShipMomentumYSouth) <= 0.2f && (spaceShipMomentumYSouth) >= -0.2f)
            {
                spaceShipMomentumYSouth = 0;
            }else if (spaceShipMomentumYSouth < -0.2)
            {
                spaceShipMomentumYSouth += 0.2f;
            }else
            {
                spaceShipMomentumYSouth -= 0.1f;
            }
            shipSprite.translateY(-spaceShipMomentumYSouth);
        }
        spaceShipPositionY = shipSprite.getY();


        if(Gdx.input.isKeyPressed(Input.Keys.A))
        {
            if (spaceShipMomentumXWest <= spaceShipSpeedCap)
            {
                spaceShipMomentumXWest = (float) (Math.pow(2, westExponent)) - spaceShipMomentumXEast;
                westExponent = westExponent + 0.1f;
            }
            shipSprite.translateX(-spaceShipMomentumXWest);
            shipDirection = 3;
            shipSprite.setRotation(90);
        }else
        {
            if((spaceShipMomentumXWest) <= 0.2f && (spaceShipMomentumXWest) >= -0.2f)
            {
                spaceShipMomentumXWest = 0;
            }else if (spaceShipMomentumXWest < -0.2)
            {
                spaceShipMomentumXWest += 0.2f;
            }else
            {
                spaceShipMomentumXWest -= 0.1f;
            }
            shipSprite.translateX(-spaceShipMomentumXWest);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D))
        {
            if (spaceShipMomentumXEast <= spaceShipSpeedCap)
            {
                spaceShipMomentumXEast = (float) (Math.pow(2, eastExponent)) - spaceShipMomentumXWest;
                eastExponent = eastExponent + 0.1f;
            }
            shipSprite.translateX(spaceShipMomentumXEast);
            shipDirection = 1;
            shipSprite.setRotation(270);
        }else
        {
            if((spaceShipMomentumXEast) <= 0.2f && (spaceShipMomentumXEast) >= -0.2f)
            {
                spaceShipMomentumXEast = 0;
            }else if (spaceShipMomentumXEast < -0.2)
            {
                spaceShipMomentumXEast += 0.2f;
            }else
            {
                spaceShipMomentumXEast -= 0.1f;
            }
            shipSprite.translateX(spaceShipMomentumXEast);
        }

        spaceShipPositionX = shipSprite.getX();

        //Every exponent after every cycle should go down as long it is above 0
        if (northExponent >= 0)
        {
            northExponent -= 0.03f;
        }

        if (eastExponent >= 0)
        {
            eastExponent -= 0.03f;
        }

        if (southExponent >= 0)
        {
            southExponent -= 0.03f;
        }

        if (westExponent >= 0)
        {
            westExponent -= 0.03f;
        }
    }

    public void pointerPosition()
    {
        switch (shipDirection)
        {
            case 0:
                pointerPositionY = spaceShipPositionY + 65;
                pointerPositionX = spaceShipPositionX + 25;
                break;
            case 1:
                pointerPositionY = spaceShipPositionY + 25;
                pointerPositionX = spaceShipPositionX + 65;
                break;
            case 2:
                pointerPositionY = spaceShipPositionY - 15;
                pointerPositionX = spaceShipPositionX + 25;
                break;
            case 3:
                pointerPositionY = spaceShipPositionY + 25;
                pointerPositionX = spaceShipPositionX - 15;
                break;
            default:
                System.out.print("This is a bug!");
                break;
        }
    }

    //Yoooo it works :O
    //When the F key is pressed, a new Bullet is created, which is then added to the bullets array.
    //See the class Bullet
    public void shipShooting()
    {
        if(Gdx.input.isKeyPressed(Input.Keys.F) )
        {
            Bullet b = new Bullet();
            b.position.set(pointerPositionX, pointerPositionY);
            bullets.add(b);
        }
    }

    public void spaceShipBoundaries()
    {
        if (spaceShipPositionX >= Gdx.graphics.getWidth())
        {
            setSpaceShipPositionX(0);
            //setSpaceShipPositionY(Gdx.graphics.getHeight() - spaceShipPositionY);
        }
        if (spaceShipPositionX <= -25)
        {
            setSpaceShipPositionX(Gdx.graphics.getWidth());
           //setSpaceShipPositionY(Gdx.graphics.getHeight() - spaceShipPositionY);
        }
        if (spaceShipPositionY >= Gdx.graphics.getHeight())
        {
            setSpaceShipPositionY(0);
            //setSpaceShipPositionX(Gdx.graphics.getWidth() - spaceShipPositionX);
        }
        if (spaceShipPositionY <= -25)
        {
            setSpaceShipPositionY(Gdx.graphics.getHeight());
        }
    }

    public void spaceShipHitCheck()
    {
        if (GameScreen.isPlayerWasHit() == true)
        {
            float tempStartTime = System.nanoTime();
            float tempElapsedTime = 0;
            for (int i = 0; tempElapsedTime < 3; i = (int) (i + (System.nanoTime() - tempStartTime)))
            {
                if (SpaceGame.gameManager.getElapsedTime() % 1 == 0)
                {
                    tempElapsedTime++;
                    invincibleTime++;
                }
            }
        }
        health--;
        invincibleTime = 0;
        GameScreen.setPlayerWasHit(false);
    }

    public ShapeRenderer getTriangle() {
        return ship;
    }

    public void setTriangle(ShapeRenderer triangle) {
        this.ship = triangle;
    }

    public float getSpaceShipPositionX() {

        return spaceShipPositionX;
    }

    public void setSpaceShipPositionX(float spaceShipPositionX) {
        this.spaceShipPositionX = spaceShipPositionX;
    }

    public float getSpaceShipPositionY() {
        return spaceShipPositionY;
    }

    public void setSpaceShipPositionY(float spaceShipPositionY) {
        this.spaceShipPositionY = spaceShipPositionY;
    }

    public float getSpaceShipSpeed() {
        return spaceShipSpeed;
    }

    public void setSpaceShipSpeed(float spaceShipSpeed) {
        this.spaceShipSpeed = spaceShipSpeed;
    }

    public void spaceShipDispose()
    {
        //ship.dispose();
        batch.dispose();
        texture.dispose();
    }

    public int getShipDirection()
    {
        return shipDirection;
    }

    public void setShipDirection(int shipDirection)
    {
        this.shipDirection = shipDirection;
    }

    public float getBulletXPosition()
    {
        return bulletXPosition;
    }

    public void setBulletXPosition(float bulletXPosition)
    {
        this.bulletXPosition = bulletXPosition;
    }

    public float getBulletYPosition()
    {
        return bulletYPosition;
    }

    public void setBulletYPosition(float bulletYPosition)
    {
        this.bulletYPosition = bulletYPosition;
    }

    public float getBulletXSpeed()
    {
        return bulletXSpeed;
    }

    public void setBulletXSpeed(float bulletXSpeed)
    {
        this.bulletXSpeed = bulletXSpeed;
    }

    public float getBulletYSpeed()
    {
        return bulletYSpeed;
    }

    public void setBulletYSpeed(float bulletYSpeed)
    {
        this.bulletYSpeed = bulletYSpeed;
    }
    public int getHealth()
    {
        return health;
    }

    public void setHealth(int health)
    {
        this.health = health;
    }

    public int getInvincibleTime()
    {
        return invincibleTime;
    }

    public void setInvincibleTime(int invincibleTime)
    {
        this.invincibleTime = invincibleTime;
    }

    //Class that controls how bullets function. Vector2 is used to calculate its position. Currently ShapeRenderer is
    //used to render the actual bullet. A sprite will be used in the future, as well as changing the ShapeRenderer to
    //an actual sprite.
    class Bullet
    {
        public Vector2 position;
        public float velocity;
        ShapeRenderer bulletCircle;
        int bulletDirection;
        //When Bullet is created, it grabs the current ship direction to decide where the bullet will be shot.
        Bullet()
        {
            position = new Vector2();
            velocity = 20f;
            bulletCircle = new ShapeRenderer();
            bulletDirection = shipDirection;
        }

        public void update()
        {
            switch (bulletDirection)
            {
                case 0:
                    position.y += velocity;
                    break;
                case 1:
                    position.x += velocity;
                    break;
                case 2:
                    position.y -= velocity;
                    break;
                case 3:
                    position.x -= velocity;
                    break;
                default:
                    System.out.print("This is a bug!");
                    break;
            }
            bulletCircle.begin(ShapeRenderer.ShapeType.Filled);
            bulletCircle.setColor(1, 0, 0, 1);
            bulletCircle.circle(position.x, position.y, 5);
            bulletCircle.end();

        }

        public Vector2 getPosition()
        {
            return position;
        }

        public void setPosition(Vector2 position)
        {
            this.position = position;
        }
    }
}
