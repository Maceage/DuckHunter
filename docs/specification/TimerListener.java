import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class TimerListener implements ActionListener
{
	private JFrame frame;
	private Zombie zombie;
	private BufferedImage displayImage;
	
	public TimerListener(Zombie theZombie,JFrame theFrame,BufferedImage theDisplayImage)
	{
		frame=theFrame;
		zombie=theZombie;
		displayImage=theDisplayImage;
	}
	
	public void actionPerformed(ActionEvent event)
	{
		zombie.move();
		
		Graphics graphics=displayImage.getGraphics();
		graphics.clearRect(0,0,400,400);
		
		graphics.drawImage(zombie.image,zombie.xPosition,zombie.yPosition,null);
		
		if (zombie.isLiving==false)
		{
			int width=zombie.image.getWidth(null);
			int height=zombie.image.getHeight(null);
			
			graphics.setColor(Color.red);
			graphics.fillOval(zombie.xPosition+width/2-13,zombie.yPosition+height/2-13,25,25);
		}
		
		graphics.dispose();
		
		frame.repaint();
	}
}
