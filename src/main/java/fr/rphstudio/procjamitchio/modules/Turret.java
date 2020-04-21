package fr.rphstudio.procjamitchio.modules;

import org.newdawn.slick.Animation;
import org.newdawn.slick.geom.Vector2f;

public class Turret extends Module implements Cloneable
{
    private int   playerID;
    
    private float energyConsumption;
    
    private float minDamage;
    private float maxDamage;
    private float maxDistance;
    private float shootRate;
    
    private float maxLife;
    private float curLife;
    
    private float weight;
    
    private static final int MAX_ALLOWED_OCCURRENCES = 0;
    
    public Turret(Vector2f position, Vector2f dimensions, Animation anim)
	{
		super(position, dimensions, anim);
	}

	@Override
	protected int getMaximumAllowedOccurrences() {
		// TODO Auto-generated method stub
		return Turret.MAX_ALLOWED_OCCURRENCES;
	}

	public Object clone()
	{
		return super.clone();
	}
}
