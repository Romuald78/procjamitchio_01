package fr.rphstudio.procjamitchio.services;
import fr.rphstudio.procjamitchio.modules.Module;
import fr.rphstudio.procjamitchio.modules.ModulesFactory;
import fr.rphstudio.procjamitchio.modules.ModulesFactoryException;
import fr.rphstudio.procjamitchio.modules.ModulesFactory.MODULE_TYPE;
import fr.rphstudio.procjamitchio.spaceship.Room;

import java.util.ArrayList;
import java.util.List;

public class ModuleGenerator {
	
	private ModuleAllocator allocator;
	
	public ModuleGenerator()
	{
		this.allocator = new ModuleAllocator();
		
		try
		{
			this.registerAllKnownModules();
		}
		catch(ModulesFactoryException e)
		{
			// couldn't register or type does not exist
			e.printStackTrace();
			
		}
	}
	
	/*!
	 * \brief Utility function to register all modules in the enum
	 * 
	 */
	private void registerAllKnownModules() throws ModulesFactoryException
	{
		for(MODULE_TYPE m: MODULE_TYPE.values() )
		{
			this.allocator.registerModule(ModulesFactory.getModule(m));
		}
	}
	
	
	public List<Module> generateModules(Room r)
	{
		List<Module> returnValue = new ArrayList<Module>();
		Module m = this.allocator.getRandomModule(r.getDimensions(), ModuleAllocator.FIT_METHOD.HIGHEST_PRIORITY_ONLY);
		
		// Module found that fits ? save it in the resulting list of modules
		if( m != null )
		{
			returnValue.add(m);
		}
		
		// TODO: compute new regions in which to put modules
		
		return returnValue;
	}

}
