package com.quansa;

import java.io.*;
import java.util.Vector;

//恢复上一局游戏的坦克所在点

class Node{
	int x;
	int y;
	int direct;
	public Node(int x,int y,int direct){
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
}

//记录类,可以保存玩家设置
class Recorder{
	//记录每关有几个敌人
	private static int enNum=20;
	//设置我的生命
	private static int myLife=3;
	
	//记录总共消灭的敌人
	private static int allEnNum=0;
	
	//记录恢复上一局游戏点
	static Vector<Node> nodes=new Vector<Node>();
	
	private static FileWriter fw=null;
	private static BufferedWriter bw=null;
	
	private static FileReader fr=null;
	private static BufferedReader br=null;
	
	private  Vector<EnemyTank> ets=new Vector<EnemyTank>();
	
	public  Vector<EnemyTank> getEts() {
		return ets;
	}
	public void setEts(Vector<EnemyTank> ets1) {
		this.ets = ets1;
	}

	//恢复读取
	public Vector<Node> getNodesAndNums(){
		try{
			//创建
			fr=new FileReader("e:\\myRecording.txt");
			br=new BufferedReader(fr);
			//读取第一行
			String n=br.readLine();
			allEnNum=Integer.parseInt(n);
			
			while((n=br.readLine())!=null){
				String []xyz=n.split(" ");
				for(int i=0;i<xyz.length;i++){
					Node node=new Node(Integer.parseInt(xyz[0]),Integer.parseInt(xyz[1]),Integer.parseInt(xyz[2]));
					nodes.add(node);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//关闭流
			try{
				//后开的先关
				br.close();
				fr.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return nodes;
	}
	
	//保存击毁敌人数量,坐标
	public void keepRecAndEnemyTank(){
		try{
			//创建
			fw=new FileWriter("e:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(allEnNum+"\n");
			
			//保存当前活着的坐标和方向
			for(int i=0;i<ets.size();i++){
				EnemyTank et=ets.get(i);
				
				if(et.isLive){
					String recode=et.x+" "+et.y+" "+et.direct;
					
					//写入
					bw.write(recode+"\r\n");
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//关闭流
			try{
				//后开的先关
				bw.close();
				fw.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	
	//从文件中读取记录
	public static void getRecoring(){
		try{
			//创建
			fr=new FileReader("e:\\myRecording.txt");
			br=new BufferedReader(fr);
			String n=br.readLine();
			allEnNum=Integer.parseInt(n);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//关闭流
			try{
				//后开的先关
				br.close();
				fr.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	//记录玩家消灭的敌人保存到文件中
	public static void keepRecording(){
		try{
			//创建
			fw=new FileWriter("e:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//关闭流
			try{
				//后开的先关
				bw.close();
				fw.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	public static int getAllEnNum() {
		return allEnNum;
	}
	public static void setAllEnNum(int allEnNum) {
		Recorder.allEnNum = allEnNum;
	}
	public static int getEnNum() {
		return enNum;
	}
	public static void setEnNum(int enNum) {
		Recorder.enNum = enNum;
	}
	public static int getMyLife() {
		return myLife;
	}
	public static void setMyLife(int myLife) {
		Recorder.myLife = myLife;
	}
	//减少敌人数
	public static void reduceEnNum(){
		enNum--;
	}
	public static void reducemyLife(){
		myLife--;
	}
	public static void addEnNum(){
		allEnNum++;
	}
	

	
}

//炸弹类
class Bomb{
	int x;
	int y;
	//炸弹生命
	int life=9;
	
	boolean isLive=true;
	
	public Bomb(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	//减少生命
	public void lifeDown(){
		if(life>0){
			life--;
		}else{
			this.isLive=false;
		}
	}
	
}


//子弹类
class Shot implements Runnable{
	int x;
	int y;
	int direct;
	int speed=1;
	//是否活着
	boolean isLive=true;
	
	public Shot(int x,int y,int direct){
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	
	public void run() {
		while(true){
			
			try{
				Thread.sleep(50);
			}catch(Exception e){
				
			}
			
			switch(direct){
			case 0:
				y-=speed;
				break;
			case 1:
				x+=speed;
				break;
			case 2:
				y+=speed;
				break;
			case 3:
				x-=speed;
				break;
			}
			
			 
			//子弹死亡
			
			//判断是否碰到边缘
			if(x<0||x>400||y<0||y>300){
				this.isLive=false;
				break;
			}
		}
		
	}
}

//坦克类
	class Tank{
		//横坐标
		int x=0;
		//纵坐标
		int y=0;
		//方向
		//0上,1右,2下,3左
		int direct=0;
		//设置坦克速度
		int speed=1;
		//颜色
		int color;
		
		boolean isLive=true;
		//构造函数
		public Tank(int x,int y){
			this.x=x;
			this.y=y;
		}
		
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}	
		public int getDirect() {
			return direct;
		}

		public void setDirect(int direct) {
			this.direct = direct;
		}
		public int getSpeed() {
			return speed;
		}

		public void setSpeed(int speed) {
			this.speed = speed;
		}
		public int getColor() {
			return color;
		}

		public void setColor(int color) {
			this.color = color;
		}


	}
	//敌人的坦克
		class EnemyTank extends Tank implements Runnable{
			
			
			int time=0;
			//定义一个向量,可访问到MyPanel上的所有敌人
			Vector<EnemyTank> ets=new Vector<EnemyTank>();
			
			//定义一个向量,可以存放敌人的子弹
			Vector<Shot> ss=new Vector<Shot>();
			Shot s=null;
			
			public EnemyTank(int x,int y){
				super(x, y);
			}
			
			
			//得到Mypanel的敌人坦克向量
			public void setEts(Vector<EnemyTank> vv){
				this.ets=vv;
			}
			
			
			//判断是否碰到别的敌人坦克
			public boolean isTouchOtheeEnemy(){
				
				boolean b=false;
				
				switch(this.direct){
				case 0:
					//坦克向上
					//取出所有敌人坦克
					for(int i=0;i<ets.size();i++){
						//取出第一个坦克
						EnemyTank et=ets.get(i);
						if(et!=this){
							if(et.direct==0||et.direct==2){
								if(this.x>et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30){
									return true;
								}
								if(this.x+20>=et.x&&this.x+20<=et.x+20&&this.y>=et.y&&this.y<=et.y+30){
									return true;
								}
							}
							if(et.direct==1||et.direct==3){
								if(this.x>et.x&&this.x<=et.x+30&&this.y>=et.y&&this.y<=et.y+20){
									return true;
								}
								if(this.x+20>=et.x&&this.x+20<=et.x+30&&this.y>=et.y&&this.y<=et.y+20){
									return true;
								}
							}
						}
					}
					break;
					case 1:
					for(int i=0;i<ets.size();i++){
						//取出第一个坦克
						EnemyTank et=ets.get(i);
						if(et!=this){
							if(et.direct==0||et.direct==2){
								if(this.x+30>et.x&&this.x+30<=et.x+20&&this.y>=et.y&&this.y<=et.y+30){
									return true;
								}
								if(this.x+30>=et.x&&this.x+30<=et.x+20&&this.y+20>=et.y&&this.y+20<=et.y+30){
									return true;
								}
							}
							if(et.direct==1||et.direct==3){
								if(this.x+30>et.x&&this.x+30<=et.x+30&&this.y>=et.y&&this.y<=et.y+20){
									return true;
								}
								if(this.x+30>=et.x&&this.x+30<=et.x+30&&this.y+20>=et.y&&this.y+20<=et.y+20){
									return true;
								}
							}
						}
					}
					break;
					case 2:
					for(int i=0;i<ets.size();i++){
						//取出第一个坦克
						EnemyTank et=ets.get(i);
						if(et!=this){
							if(et.direct==0||et.direct==2){
								if(this.x>et.x&&this.x<=et.x+20&&this.y+30>=et.y&&this.y+30<=et.y+30){
									return true;
								}
								if(this.x+20>=et.x&&this.x+20<=et.x+20&&this.y+30>=et.y&&this.y+30<=et.y+30){
									return true;
								}
							}
							if(et.direct==1||et.direct==3){
								if(this.x>et.x&&this.x<=et.x+30&&this.y+30>=et.y&&this.y+30<=et.y+20){
									return true;
								}
								if(this.x+20>=et.x&&this.x+20<=et.x+30&&this.y+30>=et.y&&this.y+30<=et.y+20){
									return true;
								}
							}
						}
					}
					break;
			
					case 3:
					for(int i=0;i<ets.size();i++){
						//取出第一个坦克
						EnemyTank et=ets.get(i);
						if(et!=this){
							if(et.direct==0||et.direct==2){
								if(this.x>et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30){
									return true;
								}
								if(this.x>=et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30){
									return true;
								}
							}
							if(et.direct==1||et.direct==3){
								if(this.x>et.x&&this.x<=et.x+30&&this.y>=et.y&&this.y<=et.y+20){
									return true;
								}
								if(this.x>=et.x&&this.x<=et.x+30&&this.y+20>=et.y&&this.y+20<=et.y+20){
									return true;
								}
							}
						}
					}
					
					break;
				}
				return b;
			}
			
			public void run() {
				while(true){
					
					
					switch(this.direct){
					case 0:
						//坦克向上走
						for(int i=0;i<30;i++){
							if(y>0&&(!this.isTouchOtheeEnemy())){
								y-=speed;
							}
							try{
								Thread.sleep(50);
							}catch(Exception e){
								e.printStackTrace();
							}
							
						}
						break;
						
					case 1:
						
						for(int i=0;i<30;i++){
							if(x<400&&(!this.isTouchOtheeEnemy())){
								x+=speed;
							}
							try{
								Thread.sleep(50);
							}catch(Exception e){
								e.printStackTrace();
							}
							
						}
						break;
						
					case 2:
						
						for(int i=0;i<30;i++){
							if(y<300&&(!this.isTouchOtheeEnemy())){
								y+=speed;
							}
							try{
								Thread.sleep(50);
							}catch(Exception e){
								e.printStackTrace();
							}	
						}
						break;
						
					case 3:
						for(int i=0;i<30;i++){
							if(x>0&&(!this.isTouchOtheeEnemy())){
								x-=speed;
							}
							try{
								Thread.sleep(50);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						break;
						
					}
					
					this.time++;
					if(time%2==0){
						if(isLive){
						if(ss.size()<5){
							Shot s=null;
							switch(direct){
							case 0:
								s=new Shot(x+10,y,0);
								ss.add(s);
								break;
							case 1:
								s=new Shot(x+30,y+10,1);
								ss.add(s);
								break;
							case 2:
								s=new Shot(x+10,y+30,2);
								ss.add(s);
								break;
							case 3:
								s=new Shot(x,y+10,3);
								ss.add(s);
								break;
							}
							
							//启动
							
							Thread t=new Thread(s);
							t.start();
						}
					}
				}
					
					//随机产生方向
					this.direct=(int)(Math.random()*4);
					//判断敌人坦克死亡
					
					if(this.isLive==false){
						break;
					}
					
					
				}
				
			}
		}
		
	
		//我的坦克 
		class Hero extends Tank{
			
			//子弹
			Shot s=null;
			Vector<Shot> ss=new Vector<Shot>();
			
			public Hero(int x,int y){
				super(x,y);
				
				
			}
			//子弹发射,开火
			public void shotEnemy(){
				
				switch(this.direct){
				case 0:
					//创建一颗子弹
					s=new Shot(x+10,y,direct);
					//加入到Vector中
					ss.add(s);
					break;
				case 1:
					s=new Shot(x+30,y+10,direct);
					ss.add(s);
					break;
				case 2:
					s=new Shot(x+10,y+30,direct);
					ss.add(s);
					break;
				case 3:
					s=new Shot(x,y+10,direct);
					ss.add(s);
					break;
					
				}
				//启动子弹线程
				Thread t=new Thread(s);
				t.start();
			}
			
			//坦克上移
			public void moveUp(){
				y-=speed;
			}
			//右移
			public void moveRight(){
				x+=speed;
			}
			//左移
			public void moveLeft(){
				x-=speed;
			}
			//下移
			public void movDown(){
				y+=speed;
			}
		}

