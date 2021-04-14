package com.game.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.game.Game;
import com.game.Screens.PlayScreen;

public class Button extends  InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private int buttonID;
    private final int noButtonID = 59;
    private boolean activated;

    public Button(PlayScreen screen, MapObject object) {
        super(screen, object);
        activated = false;
        tileSet = screen.getMap().getTileSets().getTileSet("tileset");
        fixture.setUserData(this);
        setCategoryFilter(Game.BUTTON_BIT);
        buttonID = object.getProperties().get("id", Integer.class );
    }

    public void use(){
        activated = true;
        getCell().setTile(tileSet.getTile(noButtonID));
        setCategoryFilter(Game.NOTHING_BIT);
    }

    public void update(){

    }

    public int getButtonID(){
        return buttonID;
    }

    public boolean isActivated(){
        return activated;
    }
}
