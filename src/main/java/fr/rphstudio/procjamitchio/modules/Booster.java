package fr.rphstudio.procjamitchio.modules;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class Booster extends Module implements Cloneable
{
    private float throttle;
    
    private float energyConsumption;
    
    private float maxLife;
    private float curLife;
    
    private float weight;
    
    private static final int MAX_ALLOWED_OCCURRENCES = 0;
    
    public Booster(Vector2f position, Vector2f dimensions, Animation anim)
	{
		super(position, dimensions, anim);
		this.setPriority(-1);
	}


	@Override
	protected int getMaximumAllowedOccurrences() {
		// TODO Auto-generated method stub
		return Booster.MAX_ALLOWED_OCCURRENCES;
	}
    
	public Object clone()
	{
		return super.clone();
	}
	
}
