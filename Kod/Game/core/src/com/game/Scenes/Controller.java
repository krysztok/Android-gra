package com.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.Game;

public class Controller {
    private Viewport viewport;
    private Stage stage;
    private Game game;
    private boolean leftPressed, rightPressed, aPressed, bPressed;
    private OrthographicCamera cam;

    public Controller(Game game){
        cam = new OrthographicCamera();
        this.game = game;
        viewport = new FitViewport(Game.V_WIDTH, Game.V_HEIGHT, cam);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.left().bottom();
        Image leftImg = new Image(new Texture("leftArrow.png"));
        leftImg.setSize(50, 50);
        leftImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        Image rightImg = new Image(new Texture("rightArrow.png"));
        rightImg.setSize(50, 50);
        rightImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        Image aImg = new Image(new Texture("aButton.png"));
        aImg.setSize(50, 50);
        aImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    aPressed = true;
                return true;
            }
        });

        Image bImg = new Image(new Texture("bButton.png"));
        bImg.setSize(50, 50);
        bImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                bPressed = true;
                return true;
            }

        });

        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight()).padLeft(10);
        table.add(aImg).size(aImg.getWidth(), aImg.getHeight()).padLeft(Game.V_WIDTH/2 - 20);
        table.add(bImg).size(bImg.getWidth(), bImg.getHeight()).padLeft(10);
        stage.addActor(table);
    }

    public void draw(){
        stage.draw();
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isaPressed() {
        return aPressed;
    }

    public boolean isbPressed() {
        return bPressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }

    public void setaPressed(boolean aPressed) {
        this.aPressed = aPressed;
    }

    public void setbPressed(boolean bPressed) {
        this.bPressed = bPressed;
    }

}
