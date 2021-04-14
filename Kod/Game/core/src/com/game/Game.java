package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.Screens.MenuScreen;

public class Game extends com.badlogic.gdx.Game {
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	private int level;
	private int points;
	private int coins;
	private int lives;
	private int gems;

	public Preferences prefs;

	//pixels per meter
	public static final float PPM = 100;

	public SpriteBatch batch;

	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short MINER_BIT = 2;
	public static final short SPIKES_BIT = 4;
	public static final short LADDER_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short ENEMY_BIT = 32;
	public static final short ENEMY_HEAD_BIT = 64;
	public static final short COIN_BIT = 128;
	public static final short MINER_HIT_BOX_BIT = 256;
	public static final short GEM_BIT = 512;
	public static final short BUTTON_BIT = 1024;
	public static final short RIGHT_PICKAXE_BIT = 2048;
	public static final short LEFT_PICKAXE_BIT = 4096;
	public static final short MINER_FOOT_BIT = 8192;
	public static final short GNOME_BIT = 16384;

	@Override
	public void create () {

		clear();
		batch = new SpriteBatch();

		setScreen(new MenuScreen(this));
		prefs = Gdx.app.getPreferences("game preferences");
		/*
		for(int i = 0; i < 5; i++) {
			prefs.putInteger(((Integer) i).toString(), 0);
			prefs.flush();
		}*/
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {

	}

	public void nextLevel(){
		level++;
	}

	public int getLevel(){
		return level;
	}

	public int getLives(){
		return lives;
	}

	public int getPoints(){
		return points;
	}

	public int getCoins(){
		return coins;
	}

	public int getGems(){
		return gems;
	}

	public void savePlayerStatus(int lives, int coins, int points, int gems){
		this.lives = lives;
		this.coins = coins;
		this.points = points;
		this.gems = gems;
	}

	public void clear(){
		level = 0;
		points = 0;
		coins = 0;
		lives = 5;
		gems = 0;
	}
}
