package ru.mipt.bit.platformer.adapters;

import com.badlogic.gdx.maps.MapRenderer;

import ru.mipt.bit.platformer.interfaces.LevelRenderer;

public class LevelRendererAdapter implements LevelRenderer {

    private final MapRenderer delegate;

    public LevelRendererAdapter(MapRenderer delegate) {
        this.delegate = delegate;
    }

    @Override
    public void render() {
        delegate.render();
    }
}
