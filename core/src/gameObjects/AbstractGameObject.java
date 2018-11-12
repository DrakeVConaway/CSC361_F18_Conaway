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
public abstract class AbstractGameObject {

	 public Vector2 position;
	 public Vector2 dimension;
	 public Vector2 origin;
	 public Vector2 scale;
	 public float rotation;
	 
	 /**
	  * Constructor for AGOs
	  */
	 public AbstractGameObject() {
		 position = new Vector2();
		 dimension = new Vector2();
		 origin = new Vector2();
		 scale = new Vector2(1,1);
		 rotation = 0;
	 }
	 
	 /**
	  * update method for AGOs
	  */
	 public void update(float deltaTime) {
		 
	 }
	 
	 public abstract void render(SpriteBatch batch); 
		 
	 
}
