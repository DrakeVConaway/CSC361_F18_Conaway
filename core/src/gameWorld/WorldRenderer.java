package gameWorld;
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
public class WorldRenderer implements Disposable{

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	
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
	}
	public void render() {
		renderTestObjects();
	}
	private void renderTestObjects() {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(Sprite sprite :worldController.testSprites){
			sprite.draw(batch);
		}
		batch.end();
	}

	public void resize(int width, int height) {}
	
	
	@Override public void dispose (){
		batch.dispose();
	}
}
