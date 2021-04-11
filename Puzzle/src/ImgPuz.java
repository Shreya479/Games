import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import javax.imageio.*;
public class ImgPuz extends JFrame{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
int a;
JLabel timer;
JButton[] b = new JButton[9];
JButton choose;
JPanel panel1,panel2;
List<ImageIcon> icons = new ArrayList<ImageIcon>();
JFrame jfrm;
JLabel original_img,label;
Border c;
String path;
int row = 3,col = 3;
boolean puzzleSelected = false;
ImgPuz(){
	jfrm=new JFrame("Image Puzzle!");
	for(int i = 0;i<9;i++)
	{
		b[i] = new JButton();
	}
	choose = new JButton("Choose Image");
	panel1 = new JPanel();
	panel2 = new JPanel();
	original_img = new JLabel();
	label = new JLabel("Original Image");
	label.setFont(new Font("New Times Roman", Font.BOLD, 17));
	original_img.setMinimumSize(new Dimension(400,400));
	original_img.setMaximumSize(new Dimension(400,400));
	original_img.setPreferredSize(new Dimension(400,400));
	original_img.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	choose.setAlignmentX(Component.CENTER_ALIGNMENT);
	label.setAlignmentX(Component.CENTER_ALIGNMENT);
	original_img.setAlignmentX(Component.CENTER_ALIGNMENT);
	panel1.setLayout(new GridLayout(3,3));
	panel2.setLayout(new BoxLayout (panel2,BoxLayout.Y_AXIS));  
	for(int i = 0;i<9;i++)
	{
		panel1.add(b[i]);
	}
    panel2.add(choose);
   	panel2.add(Box.createRigidArea(new Dimension(0, 20)));
   	panel2.add(label);
    panel2.add(original_img);
	choose.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
		JFileChooser file = new JFileChooser();
		file.setCurrentDirectory(new File(System.getProperty("user.home")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images",".jpg",".png",".gif");
		file.addChoosableFileFilter(filter);
		int result = file.showSaveDialog(null);
		if(result == JFileChooser.APPROVE_OPTION){
			puzzleSelected = true;
			File selectedFile = file.getSelectedFile();
			String fname = selectedFile.getName();
			fname = fname.substring(0,fname.lastIndexOf("."));
			System.out.println(fname);
			path = selectedFile.getAbsolutePath();
			CropImage(fname);
		}
		else if(result == JFileChooser.CANCEL_OPTION)
			{
				System.out.println("No file selected....");
			}

			}
		});
 	jfrm.setLayout(new GridLayout(1,2));
	jfrm.add(panel1);
	jfrm.add(panel2);
	jfrm.setSize(1000,1000);
	jfrm.setExtendedState(JFrame.MAXIMIZED_BOTH);
	jfrm.setLocationRelativeTo(null);
	jfrm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	c=(BorderFactory.createLineBorder(Color.yellow));
	for( int i = 0; i<9;i++)
	{
		b[i].addMouseListener(m);
	}
addMouseListener(m);
jfrm.setVisible(true);
}
public static BufferedImage resize(String inputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        return outputImage;
    }
public void CropImage(String fname)
	{
	  BufferedImage img = null;
	 try{
	 	img = resize(path,panel1.getWidth(),panel1.getHeight());
	 	BufferedImage og_img = resize(path,400,400);
	 	original_img.setIcon(new ImageIcon(og_img));
		}
	 catch(Exception e)
	 	{
	  		e.printStackTrace();
	  	}
	  int tw = img.getWidth();
	  int th = img.getHeight();
	  int ew = tw/col;
	  int eh = th/row;
	  int x=0,y=0;
	  for(int i = 0 ; i<row;i++)
	  {
	  	x= 0;
	  	for (int j = 0; j<col; j++)
	  	{
	  		try{
	  			System.out.println("creating "+i+j);
	  			BufferedImage sub = img.getSubimage(x,y,ew,eh);
	  			String name = "./Images/"+fname+i+j+".jpg";
	  			File of = new File(name);
	  			ImageIO.write(sub,"jpg", of);
	  			icons.add(i*col+j,new ImageIcon(name));
	  			System.out.println("created "+i+j);
	  			x += ew;
	  			  		}
	  			 catch(Exception e){
	  			 	e.printStackTrace();
	  			 }
	  	}
	  	y +=eh;
	  }
	createPuzzle();
	this.validate();
	this.repaint();
	}
MouseListener m=new MouseListener(){
	public void mouseEntered(MouseEvent e){
		((JButton) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.YELLOW));
		}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(puzzleSelected){
		JButton temp = ((JButton) e.getSource());
		for(int i =0;i<9;i++)
		{
			if(b[i]==temp)
			{
				temp.setBorder(BorderFactory.createLineBorder(Color.RED));
				MoveTile(b[i],(int)i/3,(int)i%3);
				CheckSolution();				
			}
		}			
		}
	}
	@Override
	public void mouseExited(MouseEvent e) {
			((JButton) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.GRAY));		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
};
private void createPuzzle(){
   List<Integer> index = new ArrayList<Integer>(row*col);
   for(int i=0;i<row*col;i++)
   {
   	index.add(i);
   }
  Collections.shuffle(index);
	for(int i=0;i<row*col;i++)
	{
		if(index.get(i)== row*col-1)
		{
			b[i].setIcon(null);
		}
		else{
			b[i].setIcon(icons.get(index.get(i)));
		}	
	}
};
private void MoveTile(JButton b1,int curr_row,int curr_col){
    int x[] = {0,-1,0,+1};
    int y[] = {-1,0,1,0};
    for(int i = 0; i<4;i++)
    {
        int neigh_x= curr_row+x[i];
        int neigh_y = curr_col + y[i];
        if(neigh_x<0 || neigh_y<0 || neigh_x>=row || neigh_y>=col)
        	{continue;
        	}
        else{
        int index = neigh_x*3+neigh_y;
        if(b[index].getIcon() == null)
        {
        		swapIcons(b1,b[index]);
        }
    }      	
      }
};
private void CheckSolution()
{
   for(int i = 0;i<row*col-1;i++)
   {
   	if(b[i].getIcon() != icons.get(i))
   	{
         System.out.println("Puzzle is not complete yet....");  
         return;
     }
   }
   b[row*col-1].setIcon(icons.get(row*col-1));
   System.out.println("Puzzle Completed...."); 
   JOptionPane.showMessageDialog(jfrm,"Puzzle Solved! Well done..", "Message",JOptionPane.INFORMATION_MESSAGE);
}
private void swapIcons(JButton b1,JButton b2)
{
   Icon temp = b1.getIcon();
   b1.setIcon(b2.getIcon());
   b2.setIcon(temp);
};
}