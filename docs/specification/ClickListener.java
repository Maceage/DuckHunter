import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

public class ClickListener implements MouseListener
{
	private Zombie zombie;
	private JLabel label;
	
	public ClickListener(Zombie theZombie,JLabel theLabel)
	{
		zombie=theZombie;
		label=theLabel;
	}
	
	public void mouseClicked(MouseEvent event)
	{
		int imageWidth=zombie.image.getWidth(null);
		int imageHeight=zombie.image.getHeight(null);
		
		int xDistance=Math.abs(event.getX()-zombie.xPosition-imageWidth/2);
		int yDistance=Math.abs(event.getY()-zombie.yPosition-imageHeight/2);
		
		if (xDistance<40 && yDistance<40)
			zombie.isLiving=false;
	}
	
	public void mousePressed(MouseEvent event)
	{
	}
	
	public void mouseReleased(MouseEvent event)
	{
	}
	
	public void mouseEntered(MouseEvent event)
	{
	}
	
	public void mouseExited(MouseEvent event)
	{
	}
}
