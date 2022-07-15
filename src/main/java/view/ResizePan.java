package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ResizePan extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private JPanel pan1 = new JPanel(), pan2 = new JPanel(), pan3 = new JPanel(),
			panelSize = new JPanel(), panelSize2 = new JPanel(), buttonsPan = new JPanel();
	
	private JButton parcourir1 = new JButton("Parcourir..."),
					parcourir2 = new JButton("Parcourir..."),
					validate = new JButton("Validate"),
					openFolder1 = new JButton("Ouvrir l'emplacement du fichier..."),
					openFolder2 = new JButton("Ouvrir l'emplacement du fichier..."),
					preview = new JButton("Preview");
	
	private JLabel path1 = new JLabel(), path2 = new JLabel(),
				   sizeLbl = new JLabel();
	
	private JTextPane width1 = new JTextPane(), 
			height1 = new JTextPane(),
			width2 = new JTextPane(), 
			height2 = new JTextPane();
	
	private JSlider sizeSlide = new JSlider();
	
	public ResizePan()
	{	
		this.setLayout(new GridLayout(2,1));
		
		path1.setForeground(Color.RED);
		path2.setForeground(Color.RED);
		
		pan1.setBorder(BorderFactory.createTitledBorder("Actual Image"));
		pan1.setLayout(new FlowLayout());
		
		pan1.add(new JLabel("Select an image :"));
		pan1.add(path1);
		pan1.add(parcourir1);
		
		pan2.setBorder(BorderFactory.createTitledBorder("New Image"));
		pan2.setLayout(new FlowLayout());
		
		pan2.add(pan3);
		
		pan3.add(new JLabel("Select a Folder :"));
		pan3.add(path2);
		pan3.add(parcourir2);
		
		panelSize.setLayout(new BoxLayout(panelSize, BoxLayout.LINE_AXIS));
		panelSize.add(new JLabel("Select a new Size :"));
		panelSize.add(panelSize2);
		
		panelSize2.setLayout(new BoxLayout(panelSize2, BoxLayout.LINE_AXIS));
		panelSize2.add(width2);
		panelSize2.add(new JLabel("x"));
		panelSize2.add(height2);
		panelSize2.add(sizeSlide);
		
		buttonsPan.add(validate);
		buttonsPan.add(openFolder2);

	    sizeSlide.setMaximum(100);
	    sizeSlide.setMinimum(0);
	    sizeSlide.setValue(100);
	    sizeSlide.setPaintTicks(true);
	    sizeSlide.setPaintLabels(true);
	    sizeSlide.setMinorTickSpacing(10);
	    sizeSlide.setMajorTickSpacing(20);
		
	    sizeSlide.addChangeListener(new ChangeListener(){
	        public void stateChanged(ChangeEvent event){
	          int[] screenVal = convert(Integer.parseInt(width1.getText()), Integer.parseInt(height1.getText()), ((JSlider)event.getSource()).getValue());
	          width2.setText(Integer.toString(screenVal[0]));
	          height2.setText(Integer.toString(screenVal[1]));
	        }
	      });   
	    
		parcourir1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				File f = new File(getImgPath());
				Image img;
				String str = f.getName(), name = "";
				if(str.lastIndexOf(".") != -1)
				{
					for(int i=0;i<str.length();i++)
					{
						if(i == str.lastIndexOf("."))
							name += "_copy";
						name += str.charAt(i);
					}
				}
				path1.setText(f.getAbsolutePath());
				if(f.getParent() != null)
					path2.setText(f.getParent()+File.separator+name);
				
				img = new ImageIcon(f.getAbsolutePath()).getImage();
				if(!containsComponent(pan1, sizeLbl))
					pan1.add(sizeLbl);
				
				width1.setText(""+img.getWidth(null));
				height1.setText(""+img.getHeight(null));
				width2.setText(""+img.getWidth(null));
				height2.setText(""+img.getHeight(null));

				sizeLbl.setText("The actual image size is : "+ 
								width1.getText()+"x"+height1.getText());
				
				if(!containsComponent(pan1, preview))
					pan1.add(preview);
				if(!containsComponent(pan1, openFolder1))
					pan1.add(openFolder1);
				if(!containsComponent(pan2, panelSize))
					pan2.add(panelSize);
				if(!containsComponent(pan2, buttonsPan))
					pan2.add(buttonsPan);

			}
		});
		
		parcourir2.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
				Image img;
				path2.setText(getImgPath());
				if(!containsComponent(pan2, panelSize))
					pan2.add(panelSize);
				img = new ImageIcon(path2.getText()).getImage();
				width2.setText(""+img.getWidth(null));
				height2.setText(""+img.getHeight(null));
			}
			
		});
		
		validate.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{

				try {
					if(resize(path1.getText(), path2.getText(), Integer.parseInt(width2.getText()), Integer.parseInt(height2.getText())) == 0)
					{
						JOptionPane.showMessageDialog(null, "Conversion reussi !", "Information", JOptionPane.INFORMATION_MESSAGE);
						/*if(!containsComponent(panelSize, openFolder2))
						{
							panelSize.add(openFolder2);
							repaint();
						}*/
					}
					else
						JOptionPane.showMessageDialog(null, "Une erreur est survenue !", "Erreur", JOptionPane.INFORMATION_MESSAGE);
				} catch (NumberFormatException | HeadlessException | IOException e1) {
					e1.printStackTrace();
				}

				
			}
			
		});
		
		openFolder1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(new File(path1.getText()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		openFolder2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					File output = new File(path2.getText());
					if(!output.exists()) {
						return;
					}
					Desktop.getDesktop().open(output);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		preview.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				new PreviewFrame(path1.getText());
			}
			
		});
		
		this.add(pan1);
		this.add(pan2);
		
	}
	
	class PreviewFrame extends JFrame
	{
		private static final long serialVersionUID = 1L;

		private Image img;
		private JPanel panel;
		private int W_MAX = 800, H_MAX = 600;
		
		public PreviewFrame(String imgPath)
		{
			this.img = new ImageIcon(imgPath).getImage();
			this.setTitle("Preview : "+imgPath);
			this.setResizable(false);
			int w = 800;
			int h = img.getHeight(null);
			float coeff = (float)W_MAX / (float)img.getWidth(null);
			h *= coeff;
			
			if(h > H_MAX)
			{
				coeff = (float)H_MAX / (float)h;
				h = H_MAX;
				w *= coeff;
			}
			
			this.setSize(new Dimension(w, h));
			this.panel = new PreviewPanel(img, w, h);
			this.setContentPane(panel);
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}
		
	}
	
	class PreviewPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		private Image img;
		private int w, h;
		
		public PreviewPanel(Image img, int w, int h)
		{
			this.img = img;
			this.w = w;
			this.h = h;
			repaint();
		}
		
		public void paintComponent(Graphics g)
		{
			Graphics2D g2d = (Graphics2D)g;
			if(img != null)
			{
				g2d.drawImage(img, 0, 0, w, h, this);
			}
		}
		
	}
	
	public int[] convert(int width, int height, int coeff) // coeff entre 0 et 100.
	{
		float c;
		if(coeff > 0 && coeff <= 100)
		{
			c = (float)coeff/100;
			width *= c;
			height *= c;
		}
		return new int[] {width, height};
	}
	
	public int[] convert(int width, int height, float coeff) // coeff entre 0 et 1
	{
		if(coeff > 0.0f && coeff < 1.0f)
		{
			width *= coeff;
			height *= coeff;
		}
		return new int[] {width, height};
	}
	
    public static int resize(String inputImagePath, String outputImagePath, int scaledWidth, int scaledHeight) throws IOException 
    {
    	try {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);

        ImageIO.write(outputImage, formatName, new File(outputImagePath));
        return 0;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace(); // l'inputImagePath ne correspond pas � une image.
    		return 1;
    	}
    }
    
	boolean containsComponent(Container container, Component component) 
	{
		for (Component containedComponent : container.getComponents()) 
			if (containedComponent == component) 
				return true;

		return false;
	}
	
	public String getImgPath()
	{
		JFileChooser choice = new JFileChooser();
		String path = "";
		int var = choice.showOpenDialog(this);
		if(var==JFileChooser.APPROVE_OPTION)
		{
		   //choice.getSelectedFile().getName();
		   path = choice.getSelectedFile().getAbsolutePath();
		   if(!path.contains("."))
			   path += ".jpg";
		}
		
		return path;
	}
	
	public String getFolderPath()
	{
		JFileChooser choice = new JFileChooser();
		String path = "";
		int var = choice.showOpenDialog(this);
		if(var==JFileChooser.APPROVE_OPTION)
		{
		   //choice.getSelectedFile().getName();
		   path = choice.getCurrentDirectory().getAbsolutePath();
		}
		
		return path;
	}
	
}


