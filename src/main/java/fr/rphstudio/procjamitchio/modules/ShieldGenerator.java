package fr.rphstudio.procjamitchio.modules;

import org.newdawn.slick.Animation;
import org.newdawn.slick.geom.Vector2f;

public class ShieldGenerator extends Module implements Cloneable
{
	
    private float energyConsumption;
    
    private float maxShield;
    private float curShield;
    private float shieldRegen;
    private float directionAngle;
    private float deltaAngle;
    private float maxDistance;
    
    private float maxLife;
    private float curLife;
    
    private float weight;
    
    private static final int MAX_ALLOWED_OCCURRENCES = 0;
    
    public ShieldGenerator(Vector2f position, Vector2f dimensions, Animation anim)
	{
		super(position, dimensions, anim);
	}

	@Override
	protected int getMaximumAllowedOccurrences() {
		// TODO Auto-generated method stub
		return ShieldGenerator.MAX_ALLOWED_OCCURRENCES;
	}
	
	public Object clone()
	{
		return super.clone();
	}
    
}
