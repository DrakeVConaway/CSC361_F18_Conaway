package gameObjects;
/**
 * The Necronomicon ex Mortis, book of the Dead.
 * Contains prophecies, funerary rites, and bizarre demon
 * resurrection rituals.
 * When picked up this object scales the end score at the cost of 
 * an extra life
 * @author Drake Conaway
 *
 */
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import gameWorld.Assets;
public class BookOfPain extends AbstractGameObject {
	private TextureRegion book;
	public boolean collected;
	
	public BookOfPain(){
		init();
	}

	private void init() {
		dimension.set(0.5f,0.5f);
		book = Assets.instance.book.book;
		//set bounds for detection
		bounds.set(0,0, dimension.x, dimension.y);
		collected = false;
	}
    /**
     * Render method for the books
     */
	public void render (SpriteBatch batch) {
		if (collected) return;
		TextureRegion reg = null;
		reg = book;
		batch.draw(reg.getTexture(), position.x, position.y,
		origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
		rotation, reg.getRegionX(), reg.getRegionY(),
		reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		}

	/**
	 * return method, may need tweaked 
	 * as part of the scalar
	 * @return
	 */
	public int getScore(){
		return 0;
	}
}
