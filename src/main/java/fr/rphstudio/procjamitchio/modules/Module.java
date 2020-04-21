package fr.rphstudio.procjamitchio.modules;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/*!
 * Common base for the module
 */
public abstract class Module implements Cloneable {
	protected Vector2f dimensions;
	protected Vector2f position;
	protected Animation animation;
	protected int flags = 0x00;
	protected int priority = 0x00;
	
	
	public static final byte FLAG_NONE = 0x00;
	public static final byte FLAG_EXCLUSIVE = 0x01;
	
        public static int NB_PIX_PER_MODULE = 3;

	public Module(Vector2f position, Vector2f dimensions, Animation anim)
	{
		this.position = position;
		this.dimensions = dimensions;
		this.animation = anim;
	}
	
	
	public Vector2f getDimensions()
	{
		return this.dimensions.copy();
	}
	
	public Vector2f getPosition()
	{
		return this.position.copy();
	}
	
	/*!
	 * Update flags
	 */
	public void setFlags(int flags)
	{
		this.flags = flags;
	}
	
	/*!
	 * Return flags
	 */
	public int getFlags() 
	{
		return this.flags;
	}

	/*!
	 * Sets priority
	 * 
	 * the less the number, the higher the priority
	 */
	public void setPriority(int newprio)
	{
		this.priority = newprio;
	}
	
	/*!
	 * Returns priority
	 * 
	 * the less the number, the higher the priority
	 */
	public int getPriority()
	{
		return this.priority;
	}
	
	/*!
	 * < 0: should never occur
	 * 0 : infinite number
	 * > 0 : a definite number
	 */
	protected abstract int getMaximumAllowedOccurrences();
	
	
	public int getMaximumOccurrences()
	{
		return this.getMaximumAllowedOccurrences();
	}
	
	public boolean doesFit(Vector2f dimensions)
	{
		return (this.dimensions.x <= dimensions.x) && (this.dimensions.y <= dimensions.y);
	}
	
	/*!
	 * \brief to be implemented
	 */
	public void render(Graphics g, Vector2f absPos, float rotation, float upscale) throws SlickException
	{
		// render content
		// TODO
		// WIP
		g.drawOval(absPos.x + this.position.x, absPos.y + this.position.y, this.dimensions.x * upscale, this.dimensions.y * upscale);
	}
	
	public Object clone()
	{
		Module o = null;
		try {
			o = (Module)super.clone();
			o.dimensions = this.dimensions.copy();
			o.position = this.position.copy();
			o.animation = null;
			if( this.animation != null )
				o.animation = this.animation.copy();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return o;
	}
	
}