/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procjamitchio.services;

import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author GRIGNON FAMILY
 */
public class GeoCompute
{
    public static Vector2f getPosAfterRotation(Vector2f pos, Vector2f center, float angle)
    {
        pos = pos.copy();
        // get current angle
        float dx         = pos.x;
        float dy         = pos.y;
        double initAngle = Math.atan2(dy, dx);
        double dist      = Math.sqrt( dx*dx + dy*dy );
        // set angle to Radians and add current angle
        initAngle += ((double)(angle) * Math.PI) / 180.0;
        // compute new dx and dy
        pos.x = (float)(dist * Math.cos(initAngle));
        pos.y = (float)(dist * Math.sin(initAngle));
        // return position
        return pos;
    }
}
