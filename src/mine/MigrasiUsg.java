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



class MigrasiUsg extends Maintenance {

	public static void migrasiObjectBackUpToUSG() {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		
		try {
			connectToBackUp();
			stmt = con.prepareStatement("select * from OBJECT order by ID_OBJ");
			rs = stmt.executeQuery();
			while(rs.next()) {
				String id_obj= ""+rs.getLong("ID_OBJ");
				String kdpst=""+rs.getString("KDPST");
				String obj_name=""+rs.getString("OBJ_NAME");
				String obj_desc=""+rs.getString("OBJ_DESC");
				String obj_level=""+rs.getInt("OBJ_LEVEL");
				String conditional=""+rs.getString("ACCESS_LEVEL_CONDITIONAL");
				String access=""+rs.getString("ACCESS_LEVEL");
				String defaultVaue = ""+rs.getString("DEFAULT_VALUE");
				String nickname = ""+rs.getString("OBJ_NICKNAME");
				String hak_akses = ""+rs.getString("HAK_AKSES");
				String scope_kampus = ""+rs.getString("SCOPE_KAMPUS");
				String domain_kampus = ""+rs.getString("KODE_KAMPUS_DOMISILI");
				li.add(id_obj);
				li.add(kdpst);
				li.add(obj_name);
				li.add(obj_desc);
				li.add(obj_level);
				li.add(conditional);
				li.add(access); 
				li.add(defaultVaue);
				li.add(nickname);
				li.add(hak_akses);
				li.add(scope_kampus);
				li.add(domain_kampus);
			}
			
			//update
			Vector vInsert = new Vector();
			ListIterator li1 = vInsert.listIterator();
			connectToMysql();
			stmt=con.prepareStatement("update OBJECT set KDPST=?,OBJ_NAME=?,OBJ_DESC=?,OBJ_LEVEL=?,ACCESS_LEVEL_CONDITIONAL=?,ACCESS_LEVEL=?,DEFAULT_VALUE=?,OBJ_NICKNAME=?,HAK_AKSES=?,SCOPE_KAMPUS=?,KODE_KAMPUS_DOMISILI=? where ID_OBJ=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String id_obj= (String)li.next();
				String kdpst= (String)li.next();
				String obj_name= (String)li.next();
				String obj_desc= (String)li.next();
				String obj_level= (String)li.next();
				String conditional= (String)li.next();
				String access= (String)li.next();
				String defaultVaue= (String)li.next();
				String nickname= (String)li.next();
				String hak_akses = (String)li.next();
				String scope_kampus = (String)li.next();
				String domain_kampus = (String)li.next();
				if(kdpst==null || kdpst.equalsIgnoreCase("null")) {
					stmt.setNull(1, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(1,kdpst);
				}
				if(obj_name==null || obj_name.equalsIgnoreCase("null")) {
					stmt.setNull(2, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(2,obj_name);
				}
				if(obj_desc==null || obj_desc.equalsIgnoreCase("null")) {
					stmt.setNull(3, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(3,obj_desc);
				}
				if(obj_level==null) {
					stmt.setNull(4, java.sql.Types.INTEGER);
				}
				else {
					stmt.setInt(4,Integer.valueOf(obj_level).intValue());
				}
				if(conditional==null || conditional.equalsIgnoreCase("null")) {
					stmt.setNull(5, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(5,conditional);
				}
				if(access==null || access.equalsIgnoreCase("null")) {
					stmt.setNull(6, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(6,access);
				}
				
				if(defaultVaue==null || defaultVaue.equalsIgnoreCase("null")) {
					stmt.setNull(7, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(7,defaultVaue);
				}
				if(nickname==null || nickname.equalsIgnoreCase("null")) {
					stmt.setNull(8, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(8,nickname);
				}
				if(hak_akses==null || hak_akses.equalsIgnoreCase("null")) {
					stmt.setNull(9, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(9,hak_akses);
				}
				if(scope_kampus==null || scope_kampus.equalsIgnoreCase("null")) {
					stmt.setNull(10, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(10,scope_kampus);
				}
				if(domain_kampus==null || domain_kampus.equalsIgnoreCase("null")) {
					stmt.setNull(11, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(11,domain_kampus);
				}
				
				
				stmt.setLong(12,Integer.valueOf(id_obj).intValue());
				int i = 0;
				i = stmt.executeUpdate();
				System.out.println("update = "+i);
				if(i<1) {
					System.out.println("add to v-insert "+id_obj);
					li1.add(id_obj);
					li1.add(kdpst);
					li1.add(obj_name);
					li1.add(obj_desc);
					li1.add(obj_level);
					li1.add(conditional);
					li1.add(access); 
					li1.add(defaultVaue);
					li1.add(nickname);
					li1.add(hak_akses);
					li1.add(scope_kampus);
					li1.add(domain_kampus);
				}
			}
			
			
			stmt = con.prepareStatement("INSERT INTO OBJECT(ID_OBJ,KDPST,OBJ_NAME,OBJ_DESC,OBJ_LEVEL,ACCESS_LEVEL_CONDITIONAL,ACCESS_LEVEL,DEFAULT_VALUE,OBJ_NICKNAME,HAK_AKSES,SCOPE_KAMPUS,KODE_KAMPUS_DOMISILI)VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");			
			li1 = vInsert.listIterator();
			while(li1.hasNext()) {
				String id_obj= (String)li1.next();
				String kdpst= (String)li1.next();
				String obj_name= (String)li1.next();
				String obj_desc= (String)li1.next();
				String obj_level= (String)li1.next();
				String conditional= (String)li1.next();
				String access= (String)li1.next();
				String defaultVaue= (String)li1.next();
				String nickname= (String)li1.next();
				String hak_akses= (String)li1.next();
				String scope_kampus = (String)li1.next();
				String domain_kampus = (String)li1.next();
				stmt.setLong(1,Long.valueOf(id_obj).longValue());
				if(kdpst==null || kdpst.equalsIgnoreCase("null")) {
					stmt.setNull(2, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(2,kdpst);
				}
				if(obj_name==null || obj_name.equalsIgnoreCase("null")) {
					stmt.setNull(3, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(3,obj_name);
				}
				if(obj_desc==null || obj_desc.equalsIgnoreCase("null")) {
					stmt.setNull(4, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(4,obj_desc);
				}
				if(obj_level==null) {
					stmt.setNull(5, java.sql.Types.INTEGER);
				}
				else {
					stmt.setInt(5,Integer.valueOf(obj_level).intValue());
				}
				if(conditional==null || conditional.equalsIgnoreCase("null")) {
					stmt.setNull(6, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(6,conditional);
				}
				if(access==null || access.equalsIgnoreCase("null")) {
					stmt.setNull(7, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(7,access);
				}
				if(defaultVaue==null || defaultVaue.equalsIgnoreCase("null")) {
					stmt.setNull(8, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(8,defaultVaue);
				}
				if(nickname==null || nickname.equalsIgnoreCase("null")) {
					stmt.setNull(9, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(9,nickname);
				}
				if(hak_akses==null || hak_akses.equalsIgnoreCase("null")) {
					stmt.setNull(10, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(10,hak_akses);
				}
				if(scope_kampus==null || scope_kampus.equalsIgnoreCase("null")) {
					stmt.setNull(11, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(11,scope_kampus);
				}
				if(domain_kampus==null || domain_kampus.equalsIgnoreCase("null")) {
					stmt.setNull(12, java.sql.Types.VARCHAR);
				}
				else {
					stmt.setString(12,domain_kampus);
				}
				System.out.println("insert id obj:"+id_obj+" = "+stmt.executeUpdate());
			}
			System.out.println("done");
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void syncKrklmExtCivitas() {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		try {
			
			connectToMysql();
			stmt = con.prepareStatement("select * from CIVITAS");
			rs = stmt.executeQuery();
			while(rs.next()) {
				String kdpst = rs.getString("KDPSTMSMHS");
				String npmhs = rs.getString("NPMHSMSMHS");
				String smawl = rs.getString("SMAWLMSMHS");
				li.add(kdpst+","+npmhs+","+smawl);
			}
			System.out.println("vSize="+v.size());
			
			stmt = con.prepareStatement("select * from KRKLM where KDPSTKRKLM=? and (TARGTKRKLM like ? or TARGTKRKLM like ?)");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,",");
				String kdpst = st.nextToken();
				String npmhs = st.nextToken();
				String smawl = st.nextToken();
				stmt.setString(1, kdpst);
				stmt.setString(2, "%"+npmhs+"%");
				stmt.setString(3, "%"+smawl+"%");
				rs = stmt.executeQuery();
				if(rs.next()) {
					String idKur = ""+rs.getLong("IDKURKRKLM");
					li.set(brs+","+idKur);
				}
				else {
					li.remove();
				}
			}
			System.out.println("vSize="+v.size());
			stmt = con.prepareStatement("update EXT_CIVITAS set KRKLMMSMHS=? where KDPSTMSMHS=? and NPMHSMSMHS=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,",");
				String kdpst = st.nextToken();
				String npmhs = st.nextToken();
				String smawl = st.nextToken();
				String idkur = st.nextToken();
				stmt.setString(1, idkur);
				stmt.setString(2, kdpst);
				stmt.setString(3, npmhs);
				System.out.println("updaate "+npmhs+" "+kdpst+" "+idkur+" = "+stmt.executeUpdate());
			}	
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void migrasiCalenderBukaKelasBackUpToUSG() {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		//``.`ID`,
		//`CALENDAR_BUKA_KELAS`.`ID_CALENDAR`,
		//`CALENDAR_BUKA_KELAS`.`KDPST`,
		//`CALENDAR_BUKA_KELAS`.`ALLOW_REQ_KELAS`
		
		try {
			connectToBackUp();
			stmt = con.prepareStatement("select * from CALENDAR_BUKA_KELAS order by ID");
			rs = stmt.executeQuery();
			while(rs.next()) {
				String id= ""+rs.getLong("ID");
				String idCalender= ""+rs.getLong("ID_CALENDAR");
				String kdpst=""+rs.getString("KDPST");
				String allowBukaKelas=""+rs.getBoolean("ALLOW_REQ_KELAS");
				li.add(id);
				li.add(idCalender);
				li.add(kdpst);
				li.add(allowBukaKelas);
			}
			
			//update
			Vector v1 = new Vector();
			ListIterator li1 = v1.listIterator();
			li = v.listIterator();
			while(li.hasNext()) {
				connectToMysql();
				stmt = con.prepareStatement("update CALENDAR_BUKA_KELAS set ID_CALENDAR=?,KDPST=?,ALLOW_REQ_KELAS=? where ID=?");
				String id = (String)li.next();
				String idCalender = (String)li.next();
				String kdpst = (String)li.next();
				String allowBukaKelas = (String)li.next();
				
				stmt.setLong(1, Long.valueOf(idCalender).longValue());
				stmt.setString(2,kdpst);
				stmt.setBoolean(3,Boolean.valueOf(allowBukaKelas).booleanValue());
				stmt.setLong(4, Long.valueOf(id).longValue());
				int i=0;
				i = stmt.executeUpdate();
				System.out.println("updateing "+id+","+idCalender+"="+i);
				if(i<1) {
					li1.add(id+"||"+idCalender+"||"+kdpst+"||"+allowBukaKelas);
				}
			}	
				
			//insert
			stmt = con.prepareStatement("insert into CALENDAR_BUKA_KELAS(ID_CALENDAR,KDPST,ALLOW_REQ_KELAS)values(?,?,?)");
			li1 = v1.listIterator();
			while(li1.hasNext()) {
				String brs = (String)li1.next();
				StringTokenizer st = new StringTokenizer(brs,"||");
				String id = st.nextToken();
				String idCalender = st.nextToken();
				String kdpst = st.nextToken();
				String allowBukaKelas = st.nextToken();
				stmt.setLong(1, Long.valueOf(idCalender).longValue());
				stmt.setString(2, kdpst);
				stmt.setBoolean(3, Boolean.valueOf(allowBukaKelas).booleanValue());
				int i = 0;
				i = stmt.executeUpdate();
				System.out.println("inserting "+id+","+idCalender+"="+i);
			}
			System.out.println("done");
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void tester() {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		//``.`ID`,
		//`CALENDAR_BUKA_KELAS`.`ID_CALENDAR`,
		//`CALENDAR_BUKA_KELAS`.`KDPST`,
		//`CALENDAR_BUKA_KELAS`.`ALLOW_REQ_KELAS`
		
		try {
			connectToMysql();
			stmt = con.prepareStatement("SELECT * FROM MSDOS where KDPST_HOMEBASE=? or TKN_KDPST_TEACH like ?");
    		stmt.setString(1,"61101");
    		stmt.setString(2,"%88888%");
			rs =stmt.executeQuery();
			while(rs.next()) {
				String nodos = rs.getString("NODOS");
				System.out.println(nodos);
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}	
	}		
	
	public static void migrasiClassPoolRuleBackUpToUSG() {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		//``.`ID`,
		//`CALENDAR_BUKA_KELAS`.`ID_CALENDAR`,
		//`CALENDAR_BUKA_KELAS`.`KDPST`,
		//`CALENDAR_BUKA_KELAS`.`ALLOW_REQ_KELAS`
		
		try {
			connectToBackUp();
			stmt = con.prepareStatement("select * from CLASS_POOL_RULES");
			rs = stmt.executeQuery();
			while(rs.next()) {
				String thsms= ""+rs.getString("THSMS");
				String kdpst= ""+rs.getString("KDPST");
				String rules=""+rs.getString("TKN_VERIFICATOR");
				
				li.add(thsms);
				li.add(kdpst);
				li.add(rules);
				//li.add(rules);
			}
			
			//delete prev
		
			connectToMysql();
			stmt = con.prepareStatement("delete from CLASS_POOL_RULES");
			stmt.executeUpdate();
				
			//insert
			stmt = con.prepareStatement("insert into CLASS_POOL_RULES(THSMS,KDPST,TKN_VERIFICATOR)values(?,?,?)");
			li = v.listIterator();
			while(li.hasNext()) {
				String thsms = (String)li.next();
				String kdpst = (String)li.next();
				String rules = (String)li.next();
				stmt.setString(1, thsms);
				stmt.setString(2, kdpst);
				stmt.setString(3, rules);
				int i = 0;
				i = stmt.executeUpdate();
				//System.out.println("inserting "+id+","+idCalender+"="+i);
			}
			System.out.println("done");
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	public static void updateAndSyncTrlsmWithCivitas() {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		Vector v1 = new Vector();
		ListIterator li1 = v1.listIterator();
		Vector ve = new Vector();
		ListIterator lie = ve.listIterator();
		System.out.println("start");
		Vector vf = new Vector();
		ListIterator lif = v.listIterator();
		try {
			connectToMysql();
			
			stmt = con.prepareStatement("delete FROM TRLSM");
			stmt.executeUpdate();
			
			vf = bacaFileTxt("dataTrlsmUpd");
			lif = vf.listIterator();
			stmt = con.prepareStatement("insert into TRLSM (THSMS,KDPST,NPMHS,STMHS) values(?,?,?,?)");
		}
		catch(Exception e) {
			System.out.println(e);
		}
			//stmt = con.prepareStatement("select * from CIVITAS where NIMHSMSMHS=?");
			while(lif.hasNext()) {
				String brs = (String)lif.next();
				StringTokenizer st = new StringTokenizer(brs,"$");
				String thsms = st.nextToken();
				if(!thsms.equalsIgnoreCase("20131")) {
					thsms = "0";
				}
				String kdpst = st.nextToken();
				String nimhs = st.nextToken();
				String stmhs = st.nextToken();
				String npmhs = st.nextToken();
				try {
				stmt.setString(1, thsms);
				stmt.setString(2, kdpst);
				stmt.setString(3, npmhs);
				stmt.setString(4, stmhs);
				
				System.out.println("insert="+stmt.executeUpdate());
				}
				catch(Exception e) {
					System.out.println(e);
				}	
				
				
				
			}
			//update
			lif = vf.listIterator();
			try {
			stmt = con.prepareStatement("update TRLSM set STMHS=? where NPMHS=? and THSMS=?");
			}
			catch(Exception e) {
				System.out.println(e);
			}
			//stmt = con.prepareStatement("select * from CIVITAS where NIMHSMSMHS=?");
			while(lif.hasNext()) {
				String brs = (String)lif.next();
				StringTokenizer st = new StringTokenizer(brs,"$");
				String thsms = st.nextToken();
				if(!thsms.equalsIgnoreCase("20131")) {
					thsms = "0";
				}
				String kdpst = st.nextToken();
				String nimhs = st.nextToken();
				String stmhs = st.nextToken();
				String npmhs = st.nextToken();
				try {
				stmt.setString(1, stmhs);
				stmt.setString(2, npmhs);
				stmt.setString(3, thsms);
				System.out.println("update="+stmt.executeUpdate());
				}
				catch(Exception e) {
					System.out.println(e);
				}
				
			}	
				
			
			//update
			lif = vf.listIterator();
			try {
			stmt = con.prepareStatement("update CIVITAS set STMHSMSMHS=? where NPMHSMSMHS=?");
			}
			catch(Exception e) {
				System.out.println(e);
			}
			//stmt = con.prepareStatement("select * from CIVITAS where NIMHSMSMHS=?");
			while(lif.hasNext()) {
				String brs = (String)lif.next();
				StringTokenizer st = new StringTokenizer(brs,"$");
				String thsms = st.nextToken();
				if(!thsms.equalsIgnoreCase("20131")) {
					thsms = "0";
				}
				String kdpst = st.nextToken();
				String nimhs = st.nextToken();
				String stmhs = st.nextToken();
				String npmhs = st.nextToken();
				try {
				stmt.setString(1, stmhs);
				stmt.setString(2, npmhs);
				//stmt.setString(3, thsms);
				System.out.println("update civitas="+stmt.executeUpdate());
				}
				catch(Exception e) {
					System.out.println(e);
				}
				
			}	
			/*
			writeFile("dataTrlsmUpd",vf);
			System.out.println("done");
			
			connectToMysql();
			stmt = con.prepareStatement("SELECT * FROM CIVITAS");
    		
			rs =stmt.executeQuery();
			while(rs.next()) {
				String kdpst = rs.getString("KDPSTMSMHS");
				String npmhs = rs.getString("NPMHSMSMHS");
				String stmhs = rs.getString("STMHSMSMHS");
				li.add(kdpst+"$"+npmhs+"$"+stmhs);
				//li.add(stmhs);
			}
			
			//stmt = con.prepareStatement("update TRLSM set STMHSTRLSM=? where NPMHSTRLSM=?");
			stmt = con.prepareStatement("select * from TRLSM where NPMHSTRLSM=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"$");
				String kdpst = st.nextToken();
				String npmhs = st.nextToken();
				String stmhs = st.nextToken();
				stmt.setString(1, npmhs);
				rs = stmt.executeQuery();
				if(rs.next()) {
					if(!stmhs.equalsIgnoreCase("L")) {	
						lie.add(kdpst+"$"+npmhs+"$"+stmhs);
					}
					li.remove();
				}
				
			}
			
			stmt = con.prepareStatement("insert into TRLSM (THSMSTRLSM,KDPSTTRLSM,NPMHSTRLSM,STMHSTRLSM) values(?,?,?,?)");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"$");
				String kdpst = st.nextToken();
				String npmhs = st.nextToken();
				String stmhs = st.nextToken();
				if(Checker.isStringNullOrEmpty(stmhs)) {
					stmhs = "N";
				}
				stmt.setString(1,"20132");
				stmt.setString(2,kdpst);
				stmt.setString(3,npmhs);
				stmt.setString(4,stmhs);
				System.out.println("insert "+npmhs+" = "+stmt.executeUpdate());
			}
			*/
		

	}	
	
	public static Vector getListKdpstProdi() {
		Vector v = new Vector();
		try {
			connectToMysql();
			
			stmt = con.prepareStatement("select distinct KDPST from DAFTAR_ULANG");
			rs = stmt.executeQuery();
			
			ListIterator li = v.listIterator();
			while(rs.next()) {
				String kdpst = rs.getString("KDPST");
				li.add(kdpst);
			}
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return v;
	}
	
	
	public static void updateTableDaftarUlangRules(String thsms, String rules) {
		Vector vProdi = getListKdpstProdi();
		ListIterator li = vProdi.listIterator();
		try {
			connectToMysql();
			
			stmt = con.prepareStatement("update DAFTAR_ULANG_RULES set TKN_VERIFICATOR=? where THSMS=? and KDPST=?");
			
			//update part
			while(li.hasNext()) {
				String kdpst = (String)li.next();
				stmt.setString(1, rules);
				stmt.setString(2, thsms);
				stmt.setString(3, kdpst);
				int i = stmt.executeUpdate();
				if(i>0) {
					li.remove();
				}
			}
			//insert part
			stmt = con.prepareStatement("insert into DAFTAR_ULANG_RULES(THSMS,KDPST,TKN_VERIFICATOR)values(?,?,?)");
			li = vProdi.listIterator();
			while(li.hasNext()) {
				String kdpst = (String)li.next();
				stmt.setString(1, thsms);
				stmt.setString(2, kdpst);
				stmt.setString(3, rules);
				int i = stmt.executeUpdate();
				
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static Vector getListOfCmd() {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		try {
			connectToMysql();
			
			stmt = con.prepareStatement("select * from OBJECT");
			rs = stmt.executeQuery();
			while(rs.next()) {
				String cmd = rs.getString("ACCESS_LEVEL");
				String id = ""+rs.getInt("ID_OBJ");
			
				if(cmd!=null) {
					StringTokenizer st = new StringTokenizer(cmd,"#");
					while(st.hasMoreTokens()) {
						String comm = st.nextToken();
				
						li.add(comm.replace("!", ""));
					}
				}
			}
			v = hapusDuplicateRecordFromVector(v);
			Collections.sort(v);
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return v;
	}
	
	public static void updateCmdTable() {
		try {
			Vector v = getListOfCmd();
			System.out.println("command size ="+v.size());
			ListIterator li = v.listIterator();
			stmt = con.prepareStatement("update COMANDO set CMD_NAME=? where CMD_NAME=?");
			int k=0;
			while(li.hasNext()) {
				k++;
				String cmd = (String) li.next();
				System.out.println(k+"."+cmd);
				stmt.setString(1, cmd);
				stmt.setString(2, cmd);
				int i = stmt.executeUpdate();
				if(i>0) {
					li.remove();
				}
			}
			li = v.listIterator();
			stmt = con.prepareStatement("INSERT INTO COMANDO (CMD_NAME)VALUES(?)");
			while(li.hasNext()) {
				String cmd = (String) li.next();
				stmt.setString(1, cmd);
				int i = stmt.executeUpdate();
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateKrsTxtFile(String kdpst) {
		Vector v = null;
		try {
			connectToMysql();
			stmt = con.prepareStatement("select * from MAKUL where KDPSTMAKUL=? and KDKMK_AT_PDPT like ?");
			v = bacaFileTxt("all_krs_electro");
			ListIterator li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"|");
				String thsms = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken();
				String bobot = st.nextToken();
				stmt.setString(1, kdpst);
				stmt.setString(2, "%"+kdkmk+"%");
				rs = stmt.executeQuery();
				String myKdkmk = "null";
				int skstt = 0;
				if(rs.next()) {
					myKdkmk = ""+rs.getString("KDKMKMAKUL");
					int skstm = rs.getInt("SKSTMMAKUL");
					int skspr = rs.getInt("SKSPRMAKUL");
					int skslp = rs.getInt("SKSLPMAKUL");
					skstt = skstm+skspr+skslp;
				}
				brs = brs +"|"+myKdkmk+"|"+skstt;
				li.set(brs);
				//System.out.println(brs);
			}
			//writeFile("nu_krs_elektro",v);
			System.out.println("get npm");
			System.out.println("----------------------");
			stmt = con.prepareStatement("select * from CIVITAS where NIMHSMSMHS=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"|");
				String thsms = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken(); //bisa null
				String bobot = st.nextToken();
				String nuKdkmk = st.nextToken();
				String nuSks = st.nextToken();
				stmt.setString(1, nimhs);
				rs = stmt.executeQuery();
				if(rs.next()) {
					String npmhs = rs.getString("NPMHSMSMHS");
					li.set(brs+"|"+npmhs);
					//System.out.println(nimhs);
				}
			}
			System.out.println("done");
			
			
			int j = 0;
			stmt = con.prepareStatement("insert into TRNLM (THSMSTRNLM,KDPTITRNLM,KDJENTRNLM,KDPSTTRNLM,NPMHSTRNLM,KDKMKTRNLM,NILAITRNLM,NLAKHTRNLM,BOBOTTRNLM,SKSMKTRNLM)values(?,?,?,?,?,?,?,?,?,?)");
			li = v.listIterator();
			while(li.hasNext()) {
				j++;
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"|");
				String thsms = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken(); //bisa null
				if(!nlakh.equalsIgnoreCase("A")&&!nlakh.equalsIgnoreCase("B")&&!nlakh.equalsIgnoreCase("C")&&!nlakh.equalsIgnoreCase("D")&&!nlakh.equalsIgnoreCase("E")) {
					nlakh = "T";
				}
				String bobot = st.nextToken();
				if(nlakh.equalsIgnoreCase("A")) {
					bobot = "4";
				}
				else if(nlakh.equalsIgnoreCase("B")) {
					bobot = "3";
				}
				else if(nlakh.equalsIgnoreCase("C")) {
					bobot = "2";
				}
				else if(nlakh.equalsIgnoreCase("D")) {
					bobot = "1";
				}
				else if(nlakh.equalsIgnoreCase("E")) {
					bobot = "0";
				}
				String nuKdkmk = st.nextToken();
				String nuSks = st.nextToken();
				String npmhs = st.nextToken();
				
				//THSMSTRNLM,
				stmt.setString(1, thsms);
				//KDPTITRNLM,
				stmt.setString(2, "031031");
				//KDJENTRNLM,
				stmt.setString(3, "C");
				//KDPSTTRNLM,
				stmt.setString(4, "20201");
				//NPMHSTRNLM,
				stmt.setString(5, npmhs);
				//KDKMKTRNLM,
				stmt.setString(6, nuKdkmk);
				//NILAITRNLM,
				stmt.setInt(7, 0);
				//NLAKHTRNLM,
				stmt.setString(8, nlakh);
				//BOBOTTRNLM,
				stmt.setDouble(9, Double.parseDouble(bobot));
				//SKSMKTRNLM
				stmt.setInt(10, Integer.parseInt(nuSks));
				int i = stmt.executeUpdate();
				if(i<1) {
					System.out.println(brs);
				}
				else {
					System.out.print(j+"&");
				}
			}
			System.out.println("selesai");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateKrsPindahanTxtFile(String kdpst) {
		Vector v = null;
		ListIterator li = null;
		try {
			connectToMysql();
			stmt = con.prepareStatement("select * from MAKUL where KDPSTMAKUL=? and KDKMK_AT_PDPT like ?");
			v = bacaFileTxt("krs_trnlp_elektro");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"#");
				//String thsms = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken();
				String bobot = st.nextToken();
				stmt.setString(1, kdpst);
				stmt.setString(2, "%"+kdkmk+"%");
				rs = stmt.executeQuery();
				String myKdkmk = "null";
				int skstt = 0;
				if(rs.next()) {
					myKdkmk = ""+rs.getString("KDKMKMAKUL");
					int skstm = rs.getInt("SKSTMMAKUL");
					int skspr = rs.getInt("SKSPRMAKUL");
					int skslp = rs.getInt("SKSLPMAKUL");
					skstt = skstm+skspr+skslp;
				}
				brs = brs +"#"+myKdkmk+"#"+skstt;
				li.set(brs);
				//System.out.println(brs);
			}
			//writeFile("nu_krs_elektro",v);
			System.out.println("get npm");
			System.out.println("----------------------");
			stmt = con.prepareStatement("select * from CIVITAS where NIMHSMSMHS=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"#");
				//String thsms = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken(); //bisa null
				String bobot = st.nextToken();
				String nuKdkmk = st.nextToken();
				String nuSks = st.nextToken();
				stmt.setString(1, nimhs);
				rs = stmt.executeQuery();
				if(rs.next()) {
					String npmhs = rs.getString("NPMHSMSMHS");
					li.set(brs+"#"+npmhs);
					System.out.println(nimhs);
				}
			}
			System.out.println("done");
			
			
			
			stmt = con.prepareStatement("insert into TRNLP (THSMSTRNLP,KDPSTTRNLP,NPMHSTRNLP,KDKMKTRNLP,NLAKHTRNLP,BOBOTTRNLP,SKSMKTRNLP,KDKMKASALP,TRANSFERRED)values(?,?,?,?,?,?,?,?,?)");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
			int j = 0;
			int i = 0;
			li = v.listIterator();
			while(li.hasNext()) {
				try {
				j++;
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"#");
				//String thsms = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken(); //bisa null
				if(!nlakh.equalsIgnoreCase("A")&&!nlakh.equalsIgnoreCase("B")&&!nlakh.equalsIgnoreCase("C")&&!nlakh.equalsIgnoreCase("D")&&!nlakh.equalsIgnoreCase("E")) {
					nlakh = "T";
				}
				String bobot = st.nextToken();
				if(nlakh.equalsIgnoreCase("A")) {
					bobot = "4";
				}
				else if(nlakh.equalsIgnoreCase("B")) {
					bobot = "3";
				}
				else if(nlakh.equalsIgnoreCase("C")) {
					bobot = "2";
				}
				else if(nlakh.equalsIgnoreCase("D")) {
					bobot = "1";
				}
				else if(nlakh.equalsIgnoreCase("E")) {
					bobot = "0";
				}
				String nuKdkmk = st.nextToken();
				String nuSks = st.nextToken();
				String npmhs = st.nextToken();
				//THSMSTRNLP,KDPSTTRNLP,NPMHSTRNLP,KDKMKTRNLP,NLAKHTRNLP,BOBOTTRNLP,SKSMKTRNLP,TRANSFERRED
				//THSMSTRNLM,
				stmt.setString(1, "00000");
				//KDPSTTRNLM,
				stmt.setString(2, "20201");
				//NPMHSTRNLM,
				stmt.setString(3, npmhs);
				//KDKMKTRNLM,
				stmt.setString(4, nuKdkmk);
				//NLAKHTRNLM,
				stmt.setString(5, nlakh);
				//BOBOTTRNLM,
				stmt.setDouble(6, Double.parseDouble(bobot));
				//SKSMKTRNLM
				stmt.setInt(7, Integer.parseInt(nuSks));
				stmt.setString(8,kdkmk);
				stmt.setBoolean(9, true);
				
					i=0;
				i = stmt.executeUpdate();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				if(i<1) {
					System.out.println("err&");
				}
				else {
					System.out.print(j+"&");
				}
			}
			
			System.out.println("selesai");
		

	}
	
	public static void updateTrakmTxtFile(String kdpst) {
		Vector v = null;
		ListIterator li = null;
		try {
			connectToMysql();
			stmt = con.prepareStatement("select * from MAKUL where KDPSTMAKUL=? and KDKMK_AT_PDPT like ?");
			v = bacaFileTxt("all_krs_electro");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"|");
				String thsms = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken();
				String bobot = st.nextToken();
				stmt.setString(1, kdpst);
				stmt.setString(2, "%"+kdkmk+"%");
				rs = stmt.executeQuery();
				String myKdkmk = "null";
				int skstt = 0;
				if(rs.next()) {
					myKdkmk = ""+rs.getString("KDKMKMAKUL");
					int skstm = rs.getInt("SKSTMMAKUL");
					int skspr = rs.getInt("SKSPRMAKUL");
					int skslp = rs.getInt("SKSLPMAKUL");
					skstt = skstm+skspr+skslp;
				}
				brs = brs +"|"+myKdkmk+"|"+skstt;
				li.set(brs);
				//System.out.println(brs);
			}
			//writeFile("nu_krs_elektro",v);
			System.out.println("get npm");
			System.out.println("----------------------");
			stmt = con.prepareStatement("select * from CIVITAS where NIMHSMSMHS=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"|");
				String thsms = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken(); //bisa null
				String bobot = st.nextToken();
				String nuKdkmk = st.nextToken();
				String nuSks = st.nextToken();
				stmt.setString(1, nimhs);
				rs = stmt.executeQuery();
				if(rs.next()) {
					String npmhs = rs.getString("NPMHSMSMHS");
					li.set(brs+"|"+npmhs);
					//System.out.println(nimhs);
				}
			}
			System.out.println("done");
		}
		catch(Exception e) {
			e.printStackTrace();
		}	

		try {	
			
			stmt = con.prepareStatement("insert into TRAKM (THSMSTRAKM,KDPTITRAKM,KDJENTRAKM,KDPSTTRAKM,NPMHSTRAKM,SKSEMTRAKM,NLIPSTRAKM,SKSTTTRAKM,NLIPKTRAKM)values(?,?,?,?,?,?,?,?,?)");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
			int j = 0;
			li = v.listIterator();
			while(li.hasNext()) {
				try {
				j++;
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"|");
				String thsms = st.nextToken();
				String nimhs = st.nextToken();
				String kdkmk = st.nextToken();
				String nlakh = st.nextToken(); //bisa null
				if(!nlakh.equalsIgnoreCase("A")&&!nlakh.equalsIgnoreCase("B")&&!nlakh.equalsIgnoreCase("C")&&!nlakh.equalsIgnoreCase("D")&&!nlakh.equalsIgnoreCase("E")) {
					nlakh = "T";
				}
				String bobot = st.nextToken();
				if(nlakh.equalsIgnoreCase("A")) {
					bobot = "4";
				}
				else if(nlakh.equalsIgnoreCase("B")) {
					bobot = "3";
				}
				else if(nlakh.equalsIgnoreCase("C")) {
					bobot = "2";
				}
				else if(nlakh.equalsIgnoreCase("D")) {
					bobot = "1";
				}
				else if(nlakh.equalsIgnoreCase("E")) {
					bobot = "0";
				}
				String nuKdkmk = st.nextToken();
				String nuSks = st.nextToken();
				String npmhs = st.nextToken();
				//NPMHSTRAKM,SKSEMTRAKM,NLIPSTRAKM,SKSTTTRAKM,NLIPKTRAKM)
				//THSMSTRNLM,
				stmt.setString(1, thsms);
				//KDPTITRNLM,
				stmt.setString(2, "031031");
				//KDJENTRNLM,
				stmt.setString(3, "C");
				//KDPSTTRNLM,
				stmt.setString(4, "20201");
				//NPMHSTRNLM,
				stmt.setString(5, npmhs);
				//SKSEMTRAKM,
				stmt.setInt(6, 0);
				//NLIPSTRAKM,
				stmt.setDouble(7, 0);
				//SKSTTTRAKM
				stmt.setInt(8, 0);
				//,NLIPKTRAKM)
				stmt.setDouble(9, 0);
				int i = stmt.executeUpdate();
				if(i<1) {
					System.out.println(brs);
				}
				else {
					System.out.print(j+"&");
				}
				}
				catch(Exception e) {
					System.out.println("er&");
				}
			}
			System.out.println("selesai");
	} 	
	
	public static void cekAktif(String kdpst, String thsms) {
		Vector v = new Vector();
		ListIterator li = null;
		try {
			li = v.listIterator();
			connectToMysql();
			stmt = con.prepareStatement("select distinct NPMHSTRNLM from TRNLM where KDPSTTRNLM=? and THSMSTRNLM=?");
			stmt.setString(1, kdpst);
			stmt.setString(2, thsms);
			rs = stmt.executeQuery();
			while(rs.next()) {
				String npmhs = ""+rs.getString("NPMHSTRNLM");
				li.add(npmhs);
				//System.out.println(""+npmhs);
			}
			stmt = con.prepareStatement("update CIVITAS set STMHSMSMHS=? where KDPSTMSMHS=? and NPMHSMSMHS=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String npm= (String)li.next();
				stmt.setString(1, "A");
				stmt.setString(2, kdpst);
				stmt.setString(3, npm);
				int i = stmt.executeUpdate();
				System.out.println(npm+"="+i);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateStatusPindahan(String kdpst) {
		Vector v = new Vector();
		ListIterator li = null;
		try {
			li = v.listIterator();
			connectToMysql();
			stmt = con.prepareStatement("select distinct NPMHSTRNLP from TRNLP where KDPSTTRNLP=?");
			stmt.setString(1, kdpst);
			rs = stmt.executeQuery();
			while(rs.next()) {
				String npmhs = ""+rs.getString("NPMHSTRNLP");
				li.add(npmhs);
				//System.out.println(""+npmhs);
			}
			stmt = con.prepareStatement("update CIVITAS set STPIDMSMHS=? where KDPSTMSMHS=? and NPMHSMSMHS=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String npm= (String)li.next();
				stmt.setString(1, "P");
				stmt.setString(2, kdpst);
				stmt.setString(3, npm);
				int i = stmt.executeUpdate();
				System.out.println(npm+"="+i);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setTrlsm(String kdpst) {
		Vector v = new Vector();
		ListIterator li = null;
		
		try {
			v = bacaFileTxt("data_trlsm_elektro");
			connectToMysql();
			stmt = con.prepareStatement("update CIVITAS set STMHSMSMHS=? where KDPSTMSMHS=? and NIMHSMSMHS=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"$");
				String thsms = st.nextToken();
				st.nextToken();//ignore kdpst
				String nimhs = st.nextToken();
				String stmhs = st.nextToken();
				stmt.setString(1, stmhs);
				stmt.setString(2, kdpst);
				stmt.setString(3, nimhs);
				int i = stmt.executeUpdate();
				if(i>0) {
					System.out.print(nimhs+"&");
				}
				else {
					System.out.println(nimhs+"-error");
				}
			}
			connectToMysql();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setShift(String kdpst) {
		Vector v = new Vector();
		ListIterator li = null;
		
		try {
			v = bacaFileTxt("sore");
			connectToMysql();
			stmt = con.prepareStatement("update CIVITAS set SHIFTMSMHS=? where KDPSTMSMHS=? and NIMHSMSMHS=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String nimhs = (String)li.next();
				stmt.setString(1, "REGULER MALAM");
				stmt.setString(2, kdpst);
				stmt.setString(3, nimhs);
				int i = stmt.executeUpdate();
				if(i>0) {
					System.out.print(nimhs+"&");
				}
				else {
					System.out.println(nimhs+"-error");
				}
			}
			//connectToMysql();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}	
	
	public static void tmp() {
		Vector v = new Vector();
		ListIterator li = null;
		li = v.listIterator();
		try {

			connectToMysql();
			stmt = con.prepareStatement("select * from DAFTAR_ULANG");
			rs = stmt.executeQuery();
			while(rs.next()) {
				li.add(rs.getString("KDPST"));
				li.add(rs.getString("NPMHS"));
			}
			System.out.println("vsize = "+v.size());
			stmt = con.prepareStatement("select * from CIVITAS where KDPSTMSMHS=? and NPMHSMSMHS=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String kdpst = (String)li.next();
				String npmhs = (String)li.next();
				stmt.setString(1, kdpst);
				stmt.setString(2, npmhs);
				rs = stmt.executeQuery();
				if(!rs.next()) {
					System.out.println(kdpst+" "+npmhs);
				}
			}
			//connectToMysql();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}	
	
	public static void migrasiTabelCommandBackUpToUSG() {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		
		try {
			connectToBackUp();
			stmt = con.prepareStatement("select * from TABEL_COMMAND");
			rs = stmt.executeQuery();
			while(rs.next()) {
				String cmd_kode=""+rs.getString("CMD_CODE");
				String cmd_keter=""+rs.getString("CMD_KETER");
				String used_by=""+rs.getString("USE_BY");
				String cmd_dependency=""+rs.getString("CMD_DEPENDENCY");
				String pilihan_value=""+rs.getString("PILIHAN_VALUE");
				li.add(cmd_kode);
				li.add(cmd_keter);
				li.add(used_by);
				li.add(cmd_dependency);
				li.add(pilihan_value);
			}
			
			connectToMysql();
			if(v!=null && v.size()>0) {
				System.out.println("v size before update = "+v.size());
				stmt = con.prepareStatement("update TABEL_COMMAND set CMD_KETER=?,USE_BY=?,CMD_DEPENDENCY=?,PILIHAN_VALUE=? where CMD_CODE=?");
				li = v.listIterator();
				while(li.hasNext()) {
					String cmd_kode = (String)li.next();
					String cmd_keter = (String)li.next();
					String used_by = (String)li.next();
					String cmd_dependency = (String)li.next();
					String pilihan_value = (String)li.next();
					int i = 0;
					if(Checker.isStringNullOrEmpty(cmd_keter)) {
						stmt.setNull(++i, java.sql.Types.VARCHAR);
					}
					else {
						stmt.setString(++i, cmd_keter);
					}
					
					if(Checker.isStringNullOrEmpty(used_by)) {
						stmt.setNull(++i, java.sql.Types.VARCHAR);
					}
					else {
						stmt.setString(++i, used_by);
					}
					
					if(Checker.isStringNullOrEmpty(cmd_dependency)) {
						stmt.setNull(++i, java.sql.Types.VARCHAR);
					}
					else {
						stmt.setString(++i, cmd_dependency);
					}
					
					if(Checker.isStringNullOrEmpty(pilihan_value)) {
						stmt.setNull(++i, java.sql.Types.VARCHAR);
					}
					else {
						stmt.setString(++i, pilihan_value);
					}
					
					stmt.setString(++i, cmd_kode);
					int j = 0;
					j = stmt.executeUpdate();
					if(j>0) {
						li.remove();
						li.previous();
						li.remove();
						li.previous();
						li.remove();
						li.previous();
						li.remove();
						li.previous();
						li.remove();
						//li.previous();
						//li.remove();
					}
				}	
			}
			
				
			//insert
			
			if(v!=null && v.size()>0) {
				System.out.println("v size before insert = "+v.size());
				stmt = con.prepareStatement("INSERT INTO TABEL_COMMAND (CMD_CODE,CMD_KETER,USE_BY,CMD_DEPENDENCY,PILIHAN_VALUE)values(?,?,?,?,?)");
				li = v.listIterator();
				while(li.hasNext()) {
					String cmd_kode = (String)li.next();
					String cmd_keter = (String)li.next();
					String used_by = (String)li.next();
					String cmd_dependency = (String)li.next();
					String pilihan_value = (String)li.next();
					int i = 0;
					if(Checker.isStringNullOrEmpty(cmd_kode)) {
						stmt.setNull(++i, java.sql.Types.VARCHAR);
					}
					else {
						stmt.setString(++i, cmd_kode);
					}	
					
					if(Checker.isStringNullOrEmpty(cmd_keter)) {
						stmt.setNull(++i, java.sql.Types.VARCHAR);
					}
					else {
						stmt.setString(++i, cmd_keter);
					}	
					
					if(Checker.isStringNullOrEmpty(used_by)) {
						stmt.setNull(++i, java.sql.Types.VARCHAR);
					}
					else {
						stmt.setString(++i, used_by);
					}	
					
					if(Checker.isStringNullOrEmpty(cmd_dependency)) {
						stmt.setNull(++i, java.sql.Types.VARCHAR);
					}
					else {
						stmt.setString(++i, cmd_dependency);
					}	
					
					if(Checker.isStringNullOrEmpty(pilihan_value)) {
						stmt.setNull(++i, java.sql.Types.VARCHAR);
					}
					else {
						stmt.setString(++i, pilihan_value);
					}
					System.out.println(i+".cmd-code ="+cmd_kode);
					stmt.executeUpdate();
				}	

			}
			
			//update dulu
			
				
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	public static void syncTablePymntAndTransit() {
		Vector v = new Vector();
		ListIterator li = v.listIterator();

		Vector v1 = new Vector();
		ListIterator li1 = v1.listIterator();
		
		try {
			connectToMysql();
			//pymnt_transit tabel
			stmt = con.prepareStatement("select distinct NPMHSPYMNT  from PYMNT_TRANSIT");
			rs = stmt.executeQuery();
			while(rs.next()) {
				String npmhs=""+rs.getString("NPMHSPYMNT");
				li.add(npmhs);
			}
			if(v!=null && v.size()>0) {
				
				stmt = con.prepareStatement("select ID_OBJ from CIVITAS where NPMHSMSMHS=?");
				li = v.listIterator();
				while(li.hasNext()) {
					String npmhs = (String)li.next();
					stmt.setString(1,npmhs);
					rs = stmt.executeQuery();
					rs.next();
					String idObj = ""+rs.getLong("ID_OBJ");
					li1.add(npmhs);
					li1.add(idObj);
				}
				v = new Vector();
				if(v1!=null && v1.size()>0) {
					stmt = con.prepareStatement("select KODE_KAMPUS_DOMISILI from OBJECT where ID_OBJ=?");
					li = v.listIterator();
					li1 = v1.listIterator();
					while(li1.hasNext()) {
						String npmhs = (String)li1.next();
						String idObj = (String)li1.next();
						stmt.setLong(1, Long.parseLong(idObj));
						rs = stmt.executeQuery();
						rs.next();
						String kdKampus = ""+rs.getString("KODE_KAMPUS_DOMISILI");
						li.add(npmhs);
						li.add(idObj);
						li.add(kdKampus);
					}
				}
				
				if(v!=null && v.size()>0) {
					stmt = con.prepareStatement("update PYMNT_TRANSIT set ID_OBJ=?,KODE_KAMPUS_DOMISILI=? where NPMHSPYMNT=?");
					li = v.listIterator();
					while(li.hasNext()) {
						String npmhs = (String)li.next();
						String idObj = (String)li.next();
						String kampus = (String)li.next();
						stmt.setLong(1, Long.parseLong(idObj));
						stmt.setString(2, kampus);
						stmt.setString(3, npmhs);
						System.out.println(npmhs+" "+idObj+" "+kampus+" = "+stmt.executeUpdate());
					}
				}
				
			}
			
			//pymnt tabel
			v = new Vector();
			v1 = new Vector();
			li = v.listIterator();
			li1 = v1.listIterator();
			stmt = con.prepareStatement("select distinct NPMHSPYMNT  from PYMNT");
			rs = stmt.executeQuery();
			while(rs.next()) {
				String npmhs=""+rs.getString("NPMHSPYMNT");
				li.add(npmhs);
			}
			if(v!=null && v.size()>0) {
				
				stmt = con.prepareStatement("select ID_OBJ from CIVITAS where NPMHSMSMHS=?");
				li = v.listIterator();
				while(li.hasNext()) {
					String npmhs = (String)li.next();
					stmt.setString(1,npmhs);
					rs = stmt.executeQuery();
					rs.next();
					String idObj = ""+rs.getLong("ID_OBJ");
					li1.add(npmhs);
					li1.add(idObj);
				}
				v = new Vector();
				if(v1!=null && v1.size()>0) {
					stmt = con.prepareStatement("select KODE_KAMPUS_DOMISILI from OBJECT where ID_OBJ=?");
					li = v.listIterator();
					li1 = v1.listIterator();
					while(li1.hasNext()) {
						String npmhs = (String)li1.next();
						String idObj = (String)li1.next();
						stmt.setLong(1, Long.parseLong(idObj));
						rs = stmt.executeQuery();
						rs.next();
						String kdKampus = ""+rs.getString("KODE_KAMPUS_DOMISILI");
						li.add(npmhs);
						li.add(idObj);
						li.add(kdKampus);
					}
				}
				
				if(v!=null && v.size()>0) {
					stmt = con.prepareStatement("update PYMNT set ID_OBJ=?,KODE_KAMPUS_DOMISILI=? where NPMHSPYMNT=?");
					li = v.listIterator();
					while(li.hasNext()) {
						String npmhs = (String)li.next();
						String idObj = (String)li.next();
						String kampus = (String)li.next();
						stmt.setLong(1, Long.parseLong(idObj));
						stmt.setString(2, kampus);
						stmt.setString(3, npmhs);
						System.out.println(npmhs+" "+idObj+" "+kampus+" = "+stmt.executeUpdate());
					}
				}
				
			}
				
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	public static void temp() {
		Vector v = new Vector();
		ListIterator li = v.listIterator();

		Vector v1 = new Vector();
		ListIterator li1 = v1.listIterator();
		
		try {
			connectToMysql();
			//pymnt_transit tabel
			stmt = con.prepareStatement("select * from CUTI_RULES order by THSMS,KDPST");
			rs = stmt.executeQuery();
			while(rs.next()) {
				String thsms = ""+rs.getString("THSMS");
				String kdpst = ""+rs.getString("KDPST");
				String verif = ""+rs.getString("TKN_VERIFICATOR");
				String tkn_id = ""+rs.getString("TKN_VERIFICATOR_ID");
				String urutan = ""+rs.getBoolean("URUTAN");
				String kode = ""+rs.getString("KODE_KAMPUS");
				li.add(thsms+"`"+kdpst+"`"+verif+"`"+tkn_id+"`"+urutan+"`"+kode);
			}
			stmt = con.prepareStatement("insert into PINDAH_PRODI_RULES(THSMS,KDPST,TKN_VERIFICATOR,TKN_VERIFICATOR_ID,URUTAN,KODE_KAMPUS)values(?,?,?,?,?,?)");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"`");
				String thsms = st.nextToken();
				String kdpst = st.nextToken();
				String verif = st.nextToken();
				String tkn_id = st.nextToken();
				String urutan = st.nextToken();
				String kode = st.nextToken();
				stmt.setString(1, thsms);
				stmt.setString(2, kdpst);
				stmt.setString(3, verif);
				stmt.setString(4, tkn_id);
				stmt.setBoolean(5, Boolean.parseBoolean(urutan));
				stmt.setString(6, kode);
				stmt.executeUpdate();
			}
				
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[]args)throws Exception {
		migrasiTabelCommandBackUpToUSG();
		System.out.println("command doone");
		migrasiObjectBackUpToUSG();
		//temp();
		System.out.println("object doone");
		
		//syncTablePymntAndTransit();
		
		//tmp();/Table();
		//updateAndSyncTrlsmWithCivitas();
		//syncKrklmExtCivitas();
		//migrasiCalenderBukaKelasBackUpToUSG();
		//migrasiClassPoolRuleBackUpToUSG();//tester();																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		
		//updateTableDaftarUlangRules("20132","OPERATOR BAA,OPERATOR BAK");
		//updateKrsTxtFile("20201");
		//updateTrakmTxtFile("20201");
		//updateKrsPindahanTxtFile("20201");
		//updateStatusPindahan("20201");
		
		//setTrlsm("20201");;
		//System.out.println("doone");
		//cekAktif("20201", "20132");
		//System.out.println("doone");
		//setShift("20201");
		System.out.println("doone");
	}
	

}