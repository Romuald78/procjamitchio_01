package fr.rphstudio.procjamitchio.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.geom.Vector2f;

import fr.rphstudio.procjamitchio.modules.Module;
import fr.rphstudio.procjamitchio.rng.Prng;

public class ModuleAllocator {
	private HashMap<Module, Integer> modules;
	private int highestPriority = 100;
	private Prng prng = new Prng();
	
	public enum FIT_METHOD 
	{
		HIGHEST_PRIORITY_ONLY,
		PRIORITY_IN_DESCENDING_ORDER
	}
	
	public ModuleAllocator()
	{
		this.modules = new HashMap<Module, Integer>();
	}
	
	public boolean registerModule(Module m)
	{
		this.highestPriority = Integer.min(m.getPriority(), this.highestPriority);
		return (this.modules.put(m, 0) == null);
	}
	
	private void updateHighestPriority()
	{
		this.highestPriority = 100; // default
		
		for( Map.Entry<Module,Integer> pair: this.modules.entrySet())
		{
			Module curModule = ((Module)pair.getKey());
			// current module has already reached its maximum number of occurrences ? ignore it
			if( (Integer)pair.getValue() < curModule.getMaximumOccurrences())
			{
				this.highestPriority = Integer.min(curModule.getPriority(), this.highestPriority);
			}
		}
	}
	
	
	/*!
	 * Returns a randomly selected module in the list of modules
	 * that would fit in the provided dimensions
	 * 
	 * If modules have high priority, they will be selected first
	 */
	public Module getRandomModule(Vector2f dimension, FIT_METHOD fitMethod)
	{
		List<Module> possibleCandidates = new ArrayList<Module>();
		
		// Retrieve modules with highest priority
		
		for( Map.Entry<Module, Integer> pair : this.modules.entrySet() )
		{
			Module curModule = ((Module)pair.getKey());
			// ignore module if:
			// - its priority does not match the currently searched priority
			// - it has already reached its maximum number of allowed instances
			if(curModule.getPriority() == this.highestPriority &&
				((Integer)pair.getValue()) < curModule.getMaximumOccurrences() 
				)
			{
				possibleCandidates.add(curModule);
			}
		}
		
		// trying to fit a module in a room
		Module selectedEntry = null;
		if( possibleCandidates.size() > 0)
		{
			do
			{
				int selectedIndex = (int)Math.round(prng.random() * (double)(possibleCandidates.size() - 1)); 
				selectedEntry = possibleCandidates.get(selectedIndex);
				// remove candidate from possible candidates
				possibleCandidates.remove(selectedIndex);
			}while(selectedEntry.doesFit(dimension) && possibleCandidates.size() > 0);
		}
			
		if( selectedEntry != null && selectedEntry.doesFit(dimension) )
		{
			// update counter of the selected module to indicate it is used
			this.modules.put(selectedEntry, this.modules.get(selectedEntry) + 1);
		}
		// recompute highest priority for module to be sure the next call will be OK
		this.updateHighestPriority();
		return (Module)(selectedEntry != null ? selectedEntry.clone():null);
	}
}

