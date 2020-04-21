

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procjamitchio;

import fr.rphstudio.procjamitchio.States.State01Menu;
import fr.rphstudio.procjamitchio.States.State03Generation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MainLauncher extends StateBasedGame
{
    public static void main(String[] args) throws SlickException
    {
        // Full game HD
        AppGameContainer appGame = new AppGameContainer(new MainLauncher());
        appGame.setDisplayMode(appGame.getScreenWidth(), appGame.getScreenHeight(), false);
        appGame.start();
    }  
    
    public MainLauncher()
    {
        super("Proc Jam ITCH.IO - RPH Studio - November 2017");
    }
     
    @Override
    public void initStatesList(GameContainer container) throws SlickException
    {
        // Remove or Display FPS
        container.setShowFPS(true);
        // Modify Title and Icon
        AppGameContainer appContainer = (AppGameContainer) container;
        appContainer.setVSync(true);
           
        // First set icons
        if(!container.isFullscreen())
        {
            String[] icons = { "icon32x32.png", "icon16x16.png"};
            container.setIcons(icons);
        }
        
        // Add Main menu state
        this.addState( new State03Generation() );
        
    }
  
}

