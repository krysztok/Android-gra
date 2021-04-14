package com.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.game.Game;
import com.game.Scenes.Hud;
import com.game.Screens.PlayScreen;



public class Rat extends Enemy {

    private float statetime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean destroyed;

    public Rat(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();

        for(int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("1"), i * 32, 0, 32, 16));
        }

            walkAnimation = new Animation(0.2f, frames);
            statetime = 0;
            setBounds(getX(), getY(), 16 / Game.PPM,16 / Game.PPM);
            destroyed = false;
            runningLeft = true;
    }

    public void update(float dt){
        statetime += dt;

        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("1"),96,0,32,16));
            statetime = 0;
        }
        else if(!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            TextureRegion region = (TextureRegion) walkAnimation.getKeyFrame(statetime, true);

            if((b2body.getLinearVelocity().x > 0 || !runningLeft) && !region.isFlipX()){
                region.flip(true, false);
                runningLeft = false;
            }
            else if ((b2body.getLinearVelocity().x < 0 || runningLeft) && region.isFlipX()){
                region.flip(true, false);
                runningLeft = true;
            }

            setRegion(region);
        }
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
        shape.setRadius(6/Game.PPM);
        fdef.filter.categoryBits = Game.ENEMY_BIT;
        fdef.filter.maskBits = Game.GROUND_BIT | Game.SPIKES_BIT | Game.LADDER_BIT | Game.DESTROYED_BIT | Game.ENEMY_BIT | Game.MINER_HIT_BOX_BIT | Game.RIGHT_PICKAXE_BIT | Game.LEFT_PICKAXE_BIT | Game.MINER_FOOT_BIT | Game.GNOME_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //head hitbox
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 7).scl(1/Game.PPM);
        vertice[1] = new Vector2(5, 7).scl(1/Game.PPM);
        vertice[2] = new Vector2(-5, 3).scl(1/Game.PPM);
        vertice[3] = new Vector2(5, 3).scl(1/Game.PPM);
        head.set(vertice);
        fdef.shape = head;
        fdef.filter.categoryBits = Game.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch){
        if(!destroyed || statetime < 0.35){
            super.draw(batch);
        }
    }

    @Override
    public void onHit() {
        screen.playDeathSound();
        Hud.addScore(100);
        setToDestroy = true;
    }

    @Override
    public void hitBySpikes(){
        screen.playDeathSound();
        setToDestroy = true;
    }
}
