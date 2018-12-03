package utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
public class Constants {
//visible game world is 5 meters wide
	public static final float VIEWPORT_WIDTH = 5.0f;
	
	//visible game world is 5 meters tall
	public static final float VIEWPORT_HEIGHT = 5.0f;
	//GUI Width
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	//GUI Height
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
	//Amount of extra lives at start
	public static final int LIVES_START = 3;
	//Delay after game ove
	public static final float TIME_DELAY_GAME_OVER = 3;
	
	// Location of image file for level 01
	public static final String LEVEL_01 = "levels/level-01.png";
	
	//constants 
	public static final String TEXTURE_ATLAS_OBJECTS = "images/Hellferno.atlas";
	public static final String TEXTURE_ATLAS_UI =
			"images/hellferno-ui.pack.atlas"; //need to refactor to hellferno
			
	public static final String TEXTURE_ATLAS_LIBGDX_UI =
			"images/uiskin.atlas";
			// Location of description file for skins
			
	public static final String SKIN_LIBGDX_UI =
			"images/uiskin.json";
			
	public static final String SKIN_CANYONBUNNY_UI = //need to refactor to hellferno
			"images/canyonbunny-ui.json";

	public static final String PREFERENCES = "hellferno.prefs";
}
