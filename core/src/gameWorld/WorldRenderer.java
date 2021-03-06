package gameWorld;
import com.badlogic.gdx.Gdx;
import utils.GamePreferences;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
/**
 * Handles rendering of world's objects
 * as well as the GUI
 * @author Drake Conaway
 */
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import utils.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
public class WorldRenderer implements Disposable{

	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private WorldController worldController;
	private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
	private Box2DDebugRenderer b2debugRenderer;
	
	public WorldRenderer(WorldController worldController) {
		this.worldController = worldController;
		init();
	}
	
	private void init() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(utils.Constants.VIEWPORT_WIDTH,
				utils.Constants.VIEWPORT_HEIGHT);
		camera.position.set(0,0,0);
		camera.update();
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true); // flip y-axis
		cameraGUI.update();
		b2debugRenderer = new Box2DDebugRenderer();
	}
	/**
	 * Generic Render method
	 */
	public void render() {
		renderWorld(batch);
		renderGui(batch);
	}
	/**
	 * Render the world,
	 * passes in 
	 * @param batch
	 */
	private void renderWorld (SpriteBatch batch) {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
		if(DEBUG_DRAW_BOX2D_WORLD) {
			b2debugRenderer.render(worldController.b2world, 
					camera.combined);
		}
		}
	/**
	 * Render the gui by passing
	 * @param batch
	 */
	private void renderGui (SpriteBatch batch) {
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		// draw collected gold coins icon + text
		// (anchored to top left edge)
		renderGuiScore(batch);
		// draw extra lives icon + text (anchored to top right edge)
		renderGuiExtraLive(batch);
		// draw FPS text (anchored to bottom right edge)
		if (GamePreferences.instance.showFpsCounter)
			renderGuiFpsCounter(batch);
		renderGuiFpsCounter(batch);
		//render game over text
		//renderGuiGameOverMessage(batch);
		batch.end();
		}
	/**
	 * Renders the Game Over message
	 */
//	private void renderGuiGameOverMessage (SpriteBatch batch) {
//		float x = cameraGUI.viewportWidth / 2;
//		float y = cameraGUI.viewportHeight / 2;
//		if (worldController.isGameOver()) {
//		BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
//		fontGameOver.setColor(1, 0.75f, 0.25f, 1);
//		fontGameOver.drawMultiLine(batch, "YOU FAILED TO BRING THE LIGHT TO THE PEOPLE, GAME OVER", x, y, 0,
//		BitmapFont.HAlignment.CENTER);
//		fontGameOver.setColor(1, 1, 1, 1);
//		}
//		}
    /**
     * Handle resizing
     * @param width
     * @param height
     */
	public void resize(int width, int height) {
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT
				 / (float)height) * (float)width;
		camera.update();
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT
				  / (float)height) * (float)width;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2,
		cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();
	}

	/**
	 * Renders the score
	 */
	private void renderGuiScore (SpriteBatch batch) {
		float x = -15;
		float y = -15;  //instance.currency.currency
		batch.draw(Assets.instance.currency.currency,
		x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		Assets.instance.fonts.defaultBig.draw(batch,
		"" + worldController.score,
		x + 75, y + 37);
		}
	/**
	 * Render how many
	 * extra lives are remaining
	 */
	private void renderGuiExtraLive (SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 50 -
		Constants.LIVES_START * 50;
		float y = -15;
		for (int i = 0; i < Constants.LIVES_START; i++) {
		    if (worldController.lives <= i)
		    	batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
		    	batch.draw(Assets.instance.character.character,
		    	x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
		    	batch.setColor(1, 1, 1, 1);
		    }
		}
	/**
	 * Render the current FPS
	 */
	private void renderGuiFpsCounter (SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		if (fps >= 45) {
		 // 45 or more FPS show up in green
		  fpsFont.setColor(0, 1, 0, 1);
		} else if (fps >= 30) {
		 // 30 or more FPS show up in yellow
		  fpsFont.setColor(1, 1, 0, 1);
		} else {
		 // less than 30 FPS show up in red
		  fpsFont.setColor(1, 0, 0, 1);
		}
		  fpsFont.draw(batch, "FPS: " + fps, x, y);
		  fpsFont.setColor(1, 1, 1, 1); // white
		}
	
	@Override public void dispose (){
		batch.dispose();
	}
}
