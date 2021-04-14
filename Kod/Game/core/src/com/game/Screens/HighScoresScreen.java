package com.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class HighScoresScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Image menuImage;
    private Array<Texture> textures;
    private int state;
    private float stateTime;

    private Game game;

    public HighScoresScreen(Game game){
        this.game = game;
        viewport = new FitViewport(com.game.Game.V_WIDTH, com.game.Game.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((com.game.Game)game).batch);
        textures = new Array<Texture>();
        textures.add(new Texture(Gdx.files.internal("Menu.png")));
        textures.add(new Texture(Gdx.files.internal("Menu1.png")));
        textures.add(new Texture(Gdx.files.internal("Menu2.png")));
        Texture texture = new Texture(Gdx.files.internal("Menu.png"));
        menuImage = new Image();
        menuImage.setPosition(0, 0);
        menuImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
        menuImage.setSize(texture.getWidth(), texture.getHeight());
        stage.addActor(menuImage);
        state = 0;
        stateTime = 0;
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        for(int i = 0; i < 5; i++) {
            table.add(new Label(i+1 +". " + ((com.game.Game) game).prefs.getInteger(((Integer)i).toString()), font)).expandX();
            table.row();
        }

        table.add(new Label("Click to go back", font)).expandX().padTop(10);
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    public Texture getTexture(float dt){
        stateTime+=dt;

        if(stateTime > 0.4) {
            state++;
            if(state==3){
                state = 0;
            }
            stateTime = 0;
        }

        return textures.get(state);
    }

    @Override
    public void render(float delta) {

        if(Gdx.input.justTouched()){
            game.setScreen(new MenuScreen(game));
            dispose();

        }

        menuImage.setDrawable(new TextureRegionDrawable(new TextureRegion(getTexture(delta))));
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
