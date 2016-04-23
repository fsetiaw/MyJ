package mine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Vector;
import java.util.ListIterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.StringTokenizer;
/**
 * Session Bean implementation class CommandTool
 */

public class CommandTool extends Maintenance {

    
    
    public static Vector getAllCommand() {
    	Vector v = new Vector();
    	ListIterator li = v.listIterator();
      	try {
    		connectToMysql();
    		//cretae NPM auto increment
    		stmt = con.prepareStatement("SELECT * from OBJECT");
    		rs = stmt.executeQuery();
    		String list_cmd = "";
    		boolean first = true;
    		while(rs.next()) {
    			if(first) {
    				first = false;
    				list_cmd = list_cmd+rs.getString("ACCESS_LEVEL");
    			}
    			else {
    				if(list_cmd.endsWith("#")) {
    					list_cmd = list_cmd+rs.getString("ACCESS_LEVEL");
    				}
    				else {
    					list_cmd = list_cmd+"#"+rs.getString("ACCESS_LEVEL");	
    				}
    					
    			}
    			
    			//String obj_name = ""+rs.getString("OBJ_NAME");
    			
    		}
    		
    		list_cmd = list_cmd.replace("null", "");
			list_cmd = list_cmd.replace("!", "");
			//list_cmd = list_cmd.replace(",,", "");
			
			StringTokenizer st = new StringTokenizer(list_cmd,"#");
			while(st.hasMoreTokens()) {
				String brs = st.nextToken();
				li.add(brs);
				//System.out.println(brs);
			}
			
			v = removeDuplicateFromVector(v);
			Collections.sort(v);
			//cek pengguna msing2 cmd
			stmt = con.prepareStatement("select * from OBJECT where ACCESS_LEVEL like ?");
			li = v.listIterator();
			while(li.hasNext()) {
				String cmd = (String)li.next();
				stmt.setString(1,"%"+cmd+"%");
				rs = stmt.executeQuery();
				String usr = "";
				first = true;
				Vector v1 = new Vector();
				ListIterator li1 = v1.listIterator();
				while(rs.next()) {
					usr = ""+rs.getString("OBJ_NAME");
					li1.add(usr);
				}
				v1 = removeDuplicateFromVector(v1);
				Collections.sort(v1);
				li1 = v1.listIterator();
				usr = "";
				while(li1.hasNext()) {
					usr = usr+li1.next();
					if(li1.hasNext()) {
						usr = usr+",";
					}
				}
				li.set(cmd+"#"+usr);
			}
			stmt = con.prepareStatement("select * from TABEL_COMMAND where CMD_CODE=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				st = new StringTokenizer(brs,"#");
				String cmd = st.nextToken();
				String usr = st.nextToken();
				stmt.setString(1, cmd);
				rs = stmt.executeQuery();
				if(rs.next()) {
					li.remove();
				}
				//System.out.println(cmd);
			}	
			stmt = con.prepareStatement("INSERT INTO TABEL_COMMAND (CMD_CODE,USE_BY) values(?,?)");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				System.out.println(brs);
				st = new StringTokenizer(brs,"#");
				String cmd = st.nextToken();
				String usr = st.nextToken();
				stmt.setString(1,cmd);
				stmt.setString(2,usr);
				System.out.println("insert "+brs+" = "+stmt.executeUpdate());
			}	
    	} 
        catch (Exception ex) {
        	ex.printStackTrace();
        } 
        finally {
        	if (con!=null) {
        		try {
        			con.close();
        		}
        		catch (Exception ignore) {
            		System.out.println(ignore);
        		}
        	}
        }
    	return v;	
    }
    
    public static Vector removeDuplicateFromVector(Vector v)throws Exception 
	{
		Vector v1 = new Vector(v);
		ListIterator li = v.listIterator();
		while(li.hasNext())
		{
			String baris = (String)li.next();
			//System.out.println("v "+baris);
		}	
	

		v1 = new Vector(new LinkedHashSet(v));
		ListIterator li1 = v1.listIterator();
		while(li1.hasNext())
		{
			String baris = (String)li1.next();
			//System.out.println("v1 "+baris);
		}	
		return v1;
	}
    
    
    public static void main(String[]args) throws Exception {
    	getAllCommand();
    }
}
