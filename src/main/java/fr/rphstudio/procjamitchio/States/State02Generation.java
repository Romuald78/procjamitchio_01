/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procjamitchio.States;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

class State02Generation extends BasicGameState
{
    public static final int ID = 200;

    //------------------------------------------------
    private StateBasedGame gameObject;
    private GameContainer  container;
       
    private Image          img;
    private Animation      anim;
    private boolean        isTurningLeft;
    private boolean        isTurningRight;
    private float          zoom;
    private boolean        isZoomIn;
    private boolean        isZoomOut;
    
    
    //------------------------------------------------
    // CONSTRUCTOR
    //------------------------------------------------
    public State02Generation()
    {
    }
    
    //------------------------------------------------
    // INIT METHOD
    //------------------------------------------------
    @Override
    public void init(GameContainer container, StateBasedGame sbGame) throws SlickException
    {
        this.container  = container;
        this.gameObject = sbGame;
        
        this.zoom           = 1.0f;
        this.img            = new Image("sprites/core01.gif"); 
        this.anim           = new Animation();
        
        try
        {
            SpriteSheet ss = new SpriteSheet("sprites/test.png",32,32);
            this.anim.addFrame(ss.getSprite(0, 0), 100);
            this.anim.addFrame(ss.getSprite(1, 0), 100);
            this.anim.addFrame(ss.getSprite(2, 0), 100);
            this.anim.addFrame(ss.getSprite(1, 0), 100);
            this.anim.start();
        }
        catch(SlickException e){}
        
        
        this.isTurningLeft  = false;
        this.isTurningRight = false;
        this.isZoomIn       = false;
        this.isZoomOut      = false;
    
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
    
        
       // g.drawImage(img, 100, 100);
        
        img.draw(300-this.zoom*img.getWidth()/2,200-this.zoom*img.getHeight()/2,this.zoom);
        
        // draw anim frame with zoom params
        // animation is X-axis stretched
        this.anim.draw(700-16*this.zoom, 350-16*this.zoom, 2*32*zoom, 32*zoom);
        
        
        //g.drawAnimation(this.anim, 600, 300);
        
        g.setColor(Color.gray);
        g.drawString("You shall develop your game mechanics here, dude !   ;-) ", 600, 500);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException
    {   
        // update zoom
        if(this.isZoomIn)
        {
            this.zoom *= 1.01f;
        }
        if(this.isZoomOut)
        {
            this.zoom /= 1.01f;
        }
        
        // update rotation of fixed image
        if(this.isTurningLeft)
        {
            this.img.rotate(-2);
        }
        if(this.isTurningRight)
        {
            this.img.rotate(2);
        }
        
        // update center of rotation of fixed image
        img.setCenterOfRotation(this.zoom*img.getWidth()/2, this.zoom*img.getHeight()/2);
        
        // update animation rotation angle and center (animation is X-axis stretched by 2
        for(int i=0;i<this.anim.getFrameCount();i++)
        {
            Image imgTmp = this.anim.getImage(i);
            imgTmp.setCenterOfRotation(2*this.zoom*imgTmp.getWidth()/2, this.zoom*imgTmp.getHeight()/2);
            imgTmp.setRotation(img.getRotation());
        }
    }
    
    @Override
    public void keyPressed(int key, char c)
    {
        switch(key)
        {
            case Input.KEY_LEFT:
                this.isTurningLeft = true;
                break;
            case Input.KEY_RIGHT:
                this.isTurningRight = true;
                break;
            case Input.KEY_UP:
                this.isZoomIn = true;
                break;
            case Input.KEY_DOWN:
                this.isZoomOut = true;
                break;
        }
    }
    
    
    @Override
    public void keyReleased(int key, char c)
    {
        switch(key)
        {
            case Input.KEY_ESCAPE:
                this.gameObject.enterState(State01Menu.ID, new FadeOutTransition(Color.black, 125), new FadeInTransition(Color.black, 125) );
                break;
            case Input.KEY_LEFT:
                this.isTurningLeft = false;
                break;
            case Input.KEY_RIGHT:
                this.isTurningRight = false;
                break;
            case Input.KEY_UP:
                this.isZoomIn = false;
                break;
            case Input.KEY_DOWN:
                this.isZoomOut = false;
                break;
        }
    }
    
    @Override
    public int getID()
    {
          return this.ID;
    }
}