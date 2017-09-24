package quansa;

class Pupil {
	String name;
	private int age;
	private float fee;
	
	public Pupil(){
		
	}
	
	public Pupil(String name,int age,float fee){
		this.name=name;
		this.age=age;
		this.fee=fee;
	}
	
	public int sum(int a,int b){
		return a+b;
	}
	public float sum(int a,float b){
		return a+b;
	}
	float sum(float b,int a){
		return a+b;
	}
	
}
