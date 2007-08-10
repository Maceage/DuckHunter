import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Main
{
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		BufferedImage image=new BufferedImage(400,400,BufferedImage.TYPE_4BYTE_ABGR);
		
		JLabel label=new JLabel(new ImageIcon(image));
		frame.add(label);
		
		Zombie zombie=new Zombie(200,200);
		
		TimerListener timerListener=new TimerListener(zombie,frame,image);
		
		Timer timer=new Timer(100,timerListener);
		timer.start();
		
		ClickListener clickListener=new ClickListener(zombie,label);
		frame.addMouseListener(clickListener);
		
		frame.setSize(400,400);
		frame.setVisible(true);
	}
}
