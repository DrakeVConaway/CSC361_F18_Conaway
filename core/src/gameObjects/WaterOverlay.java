package gameObjects;
/**
 * Background for the game,
 * name subject to change for final version
 * @author Drake Conaway
 *
 */
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import gameWorld.Assets;
public class WaterOverlay extends AbstractGameObject{
   private TextureRegion regWaterOverlay;
   private float length;
   
   /**
    * Constructor for background
    */
   public WaterOverlay(float length){
	   this.length = length;
	   init();
   }
   /**
    * Init method
    */
     private void init(){
    	 dimension.set(length * 10,3);
    	 
    	 regWaterOverlay=
    	Assets.instance.levelDecoration.waterOverlay;
     }
   
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = regWaterOverlay;
		batch.draw(reg.getTexture(), position.x + origin.x, position.y
		+ origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
		scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
		reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
	}

}
