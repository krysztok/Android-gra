package com.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.game.Game;
import com.game.Scenes.Hud;
import com.game.Screens.PlayScreen;

public class Miner extends Sprite {
    public enum  State { FALLING, JUMPING, STANDING, RUNNING, PICKAXEATTACK};
    public State currentState;
    public State previousState;
    public PlayScreen screen;
    public World world;
    public Body b2body;
    private TextureRegion minerStand;
    private Animation minerRun;
    private Animation minerPickaxeAttack;
    private TextureRegion minerJump;
    private float stateTimer;
    private float protectionTime;
    private boolean runningRight;
    private boolean isHit;
    private Sound getHitSound;

    public Miner(PlayScreen screen){
        super(screen.getAtlas().findRegion("miner"));
        this.world = screen.getWorld();
        this.screen = screen;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        protectionTime = 0;
        runningRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 1; i < 3; i++){
            frames.add(new TextureRegion(getTexture(),i*32-i,18,32,32));
        }

        minerRun = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 5; i < 10; i++){
                frames.add(new TextureRegion(getTexture(),i*32-i,18,32,32));
        }

        minerPickaxeAttack = new Animation(0.07f, frames);
        frames.clear();
        minerJump = (new TextureRegion(getTexture(),4 * 32-4,18,32,32));
        getHitSound = Gdx.audio.newSound(Gdx.files.internal("audio/male-grunt-uh.mp3"));
        defineMiner();
        minerStand = new TextureRegion(getTexture(), 0, 18, 32, 32 );
        setBounds(0, 0 , 32 / Game.PPM, 32/Game.PPM);
        setRegion(minerStand);
    }

    public void update(float dt){

        if(runningRight) {
            setPosition(b2body.getPosition().x - getWidth() / 4, b2body.getPosition().y - getHeight() / 4);
        }
        else {
            setPosition(b2body.getPosition().x - getWidth() / 3*2, b2body.getPosition().y - getHeight() / 4);
        }

        setRegion(getFrame(dt));

        if(protectionTime > 0){
            protectionTime -= dt;
        } else if(protectionTime < 0){
            protectionTime =0;
            isHit = false;
        }
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;

        switch (currentState){
            case JUMPING:
                region = minerJump;
                break;
            case RUNNING:
                region = (TextureRegion)minerRun.getKeyFrame(stateTimer, true);
                break;
            case PICKAXEATTACK:
                region = (TextureRegion)minerPickaxeAttack.getKeyFrame(stateTimer, false);

                break;
            case FALLING:
            case STANDING:
                default:
                    region = minerStand;
                    break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;

        if(currentState == State.PICKAXEATTACK && minerPickaxeAttack.getKeyFrameIndex(stateTimer)==4) {
            currentState = State.STANDING;
        }
        else{
            previousState = currentState;
        }

        return region;

    }

    public State getState(){
        if (currentState == State.PICKAXEATTACK) {
            return State.PICKAXEATTACK;
        }
        else  if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            return  State.JUMPING;
        }
        else if(b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        else if (b2body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }
        else {
            return State.STANDING;
        }
    }

    public void setState(State state){
        stateTimer = 0;
        currentState = state;
    }

    public boolean getRunningRight(){
        return runningRight;
    }

    public void defineMiner(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(48/Game.PPM, 64/Game.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/Game.PPM);
        fdef.filter.categoryBits = Game.MINER_BIT;
        fdef.filter.maskBits = Game.GROUND_BIT | Game.SPIKES_BIT | Game.LADDER_BIT | Game.DESTROYED_BIT | Game.ENEMY_BIT | Game.ENEMY_HEAD_BIT | Game.COIN_BIT | Game.GEM_BIT | Game.BUTTON_BIT | Game.GNOME_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);;

        //item hitbox
        PolygonShape hitBox = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-8, 24).scl(1/Game.PPM);
        vertice[1] = new Vector2(8, 24).scl(1/Game.PPM);
        vertice[2] = new Vector2(-8, -8).scl(1/Game.PPM);
        vertice[3] = new Vector2(8, -8).scl(1/Game.PPM);
        hitBox.set(vertice);
        fdef.shape = hitBox;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Game.MINER_HIT_BOX_BIT;
        b2body.createFixture(fdef).setUserData(this);

        //foot hitbox
        PolygonShape footHitBox = new PolygonShape();
        Vector2[] vector = new Vector2[4];
        vector[0] = new Vector2(-8, -8).scl(1/Game.PPM);
        vector[1] = new Vector2(8, -8).scl(1/Game.PPM);
        vector[2] = new Vector2(-8, -10).scl(1/Game.PPM);
        vector[3] = new Vector2(8, -10).scl(1/Game.PPM);
        footHitBox.set(vector);
        fdef.shape = footHitBox;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Game.MINER_FOOT_BIT;
        b2body.createFixture(fdef).setUserData(this);

        //pickaxe right hitbox
        PolygonShape rightPickaxeBox = new PolygonShape();
        vector[0] = new Vector2(8, 16).scl(1/Game.PPM);
        vector[1] = new Vector2(20, 16).scl(1/Game.PPM);
        vector[2] = new Vector2(8, -8).scl(1/Game.PPM);
        vector[3] = new Vector2(20, -8).scl(1/Game.PPM);
        rightPickaxeBox.set(vector);
        fdef.shape = rightPickaxeBox;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Game.RIGHT_PICKAXE_BIT;
        b2body.createFixture(fdef).setUserData(this);

        //pickaxe left hitbox
        PolygonShape leftPickaxeBox = new PolygonShape();
        vector[0] = new Vector2(-20, 16).scl(1/Game.PPM);
        vector[1] = new Vector2(-8, 16).scl(1/Game.PPM);
        vector[2] = new Vector2(-20, -8).scl(1/Game.PPM);
        vector[3] = new Vector2(-8, -8).scl(1/Game.PPM);
        leftPickaxeBox.set(vector);
        fdef.shape = leftPickaxeBox;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Game.LEFT_PICKAXE_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void hit() {

        if (!isHit){
            if(Hud.getLives()>1) {
                getHitSound.play();
            }

            Hud.decreaseLives();
            isHit = true;
            protectionTime = 1;
            float x;

            if (runningRight) {
                x = -1f;
            } else {
                x = 1f;
            }

            b2body.setLinearVelocity(0,0);
            b2body.applyLinearImpulse(new Vector2(x, 1f), b2body.getWorldCenter(), true);
        }
    }

    public void nextMap(){
        screen.setChangeMap();
    }
    public float getStateTimer(){
        return stateTimer;
    }


}
