package gameObjects;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import gameWorld.Assets;
/**
 * The souls object, the score collectible for the
 * game, based on currency
 * @author Drake Conaway
 *
 */
public class Soul extends AbstractGameObject {
	private TextureRegion soul;
	public boolean collected;
	/**
	 * constructor for the soul
	 */
    public Soul(){
    	init();
    }
    /**
     * initialize the souls
     */
    private void init(){
    	dimension.set(0.5f,0.5f);
    	soul = Assets.instance.currency.currency;
    	//set bounds for collision
    	bounds.set(0,0,dimension.x, dimension.y);
    	collected = false;
    }
    /**
     * render method
     */
    public void render(SpriteBatch batch){
    	if(collected) return;
    	TextureRegion reg = soul;
    	batch.draw(reg.getTexture(), position.x, position.y,
    	 origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
    	 rotation, reg.getRegionX(), reg.getRegionY(),
    	 reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }
    
    /**
     * return score
     */
    public int getScore(){
    	return 100;
    	
    }

}
