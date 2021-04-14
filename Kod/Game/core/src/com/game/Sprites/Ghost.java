package com.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.game.Game;
import com.game.Screens.PlayScreen;



public class Ghost extends Enemy {

    private float statetime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;

    public Ghost(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();

        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getGhostAtlas().findRegion("Ghost"), i * 16, 0, 16, 32));
        }

        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getGhostAtlas().findRegion("Ghost"), (3-i) * 16, 0, 16, 32));
        }

        walkAnimation = new Animation(0.2f, frames);
        statetime = 0;
        setBounds(getX(), getY(), 16 / Game.PPM,32 / Game.PPM);
        runningLeft = true;
    }

    public void update(float dt){
        statetime += dt;
        b2body.setLinearVelocity(velocity);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        TextureRegion region = (TextureRegion) walkAnimation.getKeyFrame(statetime, true);
        setRegion(region);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setGravityScale(5);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8/Game.PPM);
        fdef.filter.categoryBits = Game.ENEMY_BIT;
        fdef.filter.maskBits = Game.GROUND_BIT | Game.SPIKES_BIT | Game.LADDER_BIT | Game.DESTROYED_BIT | Game.ENEMY_BIT | Game.MINER_HIT_BOX_BIT | Game.RIGHT_PICKAXE_BIT | Game.LEFT_PICKAXE_BIT | Game.MINER_FOOT_BIT | Game.GNOME_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void onHit() {
    }

    @Override
    public void hitBySpikes(){
    }
}
