/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procjamitchio.services;

import fr.rphstudio.procjamitchio.modules.Module;
import fr.rphstudio.procjamitchio.rng.Prng;
import fr.rphstudio.procjamitchio.spaceship.Room;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author GRIGNON FAMILY
 */
public class RoomGenerator
{
    //------------------------------------------------
    // PRIVATE CONSTANTS
    //------------------------------------------------
    private final static int ROOMGEN_DIR_UP         = 0;
    private final static int ROOMGEN_DIR_DOWN       = 1;
    private final static int ROOMGEN_DIR_LEFT       = 2;
    private final static int ROOMGEN_DIR_RIGHT      = 3;
    private final static int ROOMGEN_NB_DIR         = 4;
    private final static int ROOMGEN_DEFAULT_SEED   = 123456789;

    
    //------------------------------------------------
    // PRIVATE PROPERTIES
    //------------------------------------------------
    private Vector2f     dimensions;
    private int          seed;
    private Image        shipShape;
    
    
    //------------------------------------------------
    // CONSTRUCTOR
    //------------------------------------------------
    public RoomGenerator(Image shape, int w, int h, int sd)
    {
        // store dimensions
        this.dimensions = new Vector2f(w,h);
        // store seed
        this.seed       = sd;
        // Store shape image
        this.shipShape = shape;
    }
    public RoomGenerator(Image shape, int w, int h)
    {
        this(shape, w,h,ROOMGEN_DEFAULT_SEED);
    }

    
    //------------------------------------------------
    // PRIVATE METHODS
    //------------------------------------------------
    private boolean isBoxInBox(Vector2f p1, Vector2f d1, Vector2f p2, Vector2f d2)
    {
        // check if p1 is outside p2
        if(p1.x+d1.x < p2.x)
        {
            return false;
        }
        if(p1.x >= p2.x+d2.x)
        {
            return false;
        }
        if(p1.y+d1.y < p2.y)
        {
            return false;
        }
        if(p1.y >= p2.y+d2.y)
        {
            return false;
        }
        return true;
    }
    private boolean isRoomCollision(Room myRoom, List<Room> myRoomList)
    {
        // get pos and dim for current room
        Vector2f p1 = myRoom.getPosition();
        Vector2f p2;
        Vector2f d1 = myRoom.getDimensions();
        Vector2f d2;
        
        // check the room parameter is not in collision with one of the others
        for(int i=0; i<myRoomList.size(); i++)
        {
            // First check the room is inside the global shape size
            if( (p1.x <= -this.dimensions.x/2) || (p1.y <= -this.dimensions.y/2) || (p1.x >= this.dimensions.x/2-d1.x) || (p1.y >= this.dimensions.y/2-d1.y) )
            {
                return true;
            }
            // get next room pos and dim
            p2 = myRoomList.get(i).getPosition();
            d2 = myRoomList.get(i).getDimensions();
            // check collision between them
            if(    this.isBoxInBox(p1, d1, p2, d2)
                || this.isBoxInBox(p2, d2, p1, d1)
               )
            {
                return true;
            }
        }
        return false;
    }
    
    private Vector2f getRandomDimension(Prng rng)
    {
        Vector2f curDim = new Vector2f(0,0);
        curDim.x = (int)(rng.random()*Room.ROOM_MAX_SIZE+1);
        curDim.y = (int)(rng.random()*Room.ROOM_MAX_SIZE+1);
        curDim.x = Math.max(curDim.x, Room.ROOM_MIN_SIZE) * Module.NB_PIX_PER_MODULE;
        curDim.y = Math.max(curDim.y, Room.ROOM_MIN_SIZE) * Module.NB_PIX_PER_MODULE;
        return curDim;
    }
    private Vector2f getRandomPosition(Prng rng)
    {
        Vector2f curPos = new Vector2f(0,0);
        curPos.x  = (float)(rng.random()*this.dimensions.x/Module.NB_PIX_PER_MODULE);
        curPos.x  = ((int)curPos.x)*Module.NB_PIX_PER_MODULE;        
        curPos.x -= this.dimensions.x/2;
        curPos.y  = (float)(rng.random()*this.dimensions.y/Module.NB_PIX_PER_MODULE);
        curPos.y  = ((int)curPos.y)*Module.NB_PIX_PER_MODULE;        
        curPos.y -= this.dimensions.y/2;   
        return curPos;
    }
    private boolean isAreaOutsideShape(Room room)
    {
        Vector2f pos = room.getPosition();
        Vector2f dim = room.getDimensions();
        
        for(float x=pos.x+this.shipShape.getWidth()/2; x<=pos.x+this.shipShape.getWidth()/2+dim.x; x++)
        {
            for(float y=pos.y+this.shipShape.getHeight()/2; y<=pos.y+this.shipShape.getHeight()/2+dim.y; y++)
            {
                // get image pixel alpha value
                Color clr = this.shipShape.getColor((int)x, (int)y);
                if(clr.a == 0)
                {
                    return true;
                }
            }    
        }
        return false;
    }
    
    //------------------------------------------------
    // PUBLIC METHODS
    //------------------------------------------------
    public List<Room> generateRooms(boolean isStartPosRandom) throws SlickException
    {
        // Init room list variable
        List<Room> tmpRoomList     = new ArrayList<Room>();
        List<Room> validRoomList   = new ArrayList<Room>();
        List<Room> tmpCorridorList = new ArrayList<Room>();         
        // init pseudo random generator
        Prng       rng      = new Prng((int)(this.seed*System.nanoTime()));
    
        // Set new Module size
        Module.NB_PIX_PER_MODULE = (int)(rng.random()*8)+1;
        
        // Add boosters
        Room booster;
        int nbBoosters = (int)(rng.random()*Room.ROOM_MAX_BOOSTERS) + 1;
        int xb = 0;
        int yb = this.shipShape.getHeight()/2;
        // place a booster in the middle of the ship
        if( (nbBoosters%2) == 1 )
        {
            // Search left edge of shape
            for(xb=0;xb<this.shipShape.getWidth();xb++)
            {
                if( this.shipShape.getColor(xb,yb).a > 0 )
                {
                    break;
                }
            }
            // Create booster at the coordinate
            booster = new Room( new Vector2f(xb-this.shipShape.getWidth()/2, yb-this.shipShape.getHeight()/2),
                                     new Vector2f(Module.NB_PIX_PER_MODULE,Module.NB_PIX_PER_MODULE),
                                     Room.ROOM_TYPE_BOOSTER_LEFT
                                    );
            validRoomList.add(booster);
            nbBoosters--;
        }
        // Create symetric boosters
        for(;nbBoosters>0;nbBoosters-=2)
        {
            yb = (int)(rng.random()*this.shipShape.getHeight()/2);
            xb = 0;
            // Search left edge of shape
            for(xb=0;xb<this.shipShape.getWidth();xb++)
            {
                if( this.shipShape.getColor(xb,yb).a > 0 )
                {
                    break;
                }
            }
            // Create only if boosters are not completely on the right of the shape (no shape encountering)
            if(xb < this.shipShape.getWidth()-5)
            {
                // Create booster at the coordinate
                booster = new Room( new Vector2f(xb-this.shipShape.getWidth()/2, yb-this.shipShape.getHeight()/2),
                                         new Vector2f(Module.NB_PIX_PER_MODULE,Module.NB_PIX_PER_MODULE),
                                         Room.ROOM_TYPE_BOOSTER_LEFT
                                        );
                validRoomList.add(booster);
                // create symmetric booster
                booster = new Room( new Vector2f(xb-this.shipShape.getWidth()/2, -yb+this.shipShape.getHeight()/2),
                                         new Vector2f(Module.NB_PIX_PER_MODULE,Module.NB_PIX_PER_MODULE),
                                         Room.ROOM_TYPE_BOOSTER_LEFT
                                        );
                validRoomList.add(booster);
            }
        }
        
        
        // Add 1st tiny room
        Vector2f initDim = new Vector2f(Room.ROOM_MIN_SIZE*Module.NB_PIX_PER_MODULE,Room.ROOM_MIN_SIZE*Module.NB_PIX_PER_MODULE);
        Vector2f initPos = new Vector2f(-initDim.x/2,-initDim.y/2);
        Room firstRoom = new Room( initPos,initDim, Room.ROOM_TYPE_NORMAL);
        // Randomize start position if requested
        if(isStartPosRandom)
        {
            initPos = this.getRandomPosition(rng);
            firstRoom = new Room( initPos,initDim, Room.ROOM_TYPE_NORMAL);
        }
        // check the first room is correct (infinite loop to find the start point !! TODO stop generation if too many tries
        int breakCounter = 100;
        while(this.isAreaOutsideShape(firstRoom))
        {
            initPos = this.getRandomPosition(rng);
            firstRoom = new Room( initPos,initDim, Room.ROOM_TYPE_NORMAL);
            // check counter
            breakCounter--;
            if(breakCounter <= 0)
            {
                break;
            }
            
        }
        // Add first room in the list
        tmpRoomList.add(firstRoom);
        
        // NORMAL PROCESS OF GENERATION
        while(tmpRoomList.size() > 0)
        {
            // for each tmp room , try to create three other connected rooms, add the corridors and the rooms if possible
            // Get dimensions
            Room tmpRoom            = tmpRoomList.get(0);
            Vector2f tmpRoomUnitDim = tmpRoom.getModuleUnitDimensions();
            Vector2f tmpRoomRealDim = tmpRoom.getDimensions();
            Vector2f tmpRoomPos     = tmpRoom.getPosition();

            // For all directions
            for(int dir=0;dir<ROOMGEN_NB_DIR;dir++)
            {
                Vector2f corridorPos = new Vector2f(0,0);
                Vector2f airLockPos  = new Vector2f(0,0);

                int corridorType   = -1;
                int airLockType    = -1;
                int newAirLockType = -1;
                
                int dx=0;
                int dy=0;
                
                boolean  isFutureRoomValid = false;
                
                Vector2f futureRoomDim    = this.getRandomDimension(rng);
                Vector2f futureRoomPos    = new Vector2f(0,0);
                Vector2f futureAirLockPos = new Vector2f(0,0);
                
                // We try to add the future room as much as possible, by reducing its size : may be we can make it fit into a small part of the ship
                while(    (!isFutureRoomValid)
                       && (futureRoomDim.x >= Room.ROOM_MIN_SIZE*Module.NB_PIX_PER_MODULE)
                       && (futureRoomDim.y >= Room.ROOM_MIN_SIZE*Module.NB_PIX_PER_MODULE)
                      )
                {
                    // Get position of corridor
                    switch(dir)
                    {
                        case ROOMGEN_DIR_UP    :
                            // corridor
                            corridorType  = Room.ROOM_TYPE_CORRIDOR_V;
                            corridorPos.y = tmpRoomPos.y-Module.NB_PIX_PER_MODULE;
                            corridorPos.x = tmpRoomPos.x+((int)(rng.random()*(tmpRoomUnitDim.x-2))+1)*Module.NB_PIX_PER_MODULE;
                            // airlock
                            airLockType   = Room.ROOM_TYPE_AIRLOCK_UP;
                            airLockPos.x  = corridorPos.x;
                            airLockPos.y  = corridorPos.y+Module.NB_PIX_PER_MODULE;
                            // new room
                            dx = ((int)(rng.random()*((futureRoomDim.x/Module.NB_PIX_PER_MODULE)-2))+1)*Module.NB_PIX_PER_MODULE;
                            futureRoomPos.x = airLockPos.x-dx; // TODO random position
                            futureRoomPos.y = airLockPos.y-futureRoomDim.y-Module.NB_PIX_PER_MODULE;
                            // new AirLock
                            newAirLockType     = Room.ROOM_TYPE_AIRLOCK_DOWN;
                            futureAirLockPos.x = corridorPos.x;
                            futureAirLockPos.y = corridorPos.y-Module.NB_PIX_PER_MODULE;
                            break;
                        case ROOMGEN_DIR_DOWN  :
                            // corridor
                            corridorType  = Room.ROOM_TYPE_CORRIDOR_V;
                            corridorPos.y = tmpRoomPos.y+(tmpRoomRealDim.y);
                            corridorPos.x = tmpRoomPos.x+((int)(rng.random()*(tmpRoomUnitDim.x-2))+1)*Module.NB_PIX_PER_MODULE;
                            // airlock
                            airLockType   = Room.ROOM_TYPE_AIRLOCK_DOWN;
                            airLockPos.x  = corridorPos.x;
                            airLockPos.y  = corridorPos.y-Module.NB_PIX_PER_MODULE;
                            // new room
                            dx = ((int)(rng.random()*((futureRoomDim.x/Module.NB_PIX_PER_MODULE)-2))+1)*Module.NB_PIX_PER_MODULE;
                            futureRoomPos.x = corridorPos.x-dx;
                            futureRoomPos.y = corridorPos.y+Module.NB_PIX_PER_MODULE;
                            // new AirLock
                            newAirLockType     = Room.ROOM_TYPE_AIRLOCK_UP;
                            futureAirLockPos.x = corridorPos.x;
                            futureAirLockPos.y = corridorPos.y+Module.NB_PIX_PER_MODULE;
                            break; 
                        case ROOMGEN_DIR_LEFT  :
                            // corridor
                            corridorType  = Room.ROOM_TYPE_CORRIDOR_H;
                            corridorPos.x = tmpRoomPos.x-Module.NB_PIX_PER_MODULE;
                            corridorPos.y = tmpRoomPos.y+((int)(rng.random()*(tmpRoomUnitDim.y-2))+1)*Module.NB_PIX_PER_MODULE;
                            // airlock
                            airLockType   = Room.ROOM_TYPE_AIRLOCK_LEFT;
                            airLockPos.x  = corridorPos.x+Module.NB_PIX_PER_MODULE;
                            airLockPos.y  = corridorPos.y;
                            // new Room                            
                            dy = ((int)(rng.random()*((futureRoomDim.y/Module.NB_PIX_PER_MODULE)-2))+1)*Module.NB_PIX_PER_MODULE;
                            futureRoomPos.x = corridorPos.x-futureRoomDim.x;
                            futureRoomPos.y = corridorPos.y-dy;
                            // new AirLock
                            newAirLockType     = Room.ROOM_TYPE_AIRLOCK_RIGHT;
                            futureAirLockPos.x = corridorPos.x-Module.NB_PIX_PER_MODULE;
                            futureAirLockPos.y = corridorPos.y;
                            break; 
                        case ROOMGEN_DIR_RIGHT :
                            // corridor
                            corridorType  = Room.ROOM_TYPE_CORRIDOR_H;
                            corridorPos.x = tmpRoomPos.x+(tmpRoomRealDim.x);
                            corridorPos.y = tmpRoomPos.y+((int)(rng.random()*(tmpRoomUnitDim.y-2))+1)*Module.NB_PIX_PER_MODULE;
                            // airlock
                            airLockType   = Room.ROOM_TYPE_AIRLOCK_RIGHT;
                            airLockPos.x  = corridorPos.x-Module.NB_PIX_PER_MODULE;
                            airLockPos.y  = corridorPos.y;
                            // new room
                            dy = ((int)(rng.random()*((futureRoomDim.y/Module.NB_PIX_PER_MODULE)-2))+1)*Module.NB_PIX_PER_MODULE;
                            futureRoomPos.x = corridorPos.x+Module.NB_PIX_PER_MODULE;
                            futureRoomPos.y = corridorPos.y-dy;
                            // new AirLock
                            newAirLockType     = Room.ROOM_TYPE_AIRLOCK_LEFT;
                            futureAirLockPos.x = corridorPos.x+Module.NB_PIX_PER_MODULE;
                            futureAirLockPos.y = corridorPos.y;
                            break;

                        default:
                            break;
                    }

                    // Check if the futureRoom is overlapping another room or if it is ok to keep it
                    Room futureRoomObj = new Room( futureRoomPos, futureRoomDim, Room.ROOM_TYPE_NORMAL);
                    if(    !this.isRoomCollision   (futureRoomObj, validRoomList)
                        && !this.isRoomCollision   (futureRoomObj, tmpRoomList  )
                       )
                    {
                        // Now we are sure the box is inside the full rectangle
                        // check if it is outside the shape
                        if( !this.isAreaOutsideShape(futureRoomObj) )
                        {
                            // Set the future room flag to OK
                            isFutureRoomValid = true;
                            // Add new future room into the next TMP list
                            tmpRoomList.add(futureRoomObj);
                            // add new airlock for current room in the VALID list
                            validRoomList.add(new Room(airLockPos , new Vector2f(Module.NB_PIX_PER_MODULE,Module.NB_PIX_PER_MODULE), airLockType ));
                            // Add new corridor between both rooms in the VALID list
                            validRoomList.add(new Room(corridorPos, new Vector2f(Module.NB_PIX_PER_MODULE,Module.NB_PIX_PER_MODULE), corridorType));
                            // Add new air lock for future room in the VALID list
                            validRoomList.add(new Room(futureAirLockPos , new Vector2f(Module.NB_PIX_PER_MODULE,Module.NB_PIX_PER_MODULE), newAirLockType ));
                        }
                    }
                    
                    // check if we have created the future room. If not, reduce its size to try to make it fit anyway
                    if(!isFutureRoomValid)
                    {
                        // We reduce both W and H
                        futureRoomDim.x -= Module.NB_PIX_PER_MODULE;
                        futureRoomDim.y -= Module.NB_PIX_PER_MODULE;
                    }
                }
            }
            // move current room from tmpList to valitList because we have finished to process it 
            tmpRoomList.remove(tmpRoom);
            validRoomList.add(tmpRoom);            
        }
        
        // For each room we populate with modules
        ModuleGenerator mg = new ModuleGenerator();
        for(Room room : validRoomList)
        {
            if(!room.isModuleForbidden())
            {
        	if( !room.isModuleForbidden())
        	{
                room.addModuleList( mg.generateModules(room) );
        	}
            }
        }
        
        // Sort the output list to blit the normal room FIRST
        Collections.sort(validRoomList, new Comparator<Room>()
        {
            @Override
            public int compare(Room o1, Room o2)
            {
                int res = 1;
                if (o1.getTypeValue() == o2.getTypeValue())
                {
                    res = 0;
                }
                if (o1.getTypeValue() > o2.getTypeValue())
                {
                    res = -1;
                }
                return res;
            }
        });
        
        // return room list
        return validRoomList;
    }
    
    
    //------------------------------------------------
    // GETTERS
    //------------------------------------------------
    public Vector2f getDimensions()
    {
        return this.dimensions.copy();
    }
    
    
    //------------------------------------------------
    // END OF CLASS
    //------------------------------------------------
}
