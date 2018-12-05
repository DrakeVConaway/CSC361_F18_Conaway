package gameWorld;
/**
 * Assets for the new game
 */
import com.badlogic.gdx.Gdx; 
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import utils.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
   public AssetFonts fonts;
   public AssetSounds sounds;
   public AssetMusic music;
  
   /**
    * Initialization 
    * @param assetManager
    */
   public void init(AssetManager assetManager){
	   this.assetManager = assetManager;
	// set asset manager error handler
	assetManager.setErrorListener(this);
	// load texture atlas
	assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,
	  TextureAtlas.class);
	//load sounds
	assetManager.load("sounds/jump.wav",Sound.class);
	assetManager.load("sounds/pickup_coin.wav",Sound.class);
	assetManager.load("sounds/live_lost.wav",Sound.class);
	assetManager.load("sounds/pickup_feather.wav",Sound.class);
	//load Music
	assetManager.load("music/keith303_-_brand_new_highscore.mp3",
			Music.class);
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
   //create game resource objects
   fonts = new AssetFonts();
   character = new AssetCharacter(atlas);
   rock = new AssetRock(atlas);
   currency = new AssetCurrency(atlas);
   book = new AssetBookOfPain(atlas);
   levelDecoration = new AssetLevelDecoration(atlas);
   sounds = new AssetSounds(assetManager);
   music = new AssetMusic(assetManager);
   }
   
   
   /**
    * assetSounds internal class
    */
   public class AssetSounds{
	   public final Sound jump;
	   public final Sound pickupSoul;
	   public final Sound liveLost;
	   public final Sound pickupBook;
	   public AssetSounds(AssetManager am) {
		   jump = am.get("sounds/jump.wav",Sound.class);
		   pickupSoul = am.get("sounds/pickup_coin.wav",Sound.class);
		   liveLost = am.get("sounds/live_lost.wav",Sound.class);
		   pickupBook = am.get("sounds/pickup_feather.wav",Sound.class);
	   }
   }
   /**
    * Inner class for music
    */
   public class AssetMusic{
	   public final Music song01;
	   public AssetMusic(AssetManager am) {
		   song01 = am.get("music/keith303_-_brand_new_highscore.mp3",
				   Music.class);//need to add custom mp3 for papa
	   }
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
		   character =atlas.findRegion("Papa_Game");
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
			currency = atlas.findRegion("Souls_Game");
		}
	}
		public class AssetBookOfPain{
			public final AtlasRegion book;
			
			public AssetBookOfPain(TextureAtlas atlas){
				book = atlas.findRegion("Book_of_Pain");
			}
		}
	
	
	public class AssetLevelDecoration {
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion waterOverlay;
		public final AtlasRegion knife; //rain of knives
		public final AtlasRegion goal; //did you collect the souls?
		
		public AssetLevelDecoration (TextureAtlas atlas) {
		  cloud01 = atlas.findRegion("cloud01");
		  cloud02 = atlas.findRegion("cloud02");
		  cloud03 = atlas.findRegion("cloud03");
		  mountainLeft = atlas.findRegion("mountain_left");	 
		  mountainRight = atlas.findRegion("mountain_right");
		  waterOverlay = atlas.findRegion("water_overlay");
		  knife = atlas.findRegion("knife");
		  goal = atlas.findRegion("goal"); //may be the baphomet, filler
		 }
		}
	
	/**
	 * Font interior class
	 */
	public class AssetFonts {
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		public AssetFonts () {
		// create three fonts using Libgdx's 15px bitmap font
		defaultSmall = new BitmapFont(
		  Gdx.files.internal("images/arial-15.fnt"), true);
		defaultNormal = new BitmapFont(
		  Gdx.files.internal("images/arial-15.fnt"), true);
		defaultBig = new BitmapFont(
		  Gdx.files.internal("images/arial-15.fnt"), true);
		// set font sizes
		defaultSmall.getData().setScale(0.75f);
		defaultNormal.getData().setScale(1.0f);
		defaultBig.getData().setScale(2.0f);
		// enable linear texture filtering for smooth fonts
		defaultSmall.getRegion().getTexture().setFilter(
		  TextureFilter.Linear, TextureFilter.Linear);
		defaultNormal.getRegion().getTexture().setFilter(
		  TextureFilter.Linear, TextureFilter.Linear);
		defaultBig.getRegion().getTexture().setFilter(
		  TextureFilter.Linear, TextureFilter.Linear);
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
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
		
	}

}
