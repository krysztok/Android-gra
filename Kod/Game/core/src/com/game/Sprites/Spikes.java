package com.game.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.game.Game;
import com.game.Screens.PlayScreen;
import com.game.Tools.B2WorldCreator;


public class Spikes extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int spikesID = 56;
    private final int noSpikesID = 57;
    private int mode;
    private boolean activated;
    private float statetime;

    public Spikes(PlayScreen screen, MapObject object){
        super(screen, object);
        this.mode = object.getProperties().get("mode", Integer.class );
        this.activated = true;
        this.statetime = 0;
        tileSet = screen.getMap().getTileSets().getTileSet("tileset");
        fixture.setUserData(this);
        setCategoryFilter(Game.SPIKES_BIT);
    }

    public void update(float dt, B2WorldCreator creator) {
        statetime += dt;

        if(mode == 1 && Math.round(statetime)==2) {
            if(activated){
                getCell().setTile(tileSet.getTile(noSpikesID));
                activated = false;
                setCategoryFilter(Game.NOTHING_BIT);
            }
            else {
                getCell().setTile(tileSet.getTile(spikesID));
                activated = true;
                setCategoryFilter(Game.SPIKES_BIT);
            }

            statetime = 0;
        }
        else if(mode != 0 && activated){
            for(Button buttons : creator.getButtons()) {
                if (mode == buttons.getButtonID() && buttons.isActivated()){
                    getCell().setTile(tileSet.getTile(noSpikesID));
                    activated = false;
                    setCategoryFilter(Game.NOTHING_BIT);
                }
            }
        }
    }
}
