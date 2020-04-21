package fr.rphstudio.procjamitchio.modules;

import org.newdawn.slick.Animation;
import org.newdawn.slick.geom.Vector2f;

public class Core extends Module implements Cloneable
{
	
    private float energyProduction;
    
    private float maxLife;
    private float curLife;
    
    private float weight;
    
    private static final int MAX_ALLOWED_OCCURRENCES = 1;
    
    public Core(Vector2f position, Vector2f dimensions, Animation anim)
	{
		super(position, dimensions, anim);
		this.setFlags(Module.FLAG_EXCLUSIVE);
		this.setPriority(-1);
	}

	@Override
	protected int getMaximumAllowedOccurrences() {
		// TODO Auto-generated method stub
		return Core.MAX_ALLOWED_OCCURRENCES;
	}
	
	public Object clone()
	{
		return super.clone();
	}
}
