/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procjamitchio.States;

import fr.rphstudio.procjamitchio.services.SpaceSheepGenerator;
import fr.rphstudio.procjamitchio.spaceship.SpaceShip;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class State03Generation extends BasicGameState {
    public static final int ID = 300;

    //------------------------------------------------
    private StateBasedGame gameObject;
    private GameContainer container;

    private boolean isTurningLeft;
    private boolean isTurningRight;
    private float zoom;
    private boolean isMoveForward;
    private boolean isMoveRight;
    private boolean isMoveLeft;
    private boolean isScaleUp;
    private boolean isScaleDown;
    private boolean isMoveBackward;
    public final float screenWidth = 1920;
    public final float screenHeight = 1080;
    private Image background;
    private Image controls;
    private String         version;
    
    SpaceShip spaceShip;

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
        catch (IOException e){ throw new Error(e); }
        finally
        {
            try{ if (br != null){ br.close(); } }
            catch (IOException ex){ throw new Error(ex); }
        }
    }
    
    //------------------------------------------------
    // CONSTRUCTOR
    //------------------------------------------------
    public State03Generation() {
    }

    //------------------------------------------------
    // INIT METHOD
    //------------------------------------------------
    @Override
    public void init(GameContainer container, StateBasedGame sbGame) throws SlickException
    {
        this.container = container;
        gameObject = sbGame;

        zoom = 1.0f;

        isTurningLeft = false;
        isTurningRight = false;
        isMoveForward = false;
        isMoveBackward = false;
        SpaceSheepGenerator spaceSheepGenerator = new SpaceSheepGenerator();
        SpaceSheepGenerator.GenerationParameters generationParameters = new SpaceSheepGenerator.GenerationParameters();
        spaceShip = spaceSheepGenerator.generate(generationParameters);
        background = new Image("sprites/background.png");
        controls   = new Image("sprites/controls.png").getScaledCopy(0.75f);
    
        // Get version string
        this.getVersion();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        // rendering is done : now try to scale it to fit the full screen
        int screenWidth  = ((AppGameContainer) container).getScreenWidth();
        int screenHeight = ((AppGameContainer) container).getScreenHeight();
        float sx         = screenWidth  / this.screenWidth;
        float sy         = screenHeight / this.screenHeight;
        float sxy        = Math.min(sx, sy);
        float tx         = (screenWidth  - (this.screenWidth  * sxy)) / 2;
        float ty         = (screenHeight - (this.screenHeight * sxy)) / 2;
        // Scale and translate to the center of screen
        g.translate(tx, ty);
        g.scale(sxy, sxy);

        // Display background image
        g.setBackground(Color.gray);
        background.draw(0,0,this.screenWidth,this.screenHeight);
        
        // display space ship
        spaceShip.render(g);
        
        // display controls
        g.drawImage(controls, 1920-controls.getWidth()-25, 1080-controls.getHeight()-25);

        // Render version number
        g.setColor(Color.yellow);
        g.drawString(this.version, 0, 1080-30);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        // update zoom
        if (isScaleUp) {
            spaceShip.scale(1.1f);
        }
        if (isScaleDown) {
            spaceShip.scale(1f/1.1f);
        }
        if (isMoveForward) {
            spaceShip.move(1, 0);
        }
        if (isMoveBackward) {
            spaceShip.move(-1, 0);
        }
        if (isMoveRight) {
            spaceShip.move(0, 1);
        }
        if (isMoveLeft) {
            spaceShip.move(0, -1);
        }
        // update rotation of fixed image
        if (isTurningLeft) {
            spaceShip.rotate(-2);
        }
        if (isTurningRight) {
            spaceShip.rotate(2);
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        switch (key) {
            case Input.KEY_R:
                isScaleUp = true;
                break;
            case Input.KEY_F:
                isScaleDown = true;
                break;
            case Input.KEY_Q:
                isMoveLeft = true;
                break;
            case Input.KEY_D:
                isMoveRight = true;
                break;
            case Input.KEY_A:
            case Input.KEY_LEFT:
                isTurningLeft = true;
                break;
            case Input.KEY_E:
            case Input.KEY_RIGHT:
                isTurningRight = true;
                break;
            case Input.KEY_Z:
            case Input.KEY_UP:
                isMoveForward = true;
                break;
            case Input.KEY_S:
            case Input.KEY_DOWN:
                isMoveBackward = true;
                break;
        }
    }


    @Override
    public void keyReleased(int key, char c) {
        switch (key) {
            case Input.KEY_ESCAPE:
                this.container.exit();
                break;
            case Input.KEY_R:
                isScaleUp = false;
                break;
            case Input.KEY_F:
                isScaleDown = false;
                break;
            case Input.KEY_Q:
                isMoveLeft = false;
                break;
            case Input.KEY_D:
                isMoveRight = false;
                break;
            case Input.KEY_A:
            case Input.KEY_LEFT:
                isTurningLeft = false;
                break;
            case Input.KEY_E:
            case Input.KEY_RIGHT:
                isTurningRight = false;
                break;
            case Input.KEY_Z:
            case Input.KEY_UP:
                isMoveForward = false;
                break;
            case Input.KEY_S:
            case Input.KEY_DOWN:
                isMoveBackward = false;
                break;
            case Input.KEY_SPACE:
                this.spaceShip.resetPosition();
                break;    
            case Input.KEY_ENTER:
                try
                {
                    SpaceSheepGenerator spaceSheepGenerator = new SpaceSheepGenerator();
                    SpaceSheepGenerator.GenerationParameters generationParameters = new SpaceSheepGenerator.GenerationParameters();
                    spaceShip = spaceSheepGenerator.generate(generationParameters);
                }
                catch(SlickException e){}
                break;
        }
    }

    @Override
    public int getID() {
        return ID;
    }
}