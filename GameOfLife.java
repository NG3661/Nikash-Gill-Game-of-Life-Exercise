//Game of Life Exercise by Nikash Gill, 23rd February 2016

package nikashgill_gameoflife; //include the package directory where all files are accessed

//importing Java packages
import java.awt.BorderLayout; //border layout
import java.awt.Color; //colour
import java.awt.Graphics; //graphics for the design
import java.awt.GridLayout; //layout of the grid
import java.awt.event.*;
 
import javax.swing.*;
 
public class GameOfLife extends JFrame implements ActionListener, MouseListener, KeyListener, Runnable 
{
	//declared private variables
	private static final long serialVersionUID = 1L; //declare a default serial UID
	private JPanel pane, buttons; //buttons via mouse clicks
    private boolean[][] array; //arrays
    private int nextCell, frameCount; //private integer variables for the cells and the frame count
    private JPanel[][] grid; //private variable to create a grid in an array
    private JButton start, nextFrame, clear, fill; //private variables used to add text inside a button
    private JLabel frame;
    private Thread draw; //private variable used for drawing graphics

    //main function (required)
    public static void main(String[] args) 
    {
        GameOfLife main = new GameOfLife();
        main.setVisible(true); //make the grid visible
    }
 
    //public function used for initialisation
    public GameOfLife() 
    {
        super("Nikash Gill's Game of Life"); //display the title of the app
        setVisible(true); //make it visible to the user
        setSize(750, 750); //set the size of the screen window
        setLocation(0, 0); //set the coordinate position (top-left) of the screen window
        setDefaultCloseOperation(EXIT_ON_CLOSE); //set a close operation when the user has finished
        setResizable(false); //disables resizing the window
 
        pane = new JPanel(); //create a panel for placing the grid
        pane.setLayout(new GridLayout(50, 50)); //set the grid layout
        pane.setBackground(Color.DARK_GRAY); //set the background colour to dark grey
 
        buttons = new JPanel(); //create a panel for placing the buttons
        buttons.setLayout(new GridLayout(0, 4)); //set the grid layout for the buttons
 
        array = new boolean[50][50]; //set a boolean array with 51 squares on the x and y axes
 
        //for loop to determine where the cells are on the grid when the animation/generation is running
        for (int x = 0; x < 50; x++) 
        {
            for (int y = 0; y < 50; y++)
            {
                array[y][x] = false; //all cells are not active/dead
            }
        }

        grid = new JPanel[50][50]; //create a new grid with 51 cells on the x and y axes/width & height
 
        for (int x = 0; x < 50; x++) 
        {
            for (int y = 0; y < 50; y++) 
            {
                grid[y][x] = new JPanel();
                grid[y][x].setBackground(Color.BLACK); //set the background colour to black
                grid[y][x].setBorder((BorderFactory.createLineBorder(Color.darkGray, 1))); //set the border colour to dark grey
                grid[y][x].addMouseListener(this); //add a mouse listener
                pane.add(grid[y][x]); //add the grid inside the window
            }
        }
        pane.addKeyListener(this);

        for (int x = 0; x < 50; x++) //for loop detecting the position of cells inside the grid
        {
            grid[49][x].setBackground(Color.ORANGE); //set the right side grid background colour to orange
            grid[49][x].setBorder((BorderFactory.createLineBorder(Color.orange, 1))); //set the right side grid square line border colour to orange
            grid[x][0].setBackground(Color.blue); //set the top side grid background colour to blue
            grid[x][0].setBorder((BorderFactory.createLineBorder(Color.blue, 1))); //set the top side grid square line border colour to blue
            grid[0][x].setBackground(Color.green); //set the left side grid background colour to green
            grid[0][x].setBorder((BorderFactory.createLineBorder(Color.green, 1))); //set the left side grid square line border colour to green
            grid[x][49].setBackground(Color.cyan); //set the bottom side grid background colour to cyan
            grid[x][49].setBorder((BorderFactory.createLineBorder(Color.cyan, 1))); //set the bottom side grid square line border colour to cyan
 
        }

        //display the text on the top-left of the border
        frame = new JLabel("Frame: " + frameCount + ", Paused"); //This line counts the animation frames
 
        start = new JButton("Start Animation"); //create button called 'Start Animation'
        start.addActionListener(this);
        buttons.add(start, BorderLayout.SOUTH); //place the button on the bottom part of the border
 
        clear = new JButton("Clear Screen"); //create button called 'Clear Screen'
        clear.addActionListener(this);
        buttons.add(clear, BorderLayout.SOUTH); //place the button on the bottom part of the border
 
        fill = new JButton("Fill Screen"); //create button called 'Fill Screen'
        fill.addActionListener(this);
        buttons.add(fill, BorderLayout.SOUTH); //place the button on the bottom part of the border
 
        nextFrame = new JButton("Next Frame"); //create button called 'Next Frame'
        nextFrame.addActionListener(this);
        buttons.add(nextFrame, BorderLayout.SOUTH); //place the button on the bottom part of the border
 
        addKeyListener(this); 
        add(frame, BorderLayout.NORTH); //create top border
        add(pane);
        add(buttons, BorderLayout.SOUTH); //create bottom border
 
    }
 
    //function used to draw the graphics
    public void draw(Graphics g) 
    {
        super.paintComponents(g);
    }
 
    //function used to check the conditions of the program
    @Override
    public void actionPerformed(ActionEvent p) 
    {
        String event = p.getActionCommand();
 
        if (event.equals("Start Animation")) //if 'Start Animation' is pressed ...
        {  
            frame.setText("Frame: " + frameCount + ", Running"); //create the text on the top-left corner of the border
            start.setText("Pause Animation"); //create the text 'Pause Animation' over the button
            start(); //call this function
        }
 
        if (event.equals("Clear Screen")) //when the clear screen button is pressed ...
        {
            frameCount = 0; //reset the counter to 0
            
            if(draw == null) //if draw is not active
            {
                frame.setText("Frame: " + frameCount + ", Paused"); //update the frame count when paused
            }
            else
            {
                frame.setText("Frame: " + frameCount + ", Running"); //otherwise, update the frame count when running
            }
             
            for (int x = 1; x < 49; x++) 
            {
                for (int y = 1; y < 49; y++) 
                { 
                    array[y][x] = false; //the cells are not active/dead
                    grid[y][x].setBackground(Color.black); //set the background colour to black
                    grid[y][x].setBorder(BorderFactory
                              .createLineBorder(Color.darkGray)); //set the border colour to dark grey
                } 
            }
        }
 
        if (event.equals("Fill Screen")) //when the fill screen button is pressed ...
        {
            for (int x = 1; x < 49; x++) 
            {
                for (int y = 1; y < 49; y++) 
                {
                    array[y][x] = true; //the cells are active/alive
                    grid[y][x].setBackground(Color.yellow); //set the cell colours to yellow
                    grid[y][x].setBorder(BorderFactory
                              .createLineBorder(Color.black)); //set the border colour to dark grey
                }
            } 
        }
 
        if (event.equals("Pause Animation")) //when the pause animation button is pressed ...
        {
        	//display the new frame count the moment the pause animation button is pressed
        	frame.setText("Frame: " + frameCount + ", Paused");
            //change the button text to resume animation.
            start.setText("Resume Animation"); //this allows the user to continue the animation when the button is pressed again.
            pane.setBackground(Color.DARK_GRAY); //change the background colour to dark grey
            stop(); //call this function
            revalidate(); //call this function
            repaint(); //call this function
        }
        
        if (event.equals("Next Frame")) //when the next frame button is pressed ...
        {
            frame.setText("Frame: " + frameCount); //update the frame count
            Running(); //call this function
        }
    }
 
    //function used when running the program
    public void Running() 
    {
        frameCount += 1; //increment the counter by 1
        frame.setText("Frame: " + frameCount + ", Paused"); //display the text on the top-left corner
        invalidate(); //call this function
        repaint(); //call this function

        for (int x = 1; x < 49; x++) 
        {
        	for (int y = 1; y < 49; y++) 
        	{
            	if (array[y][x] == true) //if the cells are active...
            	{
            		//output the array coordinates in the console window. Used for testing!
            		//System.out.println("array[" + y + "][" + x + "]: "); 
            		if (array[y - 1][x] == true) 
            		{
            			nextCell += 1; //... add 1 living cell adjacent to the left
                    }
                    if (array[y + 1][x] == true) 
                    {
                    	nextCell += 1; //... add 1 living cell adjacent to the right
                    }
                    if (array[y][x + 1] == true) 
                    {
                        nextCell += 1; //... add 1 living cell adjacent below
                    }
                    if (array[y][x - 1] == true) 
                    {
                        nextCell += 1; //... add 1 living cell adjacent above
                    }
                    if (array[y - 1][x - 1] == true) 
                    {
                        nextCell += 1; //... add 1 living cell adjacent above diagonal-left
                    }
                    if (array[y + 1][x - 1] == true) 
                    {
                        nextCell += 1; //... add 1 living cell adjacent above diagonal-right
                    }
                    if (array[y - 1][x + 1] == true) 
                    {
                        nextCell += 1; //... add 1 living cell adjacent above diagonal-left
                    }
                    if (array[y + 1][x + 1] == true) 
                    {
                        nextCell += 1; //... add 1 living cell adjacent above diagonal-right
                    }

                    if (nextCell != 3 && nextCell !=2) 
                    {
                        array[y][x] = false; //cell dies, over-population
                        grid[y][x].setBackground(Color.black); //change the cell's colour to black
                        grid[y][x].setBorder(BorderFactory
                                  .createLineBorder(Color.darkGray)); //change the border colour to dark grey
                        revalidate(); //call this function
                        repaint(); //call this function
                    }
            	}
            	else if(array[y][x] == false) //if the cells are not active ...
            	{
            		if (array[y - 1][x] == true) 
            		{
                        nextCell += 1; //add 1 Living Cell Adjacent to the left
                    }
                    if (array[y + 1][x] == true) 
                    {
                        nextCell += 1; //add 1 Living Cell Adjacent to the right
                    }
                    if (array[y][x + 1] == true) 
                    {
                        nextCell += 1; //add 1 Living Cell Adjacent below
                    }
                    if (array[y][x - 1] == true) 
                    {
                        nextCell += 1; //add 1 Living Cell Adjacent above
                    }
                    if (array[y - 1][x - 1] == true) 
                    {
                        nextCell += 1; //add 1 Living Cell to the Above Diagonal-Left
                    }
                    if (array[y + 1][x - 1] == true) 
                    {
                        nextCell += 1; //add 1 Living Cell to the Above Diagonal-Right
                    }
                    if (array[y - 1][x + 1] == true) 
                    {
                        nextCell += 1; //add 1 Living Cell to the Below Diagonal-Left
                    }
                    if (array[y + 1][x + 1] == true) 
                    {
                        nextCell += 1; //add 1 Living Cell to the Below Diagonal-Right
                    }
                    if (nextCell == 3) 
                    {
                        array[y][x] = true; //cell is brought back to life!
                        grid[y][x].setBackground(Color.yellow); //change the cell's colour to yellow
                        grid[y][x].setBorder((BorderFactory
                                  .createLineBorder(Color.black))); //change the border colour to black
                        revalidate(); //call this function
                        repaint(); //call this function
                    }
            	}
                nextCell = 0; //stop generating cells
        	}
        }
    }    
 
    @Override
    public void mouseClicked(MouseEvent c) //public function for when the mouse is clicked using variable c for click
    {
        for (int x = 1; x < 49; x++) 
        {
            for (int y = 1; y < 49; y++) 
            {
                if (c.getComponent() == grid[y][x] && array[y][x] == true) //if the cells are alive
                {
                    array[y][x] = false; //make the cells not active/dead
                    grid[y][x].setBackground(Color.black); //change the background colour of the cells to black
                    grid[y][x].setBorder(BorderFactory
                              .createLineBorder(Color.darkGray)); //change the border colour to dark grey
                }
 
                else if (c.getComponent() == grid[y][x] && array[y][x] == false) //if the cells are dead
                {
                    array[y][x] = true; //make the cells active/alive
                    grid[y][x].setBackground(Color.yellow); //change the background colour of the cells to yellow
                    grid[y][x].setBorder(BorderFactory
                              .createLineBorder(Color.darkGray)); //change the border colour to dark grey
                } 
            }
            repaint(); //call this function
            revalidate(); //call this function
        }
    }
 
    @Override
    public void mouseEntered(MouseEvent o) {	} //function used when a mouse event is opened with variable o for open
 
    @Override
    public void mouseExited(MouseEvent c) {	} //function used when a mouse event is closed with variable c for close
 
    @Override
    public void mousePressed(MouseEvent p) {	} //function used when a mouse click is clicked with variable p for pressed
 
    @Override
    public void mouseReleased(MouseEvent r) {	} //function used when a mouse click is released with variable r for release
 
    //function used for key presses using variable k for key
    public void keyPressed(KeyEvent k) 
    {
        if (k.getKeyCode() == KeyEvent.VK_RIGHT) //if the virtual key (right arrow key) is pressed ...
        {
            nextFrame.doClick(); //... click and perform a task
        }
    }
 
    @Override
    public void keyReleased(KeyEvent r) {	} //function used when a key is released with variable r for release
 
    @Override
    public void keyTyped(KeyEvent t) {	} //function used when a key is typed with variable t for type
     
    //function used to start drawing graphics
    public void start() 
    {
    	if (draw == null) //if draw is not active ...
    	{
        	draw = new Thread(this); 
        	draw.start(); //... start drawing
        }
       }
     
    //function used to stop drawing graphics
    public void stop()
    {
        if (draw != null) //if draw is active ...
        {
            draw.stop(); //THIS IS REQUIRED. Without it, the animation will not pause!
        	draw = null; //... stop drawing
        }
    }
 
    @Override
    public void run() 
    {
        while (true) //while the program is running (true) ...
        {
            Running(); //... call this function
            invalidate(); //... call this function
            repaint(); //... call this function
            try { Thread.sleep(150); } //sleep for 0.15 seconds
            catch (InterruptedException e) { } //throws an exception
        } 
    }
}