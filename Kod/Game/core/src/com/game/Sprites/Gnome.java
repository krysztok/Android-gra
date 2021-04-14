package com.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.game.Game;
import com.game.Screens.PlayScreen;

public class Gnome extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    private float statetime;
    private Animation animation;
    private Array<TextureRegion> frames;


    public Gnome(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineGnome();
        b2body.setActive(true);
        frames = new Array<TextureRegion>();

        for(int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getGnomeAtlas().findRegion("Gnome"), i * 16, 0, 16, 32));
        }

        animation = new Animation(1f, frames);
        statetime = 0;
        setBounds(getX()-7/Game.PPM, getY(), 16 / Game.PPM,32 / Game.PPM);
    }

    protected  void defineGnome(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()+8/Game.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setGravityScale(10);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/Game.PPM);
        fdef.filter.categoryBits = Game.GNOME_BIT;
        fdef.filter.maskBits = Game.GROUND_BIT | Game.SPIKES_BIT | Game.LADDER_BIT | Game.DESTROYED_BIT | Game.ENEMY_BIT | Game.MINER_HIT_BOX_BIT | Game.RIGHT_PICKAXE_BIT | Game.LEFT_PICKAXE_BIT | Game.MINER_FOOT_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void update(float dt){
        statetime += dt;
        TextureRegion region = (TextureRegion) animation.getKeyFrame(statetime, true);
        setRegion(region);
    }

}

