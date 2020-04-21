package fr.rphstudio.procjamitchio.modules;

import org.newdawn.slick.Animation;
import org.newdawn.slick.geom.Vector2f;

public class Pilot extends Module implements Cloneable
{
    private int   playerID;
    
    private float energyConsumption;
    
    private float weight;
    
    private static final int MAX_ALLOWED_OCCURRENCES = 0;
    
    public Pilot(Vector2f position, Vector2f dimensions, Animation anim)
	{
		super(position, dimensions, anim);
		this.setFlags(Module.FLAG_EXCLUSIVE);
		this.setPriority(-1);
		
	}

	@Override
	protected int getMaximumAllowedOccurrences() {
		// TODO Auto-generated method stub
		return Pilot.MAX_ALLOWED_OCCURRENCES;
	}
	
	public Object clone()
	{
		return super.clone();
	}
    
}
