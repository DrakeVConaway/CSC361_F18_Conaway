package utils;
/**
 * Camera Helper class 
 * @author Drake Conaway
 *
 */
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import gameObjects.AbstractGameObject;
public class CameraHelper {
   private static final String TAG = CameraHelper.class.getName();
   
   private final float MAX_ZOOM_IN = 0.25f;
   private final float MAX_ZOOM_OUT = 10.0f;
   
   private Vector2 position;
   private float zoom;
   private Sprite target;
   
   public CameraHelper(){
	   position = new Vector2();
	   zoom = 1.0f;
   }
   
   public void update(float deltaTime){
	   if(!hasTarget()) return;
	   
	   position.x = target.getX() + target.origin.x;
	   position.y = target.getY() + target.origin.y;
   }
   /**
    * Set camera target by location
    */
   public void setTarget(AbstractGameObject target){
	   this.target = target;
   }
   /**
    * return target
    */
   public AbsractGameObject getTarget(){
	   return target;
   }
   /**
    * boolean, does cam have a target?
    */
   public boolean hasTarget(AbstractGameObject target){
	   return hasTarget() && this.target.equals(target);
   }
   public void setPosition(float x,float y){
	   this.position.set(x,y);
   }
   
   /**
    * getter for cam position
    */
     public Vector2 getPosition(){return position;}
     /**
      * Add zoom
      * @param amount
      */
     public void addZoom (float amount) { setZoom(zoom + amount); }
     /**
      * set zoom amount
      * @param zoom
      */
     public void setZoom (float zoom) {
        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
     }
     /**
      * 
      * @return zoom
      */
     public float getZoom () { return zoom; }
     /**
      * set the target to
      * @param target
      */
     public void setTarget (Sprite target) { this.target = target; }
     /**
      * @return target
      */
     public Sprite getTarget () { return target; }
     /**
      * checker if cam has target
      * @return boolean
      */
     public boolean hasTarget () { return target != null; }
     public boolean hasTarget (Sprite target) {
         return hasTarget() && this.target.equals(target);
     }
     /**
      * apply changes to the
      * @param camera
      */
     public void applyTo (OrthographicCamera camera) {
     camera.position.x = position.x;
     camera.position.y = position.y;
     camera.zoom = zoom;
     camera.update();
     }
}
