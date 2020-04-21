package fr.rphstudio.procjamitchio.services;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShapeGenerator {
    Image shape;
    Random r = new Random();

    public ShapeGenerator() throws SlickException {
        this.shape = new Image(600,600);
    }

    /**
     * Returns closest point on segment to point
     *
     * @param sx1 segment x coord 1
     * @param sy1 segment y coord 1
     * @param sx2 segment x coord 2
     * @param sy2 segment y coord 2
     * @param px  point x coord
     * @param py  point y coord
     * @return closets point on segment to point
     */
    public static Vector2f computePointSymmetryV1(float sx1, float sy1, float sx2, float sy2, float px, float py) {
        double xDelta = sx2 - sx1;
        double yDelta = sy2 - sy1;

        if ((xDelta == 0) && (yDelta == 0)) {
            throw new IllegalArgumentException("Segment start equals segment end");
        }

        double u = ((px - sx1) * xDelta + (py - sy1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

        final Vector2f closestPoint;
        if (u < 0) {
            closestPoint = new Vector2f(sx1, sy1);
        } else if (u > 1) {
            closestPoint = new Vector2f(sx2, sy2);
        } else {
            closestPoint = new Vector2f((float) (sx1 + (u * xDelta)), (float) (sy1 + (u * yDelta)));
        }
        Vector2f pointA = new Vector2f(px,py);
        Vector2f sub = pointA.sub(closestPoint);
        Vector2f subinv = sub.scale(-1);
        return closestPoint.add(subinv);
    }

    public static Vector2f computePointSymmetry(Vector2f A, Vector2f B, Vector2f P)
    {
        Vector2f AP = P.sub(A);       //Vector from A to P
        Vector2f AB = B.sub(A);       //Vector from A to B

        float magnitudeAB = AB.lengthSquared();     //Magnitude of AB vector (it's length squared)
        float ABAPproduct = AP.dot(AB);    //The DOT product of a_to_p and a_to_b
        float distance = ABAPproduct / magnitudeAB; //The normalized "distance" from a to your closest point

        final Vector2f closestPoint;

        if (distance < 0)     //Check if P projection is over vectorAB
        {
            closestPoint = A;

        }
        else if (distance > 1)             {
            closestPoint = B;
        }
        else
        {
            closestPoint = A.add(AB.scale(distance));
        }

        Vector2f sub = P.sub(closestPoint);
        Vector2f subinv = sub.scale(-1);
        return closestPoint.add(subinv);
    }

    public void generate() throws SlickException {
        Graphics graphics = this.shape.getGraphics();
        float squareSize = 0.1f * this.shape.getHeight();
        Vector2f SquarePosition = new Vector2f((this.shape.getHeight()-squareSize)/2f,(this.shape.getWidth()-squareSize)/2f);
        //graphics.fillRect(SquarePosition.x,SquarePosition.y,squareSize,squareSize);
        List<Vector2f> points = new ArrayList<Vector2f>();
        points.add(new Vector2f(SquarePosition.x-squareSize/2f,SquarePosition.y));
        points.add(new Vector2f(SquarePosition.x+squareSize/2f,SquarePosition.y));
        float minDist = 20f;
        float maxDist = 200f;
        Vector2f pointASymetry = new Vector2f(0,this.shape.getHeight()/2f);
        Vector2f pointBSymetry = new Vector2f(this.shape.getWidth(),this.shape.getHeight()/2f);
        for(int i=0;i<20;i++){
            while(points.size()!=2){
                points.remove(r.nextInt(points.size()));
            }
            Polygon polyLeft=new Polygon();
            Polygon polyRight=new Polygon();
            List<Vector2f> pointsTmp;
            pointsTmp = new ArrayList<>(points);
            //compute new point
            Vector2f a = pointsTmp.get(0).copy();
            a.x+=r.nextInt((int) maxDist)-minDist;
            a.y+=r.nextInt((int) maxDist)-minDist;
            pointsTmp.add(a);
            //compute new point
            Vector2f b = pointsTmp.get(1).copy();
            b.x+=r.nextInt((int) maxDist)-minDist;
            b.y+=r.nextInt((int) maxDist)-minDist;
            pointsTmp.add(b);
            //construct shape with symmetry
            for (Vector2f point : pointsTmp) {
                polyLeft.addPoint(point.x,point.y);
                Vector2f SymetricPoint = computePointSymmetry(pointASymetry.copy(), pointBSymetry.copy(), point.copy());
                polyRight.addPoint(SymetricPoint.x,SymetricPoint.y);
            }
            graphics.setColor(Color.orange);
            graphics.fill(polyLeft);
            graphics.setColor(Color.green);
            graphics.fill(polyRight);
        }

        graphics.flush();//IMPORTANT!!!
    }

    public Image getShape() {
        return shape;
    }
}
