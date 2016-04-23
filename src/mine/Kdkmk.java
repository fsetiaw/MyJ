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



class Kdkmk extends Maintenance {

	public static void getListKdkmkMysql() {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		try {
			connectToMysql();
			stmt = con.prepareStatement("select * from MAKUL order by KDPSTMAKUL,NAKMKMAKUL");
			rs = stmt.executeQuery();
			while(rs.next()){
				String kdpst = ""+rs.getString("KDPSTMAKUL");
				String kdkmk = ""+rs.getString("KDKMKMAKUL");
				String nakmk = ""+rs.getString("NAKMKMAKUL");
				String sksmk = ""+(rs.getLong("SKSTMMAKUL")+rs.getLong("SKSPRMAKUL")+rs.getLong("SKSLPMAKUL"));
				li.add(kdpst+"#"+kdkmk+"#"+nakmk+"#"+sksmk);
			}
			writeFile("list_makul_mysql",v);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	public static void tmp() {
		try {
			Vector v = new Vector();
			ListIterator li = v.listIterator();
			Vector v_epsbed = bacaFileTxt("list_makul_trnlp");
			Vector v_mysql = bacaFileTxt("list_makul_mysql");
			ListIterator lie = v_epsbed.listIterator();
			while(lie.hasNext()) {
				String brs = (String)lie.next();
				StringTokenizer st = new StringTokenizer(brs,"#");
				String kdpst = st.nextToken();
				String kdkmk = st.nextToken();
				String nakmk = st.nextToken();
				String sksmk = st.nextToken();
				ListIterator lim = v_mysql.listIterator();
				boolean match = false;
				while(lim.hasNext()&&!match) {
					String baris = (String)lim.next();
					st = new StringTokenizer(baris,"#");
					String mypst = st.nextToken();
					String mykmk = st.nextToken();
					String mynmk = st.nextToken();
					String mysks = st.nextToken();
					if(nakmk.equalsIgnoreCase(mynmk)&&kdpst.equalsIgnoreCase(mypst)&&(Double.valueOf(sksmk).doubleValue()==Double.valueOf(mysks).doubleValue())) {
						match = true;
						li.add(kdpst+"#"+kdkmk+"#"+nakmk+"#"+sksmk+"#"+mykmk);
					}
				}
				if(!match) {
					li.add(kdpst+"#"+kdkmk+"#"+nakmk+"#"+sksmk+"#?");
				}
			}
			writeFile("list_makul_filtered",v);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void txtTbkmkToMysqlPartI(String kdpst_target,String based_thsms) {
		//based thsms hanya digunakan utk nakmk - cari thsms yg komplit
		Vector v = new Vector();
		Vector v0 = null;
		ListIterator li = null;
		try {
			v = bacaFileTxt("dbfTbkmkToTxt");
			v0 = new Vector(v);
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"#");
				String thsms=st.nextToken();
				String kdpti=st.nextToken();
				String kdjen=st.nextToken();
				String kdpst=st.nextToken();
				String kdkmk=st.nextToken();
				String nakmk=st.nextToken();
				String sksmk=st.nextToken();
				String skstm=st.nextToken();
				String skspr=st.nextToken();
				String skslp=st.nextToken();
				String semes=st.nextToken();
				String kdwpl=st.nextToken();
				String kdkur=st.nextToken();
				String kdkel=st.nextToken();
				String nodos=st.nextToken();
				String jenja=st.nextToken();
				String prodi=st.nextToken();
				String stkmk=st.nextToken();
				String slbus=st.nextToken();
				String sappp=st.nextToken();
				String bhnaj=st.nextToken();
				String diktt=st.nextToken();
				String kduta=st.nextToken();
				String kdkug=st.nextToken();
				String kdlai=st.nextToken();
				String kdmpa=st.nextToken();
				String kdmpb=st.nextToken();
				String kdmpc=st.nextToken();
				String kdmpd=st.nextToken();
				String kdmpe=st.nextToken();
				String kdmpf=st.nextToken();
				String kdmpg=st.nextToken();
				String kdmph=st.nextToken();
				String kdmpi=st.nextToken();
				String kdmpj=st.nextToken();
				String crmkl=st.nextToken();
				String prstd=st.nextToken();
				String smgds=st.nextToken();
				String rpsim=st.nextToken();
				String csstu=st.nextToken();
				String disln=st.nextToken();
				String sdiln=st.nextToken();
				String codln=st.nextToken();
				String colln=st.nextToken();
				String ctxin=st.nextToken();
				String pjbln=st.nextToken();
				String pbbln=st.nextToken();
				String ujtls=st.nextToken();
				String tgmkl=st.nextToken();
				String tgmod=st.nextToken();
				String pstsi=st.nextToken();
				String simul=st.nextToken();
				String lainn=st.nextToken();
				String ujtl1=st.nextToken();
				String tgmk1=st.nextToken();
				String tgmo1=st.nextToken();
				String psts1=st.nextToken();
				String simu1=st.nextToken();
				String lain1=st.nextToken();
				if(!kdpst.equalsIgnoreCase(kdpst_target)) {
					li.remove();
				}
			}
			
			//filtered unik kdkmk
			Vector v1 = new Vector();
			ListIterator li1 = v1.listIterator();
			boolean first = true;
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"#");
				String thsms=st.nextToken();
				String kdpti=st.nextToken();
				String kdjen=st.nextToken();
				String kdpst=st.nextToken();
				String kdkmk=st.nextToken();
				String nakmk=st.nextToken();
				String sksmk=st.nextToken();
				String skstm=st.nextToken();
				String skspr=st.nextToken();
				String skslp=st.nextToken();
				String semes=st.nextToken();
				String kdwpl=st.nextToken();
				String kdkur=st.nextToken();
				String kdkel=st.nextToken();
				String nodos=st.nextToken();
				String jenja=st.nextToken();
				String prodi=st.nextToken();
				String stkmk=st.nextToken();
				String slbus=st.nextToken();
				String sappp=st.nextToken();
				String bhnaj=st.nextToken();
				String diktt=st.nextToken();
				String kduta=st.nextToken();
				String kdkug=st.nextToken();
				String kdlai=st.nextToken();
				String kdmpa=st.nextToken();
				String kdmpb=st.nextToken();
				String kdmpc=st.nextToken();
				String kdmpd=st.nextToken();
				String kdmpe=st.nextToken();
				String kdmpf=st.nextToken();
				String kdmpg=st.nextToken();
				String kdmph=st.nextToken();
				String kdmpi=st.nextToken();
				String kdmpj=st.nextToken();
				String crmkl=st.nextToken();
				String prstd=st.nextToken();
				String smgds=st.nextToken();
				String rpsim=st.nextToken();
				String csstu=st.nextToken();
				String disln=st.nextToken();
				String sdiln=st.nextToken();
				String codln=st.nextToken();
				String colln=st.nextToken();
				String ctxin=st.nextToken();
				String pjbln=st.nextToken();
				String pbbln=st.nextToken();
				String ujtls=st.nextToken();
				String tgmkl=st.nextToken();
				String tgmod=st.nextToken();
				String pstsi=st.nextToken();
				String simul=st.nextToken();
				String lainn=st.nextToken();
				String ujtl1=st.nextToken();
				String tgmk1=st.nextToken();
				String tgmo1=st.nextToken();
				String psts1=st.nextToken();
				String simu1=st.nextToken();
				String lain1=st.nextToken();
				if(first) {
					first = false;
					li1.add(kdkmk);
				}
				else {
					li1 = v1.listIterator();
					boolean match = false;
					while(li1.hasNext()&&!match) {
						String kmk = (String)li1.next();
						if(kdkmk.equalsIgnoreCase(kmk)) {
							match = true;
						}
					}
					if(match) {
						li.remove();
					}
					else {
						li1.add(kdkmk);
					}
				}
			}
			//cek apa nakmk komplit
			//writeFile("mk_to_migrate",v);
			//System.out.println("v0 size="+v0.size());
			Vector v2 = new Vector();
			ListIterator li2 = v2.listIterator();
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				//System.out.println(brs);
				StringTokenizer st = new StringTokenizer(brs,"#");
				String thsms=st.nextToken();
				String kdpti=st.nextToken();
				String kdjen=st.nextToken();
				String kdpst=st.nextToken();
				String kdkmk=st.nextToken();
				String nakmk=st.nextToken();
				String sksmk=st.nextToken();
				String skstm=st.nextToken();
				String skspr=st.nextToken();
				String skslp=st.nextToken();
				String semes=st.nextToken();
				String kdwpl=st.nextToken();
				String kdkur=st.nextToken();
				String kdkel=st.nextToken();
				String nodos=st.nextToken();
				String jenja=st.nextToken();
				String prodi=st.nextToken();
				String stkmk=st.nextToken();
				String slbus=st.nextToken();
				String sappp=st.nextToken();
				String bhnaj=st.nextToken();
				String diktt=st.nextToken();
				String kduta=st.nextToken();
				String kdkug=st.nextToken();
				String kdlai=st.nextToken();
				String kdmpa=st.nextToken();
				String kdmpb=st.nextToken();
				String kdmpc=st.nextToken();
				String kdmpd=st.nextToken();
				String kdmpe=st.nextToken();
				String kdmpf=st.nextToken();
				String kdmpg=st.nextToken();
				String kdmph=st.nextToken();
				String kdmpi=st.nextToken();
				String kdmpj=st.nextToken();
				String crmkl=st.nextToken();
				String prstd=st.nextToken();
				String smgds=st.nextToken();
				String rpsim=st.nextToken();
				String csstu=st.nextToken();
				String disln=st.nextToken();
				String sdiln=st.nextToken();
				String codln=st.nextToken();
				String colln=st.nextToken();
				String ctxin=st.nextToken();
				String pjbln=st.nextToken();
				String pbbln=st.nextToken();
				String ujtls=st.nextToken();
				String tgmkl=st.nextToken();
				String tgmod=st.nextToken();
				String pstsi=st.nextToken();
				String simul=st.nextToken();
				String lainn=st.nextToken();
				String ujtl1=st.nextToken();
				String tgmk1=st.nextToken();
				String tgmo1=st.nextToken();
				String psts1=st.nextToken();
				String simu1=st.nextToken();
				String lain1=st.nextToken();
				ListIterator li0 = v0.listIterator();
				boolean match = false;
				String baris="";
				while(li0.hasNext()&&!match) {
					String brs0 = (String)li0.next();
					st = new StringTokenizer(brs0,"#");
					String thsms0=st.nextToken();
					String kdpti0=st.nextToken();
					String kdjen0=st.nextToken();
					String kdpst0=st.nextToken();
					String kdkmk0=st.nextToken();
					String nakmk0=st.nextToken();
					String sksmk0=st.nextToken();
					String skstm0=st.nextToken();
					String skspr0=st.nextToken();
					String skslp0=st.nextToken();
					String semes0=st.nextToken();
					String kdwpl0=st.nextToken();
					String kdkur0=st.nextToken();
					String kdkel0=st.nextToken();
					String nodos0=st.nextToken();
					String jenja0=st.nextToken();
					String prodi0=st.nextToken();
					String stkmk0=st.nextToken();
					String slbus0=st.nextToken();
					String sappp0=st.nextToken();
					String bhnaj0=st.nextToken();
					String diktt0=st.nextToken();
					String kduta0=st.nextToken();
					String kdkug0=st.nextToken();
					String kdlai0=st.nextToken();
					String kdmpa0=st.nextToken();
					String kdmpb0=st.nextToken();
					String kdmpc0=st.nextToken();
					String kdmpd0=st.nextToken();
					String kdmpe0=st.nextToken();
					String kdmpf0=st.nextToken();
					String kdmpg0=st.nextToken();
					String kdmph0=st.nextToken();
					String kdmpi0=st.nextToken();
					String kdmpj0=st.nextToken();
					String crmkl0=st.nextToken();
					String prstd0=st.nextToken();
					String smgds0=st.nextToken();
					String rpsim0=st.nextToken();
					String csstu0=st.nextToken();
					String disln0=st.nextToken();
					String sdiln0=st.nextToken();
					String codln0=st.nextToken();
					String colln0=st.nextToken();
					String ctxin0=st.nextToken();
					String pjbln0=st.nextToken();
					String pbbln0=st.nextToken();
					String ujtls0=st.nextToken();
					String tgmkl0=st.nextToken();
					String tgmod0=st.nextToken();
					String pstsi0=st.nextToken();
					String simul0=st.nextToken();
					String lainn0=st.nextToken();
					String ujtl10=st.nextToken();
					String tgmk10=st.nextToken();
					String tgmo10=st.nextToken();
					String psts10=st.nextToken();
					String simu10=st.nextToken();
					String lain10=st.nextToken();
					
					if(thsms0.equalsIgnoreCase(based_thsms)&&kdpst.equalsIgnoreCase(kdpst0)&&kdkmk.equalsIgnoreCase(kdkmk0)) {
						match=true;
						baris = kdpst0+"#"+kdkmk0+"#"+nakmk0+"#"+skstm0+"#"+skspr0+"#"+skslp0+"#"+kdwpl0+"#"+nodos0+"#"+stkmk0;
						li2.add(baris);//System.out.println("brs0="+brs0);
					}	
				}
		//		if(match) {
		//			System.out.println(baris);
		//		}
				
				
			}
			
			//cek manual apa nakmk komplit
			writeFile("file_makul_"+kdpst_target+"_to_migrate",v2);
			//bila komplit start migrasi - goto part 2
				
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	
	public static void txtTbkmkToMysqlPartI(String kdpst_target,String kdkmk_target,String based_thsms) {
		//based thsms hanya digunakan utk nakmk - cari thsms yg komplit
		Vector v = new Vector();
		Vector v0 = null;
		ListIterator li = null;
		try {
			v = bacaFileTxt("dbfTbkmkToTxt");
			v0 = new Vector(v);
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"#");
				String thsms=st.nextToken();
				String kdpti=st.nextToken();
				String kdjen=st.nextToken();
				String kdpst=st.nextToken();
				String kdkmk=st.nextToken();
				String nakmk=st.nextToken();
				String sksmk=st.nextToken();
				String skstm=st.nextToken();
				String skspr=st.nextToken();
				String skslp=st.nextToken();
				String semes=st.nextToken();
				String kdwpl=st.nextToken();
				String kdkur=st.nextToken();
				String kdkel=st.nextToken();
				String nodos=st.nextToken();
				String jenja=st.nextToken();
				String prodi=st.nextToken();
				String stkmk=st.nextToken();
				String slbus=st.nextToken();
				String sappp=st.nextToken();
				String bhnaj=st.nextToken();
				String diktt=st.nextToken();
				String kduta=st.nextToken();
				String kdkug=st.nextToken();
				String kdlai=st.nextToken();
				String kdmpa=st.nextToken();
				String kdmpb=st.nextToken();
				String kdmpc=st.nextToken();
				String kdmpd=st.nextToken();
				String kdmpe=st.nextToken();
				String kdmpf=st.nextToken();
				String kdmpg=st.nextToken();
				String kdmph=st.nextToken();
				String kdmpi=st.nextToken();
				String kdmpj=st.nextToken();
				String crmkl=st.nextToken();
				String prstd=st.nextToken();
				String smgds=st.nextToken();
				String rpsim=st.nextToken();
				String csstu=st.nextToken();
				String disln=st.nextToken();
				String sdiln=st.nextToken();
				String codln=st.nextToken();
				String colln=st.nextToken();
				String ctxin=st.nextToken();
				String pjbln=st.nextToken();
				String pbbln=st.nextToken();
				String ujtls=st.nextToken();
				String tgmkl=st.nextToken();
				String tgmod=st.nextToken();
				String pstsi=st.nextToken();
				String simul=st.nextToken();
				String lainn=st.nextToken();
				String ujtl1=st.nextToken();
				String tgmk1=st.nextToken();
				String tgmo1=st.nextToken();
				String psts1=st.nextToken();
				String simu1=st.nextToken();
				String lain1=st.nextToken();
				if(!kdpst.equalsIgnoreCase(kdpst_target)) {
					li.remove();
				}
			}
			
			//filtered unik kdkmk
			Vector v1 = new Vector();
			ListIterator li1 = v1.listIterator();
			boolean first = true;
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"#");
				String thsms=st.nextToken();
				String kdpti=st.nextToken();
				String kdjen=st.nextToken();
				String kdpst=st.nextToken();
				String kdkmk=st.nextToken();
				String nakmk=st.nextToken();
				String sksmk=st.nextToken();
				String skstm=st.nextToken();
				String skspr=st.nextToken();
				String skslp=st.nextToken();
				String semes=st.nextToken();
				String kdwpl=st.nextToken();
				String kdkur=st.nextToken();
				String kdkel=st.nextToken();
				String nodos=st.nextToken();
				String jenja=st.nextToken();
				String prodi=st.nextToken();
				String stkmk=st.nextToken();
				String slbus=st.nextToken();
				String sappp=st.nextToken();
				String bhnaj=st.nextToken();
				String diktt=st.nextToken();
				String kduta=st.nextToken();
				String kdkug=st.nextToken();
				String kdlai=st.nextToken();
				String kdmpa=st.nextToken();
				String kdmpb=st.nextToken();
				String kdmpc=st.nextToken();
				String kdmpd=st.nextToken();
				String kdmpe=st.nextToken();
				String kdmpf=st.nextToken();
				String kdmpg=st.nextToken();
				String kdmph=st.nextToken();
				String kdmpi=st.nextToken();
				String kdmpj=st.nextToken();
				String crmkl=st.nextToken();
				String prstd=st.nextToken();
				String smgds=st.nextToken();
				String rpsim=st.nextToken();
				String csstu=st.nextToken();
				String disln=st.nextToken();
				String sdiln=st.nextToken();
				String codln=st.nextToken();
				String colln=st.nextToken();
				String ctxin=st.nextToken();
				String pjbln=st.nextToken();
				String pbbln=st.nextToken();
				String ujtls=st.nextToken();
				String tgmkl=st.nextToken();
				String tgmod=st.nextToken();
				String pstsi=st.nextToken();
				String simul=st.nextToken();
				String lainn=st.nextToken();
				String ujtl1=st.nextToken();
				String tgmk1=st.nextToken();
				String tgmo1=st.nextToken();
				String psts1=st.nextToken();
				String simu1=st.nextToken();
				String lain1=st.nextToken();
				if(first) {
					first = false;
					li1.add(kdkmk);
				}
				else {
					li1 = v1.listIterator();
					boolean match = false;
					while(li1.hasNext()&&!match) {
						String kmk = (String)li1.next();
						if(kdkmk.equalsIgnoreCase(kmk)) {
							match = true;
						}
					}
					if(match) {
						li.remove();
					}
					else {
						li1.add(kdkmk);
					}
				}
			}
			//cek apa nakmk komplit
			//writeFile("mk_to_migrate",v);
			//System.out.println("v0 size="+v0.size());
			Vector v2 = new Vector();
			ListIterator li2 = v2.listIterator();
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				//System.out.println(brs);
				StringTokenizer st = new StringTokenizer(brs,"#");
				String thsms=st.nextToken();
				String kdpti=st.nextToken();
				String kdjen=st.nextToken();
				String kdpst=st.nextToken();
				String kdkmk=st.nextToken();
				String nakmk=st.nextToken();
				String sksmk=st.nextToken();
				String skstm=st.nextToken();
				String skspr=st.nextToken();
				String skslp=st.nextToken();
				String semes=st.nextToken();
				String kdwpl=st.nextToken();
				String kdkur=st.nextToken();
				String kdkel=st.nextToken();
				String nodos=st.nextToken();
				String jenja=st.nextToken();
				String prodi=st.nextToken();
				String stkmk=st.nextToken();
				String slbus=st.nextToken();
				String sappp=st.nextToken();
				String bhnaj=st.nextToken();
				String diktt=st.nextToken();
				String kduta=st.nextToken();
				String kdkug=st.nextToken();
				String kdlai=st.nextToken();
				String kdmpa=st.nextToken();
				String kdmpb=st.nextToken();
				String kdmpc=st.nextToken();
				String kdmpd=st.nextToken();
				String kdmpe=st.nextToken();
				String kdmpf=st.nextToken();
				String kdmpg=st.nextToken();
				String kdmph=st.nextToken();
				String kdmpi=st.nextToken();
				String kdmpj=st.nextToken();
				String crmkl=st.nextToken();
				String prstd=st.nextToken();
				String smgds=st.nextToken();
				String rpsim=st.nextToken();
				String csstu=st.nextToken();
				String disln=st.nextToken();
				String sdiln=st.nextToken();
				String codln=st.nextToken();
				String colln=st.nextToken();
				String ctxin=st.nextToken();
				String pjbln=st.nextToken();
				String pbbln=st.nextToken();
				String ujtls=st.nextToken();
				String tgmkl=st.nextToken();
				String tgmod=st.nextToken();
				String pstsi=st.nextToken();
				String simul=st.nextToken();
				String lainn=st.nextToken();
				String ujtl1=st.nextToken();
				String tgmk1=st.nextToken();
				String tgmo1=st.nextToken();
				String psts1=st.nextToken();
				String simu1=st.nextToken();
				String lain1=st.nextToken();
				ListIterator li0 = v0.listIterator();
				boolean match = false;
				String baris="";
				while(li0.hasNext()&&!match) {
					String brs0 = (String)li0.next();
					st = new StringTokenizer(brs0,"#");
					String thsms0=st.nextToken();
					String kdpti0=st.nextToken();
					String kdjen0=st.nextToken();
					String kdpst0=st.nextToken();
					String kdkmk0=st.nextToken();
					String nakmk0=st.nextToken();
					String sksmk0=st.nextToken();
					String skstm0=st.nextToken();
					String skspr0=st.nextToken();
					String skslp0=st.nextToken();
					String semes0=st.nextToken();
					String kdwpl0=st.nextToken();
					String kdkur0=st.nextToken();
					String kdkel0=st.nextToken();
					String nodos0=st.nextToken();
					String jenja0=st.nextToken();
					String prodi0=st.nextToken();
					String stkmk0=st.nextToken();
					String slbus0=st.nextToken();
					String sappp0=st.nextToken();
					String bhnaj0=st.nextToken();
					String diktt0=st.nextToken();
					String kduta0=st.nextToken();
					String kdkug0=st.nextToken();
					String kdlai0=st.nextToken();
					String kdmpa0=st.nextToken();
					String kdmpb0=st.nextToken();
					String kdmpc0=st.nextToken();
					String kdmpd0=st.nextToken();
					String kdmpe0=st.nextToken();
					String kdmpf0=st.nextToken();
					String kdmpg0=st.nextToken();
					String kdmph0=st.nextToken();
					String kdmpi0=st.nextToken();
					String kdmpj0=st.nextToken();
					String crmkl0=st.nextToken();
					String prstd0=st.nextToken();
					String smgds0=st.nextToken();
					String rpsim0=st.nextToken();
					String csstu0=st.nextToken();
					String disln0=st.nextToken();
					String sdiln0=st.nextToken();
					String codln0=st.nextToken();
					String colln0=st.nextToken();
					String ctxin0=st.nextToken();
					String pjbln0=st.nextToken();
					String pbbln0=st.nextToken();
					String ujtls0=st.nextToken();
					String tgmkl0=st.nextToken();
					String tgmod0=st.nextToken();
					String pstsi0=st.nextToken();
					String simul0=st.nextToken();
					String lainn0=st.nextToken();
					String ujtl10=st.nextToken();
					String tgmk10=st.nextToken();
					String tgmo10=st.nextToken();
					String psts10=st.nextToken();
					String simu10=st.nextToken();
					String lain10=st.nextToken();
					
					if(thsms0.equalsIgnoreCase(based_thsms)&&kdpst.equalsIgnoreCase(kdpst0)&&kdkmk.equalsIgnoreCase(kdkmk0)&&kdkmk.equalsIgnoreCase(kdkmk_target)) {
						match=true;
						baris = kdpst0+"#"+kdkmk0+"#"+nakmk0+"#"+skstm0+"#"+skspr0+"#"+skslp0+"#"+kdwpl0+"#"+nodos0+"#"+stkmk0;
						li2.add(baris);//System.out.println("brs0="+brs0);
					}	
				}
		//		if(match) {
		//			System.out.println(baris);
		//		}
				
				
			}
			
			//cek manual apa nakmk komplit
			writeFile("file_single_makul_"+kdpst_target+"_"+kdkmk_target+"to_migrate",v2);
			//bila komplit start migrasi - goto part 2
				
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	
	public static void txtTbkmkToMysqlPartII(String kdpst, String kdkmk) {
		try {
			Vector vmakul = bacaFileTxt("file_single_makul_"+kdpst+"_"+kdkmk+"to_migrate");
			migrateTxtKeMakul(vmakul);	
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void migrateTxtKeMakul(Vector vmakul) {
		try {
			connectToMysql();
			//update part
			stmt=con.prepareStatement("update MAKUL set NAKMKMAKUL=?,SKSTMMAKUL=?,SKSPRMAKUL=?,SKSLPMAKUL=?,KDWPLMAKUL=?,NODOSMAKUL=?,STKMKMAKUL=? where KDPSTMAKUL=? and KDKMKMAKUL=?");
			ListIterator li = vmakul.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"#");
				//kdpst0+"#"+kdkmk0+"#"+nakmk0+"#"+skstm0+"#"+skspr0+"#"+skslp0+"#"+kdwpl0+"#"+nodos0+"#"+stkmk0;
				String kdpst = st.nextToken();
				String kdkmk = st.nextToken();
				String nakmk = st.nextToken();
				String skstm = st.nextToken();
				String skspr = st.nextToken();
				String skslp = st.nextToken();
				String kdwpl = st.nextToken();
				String nodos = st.nextToken();
				String stkmk = st.nextToken();
				stmt.setString(1, nakmk);
				stmt.setDouble(2, Double.valueOf(skstm).doubleValue());
				stmt.setDouble(3, Double.valueOf(skspr).doubleValue());
				stmt.setDouble(4, Double.valueOf(skslp).doubleValue());
				stmt.setString(5, kdwpl);
				stmt.setString(6, nodos);
				stmt.setString(7, stkmk);
				stmt.setString(8, kdpst);
				stmt.setString(9, kdkmk);
				int i = 0;
				i = stmt.executeUpdate();
				System.out.println("update "+kdkmk+" = "+i);
				if(i>0) {
					li.remove();
				}
			}
			
			//input part
			stmt=con.prepareStatement("insert into MAKUL(KDPSTMAKUL,KDKMKMAKUL,NAKMKMAKUL,SKSTMMAKUL,SKSPRMAKUL,SKSLPMAKUL,KDWPLMAKUL,NODOSMAKUL,STKMKMAKUL) values(?,?,?,?,?,?,?,?,?)");
			li = vmakul.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"#");
				//kdpst0+"#"+kdkmk0+"#"+nakmk0+"#"+skstm0+"#"+skspr0+"#"+skslp0+"#"+kdwpl0+"#"+nodos0+"#"+stkmk0;
				String kdpst = st.nextToken();
				String kdkmk = st.nextToken();
				String nakmk = st.nextToken();
				String skstm = st.nextToken();
				String skspr = st.nextToken();
				String skslp = st.nextToken();
				String kdwpl = st.nextToken();
				String nodos = st.nextToken();
				String stkmk = st.nextToken();
				stmt.setString(1, kdpst);
				stmt.setString(2, kdkmk);
				stmt.setString(3, nakmk);
				stmt.setDouble(4, Double.valueOf(skstm).doubleValue());
				stmt.setDouble(5, Double.valueOf(skspr).doubleValue());
				stmt.setDouble(6, Double.valueOf(skslp).doubleValue());
				stmt.setString(7, kdwpl);
				stmt.setString(8, nodos);
				stmt.setString(9, stkmk);

				int i = 0;
				i = stmt.executeUpdate();
				System.out.println("insert "+kdkmk+" = "+i);
				if(i>0) {
					li.remove();
				}
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[]args)throws Exception {
		//getListKdkmkMysql();
		//tmp();
		//txtTbkmkToMysqlPartI("54211","20121");
		txtTbkmkToMysqlPartI("64201","ISP3210","20121");
		//txtTbkmkToMysqlPartII("54211");
		System.out.println("done");
	}
	

}