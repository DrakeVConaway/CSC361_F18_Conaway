package gameObjects;
/**
 * Abstract class for the game objects
 * types include dynamic, static,
 * kinematic
 * @author Drake Conaway
 *
 */
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;

public abstract class AbstractGameObject {

	 public Vector2 position; 
	 public Vector2 dimension;
	 public Vector2 origin;
	 public Vector2 scale;
	 public float rotation;
	 public Vector2 velocity; //objects current speed
	 public Vector2 terminalVelocity; //objects positive/neg spd in m/s
	 public Vector2 friction; //slows down velocity, 0 means no friction
	 public Vector2 acceleration; //constant accel in m/s sq
	 public Rectangle bounds; //bounds for collision detection
	 
	 /**
	  * Constructor for AGOs
	  */
	 public AbstractGameObject() {
		 position = new Vector2();
		 dimension = new Vector2();
		 origin = new Vector2();
		 scale = new Vector2(1,1);
		 rotation = 0;
		 velocity = new Vector2();
		 terminalVelocity = new Vector2(1, 1);
		 friction = new Vector2();
		 acceleration = new Vector2();
		 bounds = new Rectangle();
	 }
	 
	 /**
	  * update method for AGOs
	  */
	 public void update(float deltaTime) {
		 updateMotionX(deltaTime);
		 updateMotionY(deltaTime);
		 //Move to new position
		 position.x += velocity.x * deltaTime;
		 position.y =+ velocity.y * deltaTime;	 
	 }
	 /**
	  * manipulate the amount of friction on 
	  * the body in the x plane
	  * @param deltaTime
	  */
	 protected void updateMotionX(float deltaTime){
		 if(velocity.x != 0){
			 //Apply friction
			 if(velocity.x> 0){
				 velocity.x = 
						 Math.max(velocity.x - friction.x *deltaTime, 0);
			 
		 } else{
			     velocity.x = 
			    		 Math.min(velocity.x - friction.x *deltaTime, 0);
		  }
		 }
		 //Apply acceleration
		 velocity.x += acceleration.x * deltaTime;
		 //Make sure the objects veloicty does not exceed 
		 //the positive or neg termVelocity
		 velocity.x = MathUtils.clamp(velocity.x,
				 -terminalVelocity.x,terminalVelocity.x);
	 }
	 
	 /**
	  * Manipulate friction on the body in the 
	  * y plane
	  * @param deltaTime
	  */
	 protected void updateMotionY (float deltaTime) {
		 if (velocity.y != 0) {
		    // Apply friction
		    if (velocity.y > 0) {
		       velocity.y = Math.max(velocity.y - friction.y *
		 deltaTime, 0);
		 } else {
		 velocity.y = Math.min(velocity.y + friction.y *
		 deltaTime, 0);
		  }
		 }
		  // Apply acceleration
		  velocity.y += acceleration.y * deltaTime;
		   // Make sure the object's velocity does not exceed the
		  // positive or negative terminal velocity
		  velocity.y = MathUtils.clamp(velocity.y, -
				  terminalVelocity.y, terminalVelocity.y);
	  }
	 public abstract void render(SpriteBatch batch); 
	
	 
	 
}
