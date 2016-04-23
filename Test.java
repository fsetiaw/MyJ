package mine;


public class Test {
	
	class satu {
		String i = null;
		satu() {
			i = "oke";
		}
	}
	
	class dua extends satu {
		dua() {
			super();
		}
		public String getI() {
			return i;
		}
	}
	
	public static void main(String[]args) throws Exception {
		dua d = new dua();
		System.out.println(d.getI());
	}	
}    	