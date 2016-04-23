package mine;
import java.util.*;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;



public class UsrPwd extends Maintenance {
	   
	public static void getUsrPwd(String npmhs) {
		try {
			connectToQapla();
			
			stmt =  con.prepareStatement("select * from CIVITAS where NPMHSMSMHS=?");
			stmt.setString(1, npmhs);
			rs = stmt.executeQuery();
		
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}	
	
	public static void main(String[]args) throws Exception {
		getUsrPwd("0000512100003");
	}

}
