package gameWorld;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import utils.CameraHelper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import gameObjects.Rock;
import gameObjects.BookOfPain;
import gameObjects.PapaEmeritus;
import gameObjects.PapaEmeritus.JUMP_STATE;
import gameObjects.Soul;
import utils.Constants;
/**
 * Keeps track of objects within the world, handles updates
 * Makes sure the renderer is able to draw objects
 * after they have updated
 * @author Drake Conaway
 */
public class WorldController implements InputProcessor {

	private static final String TAG =
			WorldController.class.getName();
	public CameraHelper cameraHelper;
	public Level level;
	public int lives;
	public int score;
	
	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	private float timeLeftGameOverDelay;
	
	public boolean isGameOver(){
		return lives < 0;
	}
	public boolean isPlayerInWater(){
		return level.papaEmeritus.position.y < -5;
	}
	/**
	 * World Controller constructor
	 */
	public WorldController() {
		init();
	}
	/**
	 * Initialize level
	 */
	private void initLevel(){
		score = 0;
		level = new Level(Constants.LEVEL_01); //set level to this, if more than
											   //one level, will need tweaked
		cameraHelper.setTarget(level.papaEmeritus);
	}
	/**
	 * Initialization method for 
	 * WorldController
	 */
	public void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		timeLeftGameOverDelay = 0;
		initLevel();
	}
	
	private void onCollisionPapaWithRock(Rock rock) {
		PapaEmeritus papaEmeritus = level.papaEmeritus;
//		//float heightDifference = Math.abs(papaEmeritus.position.y
//			//	- ( rock.position.y + rock.bounds.height));
//			//if (heightDifference > 0.25f) { 
//			//	boolean hitLeftEdge = papaEmeritus.position.x > (
//			//rock.position.x + rock.bounds.width / 2.0f);
//			//if (hitLeftEdge) {
			 papaEmeritus.position.x = rock.position.x; //+ rock.bounds.width;
//			//} else {
			//papaEmeritus.position.x = rock.position.x -
			//papaEmeritus.bounds.width;
//				//}
//				//return;
//				//}
//				switch (papaEmeritus.jumpState) {
//				 case GROUNDED:
//				  break;
//				 case FALLING:
//				 case JUMP_FALLING:
//				  papaEmeritus.position.y = rock.position.y +
//				  papaEmeritus.bounds.height + papaEmeritus.origin.y;
//				  papaEmeritus.jumpState = JUMP_STATE.GROUNDED;
//				break;
//				case JUMP_RISING:
//				  papaEmeritus.position.y = rock.position.y +
//				  papaEmeritus.bounds.height + papaEmeritus.origin.y +1;
//				break;
//			}
	}
	private void onCollisionPapaWithSoul(Soul soul) {
		soul.collected = true;
		score+= soul.getScore();
		Gdx.app.log(TAG, "Soul reclaimed");
	}
	private void onCollisionPapaWithBook(BookOfPain book) {
		book.collected= true;
		lives--; //decrement lives, 
		if(!isGameOver()){ //if you don't just kill yourself
		score *= score*2;//double score when book is picked up
		level.papaEmeritus.setBookPowerup(true);
		Gdx.app.log(TAG, "Book of Pain read");
		}
		
	};
	/**
	 * Test collisions
	 */
	private void testCollisions () {
		r1.set(level.papaEmeritus.position.x, level.papaEmeritus.position.y,
		level.papaEmeritus.bounds.width, level.papaEmeritus.bounds.height);
		// Test collision: Papa <-> Rocks
		for (Rock rock : level.rocks) {
		r2.set(rock.position.x, rock.position.y, rock.bounds.width,
		rock.bounds.height);
		if (!r1.overlaps(r2)) continue;
		onCollisionPapaWithRock(rock);
		// IMPORTANT: must do all collisions for valid
		// edge testing on rocks.
		}
		//Test collision Papa ->souls
		for (Soul soul : level.souls) {
			if (soul.collected) continue;
			r2.set(soul.position.x, soul.position.y,
					soul.bounds.width, soul.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionPapaWithSoul(soul);
			break;
			}
		//Test collision papa->books
		for (BookOfPain book : level.books) {
			if (book.collected) continue;
			r2.set(book.position.x, book.position.y,
					book.bounds.width, book.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionPapaWithBook(book);
			break;
			}
			}
	
	
	/**
	 * Update method for WorldController
	 * used to update object's positions
	 * before rendering, 
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		if (isGameOver()) {
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0) init();
		} else {
		handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);
		if(!isGameOver() && isPlayerInWater()){
			lives--;
			if(isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
	}

	private void handleDebugInput(float deltaTime) {
		if(Gdx.app.getType() != ApplicationType.Desktop) return;
		
		
		if(!cameraHelper.hasTarget(level.papaEmeritus)){
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
	}

	/**
	 * Method to handle game input
	 */
	private void handleInputGame(float deltaTime){
		if (cameraHelper.hasTarget(level.papaEmeritus)) {
			// Player Movement
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			level.papaEmeritus.velocity.x =
			-level.papaEmeritus.terminalVelocity.x;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			  level.papaEmeritus.velocity.x =
			  level.papaEmeritus.terminalVelocity.x;
			} else {
			 // Execute auto-forward movement on non-desktop platform
			if (Gdx.app.getType() != ApplicationType.Desktop) {
			  level.papaEmeritus.velocity.x =
			  level.papaEmeritus.terminalVelocity.x;
			 }
			}
			// Papa Jump
			if (Gdx.input.isTouched() ||
			Gdx.input.isKeyPressed(Keys.SPACE)) {
			level.papaEmeritus.setJumping(true);
			} else {
			 level.papaEmeritus.setJumping(false);
			  }
			 }
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
		// Toggle camera follow
		else if (keycode == Keys.ENTER) {
		cameraHelper.setTarget(cameraHelper.hasTarget()
		? null: level.papaEmeritus);
		Gdx.app.debug(TAG, "Camera follow enabled: "
		+ cameraHelper.hasTarget());
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
