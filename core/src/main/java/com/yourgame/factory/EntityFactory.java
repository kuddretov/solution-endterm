package com.yourgame.factory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class EntityFactory {

    public static Sprite createPlayer(Texture texture) {
        Sprite player = new Sprite(texture);
        player.setPosition(300, 0);
        return player;
    }

    public static Sprite createEnemy(Texture texture) {
        Sprite enemy = new Sprite(texture);
        enemy.setPosition(100, 0);

        return enemy;
    }

    public static Sprite createCoin(Texture texture, float x, float y) {
        Sprite coin = new Sprite(texture);
        coin.setPosition(x, y);
        return coin;
    }
}
