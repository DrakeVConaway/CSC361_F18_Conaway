package gameObjects;
/**
 * His excellency, Papa Emeritus
 * @author Drake Conaway
 *
 */
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import utils.AudioManager;
import utils.CharacterSkin;
import utils.GamePreferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import gameWorld.Assets;
import gameWorld.WorldController;
import utils.Constants;

public class PapaEmeritus extends AbstractGameObject{
	public static final String TAG = Character.class.getName();
	
	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float BOOK_SPEED_OFFSET = 0.f; //will be tweaked
	
	public enum VIEW_DIRECTION{LEFT,RIGHT};
	public enum JUMP_STATE{
		GROUNDED,FALLING,JUMP_RISING,JUMP_FALLING
	}
	private TextureRegion regPapa;
	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasBookPowerup;
	public ParticleEffect dustParticles = new ParticleEffect();
	public Fixture playerPhysicsFixture;
	/**
	 * Awaken Papa
	 */
	public PapaEmeritus(){
		init();
	}
 
	public void init(){
		dimension.set(1, 1);
		regPapa = Assets.instance.character.character;
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		// Bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		//Create the body
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		
		Body box = WorldController.b2world.createBody(bodyDef);
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(0.5f, 0.5f);
		playerPhysicsFeature = box.createFixture(poly,1)
		/**
		 * // Set physics values
		terminalVelocity.set(3.0f, 4.0f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -25.0f);
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		// Jump state
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;
		// Power-ups
		hasBookPowerup = false;
		*/
		//Particle effect
		dustParticles.load(Gdx.files.internal("particles/dust.pfx"), 
				Gdx.files.internal("particles"));
	}
	public void setJumping (boolean jumpKeyPressed) {
		switch (jumpState) {
		 case GROUNDED: // Character is standing on a platform
		  if (jumpKeyPressed) {
		  AudioManager.instance.play(Assets.instance.sounds.jump); 
		   // Start counting jump time from the beginning
		   timeJumping = 0;
		   jumpState = JUMP_STATE.JUMP_RISING;
		}
		break;
		case JUMP_RISING: // Rising in the air
		  if (!jumpKeyPressed)
		  jumpState = JUMP_STATE.JUMP_FALLING;
		  break;
		case FALLING:// Falling down
			//books don't make you fly, needs tweaked or
			//removed
		case JUMP_FALLING: // Falling down after jump
//			if (jumpKeyPressed && hasBookPowerup) {
//				timeJumping = JUMP_TIME_OFFSET_FLYING;
//				jumpState = JUMP_STATE.JUMP_RISING;
//				}
				break;
//				}
				}
	}
	@Override
	public void updateMotionY(float deltaTime) {
		dustParticles.update(deltaTime); //update the particles 
		switch(jumpState) {
		case GROUNDED:
			jumpState = JUMP_STATE.FALLING;
			if(velocity.x !=0) {	
			}
		case JUMP_RISING:
			timeJumping += deltaTime;
			if(timeJumping <= JUMP_TIME_MAX) {
				velocity.y = terminalVelocity.y;
			}
			break;
		case FALLING:
		    break;
		case JUMP_FALLING:
			//add delta to track jump time
			timeJumping += deltaTime;
			//jump to min height
			if(timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) {
				velocity.y = -terminalVelocity.y;
			}
		}
		if(jumpState != JUMP_STATE.GROUNDED) {
			dustParticles.allowCompletion();
			super.updateMotionY(deltaTime);
		}
	}
	/**
	 * What to do when the book 
	 * is picked up
	 * @param pickedUp
	 */
	public void setBookPowerup (boolean pickedUp) {
		hasBookPowerup = pickedUp;
		if (pickedUp) {
		//empty for now, will manipulate lives
			return;
		}
	}
	public boolean hasBookPowerup () {
		return hasBookPowerup;
	}
	/**
	 * Update method for Papa
	 */
	@Override
	public void render(SpriteBatch batch){
		TextureRegion reg = null;
		//apply skin color
		batch.setColor(
				CharacterSkin.values()[GamePreferences.instance.charSkin]
				.getColor());
		//set special color when papa has book
		if(hasBookPowerup){
			batch.setColor(1.0f,0.8f,0.0f,1.0f);
		
	   }
		//Draw
		// Draw image
		reg = regPapa;
		 batch.draw(reg.getTexture(), position.x, position.y, origin.x,
		origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
		reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
		reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT,
		false);
		 
		// Reset color to white
		batch.setColor(1, 1, 1, 1);
		//Draw particles
		dustParticles.draw(batch);
		}
}
