import java.awt.Image;

import javax.swing.ImageIcon;

class Zombie
{
	public int xPosition;
	public int yPosition;
	public boolean isLiving;
	public Image image;
	
	public Zombie(int x,int y)
	{
		xPosition=x;
		yPosition=y;
		isLiving=true;
		image=new ImageIcon("zombie.png").getImage();
	}
	
	public void move()
	{
		if (Math.random()>0.5)
		{
			xPosition+=Math.random()*40-20;
			yPosition+=Math.random()*40-20;
		}
	}
}
