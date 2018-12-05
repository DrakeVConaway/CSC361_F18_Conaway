package gameObjects;
/**
 * Rock files, serves as
 * the platforms of the game
 * @author Drake Conaway
 *
 */
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import gameWorld.Assets;
public class Rock extends AbstractGameObject{
	
     private TextureRegion regEdge;
     private TextureRegion regMiddle;
     private int length;
     /**
      * Constructor of rock
      */
     public Rock() {
    	 init();
     }
     /**
      * Initialize rock's dimensions
      */
     private void init() {
    	 dimension.set(1,1.5f);
    	 regEdge = Assets.instance.rock.edge;
    	 regMiddle = Assets.instance.rock.middle;
    	 //Start length of this rock
    	 setLength(1);
     }
     /**
      * Set the length of the rock
      * @param length
      */
     public void setLength(int length) {
    	 this.length = length;
    	 //bounds box for collision detection
    	 bounds.set(0,0,dimension.x * length, dimension.y);
     }
     /**
      * Increase the length
      * @param amount added
      */
     public void increaseLength(int amount) {
    	 setLength(length + amount);
     }
     /**
      * Overridden render method
      */
     @Override
     public void render(SpriteBatch batch) {
    	 TextureRegion reg = null;
    	 float relX = 0;
    	 float relY = 0;
    	 
    	 //Draw left edge
    	 reg = regEdge;
    	 relX -= dimension.x/4;
    	 batch.draw(reg.getTexture(),position.x+relX,position.y +relY,
    			 origin.x,origin.y,dimension.x/4,dimension.y,
    			 scale.x,scale.y,rotation,reg.getRegionX(), reg.getRegionY(),
    			 reg.getRegionWidth(),reg.getRegionHeight(),false,false);
    	 
    	 //Draw middle
    	 relX=0;
    	 reg=regMiddle;
    	 for(int i = 0; i < length; i++) {
    		 batch.draw(reg.getTexture(),position.x+relX,position.y+relY,
    				 origin.x,origin.y,dimension.x,dimension.y,
    				 scale.x,scale.y,rotation,reg.getRegionX(),reg.getRegionY(),
    				 reg.getRegionWidth(),reg.getRegionHeight(),false,false);
    		 relX+= dimension.x;
    	 }
    	 
    	 //Draw right edge
    	 reg = regEdge;
    	 batch.draw(reg.getTexture(),position.x+relX,position.y+relY,
    			 origin.x+dimension.x/8,origin.y,dimension.x/4,
    			 dimension.y,scale.x,scale.y,rotation,reg.getRegionX(),
    			 reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),
    			 true,false);
     }

}
