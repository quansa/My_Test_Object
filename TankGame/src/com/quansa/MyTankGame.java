package com.quansa;
/*
 * 
 * 
 * */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class MyTankGame extends JFrame implements ActionListener{
	
	Mypanel mp=null;
	
	MyStartPanel msp=null;
	
	//�˵�
	JMenuBar jmb=null;
	//��ʼ��Ϸ
	JMenu jm1=null;
	JMenuItem jmi1=null;
	//�˳�
	JMenuItem jmi2=null;
	//�����˳�
	JMenuItem jmi3=null;
	
	//������һ����Ϸ
	JMenuItem jmi4=null;
	
	public static void main(String[] args) {
		
		MyTankGame mtg=new MyTankGame();
	}
	
	//���캯��
	public MyTankGame(){
		
		
		
		
		//�����˵�,�˵�ѡ��
		jmb=new JMenuBar();
		jm1=new JMenu("��Ϸ(G)");
		//���ÿ�ݷ�ʽ
		jm1.setMnemonic('G');
				
		jmi1=new JMenuItem("��ʼ����Ϸ(N)");
		jmi2=new JMenuItem("�˳���Ϸ(E)");
		jmi3=new JMenuItem("�����˳���Ϸ");
		jmi4=new JMenuItem("������һ����Ϸ");
		
		jmi1.setMnemonic('N');
		//��jmi1��Ӧ
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newgame");
		
		jmi2.setMnemonic('E');
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exit");
		
		jmi3.addActionListener(this);
		jmi3.setActionCommand("saveExit");
		
		jmi4.addActionListener(this);
		jmi4.setActionCommand("continu");
		
		
				
		jm1.add(jmi1);
		jmb.add(jm1);
		
		jm1.add(jmi2);
		jmb.add(jm1);
		
		jm1.add(jmi3);
		jmb.add(jm1);
		
		jm1.add(jmi4);
		jmb.add(jm1);
		
		msp=new MyStartPanel();
		Thread t=new Thread(msp);
		t.start();
		
		this.setJMenuBar(jmb);
		
		this.add(msp);
		this.setSize(400,300);
		this.setVisible(true);
		}
	
public void actionPerformed(ActionEvent e) {
		//���û���ͬ�������ͬ����Ӧ
			if(e.getActionCommand().equals("newgame")){
				
				//����ս�����
				mp=new Mypanel("newGame");
				//����mp�߳�
				Thread t1=new Thread(mp);
				t1.start();
				//ɾ�������
				this.remove(msp);
				//���������
				this.add(mp);
				//ע�����
				this.addKeyListener(mp);
				
				this.setVisible(true);
			}else if(e.getActionCommand().equals("exit")){
				//������ٵ���
				Recorder.keepRecording();
				System.exit(0);
			}
			//�����˳�
			else if(e.getActionCommand().equals("saveExit")){
				
				Recorder rd=new Recorder();
				rd.setEts(mp.ets);
				//������ٵ�������,���ŵ�����
				rd.keepRecAndEnemyTank();
				//�˳�
				System.exit(0);
				
			}//�����Ͼ���Ϸ
			else if(e.getActionCommand().equals("continu")){
				//����ս�����
				mp=new Mypanel("continu");
				//mp.flag="continu";
				
				//����mp�߳�
				Thread t1=new Thread(mp);
				t1.start();
				//ɾ�������
				this.remove(msp);
				//���������
				this.add(mp);
				//ע�����
				this.addKeyListener(mp);
				
				this.setVisible(true);
			}
			
}
}
//��ʾ
class MyStartPanel extends JPanel implements Runnable{
	int time=0;
	
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0,0,400,300);
		//��ʾ��Ϣ
		if(time%2==0){
		g.setColor(Color.yellow);
		Font myFont=new Font("����",Font.BOLD,30);
		g.setFont(myFont);
		g.drawString("STAGE  1",150,150);
		}
	}

	@Override
	public void run() {
		while(true){
			try{
				Thread.sleep(100);
			}catch(Exception e){
				e.printStackTrace();
			}
			time++;
			this.repaint();
		}
		
	}
}


//�ҵ����
class Mypanel extends JPanel implements KeyListener,Runnable{
	//����һ���ҵ�̹��
	Hero hero=null;
	
	//������˵�̹����
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	Vector<Node> nodes=new Vector<Node>();
	
	//����̹������
	int enSize=3;
	
	//�ж��Ǽ����Ͼ�,��������Ϸ
	//String flag="newGame";
	
	//����ը������
	Vector<Bomb> bombs=new Vector<Bomb>();
	
	//��������ͼƬ
	Image image1=null;
	Image image2=null;;
	Image image3=null;
	
	
	public Mypanel(String flag){
		
		//�ָ���¼
		Recorder.getRecoring();
		
		 hero=new Hero(200,200);
		 if(flag.equals("newGame")){
			  //��ʼ������̹��
			 for(int i=0;i<enSize;i++){
			 //��������Tank
				 EnemyTank et=new EnemyTank((i+1)*50,0);
				 et.setColor(0);
				 et.setDirect(2);
				 //��Mypanel�ĵ���̹�����������õ���̹��
				 et.setEts(ets);
			 
				 //����
				 Thread t=new Thread(et);
				 t.start();
			 
				 //����Tank���ӵ�
				 Shot s=new Shot(et.x+10,et.y+30,2);
				 et.ss.add(s);
				 Thread t2=new Thread(s);
				 t2.start();
			 
				 //����
				 ets.add(et);
				 }
			 }else{
				 nodes=new Recorder().getNodesAndNums();
				 for(int i=0;i<nodes.size();i++){
					 Node node=nodes.get(i);
					 
					 EnemyTank et=new EnemyTank(node.x,node.y);
					 et.setColor(0);
					 et.setDirect(node.direct);
					 //��Mypanel�ĵ���̹�����������õ���̹��
					 et.setEts(ets);
					 
					 //����
					 Thread t=new Thread(et);
					 t.start();
					 
					 //����Tank���ӵ�
					 Shot s=new Shot(et.x+10,et.y+30,2);
					 et.ss.add(s);
					 Thread t2=new Thread(s);
					 t2.start();
					 
					 //����
					 ets.add(et);
			 }
		 }
		
		 
		 //��ʼ��ͼƬ
		 image1=Toolkit.getDefaultToolkit().getImage("1.jpg");
		 image2=Toolkit.getDefaultToolkit().getImage("2.jpg");
		 image3=Toolkit.getDefaultToolkit().getImage("3.jpg");
		 
		 
		 }
	
	//������ʾ��Ϣ
	public void showInfo(Graphics g){
		//������ʾ��Ϣ̹��(��ս��)
		this.drawTank(80,315, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getEnNum()+"",110, 338);
				
		this.drawTank(200,315, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyLife()+"",230, 338);
		
		//����ܳɼ�
		g.setColor(Color.black);
		Font f=new Font("����",Font.BOLD,20);
		g.setFont(f);
		g.drawString("�ܳɼ�", 430, 70);
		
		this.drawTank(445, 80, g, 0, 0);
		
		g.setColor(Color.black);
		g.drawString(Recorder.getAllEnNum()+"",480 , 100);
		
	}
	
	//��дpaint
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		
		//������ʾ��Ϣ
		this.showInfo(g);
		
		//�����Լ���̹��
		if(hero.isLive){
			this.drawTank(hero.getX(), hero.getY(), g, this.hero.getDirect(), 1);
		}
		//����ը��
		for(int i=0;i<bombs.size();i++){
			//ȡ��ը��
			Bomb b=bombs.get(i);
			if(b.life>6){
				g.drawImage(image1, b.x, b.y, 30, 30, this);
			}else if(b.life>3){
				g.drawImage(image2, b.x, b.y, 30, 30, this);
			}else{
				g.drawImage(image3, b.x, b.y, 30, 30, this);
			}
			
			//��b��life--
			b.lifeDown();
			//life==0,ȥ��
			if(b.life==0){
				bombs.remove(b);
			}
			
		}
		
		//��������̹��
		for(int i=0;i<ets.size();i++){
			EnemyTank et=ets.get(i);
			if(et.isLive){
				this.drawTank(et.getX(),et.getY() , g, et.getDirect(),0);
				//�����ӵ�
				for(int j=0;j<et.ss.size();j++){
					Shot enemyShot=et.ss.get(j);
					if(enemyShot.isLive){
						g.fillOval(enemyShot.x,enemyShot.y,2,2);
					}else{
						//����̹����,ȥ��
						et.ss.remove(enemyShot);
					}
				}
			
			}
			
		}
		
		//��ss��ȡ��ÿһ���ӵ�,����
		for(int j=0;j<this.hero.ss.size();j++){
			Shot myShot=hero.ss.get(j);
			//�����ӵ�,һ��
			if(hero.s!=null && myShot.isLive==true){
				g.fillOval(myShot.x,myShot.y,2,2);
			}
			if(myShot.isLive==false){
				hero.ss.remove(myShot);
			}
			
		}
		
	}
	
	//�ж��ҵ��ӵ����е���̹��
	public void hitEnemyTank(){
		//�ж��Ƿ���е��� 
			for(int i=0;i<hero.ss.size();i++){
				//ȡ���ӵ�
				Shot myShot=hero.ss.get(i);
				
				//�ж��ӵ��Ƿ����
				if(myShot!=null && myShot.isLive){
					//ȡ��ÿ������̹����֮ƥ��
					for(int j=0;j<ets.size();j++){
						EnemyTank et=ets.get(j);
						
						if(et.isLive){
							if(this.hitTank(myShot, et)){
								Recorder.reduceEnNum();
								Recorder.addEnNum();
							}
						}
						
					}
				}
			}
	}
	
	//�����Ƿ������
	public void hitMe(){
		//ȡ��ÿһ�����˵�̹��
		for(int i=0;i<this.ets.size();i++){
			//ȡ��̹��
			EnemyTank et=ets.get(i);
			//ȡ��ÿ���ӵ�
			for(int j=0;j<et.ss.size();j++){
				
				//ȡ���ӵ�
				Shot enemyShot=et.ss.get(j);
				if(hero.isLive){
					if(this.hitTank(enemyShot,hero)){
						Recorder.reducemyLife();
					}
					
					
				}
				}
			
		}
	}
	
	//�ж��ҵ��ӵ����е���̹�� 
	public boolean hitTank(Shot s,Tank et){
		
		boolean b2=false;
		//�ж�̹�˷���
		switch(et.direct){
		//����Ϊ�ϻ���
		case 0:
		case 2:
			if(s.x>et.x && s.x<et.x+20 && s.y>et.y && s.y<et.y+30){
				//����
				//�ӵ���
				s.isLive=false;
				//���˵�̹����
				et.isLive=false;
				b2=true;
				//����һ��ը��,����Vector
				Bomb b=new Bomb(et.x,et.y);
				bombs.add(b);
			}
			break;
			
		case 1:
		case 3:
			if(s.x>et.x && s.x<et.x+30 && s.y>et.y && s.y<et.y+20){
				//����
				//�ӵ���
				s.isLive=false;
				//���˵�̹����
				et.isLive=false;
				b2=true;
				//����һ��ը��,����Vector
				Bomb b=new Bomb(et.x,et.y);
				bombs.add(b);
				}
			break;
			}
		return b2;
		
	}
	
	//����̹��
	public void drawTank(int x,int y,Graphics g,int direct,int type){
		//�ж�̹������
		switch(type){
		
		case 0:
			g.setColor(Color.cyan);
			break;
		//�ҵ�̹����ɫ
		case 1:
			g.setColor(Color.yellow);
			break;
		}
		
		//�ж�̹�˷���
		switch(direct){
		//����
		case 0:
			//1.������߾���
			g.fill3DRect(x, y, 5, 30,false);
			//2.�����ұ߾���
			g.fill3DRect(x+15,y,5,30,false);
			//3.�����м����
			g.fill3DRect(x+5,y+5,10,20,false);
			//Բ
			g.fillOval(x+5, y+10, 10, 10);
			//��
			g.drawLine(x+10, y+15, x+10, y);
			break;
		case 1:
			g.fill3DRect(x, y, 30,5,false);
			g.fill3DRect(x,y+15,30,5,false);
			g.fill3DRect(x+5,y+5,20,10,false);
			g.fillOval(x+10, y+5, 10, 10);
			g.drawLine(x+15, y+10, x+30, y+10);
			break;
		case 2:
			g.fill3DRect(x, y, 5, 30,false);
			g.fill3DRect(x+15,y,5,30,false);
			g.fill3DRect(x+5,y+5,10,20,false);
			g.fillOval(x+5, y+10, 10, 10);
			g.drawLine(x+10, y+15, x+10, y+30);
			break;
		case 3:
			g.fill3DRect(x, y, 30,5,false);
			g.fill3DRect(x,y+15,30,5,false);
			g.fill3DRect(x+5,y+5,20,10,false);
			g.fillOval(x+10, y+5, 10, 10);
			g.drawLine(x+15, y+10, x, y+10);
			break;
		}
		
		
	}

	
	public void keyTyped(KeyEvent arg0) {
		
	}
	
     //�����´���a��,s��,d��,w��
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode()==KeyEvent.VK_W){
			//�����ҵ�̹�˷���
			this.hero.setDirect(0);
			this.hero.moveUp();
		}else if(arg0.getKeyCode()==KeyEvent.VK_D){
			this.hero.setDirect(1);
			this.hero.moveRight();
		}else if(arg0.getKeyCode()==KeyEvent.VK_S){
			this.hero.setDirect(2);
			this.hero.movDown();
		}else if(arg0.getKeyCode()==KeyEvent.VK_A){
			this.hero.setDirect(3);
			this.hero.moveLeft();
		}
		
		//�жϰ���J��
		if(arg0.getKeyCode()==KeyEvent.VK_J){
			if(this.hero.ss.size()<5){
				this.hero.shotEnemy();
			}
			
		}
		
		
		
		//�������»���panel
		this.repaint();
		
	}

	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void run() {
		//ÿ��100�����ػ�
		while(true){
			try{
				Thread.sleep(100);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			this.hitEnemyTank();
			this.hitMe();
			
		    //�ػ�
			this.repaint();
		}
	}
}


