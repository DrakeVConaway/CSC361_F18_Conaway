package gameObjects;
/**
 * Did you escape the game?
 * @author Drake Conaway
 *
 */
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import gameWorld.Assets;
public class Goal extends AbstractGameObject {
	
	private TextureRegion regGoal;
	
	public Goal(){
		init();
	}
	/**
	 * Set dimensions, bounds
	 */
	private void init(){
		dimension.set(3.0f,3.0f);
		regGoal = Assets.instance.levelDecoration.goal;
		
		//set bounding box for collision
		bounds.set(1,Float.MIN_VALUE,10,Float.MAX_VALUE );
		origin.set(dimension.x/2.0f,0.0f);
	}
	/**
	 * Render the Baphomet
	 * @param once again a 
	 * SpriteBatch
	 */
	public void render(SpriteBatch batch){
		TextureRegion reg = regGoal;
		
		batch.draw(reg.getTexture(), position.x - origin.x,
				position.y - origin.y, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation,
				reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(),
				false, false);
	}

}
