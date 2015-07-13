package DBConnect;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;

//Author: The Man - diego.caponera@gmail.com

public class digitalClock extends JPanel implements Runnable{

	String mode="am";	
	int size=2;
	int totSecs;
	digitalNumber h1,h2,m1,m2,s1,s2;
	boolean pulse=false;
	boolean afNoon;
	GregorianCalendar cal;
	Thread th;
	
	public digitalClock(){
		h1=new digitalNumber(20,100,size);
		h2=new digitalNumber(100,100,size);
		m1=new digitalNumber(200,100,size);
		m2=new digitalNumber(280,100,size);
		s1=new digitalNumber(360,60,size/2);
		s2=new digitalNumber(400,60,size/2);
		
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		
		JCheckBox modeBox = new JCheckBox("Toggle AM/PM mode");
		modeBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if(e.getStateChange() == ItemEvent.DESELECTED)mode="am";
				if(e.getStateChange() == ItemEvent.SELECTED)mode="pm";
				repaint();
			}
		});
		add(modeBox,BorderLayout.SOUTH);
		start();
	}
	
	public void start(){
		if(th==null){
			th=new Thread(this);
			th.start();
		}
	}
	
	public void run(){
		while(th!=null){
			try{
				totSecs=setSecs();
				showTime();
				if(pulse)pulse=false;
				else pulse=true;
				repaint();
				Thread.sleep(1000);
			}catch(Exception e){}
		}
	}
	
	public void stop(){
		if(th!=null)th=null;
	}
	
	public int setSecs(){
		cal=new GregorianCalendar();
		int h,m,s;		
		h=cal.get(Calendar.HOUR)*3600;
		if(cal.get(Calendar.AM_PM)==Calendar.PM)h+=3600*12;
		m=cal.get(Calendar.MINUTE)*60;
		s=cal.get(Calendar.SECOND);
		return h+m+s;
	}
	
	public int divide(int a, int b){
		int z = 0;
        int i = a;

        while (i>= b)
          {
            i = i - b;
            z++;
          }
        return z;
    }
	
	public void showTime(){
		if(totSecs>86399)totSecs=0;		
	
		int hours=divide(totSecs,3600);
		int minutes=divide(totSecs,60)-hours*60;
		int seconds=totSecs-hours*3600-60*divide((totSecs-hours*3600),60);

		if(hours<13&&afNoon==true)afNoon=false;
		if(mode=="pm" && hours> 12){
			hours=hours-12;
			afNoon=true;
		}
		//set Hours
		if(hours<10){
			h1.turnOffNumber();
			h2.setNumber(hours);
		}else if(hours>=10 && hours<20){
			h1.setNumber(1);
			h2.setNumber(hours-10);
		}else{
			h1.setNumber(2);
			h2.setNumber(hours-20);
		}
		//set Minutes		
		int dM=divide(minutes,10);
		if(dM<6)m1.setNumber(dM);
		else m1.setNumber(0);
		m2.setNumber(minutes-dM*10);
		//set Seconds		
		int dS=divide(seconds,10);
		if(dS<6)s1.setNumber(dS);
		else s1.setNumber(0);
		s2.setNumber(seconds-dS*10);
						
		//System.out.println(""+hours+" : "+minutes+" . "+seconds);
	}
	public void showDots(Graphics2D g2){
		if(pulse)g2.setColor(Color.RED);
		else g2.setColor(new Color(230,230,230));
		g2.fill(new Rectangle2D.Double(178,65,14,14));
		g2.fill(new Rectangle2D.Double(178,135,14,14));
	}
	
	public void showMode(Graphics2D g2){
		if(afNoon && mode=="pm"){g2.setColor(Color.RED);
			g2.drawString("PM", 360, 140);
			g2.setColor(new Color(230,230,230));
			g2.drawString("AM", 360, 120);
		}else if(afNoon==false && mode=="pm"){
			g2.setColor(new Color(230,230,230));
			g2.drawString("PM", 360, 140);			
			g2.setColor(Color.RED);
			g2.drawString("AM", 360, 120);
		}else if(mode=="am"){
			g2.setColor(new Color(230,230,230));
			g2.drawString("PM", 360, 140);			
			g2.drawString("AM", 360, 120);
		}
	}
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		h1.drawNumber(g2);
		h2.drawNumber(g2);
		m1.drawNumber(g2);
		m2.drawNumber(g2);
		s1.drawNumber(g2);
		s2.drawNumber(g2);
		showDots(g2);
		showMode(g2);
	}
	
	public static void main(String[] a){
		JFrame f=new JFrame("Digital Clock");
    	f.setSize(450,260);
    	f.setTitle("Suck my Clock");
    	//f.setResizable(false);
    	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	f.setVisible(true); 
    	f.getContentPane().add(new digitalClock());
	}
}

class digitalNumber{
	int x,y;
	int k;
	led[] leds;
	
	public digitalNumber(int x, int y, int k){
		this.x=x;
		this.y=y;
		this.k=k;
		leds = new led[7];
		leds[0] = new led(x,y,"vert");
		leds[1] = new led(x,y+10*k,"vert");
		leds[2] = new led(x+8*k,y,"vert");
		leds[3] = new led(x+8*k,y+10*k,"vert");
		leds[4] = new led(x+2*k,y-9*k,"horiz");
		leds[5] = new led(x+2*k,y+k,"horiz");
		leds[6] = new led(x+2*k,y+11*k,"horiz");
	}
	
	public void setNumber(int num){
		if(num==0){
			leds[0].setState(true);
			leds[1].setState(true);
			leds[2].setState(true);
			leds[3].setState(true);
			leds[4].setState(true);
			leds[5].setState(false);
			leds[6].setState(true);
		}else if(num==1){
			leds[0].setState(false);
			leds[1].setState(false);
			leds[2].setState(true);
			leds[3].setState(true);
			leds[4].setState(false);
			leds[5].setState(false);
			leds[6].setState(false);			
		}else if(num==2){
			leds[0].setState(false);
			leds[1].setState(true);
			leds[2].setState(true);
			leds[3].setState(false);
			leds[4].setState(true);
			leds[5].setState(true);
			leds[6].setState(true);			
		}else if(num==3){
			leds[0].setState(false);
			leds[1].setState(false);
			leds[2].setState(true);
			leds[3].setState(true);
			leds[4].setState(true);
			leds[5].setState(true);
			leds[6].setState(true);			
		}else if(num==4){
			leds[0].setState(true);
			leds[1].setState(false);
			leds[2].setState(true);
			leds[3].setState(true);
			leds[4].setState(false);
			leds[5].setState(true);
			leds[6].setState(false);			
		}else if(num==5){
			leds[0].setState(true);
			leds[1].setState(false);			
			leds[2].setState(false);
			leds[3].setState(true);
			leds[4].setState(true);
			leds[5].setState(true);
			leds[6].setState(true);			
		}else if(num==6){
			leds[0].setState(true);
			leds[1].setState(true);			
			leds[2].setState(false);
			leds[3].setState(true);
			leds[4].setState(true);
			leds[5].setState(true);
			leds[6].setState(true);			
		}else if(num==7){
			leds[0].setState(false);
			leds[1].setState(false);
			leds[2].setState(true);
			leds[3].setState(true);
			leds[4].setState(true);
			leds[5].setState(false);
			leds[6].setState(false);		
		}else if(num==8 ){
			leds[0].setState(true);
			leds[1].setState(true);
			leds[2].setState(true);
			leds[3].setState(true);
			leds[4].setState(true);
			leds[5].setState(true);
			leds[6].setState(true);		
		}else if(num==9){
			leds[0].setState(true);
			leds[1].setState(false);
			leds[2].setState(true);
			leds[3].setState(true);
			leds[4].setState(true);
			leds[5].setState(true);
			leds[6].setState(true);		
		}
	}
	
	public void turnOffNumber(){
		for(int i=0;i<7;i++){
			leds[i].setState(false);
		}
	}
	
	public void drawNumber(Graphics2D g2){
		for(int i=0; i<7; i++){
			leds[i].render(g2);
		}
	}
	
	class led{
		int x, y;
		Polygon p;
		String type;
		boolean lightOn=false;
		
		public led(int x, int y, String type){
			this.x=x;
			this.y=y;
			this.type=type;

			p = new Polygon();
						
			if(type=="vert"){
				p.addPoint(x,y);
				p.addPoint(x+k,y+k);
				p.addPoint(x+2*k,y);
				p.addPoint(x+2*k,y-8*k);
				p.addPoint(x+k,y-9*k);
				p.addPoint(x,y-8*k);
			}
			
			if(type=="horiz"){
				p.addPoint(x,y);
				p.addPoint(x+k,y+k);
				p.addPoint(x+5*k,y+k);
				p.addPoint(x+6*k,y);
				p.addPoint(x+5*k,y-k);
				p.addPoint(x+k,y-k);				
			}
		}
		
		public void render(Graphics2D g2){
			g2.setColor(new Color(230,230,230));
			if(lightOn)g2.setColor(Color.RED);				
			g2.fillPolygon(p);
		}
		
		public void setState(boolean s){
			lightOn=s;
		}
	}
}