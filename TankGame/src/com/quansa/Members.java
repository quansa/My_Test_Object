package com.quansa;

import java.io.*;
import java.util.Vector;

//�ָ���һ����Ϸ��̹�����ڵ�

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

//��¼��,���Ա����������
class Recorder{
	//��¼ÿ���м�������
	private static int enNum=20;
	//�����ҵ�����
	private static int myLife=3;
	
	//��¼�ܹ�����ĵ���
	private static int allEnNum=0;
	
	//��¼�ָ���һ����Ϸ��
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

	//�ָ���ȡ
	public Vector<Node> getNodesAndNums(){
		try{
			//����
			fr=new FileReader("e:\\myRecording.txt");
			br=new BufferedReader(fr);
			//��ȡ��һ��
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
			//�ر���
			try{
				//�󿪵��ȹ�
				br.close();
				fr.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return nodes;
	}
	
	//������ٵ�������,����
	public void keepRecAndEnemyTank(){
		try{
			//����
			fw=new FileWriter("e:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(allEnNum+"\n");
			
			//���浱ǰ���ŵ�����ͷ���
			for(int i=0;i<ets.size();i++){
				EnemyTank et=ets.get(i);
				
				if(et.isLive){
					String recode=et.x+" "+et.y+" "+et.direct;
					
					//д��
					bw.write(recode+"\r\n");
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//�ر���
			try{
				//�󿪵��ȹ�
				bw.close();
				fw.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	
	//���ļ��ж�ȡ��¼
	public static void getRecoring(){
		try{
			//����
			fr=new FileReader("e:\\myRecording.txt");
			br=new BufferedReader(fr);
			String n=br.readLine();
			allEnNum=Integer.parseInt(n);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//�ر���
			try{
				//�󿪵��ȹ�
				br.close();
				fr.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	//��¼�������ĵ��˱��浽�ļ���
	public static void keepRecording(){
		try{
			//����
			fw=new FileWriter("e:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//�ر���
			try{
				//�󿪵��ȹ�
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
	//���ٵ�����
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

//ը����
class Bomb{
	int x;
	int y;
	//ը������
	int life=9;
	
	boolean isLive=true;
	
	public Bomb(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	//��������
	public void lifeDown(){
		if(life>0){
			life--;
		}else{
			this.isLive=false;
		}
	}
	
}


//�ӵ���
class Shot implements Runnable{
	int x;
	int y;
	int direct;
	int speed=1;
	//�Ƿ����
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
			
			 
			//�ӵ�����
			
			//�ж��Ƿ�������Ե
			if(x<0||x>400||y<0||y>300){
				this.isLive=false;
				break;
			}
		}
		
	}
}

//̹����
	class Tank{
		//������
		int x=0;
		//������
		int y=0;
		//����
		//0��,1��,2��,3��
		int direct=0;
		//����̹���ٶ�
		int speed=1;
		//��ɫ
		int color;
		
		boolean isLive=true;
		//���캯��
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
	//���˵�̹��
		class EnemyTank extends Tank implements Runnable{
			
			
			int time=0;
			//����һ������,�ɷ��ʵ�MyPanel�ϵ����е���
			Vector<EnemyTank> ets=new Vector<EnemyTank>();
			
			//����һ������,���Դ�ŵ��˵��ӵ�
			Vector<Shot> ss=new Vector<Shot>();
			Shot s=null;
			
			public EnemyTank(int x,int y){
				super(x, y);
			}
			
			
			//�õ�Mypanel�ĵ���̹������
			public void setEts(Vector<EnemyTank> vv){
				this.ets=vv;
			}
			
			
			//�ж��Ƿ�������ĵ���̹��
			public boolean isTouchOtheeEnemy(){
				
				boolean b=false;
				
				switch(this.direct){
				case 0:
					//̹������
					//ȡ�����е���̹��
					for(int i=0;i<ets.size();i++){
						//ȡ����һ��̹��
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
						//ȡ����һ��̹��
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
						//ȡ����һ��̹��
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
						//ȡ����һ��̹��
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
						//̹��������
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
							
							//����
							
							Thread t=new Thread(s);
							t.start();
						}
					}
				}
					
					//�����������
					this.direct=(int)(Math.random()*4);
					//�жϵ���̹������
					
					if(this.isLive==false){
						break;
					}
					
					
				}
				
			}
		}
		
	
		//�ҵ�̹�� 
		class Hero extends Tank{
			
			//�ӵ�
			Shot s=null;
			Vector<Shot> ss=new Vector<Shot>();
			
			public Hero(int x,int y){
				super(x,y);
				
				
			}
			//�ӵ�����,����
			public void shotEnemy(){
				
				switch(this.direct){
				case 0:
					//����һ���ӵ�
					s=new Shot(x+10,y,direct);
					//���뵽Vector��
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
				//�����ӵ��߳�
				Thread t=new Thread(s);
				t.start();
			}
			
			//̹������
			public void moveUp(){
				y-=speed;
			}
			//����
			public void moveRight(){
				x+=speed;
			}
			//����
			public void moveLeft(){
				x-=speed;
			}
			//����
			public void movDown(){
				y+=speed;
			}
		}

