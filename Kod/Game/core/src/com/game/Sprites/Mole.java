package com.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.game.Game;
import com.game.Scenes.Hud;
import com.game.Screens.PlayScreen;

public class Mole extends Enemy {

    private float statetime;
    private TextureRegion lookLeft;
    private TextureRegion move;
    private TextureRegion lookRight;
    private TextureRegion mound;
    private boolean destroyed;
    private int state;

    public Mole(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        statetime = 0;
        setBounds(getX(), getY(), 16 / Game.PPM,16 / Game.PPM);
        setToDestroy = false;
        destroyed = false;
        runningLeft = true;
        state = 0;
        lookLeft =  new TextureRegion(screen.getMoleAtlas().findRegion("krtek"), 0, 0, 32, 32);
        lookRight =  new TextureRegion(screen.getMoleAtlas().findRegion("krtek"), 32, 0, 32, 32);
        move =  new TextureRegion(screen.getMoleAtlas().findRegion("krtek"), 96, 0, 32, 32);
        mound =  new TextureRegion(screen.getMoleAtlas().findRegion("krtek"), 64, 0, 32, 32);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY() );
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setGravityScale(5);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3/Game.PPM);
        fdef.filter.categoryBits = Game.ENEMY_BIT;
        fdef.filter.maskBits = Game.GROUND_BIT | Game.SPIKES_BIT | Game.LADDER_BIT | Game.DESTROYED_BIT | Game.ENEMY_BIT | Game.MINER_HIT_BOX_BIT | Game.RIGHT_PICKAXE_BIT | Game.LEFT_PICKAXE_BIT | Game.GNOME_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float dt) {
        statetime += dt;

        if(Math.round(2*statetime%3)==3) {
            state++;
            if(state==4){
                state = 0;
            }
            statetime = 0;
        }

        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            statetime = 0;
        }
        else if(!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 5);
            TextureRegion region = move;

            if(state == 0){
                velocity = new Vector2(1, 0);

                if(isFlipX()){
                    region.flip(true, false);
                }
                runningLeft = false;
            }
            else if (state == 1){
                velocity = new Vector2(0, 0);
                region = lookRight;
                runningLeft = true;
            }
            else if (state == 2){
                velocity = new Vector2(-1, 0);

                if(!isFlipX()){
                    region.flip(true, false);
                }
                runningLeft = true;

            }
            else if (state == 3){
                velocity = new Vector2(0, 0);
                region = lookLeft;
                runningLeft = true;
            }

            setRegion(region);
        }
    }

    public void draw(Batch batch){
        if(!destroyed){
            super.draw(batch);
        }
    }

    @Override
    public void onHit() {
        screen.playDeathSound();
        Hud.addScore(200);
        setToDestroy = true;
    }

    @Override
    public void hitBySpikes(){
        screen.playDeathSound();
        setToDestroy = true;
    }
}
