package com.game.Items;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.game.Game;
import com.game.Screens.PlayScreen;
import com.game.Sprites.Miner;

public class Torch extends Item {
    private static TiledMapTileSet tileSet;
    private final int torchID = 11;
    private float statetime;
    private int animatioFrame;
    private int x;
    private int y;

    public Torch(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        statetime = 0;
        setBounds(getX(), getY(), 16 / Game.PPM,16 / Game.PPM);
        tileSet = screen.getMap().getTileSets().getTileSet("tileset");
        this.x =Math.round(x*Game.PPM/16);
        this.y = Math.round(y*Game.PPM/16);
        this.animatioFrame = 0;
    }


    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setRadius(3/Game.PPM);
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-8, 8).scl(1/Game.PPM);
        vertice[1] = new Vector2(8, 8).scl(1/Game.PPM);
        vertice[2] = new Vector2(-8, -8).scl(1/Game.PPM);
        vertice[3] = new Vector2(8, -8).scl(1/Game.PPM);
        shape.set(vertice);
        fdef.filter.categoryBits = Game.NOTHING_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }



    public void update(float dt, TiledMapTileLayer layer) {
        statetime += dt;

        if(Math.round(3*statetime%3)==animatioFrame) {
            animatioFrame++;

            if(animatioFrame==3){
                animatioFrame = 0;
            }

            layer.getCell(x, y).setTile(tileSet.getTile(torchID + animatioFrame));
        }


    }


    @Override
    public void use(Miner miner) {

    }

}
