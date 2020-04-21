package fr.rphstudio.procjamitchio.services;

import fr.rphstudio.procjamitchio.rng.Prng;
import fr.rphstudio.procjamitchio.spaceship.Room;
import fr.rphstudio.procjamitchio.spaceship.SpaceShip;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import org.newdawn.slick.SlickException;

import java.util.Collection;
import java.util.List;

public class SpaceSheepGenerator {
    RoomGenerator roomGenerator;

    public static class GenerationParameters{
        //RoomGenerator parameters
        int w;
        int h;
        int sd;
    }

    public SpaceSheepGenerator()
    {
    }

    private static void findFilesRecursively(File file, Collection<File> all, final String extension)
    {
        //Liste des fichiers correspondant a l'extension souhaitee
        final File[] children = file.listFiles(new FileFilter() {
            public boolean accept(File f) {
                    return f.getName().endsWith(extension) ;
                }}
        );
        if (children != null) {
            //Pour chaque fichier recupere, on appelle a nouveau la methode
            for (File child : children) {
                all.add(child);
                findFilesRecursively(child, all, extension);
            }
        }
    }
    
    private static String getRandomShape()
    {
        Prng rng = new Prng((int)(System.currentTimeMillis()*System.nanoTime()));
        // get all png files from shapes directory
        final List<File> all = new ArrayList<File>();
        findFilesRecursively(new File("sprites/shapes/alienships"), all, ".png");
        findFilesRecursively(new File("sprites/shapes/humanships"), all, ".png");
        findFilesRecursively(new File("sprites/shapes/station"  ), all, ".png");
        // Choose one random shape
        int idx = (int)(rng.random()*all.size());
        // return path
        return all.get(idx).getAbsolutePath();
    }
    
    public SpaceShip generate(GenerationParameters parameters) throws SlickException {
        SpaceShip spaceShip = new SpaceShip();
        
        ShapeLoader shapeLoader = new ShapeLoader();
        
        // RANDOM FILE SHAPE
        shapeLoader.setImage(getRandomShape(), parameters.w, parameters.h);
        spaceShip.setShape(shapeLoader.getShape());
        // FIXED FILE SHAPE
        //shapeLoader.setImage("sprites/shapes/humanships/human020.png",parameters.w,parameters.h);
        //shapeLoader.setImage("sprites/shapes/alienships/alien010.png",parameters.w,parameters.h);
        //shapeLoader.setImage("sprites/shapes/station/station003.png",parameters.w,parameters.h);
        //spaceShip.setShape(shapeLoader.getShape());
        
        // PROCEDURAL SHAPE
        //ShapeGenerator shapeGenerator = new ShapeGenerator();
        //shapeGenerator.generate();
        //spaceShip.setShape(shapeGenerator.getShape());
        
        parameters.w  = spaceShip.getShape().getWidth();
        parameters.h  = spaceShip.getShape().getHeight();
        roomGenerator = new RoomGenerator(spaceShip.getShape(), parameters.w,parameters.h);
        List<Room> rooms = roomGenerator.generateRooms(false);
        spaceShip.setRooms(rooms);
        return spaceShip;
    }

}
