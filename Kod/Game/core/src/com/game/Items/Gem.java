package com.game.Items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.game.Game;
import com.game.Scenes.Hud;
import com.game.Screens.PlayScreen;
import com.game.Sprites.Miner;

public class Gem extends Item {
    private float statetime;
    private Animation animation;
    private Array<TextureRegion> frames;

    public Gem(PlayScreen screen, float x, float y, Integer colour) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();

        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getItemsAtlas().findRegion("items8x8"), (1+colour)*8, i*8, 8, 8));
        }

        animation = new Animation(0.2f, frames);
        statetime = 0;
        setBounds(getX(), getY(), 8 / Game.PPM,8 / Game.PPM);
        toDestroy = false;
        destroyed = false;
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3/Game.PPM);
        fdef.filter.categoryBits = Game.GEM_BIT;
        fdef.filter.maskBits = Game.MINER_HIT_BOX_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Miner miner) {
        toDestroy = true;
        screen.playGemSound();
        Hud.addScore(1000);
        Hud.addGem();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        statetime += dt;

        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
            statetime = 0;
        }
        else if(!destroyed) {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            TextureRegion region = (TextureRegion) animation.getKeyFrame(statetime, true);
            setRegion(region);
        }
    }

    public void draw(Batch batch){
        if(!destroyed || statetime < 0.35){
            super.draw(batch);
        }
    }
}
