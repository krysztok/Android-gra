package com.game.Sprites;


import com.badlogic.gdx.maps.MapObject;
import com.game.Game;
import com.game.Screens.PlayScreen;


public class Ladder extends InteractiveTileObject {
    public Ladder(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Game.LADDER_BIT);
    }


}
