package com.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.Game;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private static Integer score;
    private static Integer lives;
    private static Integer coins;
    private static Integer gems;

    static Label livesLabel;
    static Label scoreLabel;
    Label livesTextLabel;
    Label coinsTextLabel;
    static Label coinsLabel;
    Label pointsLabel;

    public Hud(SpriteBatch sb, Game game){
        lives = game.getLives();

        if (lives < 5 && game.getGems() == 6){
            lives = 5;
        }

        score = game.getPoints();
        coins = game.getCoins();
        gems = 0;
        viewport = new FitViewport(Game.V_WIDTH, Game.V_WIDTH, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        livesLabel = new Label(String.format("%01d", lives), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        coinsLabel = new Label(String.format("%03d", coins), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        livesTextLabel = new Label("LIVES",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        coinsTextLabel = new Label("COINS",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        pointsLabel = new Label("POINTS",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(pointsLabel).expandX().padTop(10);
        table.add(coinsTextLabel).expandX().padTop(10);
        table.add(livesTextLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(coinsLabel).expandX();
        table.add(livesLabel).expandX();
        stage.addActor(table);
    }

    public void update(float dt){

    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    public static void addCoin(){
        coins ++;
        coinsLabel.setText(String.format("%03d", coins));
    }

    public static void addGem(){
        gems++;
    }
    public static void addLive(){
        lives++;
        livesLabel.setText(String.format("%01d", lives));
    }

    public static void decreaseLives(){
        lives--;
        livesLabel.setText(String.format("%01d", lives));
    }

    public static void decreaseCoins(int n){
        coins -= n;
        coinsLabel.setText(String.format("%03d", coins));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public static int getLives(){
        return lives;
    }

    public static int getPoints(){
        return score;
    }

    public static int getCoins(){
        return coins;
    }

    public int getGems(){
        return gems;
    }
}
