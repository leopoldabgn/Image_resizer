package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Window extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private ResizePan pan;
	
	public Window(int w, int h)
	{
		super();
		this.setTitle("Image resizer");
		this.setMinimumSize(new Dimension(w, h));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pan = new ResizePan();
		
		this.getContentPane().add(pan, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	
	
}