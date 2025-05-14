package com.yourgame.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.yourgame.MainGame;
import com.yourgame.factory.EntityFactory;
import com.yourgame.factory.Platform;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;


import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen implements Screen {

    private final MainGame game;

    private Texture playerTexture, enemyTexture, backgroundTexture, coinTexture, heartTexture, platformTexture;
    private Sprite player, enemy, enemy2;
    private ArrayList<Sprite> coins;
    private ArrayList<Platform> platforms;

    private BitmapFont font;
    private GlyphLayout layout;

    private float velocityY = 0f;
    private final float gravity = -900f;
    private final float jumpVelocity = 500f;

    private boolean isGameOver = false;
    private boolean isWin = false;
    private boolean enemyMovingRight = true;
    private boolean enemy2MovingRight = true;
    private final float enemySpeed = 100f;
    private final float enemy2Speed = 70f;
    private float enemy2StartX;

    private int score = 0;
    private int lives = 3;

    private float damageCooldown = 1.5f;
    private float timeSinceLastHit = 0f;

    public GameScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        backgroundTexture = new Texture("background.png");
        playerTexture = new Texture("player.png");
        enemyTexture = new Texture("enemy.png");
        coinTexture = new Texture("coin.png");
        heartTexture = new Texture("heart.png");
        platformTexture = new Texture("platform.png");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.otf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 22; // размер шрифта
        parameter.color = com.badlogic.gdx.graphics.Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose(); // освобождаем ресурсы

        layout = new GlyphLayout();

        player = EntityFactory.createPlayer(playerTexture);
        enemy = EntityFactory.createEnemy(enemyTexture);
        enemy2 = EntityFactory.createEnemy(enemyTexture);
        enemy2.setPosition(90, 240);
        enemy2StartX = enemy2.getX();


        coins = new ArrayList<>();
        coins.add(EntityFactory.createCoin(coinTexture, 320, 120));
        coins.add(EntityFactory.createCoin(coinTexture, 600, 170));
        coins.add(EntityFactory.createCoin(coinTexture, 120, 250));

        platforms = new ArrayList<>();
        platforms.add(new Platform(platformTexture, 300, 100));
        platforms.add(new Platform(platformTexture, 580, 150));
        platforms.add(new Platform(platformTexture, 100, 230));
    }

    @Override
    public void render(float delta) {
        timeSinceLastHit += delta;

        if (!isGameOver && !isWin) {
            update(delta);
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (Platform platform : platforms) {
            platform.sprite.draw(game.batch);
        }

        for (Sprite coin : coins) {
            coin.draw(game.batch);
        }

        enemy.draw(game.batch);
        enemy2.draw(game.batch);
        player.draw(game.batch);

        font.draw(game.batch, "Score: " + score, 420, Gdx.graphics.getHeight() - 10);
        for (int i = 0; i < lives; i++) {
            game.batch.draw(heartTexture, 10 + i * 40, Gdx.graphics.getHeight() - 50, 32, 32);
        }

        if (isGameOver) {
            drawCenteredText("GAME OVER", 0);
            drawCenteredText("Press R to Restart", -50);
        }

        if (isWin) {
            drawCenteredText("YOU WIN!", 0);
            drawCenteredText("Press R to Restart", -50);
        }

        game.batch.end();

        if ((isGameOver || isWin) && Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    private void update(float delta) {
        handleInput();

        velocityY += gravity * delta;
        float newY = player.getY() + velocityY * delta;

        boolean onPlatform = false;
        for (Platform platform : platforms) {
            if (platform.isPlayerOnTop(player) && velocityY <= 0) {
                newY = platform.getTop();
                velocityY = 0;
                onPlatform = true;
                break;
            }
        }

        if (newY <= 0 && !onPlatform) {
            newY = 0;
            velocityY = 0;
        }

        player.setY(newY);

        float speed = 200f;
        float newX = player.getX();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            newX -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            newX += speed * delta;
        }
        newX = Math.max(0, Math.min(newX, Gdx.graphics.getWidth() - player.getWidth()));
        player.setX(newX);

        // Enemy patrol
        float enemyX = enemy.getX();
        if (enemyMovingRight) {
            enemyX += enemySpeed * delta;
            if (enemyX > Gdx.graphics.getWidth() - enemy.getWidth()) {
                enemyX = Gdx.graphics.getWidth() - enemy.getWidth();
                enemyMovingRight = false;
            }
        } else {
            enemyX -= enemySpeed * delta;
            if (enemyX < 0) {
                enemyX = 0;
                enemyMovingRight = true;
            }
        }
        enemy.setX(enemyX);

        if (player.getBoundingRectangle().overlaps(enemy.getBoundingRectangle())) {
            if (timeSinceLastHit > damageCooldown) {
                lives--;
                timeSinceLastHit = 0f;
                if (lives <= 0) isGameOver = true;
            }
        }
        float enemy2X = enemy2.getX();
        if (enemy2MovingRight) {
            enemy2X += enemy2Speed * delta;
            if (enemy2X > enemy2StartX + 90) {
                enemy2X = enemy2StartX + 90;
                enemy2MovingRight = false;
            }
        } else {
            enemy2X -= enemy2Speed * delta;
            if (enemy2X < enemy2StartX) {
                enemy2X = enemy2StartX;
                enemy2MovingRight = true;
            }
        }
        enemy2.setX(enemy2X);

        if (player.getBoundingRectangle().overlaps(enemy2.getBoundingRectangle())) {
            if (timeSinceLastHit > damageCooldown) {
                lives--;
                timeSinceLastHit = 0f;
                if (lives <= 0) isGameOver = true;
            }
        }

        Iterator<Sprite> iter = coins.iterator();
        while (iter.hasNext()) {
            Sprite coin = iter.next();
            if (player.getBoundingRectangle().overlaps(coin.getBoundingRectangle())) {
                iter.remove();
                score++;
            }
        }

        if (coins.isEmpty()) {
            isWin = true;
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (player.getY() == 0 || isStandingOnPlatform()) {
                velocityY = jumpVelocity;
            }
        }
    }

    private boolean isStandingOnPlatform() {
        for (Platform platform : platforms) {
            if (platform.isPlayerOnTop(player)) {
                return true;
            }
        }
        return false;
    }

    private void drawCenteredText(String text, float offsetY) {
        layout.setText(font, text);
        float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float y = (Gdx.graphics.getHeight() + layout.height) / 2f + offsetY;
        font.draw(game.batch, layout, x, y);
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        playerTexture.dispose();
        enemyTexture.dispose();
        backgroundTexture.dispose();
        coinTexture.dispose();
        font.dispose();
        heartTexture.dispose();
        platformTexture.dispose();
    }
}
