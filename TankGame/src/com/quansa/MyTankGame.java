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
	
	//菜单
	JMenuBar jmb=null;
	//开始游戏
	JMenu jm1=null;
	JMenuItem jmi1=null;
	//退出
	JMenuItem jmi2=null;
	//存盘退出
	JMenuItem jmi3=null;
	
	//继续上一局游戏
	JMenuItem jmi4=null;
	
	public static void main(String[] args) {
		
		MyTankGame mtg=new MyTankGame();
	}
	
	//构造函数
	public MyTankGame(){
		
		
		
		
		//创建菜单,菜单选项
		jmb=new JMenuBar();
		jm1=new JMenu("游戏(G)");
		//设置快捷方式
		jm1.setMnemonic('G');
				
		jmi1=new JMenuItem("开始新游戏(N)");
		jmi2=new JMenuItem("退出游戏(E)");
		jmi3=new JMenuItem("存盘退出游戏");
		jmi4=new JMenuItem("继续上一局游戏");
		
		jmi1.setMnemonic('N');
		//对jmi1响应
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
		//对用户不同点击做不同的响应
			if(e.getActionCommand().equals("newgame")){
				
				//创建战场面板
				mp=new Mypanel("newGame");
				//启动mp线程
				Thread t1=new Thread(mp);
				t1.start();
				//删除旧面板
				this.remove(msp);
				//创建新面板
				this.add(mp);
				//注册监听
				this.addKeyListener(mp);
				
				this.setVisible(true);
			}else if(e.getActionCommand().equals("exit")){
				//保存击毁敌人
				Recorder.keepRecording();
				System.exit(0);
			}
			//存盘退出
			else if(e.getActionCommand().equals("saveExit")){
				
				Recorder rd=new Recorder();
				rd.setEts(mp.ets);
				//保存击毁敌人数量,活着的坐标
				rd.keepRecAndEnemyTank();
				//退出
				System.exit(0);
				
			}//继续上局游戏
			else if(e.getActionCommand().equals("continu")){
				//创建战场面板
				mp=new Mypanel("continu");
				//mp.flag="continu";
				
				//启动mp线程
				Thread t1=new Thread(mp);
				t1.start();
				//删除旧面板
				this.remove(msp);
				//创建新面板
				this.add(mp);
				//注册监听
				this.addKeyListener(mp);
				
				this.setVisible(true);
			}
			
}
}
//提示
class MyStartPanel extends JPanel implements Runnable{
	int time=0;
	
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0,0,400,300);
		//提示信息
		if(time%2==0){
		g.setColor(Color.yellow);
		Font myFont=new Font("楷体",Font.BOLD,30);
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


//我的面板
class Mypanel extends JPanel implements KeyListener,Runnable{
	//定义一个我的坦克
	Hero hero=null;
	
	//定义敌人的坦克组
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	Vector<Node> nodes=new Vector<Node>();
	
	//敌人坦克数量
	int enSize=3;
	
	//判断是继续上局,换是新游戏
	//String flag="newGame";
	
	//定义炸弹集合
	Vector<Bomb> bombs=new Vector<Bomb>();
	
	//定义三张图片
	Image image1=null;
	Image image2=null;;
	Image image3=null;
	
	
	public Mypanel(String flag){
		
		//恢复记录
		Recorder.getRecoring();
		
		 hero=new Hero(200,200);
		 if(flag.equals("newGame")){
			  //初始化敌人坦克
			 for(int i=0;i<enSize;i++){
			 //创建敌人Tank
				 EnemyTank et=new EnemyTank((i+1)*50,0);
				 et.setColor(0);
				 et.setDirect(2);
				 //将Mypanel的敌人坦克向量交给该敌人坦克
				 et.setEts(ets);
			 
				 //启动
				 Thread t=new Thread(et);
				 t.start();
			 
				 //敌人Tank加子弹
				 Shot s=new Shot(et.x+10,et.y+30,2);
				 et.ss.add(s);
				 Thread t2=new Thread(s);
				 t2.start();
			 
				 //加入
				 ets.add(et);
				 }
			 }else{
				 nodes=new Recorder().getNodesAndNums();
				 for(int i=0;i<nodes.size();i++){
					 Node node=nodes.get(i);
					 
					 EnemyTank et=new EnemyTank(node.x,node.y);
					 et.setColor(0);
					 et.setDirect(node.direct);
					 //将Mypanel的敌人坦克向量交给该敌人坦克
					 et.setEts(ets);
					 
					 //启动
					 Thread t=new Thread(et);
					 t.start();
					 
					 //敌人Tank加子弹
					 Shot s=new Shot(et.x+10,et.y+30,2);
					 et.ss.add(s);
					 Thread t2=new Thread(s);
					 t2.start();
					 
					 //加入
					 ets.add(et);
			 }
		 }
		
		 
		 //初始化图片
		 image1=Toolkit.getDefaultToolkit().getImage("1.jpg");
		 image2=Toolkit.getDefaultToolkit().getImage("2.jpg");
		 image3=Toolkit.getDefaultToolkit().getImage("3.jpg");
		 
		 
		 }
	
	//画出提示信息
	public void showInfo(Graphics g){
		//画出提示信息坦克(不战斗)
		this.drawTank(80,315, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getEnNum()+"",110, 338);
				
		this.drawTank(200,315, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyLife()+"",230, 338);
		
		//玩家总成绩
		g.setColor(Color.black);
		Font f=new Font("宋体",Font.BOLD,20);
		g.setFont(f);
		g.drawString("总成绩", 430, 70);
		
		this.drawTank(445, 80, g, 0, 0);
		
		g.setColor(Color.black);
		g.drawString(Recorder.getAllEnNum()+"",480 , 100);
		
	}
	
	//重写paint
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		
		//画出提示信息
		this.showInfo(g);
		
		//画出自己的坦克
		if(hero.isLive){
			this.drawTank(hero.getX(), hero.getY(), g, this.hero.getDirect(), 1);
		}
		//画出炸弹
		for(int i=0;i<bombs.size();i++){
			//取出炸弹
			Bomb b=bombs.get(i);
			if(b.life>6){
				g.drawImage(image1, b.x, b.y, 30, 30, this);
			}else if(b.life>3){
				g.drawImage(image2, b.x, b.y, 30, 30, this);
			}else{
				g.drawImage(image3, b.x, b.y, 30, 30, this);
			}
			
			//让b的life--
			b.lifeDown();
			//life==0,去除
			if(b.life==0){
				bombs.remove(b);
			}
			
		}
		
		//画出敌人坦克
		for(int i=0;i<ets.size();i++){
			EnemyTank et=ets.get(i);
			if(et.isLive){
				this.drawTank(et.getX(),et.getY() , g, et.getDirect(),0);
				//敌人子弹
				for(int j=0;j<et.ss.size();j++){
					Shot enemyShot=et.ss.get(j);
					if(enemyShot.isLive){
						g.fillOval(enemyShot.x,enemyShot.y,2,2);
					}else{
						//敌人坦克死,去掉
						et.ss.remove(enemyShot);
					}
				}
			
			}
			
		}
		
		//从ss中取出每一刻子弹,画出
		for(int j=0;j<this.hero.ss.size();j++){
			Shot myShot=hero.ss.get(j);
			//画出子弹,一颗
			if(hero.s!=null && myShot.isLive==true){
				g.fillOval(myShot.x,myShot.y,2,2);
			}
			if(myShot.isLive==false){
				hero.ss.remove(myShot);
			}
			
		}
		
	}
	
	//判断我的子弹击中敌人坦克
	public void hitEnemyTank(){
		//判断是否击中敌人 
			for(int i=0;i<hero.ss.size();i++){
				//取出子弹
				Shot myShot=hero.ss.get(i);
				
				//判断子弹是否活着
				if(myShot!=null && myShot.isLive){
					//取出每个敌人坦克与之匹配
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
	
	//敌人是否击中我
	public void hitMe(){
		//取出每一个敌人的坦克
		for(int i=0;i<this.ets.size();i++){
			//取出坦克
			EnemyTank et=ets.get(i);
			//取出每颗子弹
			for(int j=0;j<et.ss.size();j++){
				
				//取出子弹
				Shot enemyShot=et.ss.get(j);
				if(hero.isLive){
					if(this.hitTank(enemyShot,hero)){
						Recorder.reducemyLife();
					}
					
					
				}
				}
			
		}
	}
	
	//判断我的子弹击中敌人坦克 
	public boolean hitTank(Shot s,Tank et){
		
		boolean b2=false;
		//判断坦克方向
		switch(et.direct){
		//方向为上或下
		case 0:
		case 2:
			if(s.x>et.x && s.x<et.x+20 && s.y>et.y && s.y<et.y+30){
				//击中
				//子弹死
				s.isLive=false;
				//敌人的坦克死
				et.isLive=false;
				b2=true;
				//创建一颗炸弹,加入Vector
				Bomb b=new Bomb(et.x,et.y);
				bombs.add(b);
			}
			break;
			
		case 1:
		case 3:
			if(s.x>et.x && s.x<et.x+30 && s.y>et.y && s.y<et.y+20){
				//击中
				//子弹死
				s.isLive=false;
				//敌人的坦克死
				et.isLive=false;
				b2=true;
				//创建一颗炸弹,加入Vector
				Bomb b=new Bomb(et.x,et.y);
				bombs.add(b);
				}
			break;
			}
		return b2;
		
	}
	
	//画出坦克
	public void drawTank(int x,int y,Graphics g,int direct,int type){
		//判断坦克类型
		switch(type){
		
		case 0:
			g.setColor(Color.cyan);
			break;
		//我的坦克颜色
		case 1:
			g.setColor(Color.yellow);
			break;
		}
		
		//判断坦克方向
		switch(direct){
		//向上
		case 0:
			//1.画出左边矩形
			g.fill3DRect(x, y, 5, 30,false);
			//2.画出右边矩形
			g.fill3DRect(x+15,y,5,30,false);
			//3.画出中间矩形
			g.fill3DRect(x+5,y+5,10,20,false);
			//圆
			g.fillOval(x+5, y+10, 10, 10);
			//线
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
	
     //键按下处理a左,s下,d右,w上
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode()==KeyEvent.VK_W){
			//设置我的坦克方向
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
		
		//判断按下J键
		if(arg0.getKeyCode()==KeyEvent.VK_J){
			if(this.hero.ss.size()<5){
				this.hero.shotEnemy();
			}
			
		}
		
		
		
		//必须重新绘制panel
		this.repaint();
		
	}

	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void run() {
		//每隔100毫秒重画
		while(true){
			try{
				Thread.sleep(100);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			this.hitEnemyTank();
			this.hitMe();
			
		    //重绘
			this.repaint();
		}
	}
}


