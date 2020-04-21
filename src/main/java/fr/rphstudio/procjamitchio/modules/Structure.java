package fr.rphstudio.procjamitchio.modules;

import org.newdawn.slick.Animation;
import org.newdawn.slick.geom.Vector2f;

public class Structure extends Module implements Cloneable
{
	private float maxLife;
    private float curLife;
    
    private float weight;
    
    private static final int MAX_ALLOWED_OCCURRENCES = 0;
    
	public Structure(Vector2f position, Vector2f dimensions, Animation anim)
	{
		super(position, dimensions, anim);
	}

	@Override
	protected int getMaximumAllowedOccurrences() {
		// TODO Auto-generated method stub
		return Structure.MAX_ALLOWED_OCCURRENCES;
	}
 
	public Object clone()
	{
		return super.clone();
	}
}
