package gameWorld;
/**
 * Level loader for the game
 * @author Drake Conaway
 *
 */
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import gameObjects.BookOfPain;
import gameObjects.PapaEmeritus;
import gameObjects.Soul;

import gameObjects.
AbstractGameObject;

import gameObjects.Mountains;
import gameObjects.Rock;
import gameObjects.WaterOverlay;
//import gameObjects.WaterOverlay;
public class Level {
  public static final String TAG = Level.class.getName();
  
  //objects
  public Array<Rock>rocks;
  //decoration
  public Mountains mountains;
  public WaterOverlay waterOverlay;
  public PapaEmeritus papaEmeritus;
  public Array<Soul> souls;
  public Array<BookOfPain> books;
  
  
  public enum BLOCK_TYPE{
	  EMPTY(0,0,0), //black
	  ROCK(0,255,0), //green
	  PLAYER_SPAWNPOINT(255,255,255), //white
	  ITEM_BOOK(255,0,255), //purple
	  ITEM_CURRENCY(255,255,0); //yellow
  
  private int color;
   private BLOCK_TYPE(int r, int g, int b) {
	   color = r<<24|g<<16|b<<8| 0xff;
   }
   
   public boolean sameColor(int color) {
	   return this.color ==color;
   }
   
   public int getColor() {
	   return color;
   }
  }
  
  
  public Level(String filename) {
	  init(filename);
	  papaEmeritus.position.y = 1.0f;
  }
  
   private void init(String filename) {
	   //player character
	   papaEmeritus = null;
	   //objects
	   rocks = new Array<Rock>();
	   souls = new Array<Soul>();
	   books = new Array<BookOfPain>();
	   
	   //load image file that represents level data
	   Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
	   //scan pixels from top-left to bottom-right
	   int lastPixel = -1;
	   for(int pixelY=0; pixelY<pixmap.getHeight(); pixelY++) {
		   for(int pixelX = 0; pixelX<pixmap.getWidth(); pixelX++) {
			   AbstractGameObject obj = null;
			   float offsetHeight = 0;
			   //height grows from bot to top
			   float baseHeight = pixmap.getHeight() - pixelY;
			   //get color of current pixel as 32-bit RGBA value
			   int currentPixel = pixmap.getPixel(pixelX, pixelY);
			   //find matching color val to identify block type
			   //point and create corresponding game obj if match
			   
			   //empty space
			   if(BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
				   //do nothing
			   }
			   //rock
			   else if(BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
				   if(lastPixel != currentPixel) {
					   obj = new Rock();
					   float heightIncreaseFactor = 0.25f;
					   offsetHeight = -2.5f;
					   obj.position.set(pixelX,baseHeight * obj.dimension.y
							   *heightIncreaseFactor +offsetHeight);//was + offf
					   rocks.add((Rock) obj);
				   }else {
					   rocks.get(rocks.size - 1).increaseLength(1);
				   }
				   System.out.println("add rock");
			   }
			   //player spawn point
			   else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
				   obj = new PapaEmeritus();
				   offsetHeight = 0.75f;
				   obj.position.set(pixelX +1f ,baseHeight * obj.dimension.y +
						   offsetHeight+1);
				   papaEmeritus = (PapaEmeritus)obj;
			   }
			   //book of pain
			   else if (BLOCK_TYPE.ITEM_BOOK.sameColor(currentPixel)) {
				   obj = new BookOfPain();
				   offsetHeight = -1.5f;
				   obj.position.set(pixelX,baseHeight * obj.dimension.y
				   + offsetHeight);
				   books.add((BookOfPain)obj);
			   }
			   //souls
			   else if(BLOCK_TYPE.ITEM_CURRENCY.sameColor(currentPixel)) {
				   obj = new Soul();
				   offsetHeight = -1.5f;
				   obj.position.set(pixelX,baseHeight * obj.dimension.y
				   + offsetHeight);
				   souls.add((Soul)obj);
			   }
			   //unknown obj/pixel color
			   else {
				   int r = 0xff & (currentPixel >>> 24); //red color channel
				   int g = 0xff & (currentPixel >>> 16); //green color channel
				   int b = 0xff & (currentPixel >>> 8); //blue color channel
				   int a = 0xff & currentPixel; //alpha channel
				   Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
				   + pixelY + ">: r<" + r+ "> g<" + g + "> b<" + b + "> a<" + a + ">");
				   
			   }
			   lastPixel = currentPixel;
		   }
		   }
	   //decoration
	   mountains = new Mountains(pixmap.getWidth());
	   mountains.position.set(-1,-1);
	   waterOverlay = new WaterOverlay(pixmap.getWidth());
	   waterOverlay.position.set(0,-3.75f);
	   
	   //free memory
	   pixmap.dispose();
	   Gdx.app.debug(TAG, "level '" + filename+ "'loaded");
	   }
   
   public void render(SpriteBatch batch) {
	   //Draw mts
	   mountains.render(batch);
	   //Draw Rocks
	   for(Rock rock : rocks)
		   rock.render(batch);
	   //Draw Souls
	   for(Soul soul :souls)
		   soul.render(batch);
	   //Draw Books
	   for(BookOfPain book:books)
		   book.render(batch);
	   //Draw Papa
	   papaEmeritus.render(batch);
	   
	   //Draw water overlay
	  waterOverlay.render(batch);
   }
   public void update (float deltaTime) {
	   papaEmeritus.update(deltaTime);
	   for(Rock rock : rocks)
	   rock.update(deltaTime);
	   for(Soul soul : souls)
	   soul.update(deltaTime);
	   for(BookOfPain book : books)
		   book.update(deltaTime);
}
}
