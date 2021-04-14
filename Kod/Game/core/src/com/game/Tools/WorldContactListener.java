package com.game.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.game.Game;
import com.game.Items.Coin;
import com.game.Items.Gem;
import com.game.Sprites.Button;
import com.game.Sprites.Enemy;
import com.game.Sprites.Miner;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case Game.ENEMY_BIT | Game.LEFT_PICKAXE_BIT:
                if(fixA.getFilterData().categoryBits == Game.ENEMY_BIT){
                    if(!((Miner)fixB.getUserData()).getRunningRight() && ((Miner)fixB.getUserData()).getState() == Miner.State.PICKAXEATTACK ) {
                        ((Enemy) fixA.getUserData()).onHit();
                    }
                }
                else{
                    if(!((Miner)fixA.getUserData()).getRunningRight()&& ((Miner)fixA.getUserData()).getState() == Miner.State.PICKAXEATTACK ) {
                        ((Enemy) fixB.getUserData()).onHit();
                    }
                }
                break;
            case Game.ENEMY_BIT | Game.RIGHT_PICKAXE_BIT:
                if(fixA.getFilterData().categoryBits == Game.ENEMY_BIT){
                    if(((Miner)fixB.getUserData()).getRunningRight()&& ((Miner)fixB.getUserData()).getState() == Miner.State.PICKAXEATTACK ) {
                        ((Enemy) fixA.getUserData()).onHit();
                    }
                }
                else{
                    if(((Miner)fixA.getUserData()).getRunningRight()&& ((Miner)fixA.getUserData()).getState() == Miner.State.PICKAXEATTACK ) {
                        ((Enemy) fixB.getUserData()).onHit();
                    }
                }
                break;
            case Game.ENEMY_HEAD_BIT | Game.MINER_FOOT_BIT:
                if(fixA.getFilterData().categoryBits == Game.ENEMY_HEAD_BIT){
                    ((Enemy)fixA.getUserData()).onHit();
                }
                else{
                    ((Enemy)fixB.getUserData()).onHit();
                }
                break;
            case Game.ENEMY_BIT | Game.GROUND_BIT:
            case Game.ENEMY_BIT | Game.LADDER_BIT:
                if(fixA.getFilterData().categoryBits == Game.ENEMY_BIT){
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                    ((Enemy)fixA.getUserData()).changeRunningDirection();
                }
                else{
                    ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                    ((Enemy)fixB.getUserData()).changeRunningDirection();
                }
                break;
            case Game.ENEMY_BIT | Game.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                ((Enemy)fixA.getUserData()).changeRunningDirection();
                ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                ((Enemy)fixB.getUserData()).changeRunningDirection();
                break;
            case Game.MINER_HIT_BOX_BIT | Game.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == Game.ENEMY_BIT){
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                    ((Enemy)fixA.getUserData()).changeRunningDirection();
                    if(!((Enemy)fixA.getUserData()).setToDestroy) {
                        ((Miner) fixB.getUserData()).hit();
                    }
                }
                else{
                    ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                    ((Enemy)fixB.getUserData()).changeRunningDirection();
                    if(!((Enemy)fixB.getUserData()).setToDestroy) {
                        ((Miner) fixA.getUserData()).hit();
                    }
                }
                break;
            case Game.MINER_HIT_BOX_BIT | Game.COIN_BIT:
                if(fixA.getFilterData().categoryBits == Game.COIN_BIT){
                    ((Coin)fixA.getUserData()).use((Miner) fixB.getUserData());
                }
                else{
                    ((Coin)fixB.getUserData()).use((Miner) fixA.getUserData());
                }
                break;
            case Game.MINER_HIT_BOX_BIT | Game.GEM_BIT:
                if(fixA.getFilterData().categoryBits == Game.GEM_BIT){
                    ((Gem)fixA.getUserData()).use((Miner) fixB.getUserData());
                }
                else{
                    ((Gem)fixB.getUserData()).use((Miner) fixA.getUserData());
                }
                break;
            case Game.MINER_HIT_BOX_BIT | Game.BUTTON_BIT:
                if(fixA.getFilterData().categoryBits == Game.BUTTON_BIT){
                    ((Button)fixA.getUserData()).use();
                }
                else{
                    ((Button)fixB.getUserData()).use();
                }
                break;
            case Game.MINER_BIT | Game.SPIKES_BIT:
                if(fixA.getFilterData().categoryBits == Game.MINER_BIT){
                    ((Miner)fixA.getUserData()).hit();
                }
                else{
                    ((Miner)fixB.getUserData()).hit();
                }
                break;
            case Game.ENEMY_BIT | Game.SPIKES_BIT:
                if(fixA.getFilterData().categoryBits == Game.MINER_BIT){
                    ((Enemy)fixA.getUserData()).hitBySpikes();
                }
                else{
                    ((Enemy)fixB.getUserData()).hitBySpikes();
                }
                break;
            case Game.MINER_BIT | Game.LADDER_BIT:
                if(fixA.getFilterData().categoryBits == Game.MINER_BIT){
                    ((Miner)fixA.getUserData()).nextMap();
                }
                else{
                    ((Miner)fixB.getUserData()).nextMap();
                }
                break;
        }


    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
