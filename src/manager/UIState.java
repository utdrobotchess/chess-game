/**
 *
 * @author Ryan J. Marcotte
 */

package manager;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;

import gui.MainFrame;

public class UIState
{
    final String[] imageNames = {"green-pawn",
                                 "green-rook",
                                 "green-knight",
                                 "green-bishop",
                                 "green-queen",
                                 "green-king",
                                 "orange-pawn",
                                 "orange-rook",
                                 "orange-knight",
                                 "orange-bishop",
                                 "orange-queen",
                                 "orange-king"};

    final int NUM_PIECES = 32;
    int pieceLocations[];
    int pieceIDs[];

    MainFrame mainFrame;
    boolean demoMode;
    boolean demoModeSetup;
    int selectedIndex;
    Map<String, ImageIcon> imageMap;
    
    public UIState()
    {
        selectedIndex = -1;
        demoMode = false;
        demoModeSetup = false;
        
        pieceLocations = new int[NUM_PIECES];
        pieceIDs = new int[64];

        for (int location : pieceLocations)
            location = -1;
        
        for (int location : pieceIDs)
            location = -1;
        
        configureImages();
    }

    private void configureImages()
    {
        final String basePath = "resources/";
        
        imageMap = new HashMap<>();

        for (int i = 0; i < imageNames.length; i++)
            imageMap.put(imageNames[i], new ImageIcon(basePath + "" +
                                                      imageNames[i] + ".png"));

        resizeImages(MainFrame.SQUARE_SIZE, MainFrame.SQUARE_SIZE);
    }

    public synchronized void resizeImages(int scaledWidth, int scaledHeight)
    {
        for (int i = 0; i < imageNames.length; i++) {
            BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight,
                                                       BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = scaledBI.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.drawImage(imageMap.get(imageNames[i]).getImage(), 0, 0, scaledWidth, scaledHeight, null);
            g.dispose();
            imageMap.put(imageNames[i], new ImageIcon(scaledBI));
        }
    }
    
    public synchronized MainFrame getMainFrame()
    {
        return mainFrame;
    }

    public synchronized Icon getPieceImage(String pieceName)
    {
        return imageMap.get(pieceName);
    }
    
    public synchronized int getPieceLocation(int pieceID)
    {
        return pieceLocations[pieceID];
    }
    
    public synchronized int[] getAllPieceLocations()
    {
        int newArray[] = new int[pieceLocations.length];

        System.arraycopy(pieceLocations, 0, newArray, 0, pieceLocations.length);

        return newArray;
    }
    
    public synchronized int getPieceIDFromLocation(int location)
    {
        return pieceIDs[location];
    }

    public synchronized int getSelectedIndex()
    {
        return selectedIndex;
    }

    public synchronized boolean isDemoMode()
    {
        return demoMode;
    }

    public synchronized boolean isDemoModeSetup()
    {
        return demoModeSetup;
    }

    public synchronized void setDemoMode(boolean demoMode)
    {
        this.demoMode = demoMode;
    }

    public synchronized void setDemoModeSetup(boolean demoModeSetup)
    {
        this.demoModeSetup = demoModeSetup;
    }

    public synchronized void setMainFrame(MainFrame mf)
    {
        mainFrame = mf;
    }
    
    public synchronized void setPieceLocation(int pieceID, int location)
    {
        pieceIDs[pieceLocations[pieceID]] = -1;
        pieceLocations[pieceID] = location;
        pieceIDs[location] = pieceID;
    }

    public synchronized void setSelectedIndex(int selectedIndex)
    {
        this.selectedIndex = selectedIndex;
    }
}
