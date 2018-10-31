package gameWorld;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import utils.CameraHelper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
/**
 * Keeps track of objects within the world, handles updates
 * Makes sure the renderer is able to draw objects
 * after they have updated
 * @author Drake Conaway
 */
public class WorldController implements InputProcessor {

	private static final String TAG =
			WorldController.class.getName();
	public Sprite[] testSprites;
	public int selectedSprite;
	public CameraHelper cameraHelper;
	/**
	 * World Controller constructor
	 */
	public WorldController() {
		init();
	}
	
	/**
	 * Initialization method for 
	 * WorldController
	 */
	public void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		initTestObjects();
	}
	
	private void initTestObjects(){
		// Create new array for 5 sprites
		testSprites = new Sprite[5];
		//Create a list of texture regions
		Array<TextureRegion> regions = new Array<TextureRegion>
		regions.add(Assets.instance.character.character);
		regions.add(Assets.instance.book.book);
		regions.add(Assets.instance.currency.currency);
		// Create empty POT-sized Pixmap with 8 bit RGBA pixel data
		int width = 32;
		int height = 32;
		Pixmap pixmap = createProceduralPixmap(width, height);
		// Create a new texture from pixmap data
		Texture texture = new Texture(pixmap);
		// Create new sprites using the just created texture
		for (int i = 0; i < testSprites.length; i++) {
		  Sprite spr = new Sprite(texture);
		  // Define sprite size to be 1m x 1m in game world
		  spr.setSize(1, 1);
		  // Set origin to sprite's center
		  spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
		  // Calculate random position for sprite
		  float randomX = MathUtils.random(-2.0f, 2.0f);
		  float randomY = MathUtils.random(-2.0f, 2.0f);
		  spr.setPosition(randomX, randomY);
		  // Put new sprite into array
		  testSprites[i] = spr;
		}
		// Set first sprite as selected one
		selectedSprite = 0;
	}
	
	private Pixmap createProceduralPixmap(int width,int height){
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		//Fill square w/ red color at 50 %opacity
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		// Draw a yellow-colored X shape on square
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		// Draw a cyan-colored border around square
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}
	
	/**
	 * Update method for WorldController
	 * used to update object's positions
	 * before rendering, 
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		updateTestObjects(deltaTime);
		cameraHelper.update(deltaTime);
	}

	private void handleDebugInput(float deltaTime) {
		if(Gdx.app.getType() != ApplicationType.Desktop) return;
		
		//selected sprite controls
		float sprMoveSpeed = 5 * deltaTime;
		if (Gdx.input.isKeyPressed(Keys.A)) moveSelectedSprite(
		   -sprMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.D))
		    moveSelectedSprite(sprMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.W)) moveSelectedSprite(0,
		    sprMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.S)) moveSelectedSprite(0,
		    -sprMoveSpeed);
		
		// Camera Controls (move)
		float camMoveSpeed = 5 * deltaTime;
		float camMoveSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *=
		    camMoveSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed,
		     0);
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed,
		    0);
		if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0,
		    -camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
		     cameraHelper.setPosition(0, 0);
		
		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *=
		     camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA))
		     cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(
		    -camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
	}

	/**
	 * Method to move camera
	 * @param x
	 * @param y
	 */
	private void moveCamera(float x, float y){
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x,y);
	}
	private void moveSelectedSprite(float x, float y) {
		testSprites[selectedSprite].translate(x, y);
		
	}

	private void updateTestObjects(float deltaTime) {
	  //Get current rotation from selected sprite
		float rotation = testSprites[selectedSprite].getRotation();
		//Rotate sprite by 90 degrees per secon
		rotation += 90 * deltaTime;
		//Wrap around at 360 degrees
		rotation %= 360;
		//Set new rotation val to selected sprite
		testSprites[selectedSprite].setRotation(rotation);
		
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
	  //Reset game world
		if(keycode == Keys.R){
			init();
			Gdx.app.debug(TAG, "Game world reset");
		}
		//select next sprite
		else if(keycode == Keys.SPACE){
			selectedSprite = (selectedSprite +1) % testSprites.length;
			//Update camera's target to follow new sprite
			if(cameraHelper.hasTarget()){
				cameraHelper.setTarget(testSprites[selectedSprite]);
			}
			Gdx.app.debug(TAG, "Sprite #" + selectedSprite + "selected");
		}
		//Toggle cam follow
		else if(keycode == Keys.ENTER){
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null:
				testSprites[selectedSprite]);
			Gdx.app.debug(TAG, "Camera follow enabled: " + 
				cameraHelper.hasTarget());
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
