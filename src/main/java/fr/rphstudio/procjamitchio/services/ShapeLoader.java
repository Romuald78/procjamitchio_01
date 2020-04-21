package fr.rphstudio.procjamitchio.services;

import fr.rphstudio.procjamitchio.rng.Prng;
import java.io.IOException;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ShapeLoader {
    Image shape;
    public void setImage(String imageString, int finalW, int finalH) throws SlickException
    {
        // get image for ship shape
        System.out.println(imageString);
        this.shape = new Image(imageString);
        int w1 = this.shape.getWidth();
        int h1 = this.shape.getHeight();
        w1 = this.shape.getWidth();
        h1 = this.shape.getHeight();
        // Get image for ship texture
        Image texture = new Image("sprites/textures/texture01.jpg").getScaledCopy(0.20f);
        //Image texture = new Image("sprites/modules/floor.png").getScaledCopy(16,16);
        int w2 = texture.getWidth();
        int h2 = texture.getHeight();
        // Loop to apply texture to the whole ship
        Graphics g = this.shape.getGraphics();
        Prng rng = new Prng((int)System.nanoTime());
        Color clr = new Color((float)rng.random(),(float)rng.random(),(float)rng.random());
        for(int x=0;x<w1;x+=w2)
        {
            for(int y=0;y<h1;y+=h2)
            {
                // Get image for ship texture
                g.setDrawMode(Graphics.MODE_ALPHA_BLEND);
                g.drawImage(texture, x, y, clr);
                g.setDrawMode(Graphics.MODE_NORMAL);
                this.shape.getGraphics().flush();
            }
        }
    }

    public Image getShape() {
        return shape;
    }
}
