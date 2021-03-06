package gameObjects;
/**
 * Mountain concrete object
 * its render and dimensions
 * @author Drake Conaway
 *
 */
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import gameWorld.Assets;
public class Mountains extends AbstractGameObject{
	
	private TextureRegion regMountainLeft;
	private TextureRegion regMountainRight;
	
	private int length;
	
	public Mountains(int length) {
		this.length=length;
		init();
	}
	
	/**
	 * Initialization of mountains,
	 * set dimensions
	 */
	private void init() {
		dimension.set(10,2);
		regMountainLeft = Assets.instance.levelDecoration.mountainLeft;
		regMountainRight=
				Assets.instance.levelDecoration.mountainRight;
		
		//shift moutain and extend length
		origin.x = -dimension.x*2;
		length += dimension.x*2;
	}
	
	/**
	 * Draw mountain method,
	 * controls color tinting
	 */
	private void drawMountain (SpriteBatch batch, float offsetX,
		float offsetY, float tintColor,float parallaxSpeedX) {
			TextureRegion reg = null;
			batch.setColor(tintColor, tintColor, tintColor, 1);
			float xRel = dimension.x * offsetX;
			float yRel = dimension.y * offsetY;
			
			// mountains span the whole level
			int mountainLength = 0;
			mountainLength  += MathUtils.ceil(
					length / (2*dimension.x) * (1-parallaxSpeedX));
			mountainLength += MathUtils.ceil(length / (2 * dimension.x));
			mountainLength += MathUtils.ceil(0.5f + offsetX);
			for (int i = 0; i < mountainLength; i++) {
			 // mountain left

			 reg = regMountainLeft;
			 batch.draw(reg.getTexture(), origin.x + xRel, position.y +
			  origin.y + yRel, origin.x, origin.y, dimension.x, dimension.y,
			  scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
			  reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			 
			  
			// mountain right
			 reg = regMountainRight;
			 batch.draw(reg.getTexture(),origin.x + xRel + position.x *parallaxSpeedX,
			position.y +
			 origin.y + yRel, origin.x, origin.y, dimension.x, dimension.y,
			 scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
			 reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			 xRel += dimension.x;
			 }
			// reset color to white
			batch.setColor(1, 1, 1, 1);
			}
	/**
	 * Parallax scrolling method for th
	 * mts
	 * @param camPosition
	 */
	public void updateScrollPosition(Vector2 camPosition) {
		position.set(camPosition.x,position.y);
	}
	@Override
	public void render(SpriteBatch batch) {
		//distant mts(dark grey)
		drawMountain(batch, 0.5f,0.5f,0.5f,0.8f);
		//distant mts(grey)
		drawMountain(batch,0.25f,0.25f,0.7f,0.5f);
		//distant mts(light grey)
		drawMountain(batch, 0.0f,0.0f,0.9f,0.3f);
	}
}
 
