/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procjamitchio.States;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import fr.rphstudio.procjamitchio.Common;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class State01Menu extends BasicGameState
{   
    //------------------------------------------------
    // PRIVATE CONSTANTS
    //------------------------------------------------
    private final static int INIT    = 0;
    private final static int SELECT  = 1;
    private final static int STARTED = 2;
    
    
    //------------------------------------------------
    // PUBLIC CONSTANTS
    //------------------------------------------------
    public static final int ID = 100;

    
    //------------------------------------------------
    // PRIVATE PROPERTIES
    //------------------------------------------------
    private StateBasedGame gameObject;
    private GameContainer  container;
    private String         version;
    private int            internalState = State01Menu.INIT;   
    private int[]          playerRequest;   
    
    
    //------------------------------------------------
    // CONSTRUCTOR
    //------------------------------------------------
    public State01Menu()
    {
        
    }
    
    //------------------------------------------------
    // INIT METHOD
    //------------------------------------------------
    @Override
    public void init(GameContainer container, StateBasedGame sbGame) throws SlickException
    {
        this.container         = container;
        this.gameObject        = sbGame;
        // init state machine (selection)
        this.internalState = State01Menu.INIT;
        // init player controls
        this.playerRequest = new int[Common.MAX_PLAYERS];
        for(int i=0;i<this.playerRequest.length;i++)
        {
            this.playerRequest[i] = Common.UNUSED_CTRL;
        }

        // Get version string
        this.getVersion();
    
    }
    
    
    private boolean isControllerAlreadyRequested(int ctrl)
    {
        // check if the controller ID is already requested for this game
        for(int i=0;i<this.playerRequest.length;i++)
        {
            if(this.playerRequest[i] == ctrl)
            {
                return true;
            }
        }
        return false;
    }
    
    private void addControllerRequest(int ctrl)
    {
        for(int i=0;i<this.playerRequest.length;i++)
        {
            if(this.playerRequest[i] == Common.UNUSED_CTRL)
            {
                this.playerRequest[i] = ctrl;
                return; 
            }
        }
        // there is no more space
        throw new Error("No more space available for another player.");
    }
    
    private void flowState(int controlID)
    {
        switch(this.internalState)
        {
            // Start step
            case State01Menu.INIT:
                // Check if this controller is already requested
                if(this.isControllerAlreadyRequested(controlID) == false)
                {
                    this.addControllerRequest(controlID);
                }
                else
                {
                    throw new Error("Impossible to have a player requested at this state");
                }
                // Now set the next step (select players)
                this.internalState = State01Menu.SELECT;
                break;
                
            // Selection step
            case State01Menu.SELECT:
                // Check if this controller is already requested
                if(this.isControllerAlreadyRequested(controlID) == false)
                {
                    this.addControllerRequest(controlID);
                }
                else
                {
                    // It is time to start the game
                    State03Generation sig = new State03Generation();
                    this.gameObject.addState( sig );
                    try{sig.init(this.container, this.gameObject);}catch(Exception e){throw new Error(e);};
                    this.gameObject.enterState( State03Generation.ID, new FadeOutTransition(Color.orange, 250), new FadeInTransition(Color.yellow, 250) );
                    this.internalState = State01Menu.STARTED;
                }
                break;
            
            // Resume game
            case State01Menu.STARTED:
                this.gameObject.enterState( State03Generation.ID, new FadeOutTransition(Color.black, 125), new FadeInTransition(Color.black, 125) );
                break;

            // Do nothing else
            default:
                break;
        }
    }
    
    private void getVersion()
    {
        // Get display version
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader("info/version.txt"));
            String line;
            line = br.readLine();
            if(line != null)
            {
                this.version = line;
            }
            if (br != null)
            {
                br.close();
            }
        }
        catch (IOException e)
        {
            throw new Error(e);
        }
        finally
        {
            try
            {
                if (br != null)
                {
                    br.close();
                }
            }
            catch (IOException ex)
            {
                throw new Error(ex);
            }
        }
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException
    {
        // rendering is done : now try to scale it to fit the full screen
        float sx  = ((AppGameContainer)container).getScreenWidth()/1920.0f;
        float sy  = ((AppGameContainer)container).getScreenHeight()/1080f;
        float sxy = Math.min(sx,sy); 
        float tx  = (((AppGameContainer)container).getScreenWidth() -(1920*sxy))/2;
        float ty  = (((AppGameContainer)container).getScreenHeight()-(1080*sxy))/2; 
        // Scale and translate to the center of screen
        g.translate(tx, ty);
        g.scale( sxy, sxy );

        // Render main menu
        g.setColor(Color.orange);
        g.drawString("PROC JAM - ITCH.io - RPH Studio - November 2017", 600, 200);
        g.drawString("Procedural SpaceShip Generation.", 600, 225);
        switch(this.internalState)
        {
            case State01Menu.INIT :
                g.setColor(Color.white);
                g.drawString("Press any BUTTON or SPACE to :", 600, 325);    
                g.drawString(" - Generate a new Spaceship", 650, 350);    
                g.drawString(" - Select a previously captured SpaceShip", 650, 375);
                g.setColor(Color.red);
                g.drawString("Press ESCAPE to exit.", 600, 425);
                g.setColor(Color.gray);
                g.drawString("(During game, press ESCAPE to pause).", 600, 450);
                break;
            case State01Menu.SELECT :                
                g.setColor(Color.red);
                g.drawString("Press ESCAPE to kill the current game.", 600, 425);
                break;
            case State01Menu.STARTED :
                g.setColor(Color.white);
                g.drawString("Press BUTTON or SPACE to resume game.", 600, 325);
                g.setColor(Color.red);
                g.drawString("Press ESCAPE to kill the current game.", 600, 425);
                break;
            default :
                break;
        }
        
        // Render version number
        g.setColor(Color.gray);
        g.drawString(this.version, 1720, 1080-30);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException
    {   
        
    }
    
    @Override
    public void keyReleased(int key, char c)
    {
        GameState gs;
        System.out.println("Menu Key released "+key);
        switch(key)
        {
            case Input.KEY_ESCAPE:
                switch(this.internalState)
                {
                    case State01Menu.INIT:
                        this.container.exit();
                        break;
                        
                    case State01Menu.SELECT:
                        // Reset the main menu state
                        gs = this.gameObject.getState(State01Menu.ID);
                        try{gs.init(this.container, this.gameObject);}catch(Exception e){throw new Error(e);};
                        break;
                        
                    case State01Menu.STARTED:
                        // Remove in game controller
                        gs = this.gameObject.getState(State03Generation.ID);
                        try{gs.leave(this.container, this.gameObject);}catch(Exception e){throw new Error(e);};
                        // Reset the main menu state
                        gs = this.gameObject.getState(State01Menu.ID);
                        try{gs.init(this.container, this.gameObject);}catch(Exception e){throw new Error(e);};
                        break;
                        
                    default:
                        break;
                }
                break;
                
            case Input.KEY_SPACE:
                this.flowState(Common.KEYBOARD_CTRL);
                break;
                
            default:
                break;
        }
    }
    
    @Override
    public void controllerButtonReleased(int controller, int button)
    {   
        this.flowState(controller);
    }

    @Override
    public int getID()
    {
          return this.ID;
    }
}