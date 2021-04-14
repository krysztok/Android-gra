package com.game.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Game;
import com.game.Screens.PlayScreen;
import com.game.Sprites.Miner;

public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen, float x, float y){
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x + 8 / Game.PPM, y + 8 / Game.PPM);
        defineItem();
        toDestroy = false;
        destroyed = false;

    }

    public abstract void defineItem();
    public abstract void use(Miner miner);

    public void update(float dt){
        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        }
    }
}
