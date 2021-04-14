package com.game.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.Game;
import com.game.Items.Coin;
import com.game.Items.Gem;
import com.game.Items.Torch;
import com.game.Scenes.Controller;
import com.game.Scenes.Hud;
import com.game.Sprites.Button;
import com.game.Sprites.Enemy;
import com.game.Sprites.Gnome;
import com.game.Sprites.Miner;
import com.game.Sprites.Spikes;
import com.game.Tools.B2WorldCreator;
import com.game.Tools.WorldContactListener;

import static java.lang.StrictMath.abs;

public class PlayScreen implements Screen {

    private Game game;
    private Music music;
    private TextureAtlas atlas;
    private TextureAtlas itemAtlas;
    private TextureAtlas moleAtlas;
    private TextureAtlas ghostAtlas;
    private TextureAtlas gnomeAtlas;

    public boolean changeMap;

    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    private Controller controller;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    TiledMapTileLayer layerBackground;
    TiledMapTileLayer layerGraphics;

    //b2d
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    private Miner player;
    private Sound jumpSound;
    private Sound pickaxeAttackSound;
    private Sound gameOverSound;
    private Sound buyLiveSound;
    private Sound deathSound;
    private Sound coinSound;
    private Sound gemSound;

    public PlayScreen(Game game){
        atlas = new TextureAtlas("Miner_Enemies.pack");
        itemAtlas = new TextureAtlas("Items.pack");
        moleAtlas = new TextureAtlas("Mole.pack");
        ghostAtlas = new TextureAtlas("Ghost.pack");
        gnomeAtlas = new TextureAtlas("Gnome.pack");
        changeMap = false;
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Game.V_WIDTH/game.PPM,Game.V_HEIGHT/game.PPM, gameCam);
        hud = new Hud(game.batch, game);
        controller = new Controller(game);
        mapLoader = new TmxMapLoader();

        if(game.getLevel() == 0){
            map = mapLoader.load("level1.tmx");
        }else if(game.getLevel() == 1){
            map = mapLoader.load("level2.tmx");
        }else if(game.getLevel() == 2){
            map = mapLoader.load("level3.tmx");
        }

        renderer = new OrthogonalTiledMapRenderer(map, 1/game.PPM);
        layerBackground = (TiledMapTileLayer) map.getLayers().get(0);
        layerGraphics = (TiledMapTileLayer) map.getLayers().get(1);
        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
        world = new World(new Vector2(0,-10), true);
        b2dr = new Box2DDebugRenderer();
        creator = new B2WorldCreator(this);
        player = new Miner(this);
        world.setContactListener(new WorldContactListener());
        buyLiveSound = Gdx.audio.newSound(Gdx.files.internal("audio/coin-on-coins-03.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("audio/game-over.wav"));
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("audio/jump01.wav"));
        pickaxeAttackSound = Gdx.audio.newSound(Gdx.files.internal("audio/stab.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/through-the-caves.mp3"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("audio/goblin-death.mp3"));
        coinSound = Gdx.audio.newSound(Gdx.files.internal("audio/coins-1.wav"));
        gemSound = Gdx.audio.newSound(Gdx.files.internal("audio/coin6.wav"));
        music.setLooping(true);
        music.play();
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }
    public TextureAtlas getMoleAtlas(){
        return moleAtlas;
    }
    public TextureAtlas getItemsAtlas(){
        return itemAtlas;
    }
    public TextureAtlas getGhostAtlas(){
        return ghostAtlas;
    }
    public TextureAtlas getGnomeAtlas(){
        return gnomeAtlas;
    }


    @Override
    public void show() {

    }

    public void handleInput(float dt){
        if (((controller.isbPressed() && Gdx.app.getType() ==  Application.ApplicationType.Android) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) && player.b2body.getLinearVelocity().y == 0){
            player.b2body.applyLinearImpulse(new Vector2(0, 3.4f), player.b2body.getWorldCenter(), true);
            jumpSound.play();

            if(controller.isbPressed()){
                controller.setbPressed(false);
            }
        }
        if (((controller.isRightPressed() && Gdx.app.getType() ==  Application.ApplicationType.Android) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && player.b2body.getLinearVelocity().x <= 1.5){
            player.b2body.applyLinearImpulse(new Vector2(0.1f,0), player.b2body.getWorldCenter(), true);
        }
        if (((controller.isLeftPressed() && Gdx.app.getType() ==  Application.ApplicationType.Android) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) && player.b2body.getLinearVelocity().x >= -1.5){
            player.b2body.applyLinearImpulse(new Vector2(-0.1f,0), player.b2body.getWorldCenter(), true);
        }
        if ((controller.isaPressed() && Gdx.app.getType() ==  Application.ApplicationType.Android) || Gdx.input.isKeyJustPressed(Input.Keys.A)){
            pickaxeAttackSound.play();
            player.setState(Miner.State.PICKAXEATTACK);

            if(controller.isaPressed()){
                controller.setaPressed(false);
            }
        }
    }

    public void update(float dt){
        handleInput(dt);
        world.step(1/60f, 6, 2);
        player.update(dt);

        for(Enemy enemy : creator.getRats()){
            enemy.update(dt);

            if(enemy.getX() < player.getX() + 220/Game.PPM ){
                enemy.b2body.setActive(true);
            }

            if(player.getX() > enemy.getX() + 220/Game.PPM ){
                enemy.b2body.setActive(false);
            }
        }

        for(Coin coin : creator.getCoins()) {
            coin.update(dt);
        }

        for(Gem gem : creator.getGems()) {
            gem.update(dt);
        }

        for(Torch torch : creator.getTorches()) {
            torch.update(dt, layerBackground);
        }

        for(Spikes spikes : creator.getSpikes()) {
            spikes.update(dt, creator);
        }

        for(Button buttons : creator.getButtons()) {
            buttons.update();
        }

        for(Gnome gnomes : creator.getGnomes()) {
            gnomes.update(dt);

            if(abs(gnomes.getX() - player.getX()) < 30/Game.PPM){
                if(Hud.getCoins()>=10 && Hud.getLives()<8 && player.getStateTimer() < 0.01f && player.getState() == Miner.State.PICKAXEATTACK){
                    buyLiveSound.play();
                    Hud.decreaseCoins(10);
                    Hud.addLive();
                }

            }
        }

        for(Enemy enemy : creator.getMoles()){
            enemy.update(dt);

            if(enemy.getX() < player.getX() + 220/Game.PPM){
                enemy.b2body.setActive(true);
            }

            if(player.getX() > enemy.getX() + 220/Game.PPM ){
                enemy.b2body.setActive(false);
            }
        }

        for(Enemy enemy : creator.getGhosts()){
            enemy.update(dt);

            if(enemy.getX() < player.getX() + 220/Game.PPM){
                enemy.b2body.setActive(true);
            }

            if(player.getX() > enemy.getX() + 220/Game.PPM ){
                enemy.b2body.setActive(false);
            }
        }

        hud.update(dt);

        if(player.b2body.getPosition().x > 200/Game.PPM && player.b2body.getPosition().x < ( 240 * 16 - 200 )/game.PPM ) {
            gameCam.position.x = player.b2body.getPosition().x;
        }

        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();

        //narysowanie konturow wokol wszystkich elementow
        //b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);

        for(Enemy enemy : creator.getRats()){
            enemy.draw(game.batch);
        }

        for(Enemy enemy : creator.getMoles()){
            enemy.draw(game.batch);
        }

        for(Enemy enemy : creator.getGhosts()){
            enemy.draw(game.batch);
        }

        for(Gnome gnomes : creator.getGnomes()){
            gnomes.draw(game.batch);
        }

        for(Coin coin : creator.getCoins()){
            coin.draw(game.batch);
        }

        for(Gem gem : creator.getGems()){
            gem.draw(game.batch);
        }


        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(Hud.getLives() == 0){
            gameOver();
        }

        if(changeMap){
            game.nextLevel();
            game.savePlayerStatus(hud.getLives(), hud.getCoins(), hud.getPoints(), hud.getGems());

            if(game.getLevel() != 3){
                music.stop();
                game.setScreen(new PlayScreen(game));
                dispose();
                changeMap = false;

            }else{
                gameOver();
            }
        }

        if(Gdx.app.getType() ==  Application.ApplicationType.Android) {
            controller.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        controller.resize(width, height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    public void setChangeMap(){
        changeMap = true;
    }

    private void gameOver(){
        music.stop();
        gameOverSound.play();

        for(int i = 0; i < 5; i++) {
             if(Hud.getPoints() > ((com.game.Game) game).prefs.getInteger(((Integer)i).toString())){
                 int tmp = ((com.game.Game) game).prefs.getInteger(((Integer)i).toString());
                 ((com.game.Game) game).prefs.putInteger(((Integer)i).toString(), Hud.getPoints());
                 ((com.game.Game) game).prefs.flush();

                 for(int j = i; j < 4; j++){
                     int tmp2 = ((com.game.Game) game).prefs.getInteger(((Integer)(j+1)).toString());
                     ((com.game.Game) game).prefs.putInteger(((Integer)(j+1)).toString(), tmp);
                     ((com.game.Game) game).prefs.flush();
                     tmp = tmp2;
                 }

                 break;
             }
        }

        game.clear();
        game.setScreen(new GameOverScreen(game, Hud.getPoints()));
        dispose();
    }

    public void playDeathSound(){
        deathSound.play();
    }

    public void playCoinSound(){
        coinSound.play();
    }

    public void playGemSound(){
        gemSound.play();
    }
}
