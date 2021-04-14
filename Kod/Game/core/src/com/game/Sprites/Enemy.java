package com.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;
    public boolean runningLeft;
    public boolean setToDestroy;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1, 0);
        b2body.setActive(false);
        setToDestroy = false;
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void onHit();

    public void reverseVelocity(boolean x, boolean y){
        if(x){
            velocity.x = -velocity.x;
        }

        if(y){
            velocity.y = -velocity.y;
        }
    }

    public abstract void hitBySpikes();

    public void changeRunningDirection(){
        if(runningLeft){
            runningLeft = false;
        }
        else{
            runningLeft = true;
        }
    }
}

