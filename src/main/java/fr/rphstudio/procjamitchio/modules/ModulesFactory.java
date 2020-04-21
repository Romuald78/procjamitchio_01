package fr.rphstudio.procjamitchio.modules;

import org.newdawn.slick.geom.Vector2f;

public class ModulesFactory {
	public enum MODULE_TYPE
	{
		CARGO,
		CORE,
		PILOT,
		SHIELD_GENERATOR,
		STRUCTURE,
		TURRET
	}
	
	
	public static Module getModule(MODULE_TYPE mt) throws ModulesFactoryException
	{
		Module result = null;
		switch(mt)
		{
		case CARGO:
			result =  new Cargo(new Vector2f(), new Vector2f(), null);
			break;
		case CORE:
			result =  new Core(new Vector2f(), new Vector2f(), null);
			break;
		case PILOT:
			result =  new Pilot(new Vector2f(), new Vector2f(), null);
			break;
		case SHIELD_GENERATOR:
			result =  new ShieldGenerator(new Vector2f(), new Vector2f(), null);
			break;
		case STRUCTURE:
			result =  new Structure(new Vector2f(), new Vector2f(), null);
			break;
		case TURRET:
			result =  new Turret(new Vector2f(), new Vector2f(), null);
			break;
		default:
			break;
		}
		
		if( null != result)
			return result;
		
		throw new ModulesFactoryException();
	}
}

