package fr.rphstudio.procjamitchio.spaceship;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.List;

public class SpaceShip {
    Image shape;
    Image texture;
    List<Room> rooms;
    private float scale=1.0f;
    private Vector2f speed = new Vector2f(0,0);
    private Vector2f position = new Vector2f(1920/2,1080/2);
    private float dampening = 0.1f;
    private float angle;


    public SpaceShip()  throws SlickException
    {
        this.texture = new Image("sprites/modules/floor.png");
    }

    public void render(Graphics g) throws SlickException {
        shape.setCenterOfRotation(scale*shape.getWidth()/2, scale*shape.getHeight()/2);
        shape.setRotation(angle);
        //update position
        position.x += (float) (Math.cos(Math.toRadians(angle))* this.speed.x * scale);
        position.y += (float) (Math.sin(Math.toRadians(angle))* this.speed.x * scale);
        position.x += (float) (Math.cos(Math.toRadians(angle+90))* this.speed.y * scale);
        position.y += (float) (Math.sin(Math.toRadians(angle+90))* this.speed.y * scale);
        shape.draw(position.x-scale*shape.getWidth()/2,position.y-scale*shape.getHeight()/2,scale);
        for (Room room : rooms) {
            room.render(g,new Vector2f(position.x,position.y),angle,scale);
        }
        this.speed.x= this.speed.x- this.speed.x*dampening;
        this.speed.y= this.speed.y- this.speed.y*dampening;
    }

    public void scale(float scale){
        this.scale*=scale;
    }

    public void rotate(float angle) {
        this.angle+=angle;
    }

    public void move(float x,float y){
        speed.x+=x;
        speed.y+=y;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public Image getShape() {
        return shape;
    }

    public void setShape(Image shape) {
        this.shape = shape;
    }
    
    public Vector2f getPosition()
    {
        return this.position.copy();
    }

    public void resetPosition()
    {
        this.position.x = 1920/2;
        this.position.y = 1080/2;
    }

    
}
