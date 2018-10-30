package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import gameWorld.WorldController;
import gameWorld.WorldRenderer;

public class CSC361_F18_Conaway extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	WorldController worldController;
	WorldRenderer worldRenderer;
	@Override
	public void create () {
		//Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//Initialize controller and renderer
		 worldController = new WorldController();
		 worldRenderer = new WorldRenderer(worldController);
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		// Update game world by the time that has passed
		// since last rendered frame.
		worldController.update(Gdx.graphics.getDeltaTime());
		// Sets the clear screen color to: Cornflower Blue
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f,
		   0xff/255.0f);
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Render game world to screen
		worldRenderer.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		worldRenderer.dispose();
	}
	@Override 
	public void resize(int width, int height){
		worldRenderer.resize(width, height);
	}
}
