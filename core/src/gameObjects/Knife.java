package gameObjects;
/**
 * Based of of Carrot code,
 * knives will rain from the sky and
 * hurt Papa
 * @author Drake Conaway
 *
 */
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import gameWorld.Assets;
public class Knife extends AbstractGameObject{
	private TextureRegion regKnife;
	
	public Knife(){
		init();
	}
	
	/**
	 * Initialze these
	 * dealers of death
	 */
	private void init(){
		dimension.set(0.25f,.5f);
		regKnife = Assets.instance.levelDecoration.knife;
		//set bounding
		bounds.set(0,0,dimension.x,dimension.y);
		origin.set(dimension.x/2,dimension.y/2);
	}
	
	public void render(SpriteBatch batch){
		TextureRegion reg = null;
		reg = regKnife;
		batch.draw(reg.getTexture(), position.x-origin.x,
				position.y - origin.y, origin.x, origin.y, 
				dimension.x, dimension.y, scale.x, scale.y, rotation,
				reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),
				reg.getRegionHeight(),false,false);
	}

}
