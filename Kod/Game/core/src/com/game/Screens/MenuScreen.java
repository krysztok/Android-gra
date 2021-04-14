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

public class MenuScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Image menuImage;
    private Array<Texture> textures;
    private int state;
    public int xSize;
    public int ySize;
    private float stateTime;

    private Game game;

    public MenuScreen(Game game){
        this.game = game;
        viewport = new FitViewport(com.game.Game.V_WIDTH, com.game.Game.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((com.game.Game)game).batch);
        xSize = 1;
        ySize = 1;
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
        Label startLabel = new Label("Start", font);
        Label highScoresLabel = new Label("High Scores", font);
        Label quitLabel = new Label("Quit", font);
        table.add(startLabel).expandX();
        table.row();
        table.add(highScoresLabel).expandX().padTop(5);
        table.row();
        table.add(quitLabel).expandX().padTop(5);
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
            int yTouched = Gdx.input.getY();
            int xTouched = Gdx.input.getX();

            if(xTouched > xSize * 3/8 && xTouched < xSize * 5/8){
                if (yTouched > ySize * 5/16 && yTouched < ySize * 7/16){
                    game.setScreen(new PlayScreen((com.game.Game)game));
                    dispose();
                }else if(yTouched > ySize * 7/16 && yTouched < ySize * 9/16){
                    game.setScreen(new HighScoresScreen(game));
                    dispose();
                }
                else if(yTouched > ySize * 9/16 && yTouched < ySize * 11/16){
                    dispose();
                    Gdx.app.exit();
                }
            }
        }

        menuImage.setDrawable(new TextureRegionDrawable(new TextureRegion(getTexture(delta))));
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        xSize = width;
        ySize = height;
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
