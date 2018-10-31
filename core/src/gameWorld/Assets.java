package gameWorld;
/**
 * Assets for the new game
 */
import com.badlogic.gdx.Gdx; 
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import utils.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
public class Assets implements Disposable, AssetErrorListener {
  public static final String TAG = Assets.class.getName();
  
  public static final Assets instance = new Assets();
  private AssetManager assetManager;
  
  //singleton: prevent instantiation from other classes
   private Assets() {}
   
   public AssetCharacter character;
   public AssetRock rock;
   public AssetCurrency currency;
   public AssetBookOfPain book;
   public AssetLevelDecoration levelDecoration;
   public void init(AssetManager assetManager){
	   this.assetManager = assetManager;
	// set asset manager error handler
	assetManager.setErrorListener(this);
	// load texture atlas
	assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,
	  TextureAtlas.class);
	// start loading assets and wait until finished
	assetManager.finishLoading();
	   Gdx.app.debug(TAG, "# of assets loaded: "
	    + assetManager.getAssetNames().size);
	for (String a : assetManager.getAssetNames())
	    Gdx.app.debug(TAG, "asset: " + a);
   
   
   TextureAtlas atlas =
		   assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
   
   //enable texture filtering for smoother pixels
   for(Texture t : atlas.getTextures()){
	   t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
   }
   character = new AssetCharacter(atlas);
   rock = new AssetRock(atlas);
   currency = new AssetCurrency(atlas);
   book = new AssetBookOfPain(atlas);
   levelDecoration = new AssetLevelDecoration(atlas);
   }
   /**
    * Class for the asset managing of main character
    * sprite
    * @author Drake Conaway
    *
    */
   public class AssetCharacter{
	   public final AtlasRegion character;
	   
	   public AssetCharacter(TextureAtlas atlas){
		   character =atlas.findRegion("character");
	   }
   }
	
	public class AssetRock{
		public final AtlasRegion edge;
		public final AtlasRegion middle;
		
		public AssetRock(TextureAtlas atlas){
			edge = atlas.findRegion("rock_edge");
			middle = atlas.findRegion("rock_middle");
		}
	}
	public class AssetCurrency{
		public final AtlasRegion currency;
		
		public AssetCurrency(TextureAtlas atlas){
			currency = atlas.findRegion("currency");
		}
	}
		public class AssetBookOfPain{
			public final AtlasRegion book;
			
			public AssetBookOfPain(TextureAtlas atlas){
				book = atlas.findRegion("book");
			}
		}
	
	
	public class AssetLevelDecoration {
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion waterOverlay;
		
		public AssetLevelDecoration (TextureAtlas atlas) {
		  cloud01 = atlas.findRegion("cloud01");
		  cloud02 = atlas.findRegion("cloud02");
		  cloud03 = atlas.findRegion("cloud03");
		  mountainLeft = atlas.findRegion("mountain_left");
		  mountainRight = atlas.findRegion("mountain_right");
		  waterOverlay = atlas.findRegion("water_overlay");
		 }
		}
	
   
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '"+ 
	asset.fileName + "'",(Exception)throwable);
		
	}
	public void error(String filename, Class type,
			Throwable throwable){
		Gdx.app.error(TAG, "Couldn't load asset '" +
			filename + "'",(Exception)throwable);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		
	}

}
