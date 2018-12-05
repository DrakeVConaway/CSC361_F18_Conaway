package gameWorld;
import com.badlogic.gdx.graphics.Pixmap;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

import utils.AudioManager;
import utils.CameraHelper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import Screens.MenuScreen;
import gameObjects.Rock;
import gameObjects.BookOfPain;
import gameObjects.PapaEmeritus;
import gameObjects.PapaEmeritus.JUMP_STATE;
import gameObjects.Soul;
import utils.Constants;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import gameObjects.Knife;
/**
 * Keeps track of objects within the world, handles updates
 * Makes sure the renderer is able to draw objects
 * after they have updated
 * @author Drake Conaway
 */
public class WorldController extends InputAdapter implements Disposable {

	private static final String TAG =
			WorldController.class.getName();
	public CameraHelper cameraHelper;
	public Level level;
	public int lives;
	public int score;
	private boolean goalReached;
	public static World b2world;
	private Vector2 movement = new Vector2();
	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	private float timeLeftGameOverDelay;
	private Game game;
	
	public boolean isGameOver(){
		return lives < 0;
	}
	public boolean isPlayerInWater(){
		return level.papaEmeritus.position.y < -5;
	}
	
	private void backToMenu() {
		//swith to menuscreen
		game.setScreen(new MenuScreen(game));
	}
	/**
	 * World Controller constructor
	 */
	public WorldController(Game game) {
		this.game = game;
		init();
	}
	/**
	 * Initialize level
	 */
	private void initLevel(){
		score = 0;
		//scoreVisual = score;
		goalReached = false;
		level = new Level(Constants.LEVEL_01); //set level to this, if more than
											   //one level, will need tweaked
		cameraHelper.setTarget(level.papaEmeritus);
		initPhysics();
	}
	/**
	 * Initialization method for 
	 * WorldController
	 */
	public void init() {
		 b2world = new World(new Vector2(0,-200f),true);
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		timeLeftGameOverDelay = 0;
		initLevel();
	}
	/**
	 * Initials the physics engine
	 */
	 private void initPhysics(){
		// if(b2world != null) b2world.dispose();
		
		 //Rocks
		 Vector2 origin = new Vector2();
		 for(Rock rock: level.rocks){
			 BodyDef bodyDef = new BodyDef();
			 bodyDef.type = BodyType.KinematicBody;
			 bodyDef.position.set(rock.position);
			 	Body body = b2world.createBody(bodyDef);
			 rock.body = body;
			 PolygonShape polygonShape = new PolygonShape();
			 origin.x = rock.bounds.width / 2.0f;
			 origin.y = rock.bounds.height / 2.0f;
			 
			 polygonShape.setAsBox(rock.bounds.width / 2.0f,
			 rock.bounds.height / 2.0f, origin, 0);
			 
			 FixtureDef fixtureDef = new FixtureDef();
			 fixtureDef.shape = polygonShape;
			 body.createFixture(fixtureDef);
			 polygonShape.dispose();
		 }
	 }
	
	 private void spawnKnives(Vector2 pos, int numKnives,
			 float radius){
		 float knifeShapeScale = 0.5f;
		 //create knives w/ box2d
		 for(int i = 0; i < numKnives; i++){
			 Knife knife = new Knife();
			 //calc random spawn position
			 float x = MathUtils.random(-radius,radius);
			 float y = MathUtils.random(5.0f, 15.0f);
			 float rotation = MathUtils.random(0.0f, 360.0f)
			 *  MathUtils.degreesToRadians;
			 float knifeScale = MathUtils.random(0.5f, 1.5f);
			 knife.scale.set(knifeScale, knifeScale);
			  // create box2d body for knife with start position
			  // and angle of rotation
			 BodyDef bodyDef = new BodyDef();
			 bodyDef.position.set(pos);
			 bodyDef.position.add(x, y);
			 bodyDef.angle = rotation;
			 Body body = b2world.createBody(bodyDef);
			 body.setType(BodyType.DynamicBody);
			 knife.body = body;
			 // create rectangular shape for carrot to allow
			 // interactions (collisions) with other objects
			 PolygonShape polygonShape = new PolygonShape();
			 float halfWidth = knife.bounds.width / 2.0f * i;
			 float halfHeight = knife.bounds.height /2.0f * i;
			 polygonShape.setAsBox(halfWidth * knifeShapeScale,
			 halfHeight * knifeShapeScale);
			 // set physics attributes
			 FixtureDef fixtureDef = new FixtureDef();
			 fixtureDef.shape = polygonShape;
			 fixtureDef.density = 50;
			 fixtureDef.restitution = 0.5f;
			 fixtureDef.friction = 0.5f;
			 body.createFixture(fixtureDef);
			 polygonShape.dispose();
			 // finally, add new carrot to list for updating/rendering
			 level.knives.add(knife);
		 }
	 }
	 /**
	  * How does Papa interact with
	  * the rocks,
	  * state switching here
	  * @param rock
	  */
	private void onCollisionPapaWithRock(Rock rock) {
		PapaEmeritus papaEmeritus = level.papaEmeritus;
float heightDifference = Math.abs(papaEmeritus.position.y
- ( rock.position.y + rock.bounds.height));
   boolean hitLeftEdge = false;
if (heightDifference > .25f) {//was .25 
	 hitLeftEdge = papaEmeritus.position.x > 
rock.position.x + rock.bounds.width / 2.0f;
			if(hitLeftEdge) {
			 papaEmeritus.position.x = rock.position.x + rock.bounds.width;
		} else {
			
			papaEmeritus.position.x = rock.position.x -
			papaEmeritus.bounds.width;
			}
				return;
			}
       
			switch (papaEmeritus.jumpState) {
				 case GROUNDED:
					// papaEmeritus.terminalVelocity.setZero();	
					 papaEmeritus.position.x = rock.position.x;
					 papaEmeritus.terminalVelocity.set(3.0f, 4.0f);
					 break;
				 case FALLING:
					 if(hitLeftEdge) {
						 papaEmeritus.position.x = rock.position.x + rock.bounds.width;
						} else {
//							
							papaEmeritus.jumpState = JUMP_STATE.GROUNDED;
							}
					 
				//Jump Fall state	 
				 case JUMP_FALLING:
					 System.out.print("JUMP_FALL");
			  papaEmeritus.position.y = rock.position.y +
				  papaEmeritus.bounds.height + papaEmeritus.origin.y;
				  papaEmeritus.jumpState = JUMP_STATE.GROUNDED;
			break;
				case JUMP_RISING:
				  papaEmeritus.position.y = rock.position.y +
				  papaEmeritus.bounds.height + papaEmeritus.origin.y +1;
				break;
			}
	}
	private void onCollisionPapaWithSoul(Soul soul) {
		soul.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupSoul);
		score+= soul.getScore();
		Gdx.app.log(TAG, "Soul reclaimed");
	}
	private void onCollisionPapaWithBook(BookOfPain book) {
		book.collected= true;
		AudioManager.instance.play(Assets.instance.sounds.pickupBook);
		lives--; //decrement lives, 
		if(!isGameOver()){ //if you don't just kill yourself
		score *= score*2;//double score when book is picked up
		level.papaEmeritus.setBookPowerup(true);
		Gdx.app.log(TAG, "Book of Pain read");
		}
		
	}
	/**
	 * collision w/ goal
	 */
//	private void onCollisionPapaWithGoal(){
//		goalReached = true;
//		timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED);
//		 Vector2 centerPosPapa =
//				 new Vector2(level.papaEmeritus.position);
//		 centerPosPapa.x += level.papaEmeritus.bounds.width;
//		 spawnKnives(centerPosPapa, Constants.KNIVES_SPAWN_MAX,
//		 Constants.KNIVES_SPAWN_RADIUS);
	//}
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
		// Test collision: Bunny Head <-> Goal
//		if (!goalReached) {
//		r2.set(level.goal.bounds);
//		r2.x += level.goal.position.x;
//		r2.y += level.goal.position.y;
//		//if (r1.overlaps(r2)) onCollisionPapaWithGoal();
//		}
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
			timeLeftGameOverDelay -= deltaTime; //menu code needed
			if (timeLeftGameOverDelay < 0) backToMenu(); //return to menu when over
		} else {
		handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		level.papaEmeritus.updateMotionY(deltaTime);
		testCollisions();
		b2world.step(deltaTime, 4, 4);
		cameraHelper.update(deltaTime);
		if(!isGameOver() && isPlayerInWater()){
			AudioManager.instance.play(Assets.instance.sounds.liveLost);
			lives--;
			if(isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
		level.mountains.updateScrollPosition(cameraHelper.getPosition());
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
		Vector2 velocity = new Vector2(0,0);
		level.papaEmeritus.playerPhysicsFixture.setFriction(0f);
		if (cameraHelper.hasTarget(level.papaEmeritus)) {
			// Player Movement
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				velocity.x -= 4;
			//level.papaEmeritus.velocity.x =
			//-level.papaEmeritus.terminalVelocity.x;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				velocity.x += 4;
				//level.papaEmeritus.body.setLinearVelocity(1.0f,0f);
				//level.papaEmeritus.velocity.x =
			  //level.papaEmeritus.terminalVelocity.x;
			} 
			
			  //Execute auto-forward movement on non-desktop platform
			if (Gdx.app.getType() != ApplicationType.Desktop) {
			 // level.papaEmeritus.velocity.x =
			  //level.papaEmeritus.terminalVelocity.x;
			 }
			}
			// Papa Jump
			if (//Gdx.input.isTouched() ||
			Gdx.input.isKeyPressed(Keys.SPACE)) {
			level.papaEmeritus.setJumping(true);
			velocity.y += 8;
			level.papaEmeritus.body.applyLinearImpulse(velocity, level.papaEmeritus.body.getPosition(), true);
			} else {
			level.papaEmeritus.setJumping(false);
			//velocity.y -= 4;
			  }

			level.papaEmeritus.body.setLinearVelocity(velocity);
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
		//back to menu
		else if(keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			backToMenu();
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
	@Override
	public void dispose() {
		if(b2world != null) b2world.dispose();
	}
}
