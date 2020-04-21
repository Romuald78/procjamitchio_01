package fr.rphstudio.procjamitchio.spaceship;

import fr.rphstudio.procjamitchio.modules.Module;
import fr.rphstudio.procjamitchio.rng.Prng;
import fr.rphstudio.procjamitchio.services.GeoCompute;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ConfigurableEmitter.Range;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class Room
{
    //--------------------------------
    // PUBLIC CONSTANTS
    //--------------------------------
    public final static int ROOM_MIN_SIZE         = 3;
    public final static int ROOM_MAX_SIZE         = 12;
    public final static int ROOM_MIN_SPACE        = 1;
    public final static int ROOM_MAX_SPACE        = 2;
    
    public final static int ROOM_MAX_BOOSTERS     = 10;
    
    public final static int ROOM_TYPE_SPECIAL       = 0; 
    public final static int ROOM_TYPE_NORMAL        = 1000;
    
    public final static int ROOM_TYPE_WALL_UL       = 0; 
    public final static int ROOM_TYPE_WALL_UR       = 1; 
    public final static int ROOM_TYPE_WALL_DL       = 2; 
    public final static int ROOM_TYPE_WALL_DR       = 3;
    public final static int ROOM_TYPE_FLOOR         = 4;
    public final static int ROOM_TYPE_WALL_L        = 5;
    public final static int ROOM_TYPE_WALL_R        = 6;
    public final static int ROOM_TYPE_WALL_U        = 7;
    public final static int ROOM_TYPE_WALL_D        = 8;
    public final static int ROOM_TYPE_WALL_L2       = 9;
    public final static int ROOM_TYPE_WALL_R2       = 10;
    public final static int ROOM_TYPE_WALL_U2       = 11;
    public final static int ROOM_TYPE_WALL_D2       = 12;
    public final static int ROOM_TYPE_NB_NORMAL     = 13;
    
    public final static int ROOM_TYPE_CORRIDOR_H    = 0; 
    public final static int ROOM_TYPE_CORRIDOR_V    = 1; 
    public final static int ROOM_TYPE_CORRIDOR_UL   = 2; 
    public final static int ROOM_TYPE_CORRIDOR_DL   = 3; 
    public final static int ROOM_TYPE_CORRIDOR_UR   = 4; 
    public final static int ROOM_TYPE_CORRIDOR_DR   = 5; 
    public final static int ROOM_TYPE_AIRLOCK_UP    = 6;
    public final static int ROOM_TYPE_AIRLOCK_DOWN  = 7;
    public final static int ROOM_TYPE_AIRLOCK_LEFT  = 8;
    public final static int ROOM_TYPE_AIRLOCK_RIGHT = 9;
    public final static int ROOM_TYPE_BOOSTER_LEFT  = 10;
    public final static int ROOM_TYPE_NB_SPECIAL    = 11;
    
    private final static int ROOM_DEFAULT_SEED      = 987654321;
    private final static float ROOM_LIGHTWALL_RATIO = 0.12f;
    
    //--------------------------------
    // PRIVATE PROPERTIES
    //--------------------------------
    private Vector2f     dimensions;
    private Vector2f     position;
    private List<Module> moduleList;
    private int          roomType;
    private int          seed;
    private Image[]      imgFloor;
    private Image[]      imgSpecial;
    private ParticleSystem particleSystem;
    private ConfigurableEmitter particleEmitter;
    
    
    //--------------------------------
    // CONSTRUCTOR
    //--------------------------------
    public Room(Vector2f pos, Vector2f dim, int rType)
    {
        this(pos, dim, rType, ROOM_DEFAULT_SEED);
    }
    public Room(Vector2f pos, Vector2f dim, int rType, int sd)
    {
        this.moduleList = new ArrayList<Module>();
        this.dimensions = dim;
        this.position   = pos;
        this.roomType   = rType;
        this.seed       = sd;
        try
        {
            SpriteSheet ss = new SpriteSheet("sprites/modules/room_tiles.png",32,32);
            this.imgSpecial = new Image[ROOM_TYPE_NB_SPECIAL];
            this.imgFloor   = new Image[ROOM_TYPE_NB_NORMAL];
            
            this.imgFloor[ROOM_TYPE_WALL_UL]    = ss.getSprite(0, 0).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgFloor[ROOM_TYPE_WALL_UR]    = ss.getSprite(3, 0).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgFloor[ROOM_TYPE_WALL_DL]    = ss.getSprite(0, 3).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgFloor[ROOM_TYPE_WALL_DR]    = ss.getSprite(3, 3).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgFloor[ROOM_TYPE_FLOOR  ]    = ss.getSprite(1, 1).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgFloor[ROOM_TYPE_WALL_L ]    = ss.getSprite(0, 1).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgFloor[ROOM_TYPE_WALL_R ]    = ss.getSprite(3, 2).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgFloor[ROOM_TYPE_WALL_U ]    = ss.getSprite(1, 0).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgFloor[ROOM_TYPE_WALL_D ]    = ss.getSprite(2, 3).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgFloor[ROOM_TYPE_WALL_L2]    = ss.getSprite(0, 2).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgFloor[ROOM_TYPE_WALL_R2]    = ss.getSprite(3, 1).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgFloor[ROOM_TYPE_WALL_U2]    = ss.getSprite(2, 0).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgFloor[ROOM_TYPE_WALL_D2]    = ss.getSprite(1, 3).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            
            this.imgSpecial[ROOM_TYPE_CORRIDOR_H]    = ss.getSprite(1, 4).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgSpecial[ROOM_TYPE_CORRIDOR_V]    = ss.getSprite(0, 5).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgSpecial[ROOM_TYPE_AIRLOCK_UP]    = ss.getSprite(4, 2).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgSpecial[ROOM_TYPE_AIRLOCK_DOWN]  = ss.getSprite(4, 3).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgSpecial[ROOM_TYPE_AIRLOCK_LEFT]  = ss.getSprite(4, 1).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgSpecial[ROOM_TYPE_AIRLOCK_RIGHT] = ss.getSprite(4, 0).getScaledCopy(Module.NB_PIX_PER_MODULE, Module.NB_PIX_PER_MODULE);
            this.imgSpecial[ROOM_TYPE_BOOSTER_LEFT]  = new Image("sprites/modules/booster.png").getScaledCopy(3*Module.NB_PIX_PER_MODULE,3*Module.NB_PIX_PER_MODULE);
        
            // Init particle system for Booster
            // Particle system
            Image image = new Image("sprites/circleParticle.png", false);
            this.particleSystem = new ParticleSystem(image,1000);
            this.particleSystem.setBlendingMode(ParticleSystem.BLEND_COMBINE);
            File xmlFile = new File("sprites/scoreEmitter.xml");
            this.particleEmitter = ParticleIO.loadEmitter(xmlFile);
            this.particleSystem.addEmitter(this.particleEmitter);
        }
        catch(SlickException e)
        {
            throw new Error("Impossible to load image");
        }
        catch(IOException e)
        {
            throw new Error("Impossible to load XMl file");
        }
        
    
    }
    
    
    //--------------------------------
    // PUBLIC METHODS
    //--------------------------------
    public void addModuleList(List<Module> modList)
    {
        if(this.isModuleForbidden())
        {
            throw new Error("Impossible to add a Module into a Room 'Corridor'.");
        }
        this.moduleList.addAll(modList);
    }
    public void addModule(Module mod)
    {
        if(this.isModuleForbidden())
        {
            throw new Error("Impossible to add a Module into a Room 'Corridor'.");
        }
        this.moduleList.add(mod);
    }
    public void removeModule(Module mod)
    {
        if(this.isModuleForbidden())
        {
            throw new Error("Impossible to remove a Module from a Room 'Corridor' (because we cannot add it, captain obvious).");
        }
        this.moduleList.remove(mod);
    }
    
    
    //--------------------------------
    // PUBLIC METHODS
    //--------------------------------
    public void render(Graphics g, Vector2f absPos, float absRot, float absScale) throws SlickException
    {
        Image img;
        Prng rng = new Prng(this.seed);
        
        // Draw each image of the room
        for(int x=0;x<this.dimensions.x/Module.NB_PIX_PER_MODULE;x++)
        {
            for(int y=0;y<this.dimensions.y/Module.NB_PIX_PER_MODULE;y++)
            {
                img = this.imgFloor[ROOM_TYPE_FLOOR];
                if(this.isModuleForbidden())
                {
                    img = this.imgSpecial[this.roomType];
                }
                // Set img according to normal room
                if(this.roomType == ROOM_TYPE_NORMAL)
                {
                    if(x==0 && y==0)
                    {
                        img = this.imgFloor[ROOM_TYPE_WALL_UL];
                    }
                    else if(x==0 && y==(int)(this.dimensions.y/Module.NB_PIX_PER_MODULE)-1)
                    {
                        img = this.imgFloor[ROOM_TYPE_WALL_DL];
                    }
                    else if(y==0 && x==(int)(this.dimensions.x/Module.NB_PIX_PER_MODULE)-1)
                    {
                        img = this.imgFloor[ROOM_TYPE_WALL_UR];
                    }
                    else if(y==(int)(this.dimensions.y/Module.NB_PIX_PER_MODULE)-1 && x==(int)(this.dimensions.x/Module.NB_PIX_PER_MODULE)-1)
                    {
                        img = this.imgFloor[ROOM_TYPE_WALL_DR];
                    }
                    else if(x==0)
                    {
                        if(rng.random() <= ROOM_LIGHTWALL_RATIO)
                        {
                            img = this.imgFloor[ROOM_TYPE_WALL_L2];
                        }
                        else
                        {
                            img = this.imgFloor[ROOM_TYPE_WALL_L];
                        }
                    }
                    else if(x==(int)(this.dimensions.x/Module.NB_PIX_PER_MODULE)-1)
                    {
                        if(rng.random() <= ROOM_LIGHTWALL_RATIO)
                        {
                            img = this.imgFloor[ROOM_TYPE_WALL_R2];
                        }
                        else
                        {
                            img = this.imgFloor[ROOM_TYPE_WALL_R];
                        }
                    }
                    else if(y==0)
                    {
                        if(rng.random() <= ROOM_LIGHTWALL_RATIO)
                        {
                            img = this.imgFloor[ROOM_TYPE_WALL_U2];
                        }
                        else
                        {
                            img = this.imgFloor[ROOM_TYPE_WALL_U];
                        }
                    }
                    else if(y==(int)(this.dimensions.y/Module.NB_PIX_PER_MODULE)-1)
                    {
                        if(rng.random() <= ROOM_LIGHTWALL_RATIO)
                        {
                            img = this.imgFloor[ROOM_TYPE_WALL_D2];
                        }
                        else
                        {
                            img = this.imgFloor[ROOM_TYPE_WALL_D];
                        }
                    }
                }
                // Set rotation
                img.setRotation(absRot);
                // Set init position
                Vector2f iniPos = new Vector2f( this.position.x+x*Module.NB_PIX_PER_MODULE, this.position.y+y*Module.NB_PIX_PER_MODULE ); 
                // Get rotated position
                Vector2f rotPos = GeoCompute.getPosAfterRotation(iniPos, absPos, absRot);
                // Compute final pos
                float finalX  = absPos.x+absScale*(rotPos.x-img.getWidth() /2);
                float finalY  = absPos.y+absScale*(rotPos.y-img.getHeight()/2);
                float finalX2 = absPos.x+absScale*(rotPos.x-img.getWidth() /3);
                float finalY2 = absPos.y+absScale*(rotPos.y);
                // Draw particles for BOOSTER if needed
                if(this.roomType == ROOM_TYPE_BOOSTER_LEFT)
                {   
                    this.particleEmitter.setPosition(finalX2, finalY2,false);
                    this.particleEmitter.initialSize.setMin(10*absScale);
                    this.particleEmitter.initialSize.setMax(20*absScale);
                    this.particleSystem.update(100);
                    this.particleSystem.render();
                }
                // draw finally
                img.draw(finalX,finalY,absScale);                
            }
        }    
    }
    
    
    //--------------------------------
    // GETTERS
    //--------------------------------
    public Vector2f getDimensions()
    {
        return this.dimensions.copy();
    }
    public Vector2f getModuleUnitDimensions()
    {
        Vector2f moduleUnit = new Vector2f(this.dimensions.x/Module.NB_PIX_PER_MODULE, this.dimensions.y/Module.NB_PIX_PER_MODULE);
        return moduleUnit;
    }
    public Vector2f getPosition()
    {
        return this.position.copy();
    }
    public boolean isModuleForbidden()
    {
        return (this.roomType != ROOM_TYPE_NORMAL);
    }
    public int getTypeValue()
    {
        return this.roomType;
    }
    
    //--------------------------------
    // END OF CLASSE
    //--------------------------------
}
