package Screens;
/**
 * Game Screen class
 * @author Drake Conaway
 *
 */
import com.badlogic.gdx.Game;
import utils.GamePreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import gameWorld.WorldController;
import gameWorld.WorldRenderer;
public class GameScreen extends AbstractGameScreen{
	private static final String TAG = GameScreen.class.getName();
	
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean paused;
	
	public GameScreen(Game game) {
		super(game);
	}
	
	@Override
	public void render(float deltaTime) {
		//Don't update game world when paused
		if(!paused) {
			//update game world by the time that has passed
			//since last render
			worldController.update(deltaTime);
		}
		//Sets the clear screen to cornflower blue
		Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed /
				255.0f, 0xff / 255.0f);
				// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				// Render game world to screen
		worldRenderer.render();
	}
    @Override
    public void resize(int width, int height) {
    	worldRenderer.resize(width, height);
    }
    @Override
    public void show() {
    	GamePreferences.instance.load();
    	worldController = new WorldController(game);
    	worldRenderer = new WorldRenderer(worldController);
    	Gdx.input.setCatchBackKey(true);
    	
    }
    
    @Override
    public void hide() {
    	worldRenderer.dispose();
    	Gdx.input.setCatchBackKey(false);
    }
    @Override 
    public void pause() {
    	paused = true;
    }
    @Override
    public void resume() {
    	super.resume();
    	//only called on android
    	paused = false;
    }
}
