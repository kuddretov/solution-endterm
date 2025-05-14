package com.yourgame.factory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Platform {
    public Sprite sprite;

    public Platform(Texture texture, float x, float y) {
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
    }

    public float getTop() {
        return sprite.getY() + sprite.getHeight();
    }

    public boolean isPlayerOnTop(Sprite player) {
        return player.getY() > sprite.getY()
            && player.getY() < getTop() + 10
            && player.getX() + player.getWidth() > sprite.getX()
            && player.getX() < sprite.getX() + sprite.getWidth();
    }
}
