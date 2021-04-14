package com.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.game.Game;
import com.game.Items.Coin;
import com.game.Items.Gem;
import com.game.Items.Torch;
import com.game.Screens.PlayScreen;
import com.game.Sprites.Button;
import com.game.Sprites.Ghost;
import com.game.Sprites.Gnome;
import com.game.Sprites.Ladder;
import com.game.Sprites.Mole;
import com.game.Sprites.Rat;
import com.game.Sprites.Spikes;

public class B2WorldCreator {
    private Array<Rat> rats;
    private Array<Coin> coins;
    private Array<Gem> gems;
    private Array<Torch> torches;
    private Array<Spikes> spikes;
    private Array<Button> buttons;
    private Array<Mole> moles;
    private Array<Ghost> ghosts;
    private Array<Gnome> gnomes;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;


        //wczytanie ziemi
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/Game.PPM, (rect.getY() + rect.getHeight() /2)/Game.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth()/2)/Game.PPM, (rect.getHeight()/2)/Game.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }


        //wczytanie kolcow
        spikes = new Array<Spikes>();
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            spikes.add(new Spikes(screen, object));
        }

        //wczytanie drabiny
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            new Ladder(screen, object);
        }

        //wczytanie szczurow
        rats = new Array<Rat>();
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            rats.add(new Rat(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }

        //wczytanie coins
        coins = new Array<Coin>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            coins.add(new Coin(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }

        //wczytanie gems
        gems = new Array<Gem>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            gems.add(new Gem(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM,object.getProperties().get("colour", Integer.class )));
        }

        //wczytanie torches
        torches = new Array<Torch>();
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            torches.add(new Torch(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }

        //wczytanie buttons
        buttons = new Array<Button>();
        for(MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            buttons.add(new Button(screen, object));
        }

        //wczytanie kretow
        moles = new Array<Mole>();
        for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            moles.add(new Mole(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }

        //wczytanie duchow
        ghosts = new Array<Ghost>();
        for(MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            ghosts.add(new Ghost(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }

        //wczytanie skrzatow
        gnomes = new Array<Gnome>();
        for(MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            gnomes.add(new Gnome(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }



    }

    public Array<Rat> getRats() {
        return rats;
    }
    public Array<Coin> getCoins() {
        return coins;
    }
    public Array<Gem> getGems() {
        return gems;
    }
    public Array<Torch> getTorches() { return torches; }
    public Array<Spikes> getSpikes() {
        return spikes;
    }
    public Array<Button> getButtons() { return buttons; }
    public Array<Mole> getMoles() {
        return moles;
    }
    public Array<Ghost> getGhosts() {
        return ghosts;
    }
    public Array<Gnome> getGnomes() {
        return gnomes;
    }

}
