package mine;


import java.text.*;
import java.io.*;
import java.sql.*;
import java.net.*;
import java.util.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import java.awt.Color;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.hssf.usermodel.HSSFSheet;







class Msmhs extends Maintenance {

	
	public static Vector listMhs(String kdpst) {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		try {
			connectToMysql();
			stmt = con.prepareStatement("select * from CIVITAS where KDPSTMSMHS=? order by SMAWLMSMHS,NPMHSMSMHS,NIMHSMSMHS");
			stmt.setString(1, kdpst);
			rs = stmt.executeQuery();
			while(rs.next()) {
				String smawl = rs.getString("SMAWLMSMHS");
				String nimhs = rs.getString("NIMHSMSMHS");
				String npmhs = rs.getString("NPMHSMSMHS");
				String nmmhs = rs.getString("NMMHSMSMHS");
				li.add(smawl+","+nimhs+","+npmhs+","+nmmhs);
			}
			writeFile("list_mhs_"+kdpst+"_v2",v);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return v;
	}

	public static void hapusCivitas(String kdpst,String npmhs) {
		try {
			String npmMhsTdkTerdaftar="0000000000002";
			String kdpstMhsTdkTerdaftar="UNREG";
			String kdjenMhsTdkTerdaftar="Z";
			connectToMysql();
			//stmt = con.prepareStatement("delete from EXT_CIVITAS where NPMHSMSMHS=?");
			//stmt.setString(1, npmhs);
			//System.out.println("delete EXT CIVIATS = "+stmt.executeUpdate());
			//stmt = con.prepareStatement("delete from CIVITAS where NPMHSMSMHS=?");
			//stmt.setString(1, npmhs);
			//System.out.println("delete CIVIATS = "+stmt.executeUpdate());
			
			stmt = con.prepareStatement("UPDATE PYMNT set KDPSTPYMNT=?,NPMHSPYMNT=? where NPMHSPYMNT=?");
    		stmt.setString(1, kdpstMhsTdkTerdaftar);
    		stmt.setString(2, npmMhsTdkTerdaftar);
    		stmt.setString(3, npmhs);
    		int upd = stmt.executeUpdate();
    		System.out.println("upd="+upd);
    		stmt = con.prepareStatement("DELETE FROM EXT_CIVITAS where KDPSTMSMHS=? and NPMHSMSMHS=?");
    		stmt.setString(1,kdpst);
    		stmt.setString(2,npmhs);
    		upd = stmt.executeUpdate();
    		System.out.println("upd="+upd);
    		stmt = con.prepareStatement("DELETE FROM CIVITAS where KDPSTMSMHS=? and NPMHSMSMHS=?");
    		stmt.setString(1,kdpst);
    		stmt.setString(2,npmhs);
    		upd = stmt.executeUpdate();
    		System.out.println("upd="+upd);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void setStmhsmsmhs(String npmhs,String stmhs) {
		try {
			connectToMysql();
			stmt = con.prepareStatement("update CIVITAS set STMHSMSMHS=? where NPMHSMSMHS=?");
			stmt.setString(1, stmhs);
			stmt.setString(2, npmhs);
			System.out.println("update status "+npmhs+" "+stmhs+"= "+stmt.executeUpdate());
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void getMhsYgBlmAdaNimSortByAmntPymnt(String kdpst) {
		Vector v = new Vector();
		try {
			ListIterator li = v.listIterator();
			connectToMysql();
			stmt = con.prepareStatement("select * from CIVITAS where KDPSTMSMHS=?");
			stmt.setString(1, kdpst);
			rs = stmt.executeQuery();
			while(rs.next()) {
				String nmmhs = rs.getString("NMMHSMSMHS");
				String npmhs = rs.getString("NPMHSMSMHS");
				String nimhs = ""+rs.getString("NIMHSMSMHS");
				if(nimhs!=null && !nimhs.equalsIgnoreCase("null")  && !nimhs.equalsIgnoreCase(" ") && nimhs.length()>0) {
					if(nimhs.startsWith("NB")||nimhs.startsWith("MM")) {
						li.add(nimhs+","+npmhs+","+nmmhs);
					}
				}
				else {
					li.add(nimhs+","+npmhs+","+nmmhs);
				}
				//System.out.println(nimhs);
			}
			
			stmt = con.prepareStatement("select * from PYMNT where NPMHSPYMNT=?");
			
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,",");
				String nimhs = st.nextToken();
				String npmhs = st.nextToken();
				String nmmhs = st.nextToken();
				stmt.setString(1, npmhs);
				rs = stmt.executeQuery();
				double amnt = 0;
				while(rs.next()) {
					amnt = amnt + rs.getDouble("AMONTPYMNT");
				}
				li.set(amnt+","+npmhs+","+nimhs+","+nmmhs);
				//System.out.println(brs);
			}
			
			Collections.sort(v);
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				System.out.println(brs);
			}
			writeFile("tot bayaran mhs ver2 "+kdpst,v);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public static void bandingNamaDgnSql(String nmfile) {
		Vector v = null;
		try {
			v = bacaFileTxt(nmfile);
			ListIterator li = v.listIterator();
			connectToMysql();
			stmt = con.prepareStatement("select * from CIVITAS where NIMHSMSMHS=?");
			while(li.hasNext()) {
				String brs = (String) li.next();
				StringTokenizer st = new StringTokenizer(brs,",");
				String nimhs = st.nextToken();
				String nmmhs = st.nextToken();
				String thsms = st.nextToken();
				String noija = st.nextToken();
				stmt.setString(1, nimhs);
				rs = stmt.executeQuery();
				String nmsql = "null";
				if(rs.next()) {
					nmsql = rs.getString("NMMHSMSMHS");
				}
				li.set(brs+","+nmsql);
				System.out.println(brs+","+nmsql);
			}
			writeFile("listLulusanMMupdated",v);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	public static void temp() {
		Vector vf = new Vector();
		ListIterator lif = vf.listIterator();
		try {
			connectToMysql();
			stmt = con.prepareStatement("select * from CIVITAS");
			rs = stmt.executeQuery();
			while(rs.next()) {
				String kdpst = rs.getString("KDPSTMSMHS");
				String nimhs = rs.getString("NIMHSMSMHS");
				String npmhs = rs.getString("NPMHSMSMHS");
				String nmmhs = rs.getString("NMMHSMSMHS");
				String smawl = rs.getString("SMAWLMSMHS");
				if(nimhs!=null && nimhs.length()>15) {
					System.out.println(nimhs);
					lif.add(kdpst+","+nimhs+","+npmhs+","+nmmhs+","+smawl+","+nimhs.substring(2,17));
				}
			}
			writeFile("okedeh",vf);
		}
		catch(Exception e) {
			System.out.println(e);
		}	
		System.out.println("done");
	}
	
	public static void temp1() {
		try {
			connectToMysql();
			stmt=con.prepareStatement("update CIVITAS set KDPTIMSMHS='031031'");
			System.out.println("update kdpti = "+stmt.executeUpdate());
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
    public static String generateNpm(String thsms,String kdpst) {
    	int ins = 0;
    	String npm =null;
    	try {
    		connectToMysql();
    		//cretae NPM auto increment
    		stmt = con.prepareStatement("SELECT * FROM CIVITAS where NPMHSMSMHS like ? order by NPMHSMSMHS desc");
    		if(Checker.isStringNullOrEmpty(thsms)) {
    			npm = kdpst+"000";//3 angka 000 - 3 digit thsms
    		}
    		else {
    			npm = kdpst+thsms.substring(2,5);
    		}
    		stmt.setString(1,npm+"%");
    		rs = stmt.executeQuery();
    		if(rs.next()) {
    			npm = rs.getString("NPMHSMSMHS");
    			String first8dig = npm.substring(0,8);
    			String tmp = npm.substring(npm.length()-5,npm.length());
    			//System.out.println("norut ="+tmp);
    			String norut = ""+(Long.valueOf(tmp).longValue()+1);
    			for(int i=norut.length();i<5;i++) {
    				norut = "0"+norut;
    			}
    			npm = first8dig+norut;
    		}
    		else {
    			npm = npm+"00001";
    		}
    		//System.out.println(npm);
    	} 
        catch (SQLException ex) {
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
    	return npm;	
    }
	
	public static void migrasiMsmhsUpdatePart() {
		Vector v = null;
		Vector v1 = new Vector();
		ListIterator li1 = v1.listIterator();
		try {
			connectToMysql();
			stmt=con.prepareStatement("update CIVITAS set TPLHRMSMHS=?,TGLHRMSMHS=?,KDJEKMSMHS=?,SMAWLMSMHS=?,TGMSKMSMHS=?,TGLLSMSMHS=?,STMHSMSMHS=?,STPIDMSMHS=? where KDPSTMSMHS=? and NIMHSMSMHS=?");
			v = bacaFileTxt("list_all_mhs");
			ListIterator li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,",");
				String kdpst = st.nextToken();
				String nimhs = st.nextToken();
				String nmmhs = st.nextToken();
				String tplhr = st.nextToken();
				String tglhr = st.nextToken();
				String kdjek = st.nextToken();
				String smawl = st.nextToken();
				String tgmsk = st.nextToken();
				String tglls = st.nextToken();
				String stmhs = st.nextToken();
				String stpid = st.nextToken();
				
				int i = 0;
				stmt.setString(1, tplhr);
				if(tglhr.equalsIgnoreCase("null")) {
					stmt.setNull(2, java.sql.Types.DATE);
				}
				else {
					stmt.setDate(2, java.sql.Date.valueOf(tglhr));
				}	
				stmt.setString(3, kdjek);
				stmt.setString(4, smawl);
				if(tgmsk.equalsIgnoreCase("null")) {
					stmt.setNull(5, java.sql.Types.DATE);
				}
				else {
					stmt.setDate(5, java.sql.Date.valueOf(tgmsk));
				}	
				if(tglls.equalsIgnoreCase("null")) {
					stmt.setNull(6, java.sql.Types.DATE);
				}
				else {
					stmt.setDate(6, java.sql.Date.valueOf(tglls));
				}	
				
				stmt.setString(7, stmhs);
				stmt.setString(8, stpid);
				stmt.setString(9, kdpst);
				stmt.setString(10, nimhs);
				i = stmt.executeUpdate();
				if(i<1) {
					li1.add(brs);
				}
				System.out.println(nimhs+" = "+i);
			}
			writeFile("mhs_tobe_inserted",v1);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
	}

	public static String getIdObjMhs(String kdpst) {
		String idObj="";
		if(kdpst.equalsIgnoreCase("74201")) {
			idObj="100";
		}
		if(kdpst.equalsIgnoreCase("20201")) {
			idObj="101";
		}
		if(kdpst.equalsIgnoreCase("22201")) {
			idObj="102";
		}
		if(kdpst.equalsIgnoreCase("23201")) {
			idObj="103";
		}
		if(kdpst.equalsIgnoreCase("26201")) {
			idObj="104";
		}
		if(kdpst.equalsIgnoreCase("54201")) {
			idObj="105";
		}
		if(kdpst.equalsIgnoreCase("54211")) {
			idObj="106";
		}
		if(kdpst.equalsIgnoreCase("55201")) {
			idObj="107";
		}
		if(kdpst.equalsIgnoreCase("61101")) {
			idObj="108";
		}
		if(kdpst.equalsIgnoreCase("61201")) {
			idObj="109";
		}
		if(kdpst.equalsIgnoreCase("62201")) {
			idObj="110";
		}
		if(kdpst.equalsIgnoreCase("64201")) {
			idObj="111";
		}
		if(kdpst.equalsIgnoreCase("65001")) {
			idObj="112";
		}
		if(kdpst.equalsIgnoreCase("65101")) {
			idObj="113";
		}
		if(kdpst.equalsIgnoreCase("65201")) {
			idObj="114";
		}
		if(kdpst.equalsIgnoreCase("93402")) {
			idObj="115";
		}
		if(kdpst.equalsIgnoreCase("88888")) {
			idObj="116";
		}
		if(kdpst.equalsIgnoreCase("57301")) {
			idObj="117";
		}
		if(kdpst.equalsIgnoreCase("57302")) {
			idObj="118";
		}
		return idObj;
	}
	
    public static boolean insertCivitasSimple(String smawl,String kdpst,String nimhs,String nmmhs,String kdjek, String stpid,String tplhr,String tglhr,String stmhs,String tgmsk,String tglls) {
    	//kdjen ngga dipake
    	int ins = 0;
    	String npm =null,kdjen=null;
    	try {
    		String nglhr="INDONESIA"; 
    		npm = generateNpm(smawl,kdpst);
    		kdjen = "Y";
    		String id_obj = getIdObjMhs(kdpst);
    		connectToMysql();
    		stmt = con.prepareStatement("INSERT INTO CIVITAS(ID_OBJ,KDPTIMSMHS,KDPSTMSMHS,NPMHSMSMHS,NMMHSMSMHS,TPLHRMSMHS,TGLHRMSMHS,KDJEKMSMHS,TAHUNMSMHS,SMAWLMSMHS,STPIDMSMHS,STMHSMSMHS,TGMSKMSMHS,TGLLSMSMHS,NIMHSMSMHS)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
    		stmt.setLong(1, Long.valueOf(id_obj).longValue());
    		stmt.setString(2,kdjen);
    		//stmt.setString(3, kdjen);
      		if(Checker.isStringNullOrEmpty(kdpst)) {
    			stmt.setNull(3, java.sql.Types.VARCHAR);
    		}
    		else {
    			stmt.setString(3, kdpst.toUpperCase());
    		}
    		stmt.setString(4,npm);
    		stmt.setString(5, nmmhs.toUpperCase());
    		stmt.setString(6, tplhr.toUpperCase());
    		stmt.setDate(7, java.sql.Date.valueOf(tglhr));
    		stmt.setString(8, kdjek.toUpperCase());
    		stmt.setString(9,smawl.substring(0,4));
    		stmt.setString(10,smawl);
    		stmt.setString(11, stpid.toUpperCase());
    		stmt.setString(12, stmhs.toUpperCase());
    		if(tgmsk.equalsIgnoreCase("null")) {
    			stmt.setNull(13, java.sql.Types.DATE);
    		}
    		else {
    			stmt.setDate(13, java.sql.Date.valueOf(tgmsk));
    		}	
    		if(tglls.equalsIgnoreCase("null")) {
    			stmt.setNull(14, java.sql.Types.DATE);
    		}
    		else {
    			stmt.setDate(14, java.sql.Date.valueOf(tglls));
    		}
    		stmt.setString(15, nimhs);
    		ins = stmt.executeUpdate();
    		System.out.println("insert to civitas "+nimhs+" = "+ins); 
    		stmt = con.prepareStatement("INSERT INTO EXT_CIVITAS(KDPSTMSMHS,NPMHSMSMHS,EMAILMSMHS,NEGLHMSMHS,NOHPEMSMHS)VALUES(?,?,?,?,?)");
    		stmt.setString(1, kdpst);
    		stmt.setString(2, npm);
    		stmt.setString(3, "no@ne");
    		stmt.setString(4, nglhr.toUpperCase());
    		stmt.setString(5, "0");
    		ins = stmt.executeUpdate();
    		System.out.println("insert to ext_civitas "+nimhs+" = "+ins); 
    	} 
        catch (SQLException ex) {
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
    	boolean sukses = false;
    	if(ins>0) {
    		sukses = true;
    	}
    	return sukses;	
    }
	
	public static void migrasiMsmhsInsertPart() {
		Vector v = null;
		try {
			v = bacaFileTxt("mhs_tobe_inserted");
			ListIterator li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,",");
				String kdpst = st.nextToken();
				String nimhs = st.nextToken();
				String nmmhs = st.nextToken();
				String tplhr = st.nextToken();
				String tglhr = st.nextToken();
				String kdjek = st.nextToken();
				String smawl = st.nextToken();
				String tgmsk = st.nextToken();
				String tglls = st.nextToken();
				String stmhs = st.nextToken();
				String stpid = st.nextToken();
				boolean sukses = insertCivitasSimple(smawl,kdpst,nimhs,nmmhs,kdjek,stpid,tplhr,tglhr,stmhs,tgmsk,tglls);
				System.out.println(sukses);
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public static void getMhsBlmAdaNim() {
		try {
			Vector v = new Vector();
			ListIterator li = v.listIterator();
			
			connectToMysql();
			stmt=con.prepareStatement("select * from CIVITAS where NIMHSMSMHS is NULL or NIMHSMSMHS='null'");
			rs = stmt.executeQuery();
			while(rs.next()) {
				String kdpst = rs.getString("KDPSTMSMHS");
				String npmhs = rs.getString("NPMHSMSMHS");
				String nmmhs = rs.getString("NMMHSMSMHS");
				System.out.println(kdpst+","+npmhs+","+nmmhs);
				li.add(kdpst+","+npmhs+","+nmmhs);
			}
			writeFile("noNIm",v);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[]args)throws Exception {
		
		listMhs("65101");
		//System.out.println("done");
		//hapusCivitas("61201","6120113100001");
		//setStmhsmsmhs("6500112200004","K");
		//getMhsYgBlmAdaNimSortByAmntPymnt("61101");
		//bandingNamaDgnSql("ListLulusanMM");
		//migrasiMsmhsInsertPart();
		//temp1();
		//getMhsBlmAdaNim();
		System.out.println("completed");
	}
	

}