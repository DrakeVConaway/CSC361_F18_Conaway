package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import Screens.MenuScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.assets.AssetManager;
import gameWorld.Assets;
import gameWorld.WorldController;
import gameWorld.WorldRenderer;
import utils.AudioManager;
import utils.GamePreferences;

public class CSC361_F18_Conaway extends Game {
	@Override
	public void create() {
		//Set libgdx log lvl
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//Load assets
		Assets.instance.init(new AssetManager());
		//load preferences for audio/start playing music
		GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.song01);
		//Start game at menu screen
		setScreen(new MenuScreen(this));
	}
}
