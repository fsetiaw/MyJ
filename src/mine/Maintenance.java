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



class Maintenance {

  public static Connection con;
  public static PreparedStatement stmt;
  public static ResultSet rs;
  public static String url;
  String log_date, log_time, status; 
  long counter=0;
  GregorianCalendar gc = new GregorianCalendar();
  PrintWriter pw;
  NumberFormat formatter = new DecimalFormat("#0.00");
  SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
	FileOutputStream fileOut;
  HSSFSheet sheet;

  Row row;
  Cell cell;
	CellStyle style1,style2,style3;
	File file;
	FileWriter fw;
	BufferedWriter bw;
  String filename, kdpst;

  public static void connectToTmp () {
    try {
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      con = DriverManager.getConnection("jdbc:odbc:tmp","","");
      con.setAutoCommit(true) ;
    } catch (Exception e) {  
      System.out.println(e);  
    }
  }

  public static void connectToMysql () {
	  try {
		  Class.forName("com.mysql.jdbc.Driver");
		  url =  "jdbc:mysql://localhost:3306/USG";
		  con = DriverManager.getConnection(url,"root","b1sm1llah");  
		  con.setAutoCommit(true) ;
	  } catch (Exception e) {  
	      System.out.println(e);  
	  }			
  }
  
  public static void connectToQapla () {
	  try {
		  Class.forName("com.mysql.jdbc.Driver");
		  url =  "jdbc:mysql://localhost:3306/qapla_sistem";
		  con = DriverManager.getConnection(url,"admin_my_qapla","qaplaqapla");  
		  con.setAutoCommit(true) ;
	  } catch (Exception e) {  
	      System.out.println(e);  
	  }			
  }

  public static void connectToBackUp () {
	  try {
		  Class.forName("com.mysql.jdbc.Driver");
		  url =  "jdbc:mysql://localhost:3306/backup_USG";
		  con = DriverManager.getConnection(url,"root","b1sm1llah");  
		  con.setAutoCommit(true) ;
	  } catch (Exception e) {  
	      System.out.println(e);  
	  }			
  }
  
  public static void connectToOt () {
	  try {
		  Class.forName("com.mysql.jdbc.Driver");
		  url =  "jdbc:mysql://localhost:3306/ONLINE_TEST";
		  con = DriverManager.getConnection(url,"root","b1sm1llah");  
		  con.setAutoCommit(true) ;
	  } catch (Exception e) {  
	      System.out.println(e);  
	  }			
  }  
	public static int convertNamaBulan(String nama) throws Exception {
		int bln = 0;
		if(nama.equalsIgnoreCase("JAN") || nama.equalsIgnoreCase("JANuary") || nama.equalsIgnoreCase("JANuari")) {
			bln = 1;
		}
		if(nama.equalsIgnoreCase("FEB") || nama.equalsIgnoreCase("FEBRUARY") || nama.equalsIgnoreCase("FEBRUARI") || nama.equalsIgnoreCase("PEBRUARI")) {
			bln = 2;
		}
		if(nama.equalsIgnoreCase("MAR") || nama.equalsIgnoreCase("MARET")) {
			bln = 3;
		}
		if(nama.equalsIgnoreCase("APR") || nama.equalsIgnoreCase("APRIL")) {
			bln = 4;
		}
		if(nama.equalsIgnoreCase("MAY") || nama.equalsIgnoreCase("MEI")) {
			bln = 5;
		}
		if(nama.equalsIgnoreCase("JUN") || nama.equalsIgnoreCase("JUNI") || nama.equalsIgnoreCase("JUNE")) {
			bln = 6;
		}
		if(nama.equalsIgnoreCase("JUL") || nama.equalsIgnoreCase("JULI") || nama.equalsIgnoreCase("JULY")) {
			bln = 7;
		}
		if(nama.equalsIgnoreCase("AUG") || nama.equalsIgnoreCase("AUGUST") || nama.equalsIgnoreCase("AGUSTUS")) {
			bln = 8;
		}
		if(nama.equalsIgnoreCase("SEPTEMBER") || nama.equalsIgnoreCase("SEP") || nama.equalsIgnoreCase("SEPT")) {
			bln = 9;
		}
		if(nama.equalsIgnoreCase("OCT") || nama.equalsIgnoreCase("OCTOBER") || nama.equalsIgnoreCase("OKTOBER")) {
			bln = 10;
		}
		if(nama.equalsIgnoreCase("NOV") || nama.equalsIgnoreCase("NOVEMBER") || nama.equalsIgnoreCase("NOPEMBER")) {
			bln = 11;
		}
		if(nama.equalsIgnoreCase("DES") || nama.equalsIgnoreCase("DECEMBER") || nama.equalsIgnoreCase("DESEMBER")) {
			bln = 12;
		}
		return bln;
	}

	public static String  getBulanRomawi(int bln)throws Exception{
		String kode="";
		switch(bln) {
			case 1:
				kode = "I";
				break;
			case 2:
				kode = "III";
				break;
			case 3:
				kode = "III";
				break;
			case 4:
				kode = "IV";
				break;
			case 5:
				kode = "V";
				break;
			case 6:
				kode = "VI";
				break;
			case 7:
				kode = "VII";
				break;
			case 8:
				kode = "VIII";
				break;
			case 9:
				kode = "IX";
				break;
			case 10:
				kode = "X";
				break;
			case 11:
				kode = "XI";
				break;
			case 12:
				kode = "XII";
				break;
		}
		return kode;
	}
  
	public static long calcTotDaysBetween(java.sql.Date tgl1, java.sql.Date tgl2){
    Calendar calendar1 = Calendar.getInstance();
    Calendar calendar2 = Calendar.getInstance();

		String baruan = ""+tgl1;
		String lamaan = ""+tgl2;
		StringTokenizer st = new StringTokenizer(baruan,"-");
		String yy_baru = st.nextToken();
		String bb_baru = st.nextToken();
		String dd_baru = st.nextToken();
    calendar1.set(Integer.valueOf(yy_baru).intValue(),Integer.valueOf(bb_baru).intValue(),Integer.valueOf(dd_baru).intValue());

		st = new StringTokenizer(lamaan,"-");
		yy_baru = st.nextToken();
		bb_baru = st.nextToken();
		dd_baru = st.nextToken();
    calendar2.set(Integer.valueOf(yy_baru).intValue(),Integer.valueOf(bb_baru).intValue(),Integer.valueOf(dd_baru).intValue());

    long milliseconds1 = calendar1.getTimeInMillis();
    long milliseconds2 = calendar2.getTimeInMillis();
    long diff = milliseconds2 - milliseconds1;
    long diffSeconds = diff / 1000;
    long diffMinutes = diff / (60 * 1000);
    long diffHours = diff / (60 * 60 * 1000);//jam diff
    long diffDays = diff / (24 * 60 * 60 * 1000);//day diff
		if(diffDays<0) {
			diffDays = -diffDays;
		}
		return diffDays;
  }

	public static double calcTotBulanBetweenSmawlSmsLulus(java.sql.Date tgl_muda, java.sql.Date tgl_tua) {
		String baruan = ""+tgl_muda;
		String lamaan = ""+tgl_tua;
		StringTokenizer st = new StringTokenizer(baruan,"-");
		String yy_baru = st.nextToken();
		String bb_baru = st.nextToken();
		String dd_baru = st.nextToken();
		double yr = Integer.valueOf(yy_baru).intValue();
		double mm = Integer.valueOf(bb_baru).intValue();
		double dd = Integer.valueOf(dd_baru).intValue();
		double new_val = (yr*12)+mm;


		st = new StringTokenizer(lamaan,"-");
		yy_baru = st.nextToken();
		bb_baru = st.nextToken();
		yr = Integer.valueOf(yy_baru).intValue();
		mm = Integer.valueOf(bb_baru).intValue();
		double old_val = (yr*12)+mm;

		return (new_val-old_val)/6;
	}
/*
	public static double calcLamaStudi(java.sql.Date tgl_muda, java.sql.Date tgl_tua) {
		String baruan = ""+tgl_muda;
		String lamaan = ""+tgl_tua;
		StringTokenizer st = new StringTokenizer(baruan,"-");
		String yy_baru = st.nextToken();
		String bb_baru = st.nextToken();
		String dd_baru = st.nextToken();
		double yr = Integer.valueOf(yy_baru).intValue();
		double mm = Integer.valueOf(bb_baru).intValue();
		double dd = Integer.valueOf(dd_baru).intValue();
		double new_val = (yr*12)+mm;


		st = new StringTokenizer(lamaan,"-");
		yy_baru = st.nextToken();
		bb_baru = st.nextToken();
		yr = Integer.valueOf(yy_baru).intValue();
		mm = Integer.valueOf(bb_baru).intValue();
		double old_val = (yr*12)+mm;

		return (new_val-old_val)/6;
	}
*/
	public static long calcTotSmsDariSmawlSmsLulus(String smawl, String smakh) {
		long yr_awl = Long.valueOf(smawl.substring(0,4)).longValue();
		long sms_awl = Long.valueOf(smawl.substring(4,5)).longValue();

		long yr_akh = Long.valueOf(smakh.substring(0,4)).longValue();
		long sms_akh = Long.valueOf(smakh.substring(4,5)).longValue();

		long tot_sms=0;
		if(sms_akh == sms_awl) {
			tot_sms = ((yr_akh-yr_awl)*2)+1;
		} else {
			if(sms_akh < sms_awl) {
				tot_sms = ((yr_akh-yr_awl)*2);
			} else {
				if(sms_akh > sms_awl) {
					tot_sms = ((yr_akh-yr_awl)*2)+2;
				}
			}
		}
		return tot_sms;
	}


	public static String getThsmsGivenStrTgl8digit(String tgll) throws Exception {
		String thsms="";

		String yy = tgll.substring(0,4);
		String mm = tgll.substring(4,6);
		String dd = tgll.substring(6,8);
		long mm_lls = Long.valueOf(mm).longValue();
		long yy_lls = Long.valueOf(yy).longValue();
		if((mm_lls>2)&&(mm_lls<9)) {//lulus sms genap
			thsms = (yy_lls-1)+"2";
		}else{//lulus sms ganjil
			if(mm_lls==1 || mm_lls==2) {
				thsms = (yy_lls-1)+"1";
			} else {
				thsms = (yy_lls)+"1";
			}
		}
		return thsms;
	}

	public static double CalcLamaStudiMhs(String kdpst)throws Exception
	{
		java.sql.Date  tgmsk = null, tglls=null;
		connectToTmp();
		double rerata = 0;
		stmt = con.prepareStatement("select * from msmhs where kdpstmsmhs=? and tgllsmsmhs is not null order by tgllsmsmhs,nimhsmsmhs");
		stmt.executeQuery();
		while(rs.next())
		{
			tgmsk = rs.getDate("tglmskmsmhs");
			tglls = rs.getDate("tgllskmsmhs");
			rerata = calcTotDaysBetween(tgmsk, tglls);
		}
		return rerata;
	}

	public static String getThsmsGivenStrTgl(String tglls) throws Exception {
		String thsms="";
		String tgllstr = tglls;
		StringTokenizer st = new StringTokenizer(tgllstr,"-");
		String yy = st.nextToken();
		String mm = st.nextToken();
		String dd = st.nextToken();
		long mm_lls = Long.valueOf(mm).longValue();
		long yy_lls = Long.valueOf(yy).longValue();
		if((mm_lls>2)&&(mm_lls<9)) {//lulus sms genap
			thsms = (yy_lls-1)+"2";
		}else{//lulus sms ganjil
			if(mm_lls==1 || mm_lls==2) {
				thsms = (yy_lls-1)+"1";
			} else {
				thsms = (yy_lls)+"1";
			}
		}
		return thsms;
	}

	public static Vector bacaFileTxt(String nama_file) throws Exception {
		Vector v = new Vector();
		ListIterator li = v.listIterator();

 		FileInputStream fstream  = new FileInputStream("input_txt/"+nama_file+".txt");
 		DataInputStream in = new DataInputStream(fstream);
 		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String tmp="";
		while((tmp = br.readLine())!=null) {
			li.add(tmp);
		}
		return v;
	}

	public static void writeFile(String nama_file, Vector v) throws Exception {
		PrintWriter pw = new PrintWriter(new FileWriter("output_txt/"+nama_file+".txt"));
		ListIterator li = v.listIterator();
		while(li.hasNext()) {
			String row = (String)li.next();
			pw.println(row);
		}
		pw.close();
	}

	public static void writeFileAdjustedForUsrPwd(Vector v) throws Exception {
		ListIterator li = v.listIterator();
		while(li.hasNext()) {
			String nimhs = (String)li.next();
			String nmmhs = (String)li.next();
			String usrnmm = (String)li.next();
			String pwdusr = (String)li.next();
			PrintWriter pw = new PrintWriter(new FileWriter("e:\\Documents and Settings\\ayah\\My Documents\\shortcut\\folder\\MyWorks\\pusat\\txt\\"+nimhs+"_"+nmmhs+".txt"));
			for(int i=0;i<30;i++)
			{			
				pw.println("");
			}
			pw.println("								No NIM       :  "+nimhs);
			pw.println("								NAMA         :  "+nmmhs);
			pw.println("								USER         :  "+usrnmm);
			pw.println("								PASSWORD     :  "+pwdusr);
			pw.close();
		}
	}

	public static String returnNextThsmsGiven(String smawl)throws Exception {
		String tahun = smawl.substring(0,4);
		String sms = smawl.substring(4,5);
		String thsms = "";
		if(sms.equalsIgnoreCase("1")) {
			thsms = tahun+"2";
		} else {
			thsms = (1+Long.valueOf(tahun).longValue())+"1";
		}
		return thsms;
	}

	public static String returnPrevThsmsGiven(String smawl)throws Exception {
		String tahun = smawl.substring(0,4);
		String sms = smawl.substring(4,5);
		String thsms = "";
		if(sms.equalsIgnoreCase("1")) {
			thsms = (Long.valueOf(tahun).longValue()-1)+"2";
		} else {
			thsms = tahun+"1";
		}
		return thsms;
	}




	public static Vector bacaFileExcel(String nama_file) throws Exception {
		Vector v = new Vector();
		ListIterator li = v.listIterator();

 		FileInputStream fstream  = new FileInputStream("e:\\myj\\mip\\xls_input\\"+nama_file+".xls");
  	DataInputStream in = new DataInputStream(fstream);
  	BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String tmp="";
		while((tmp = br.readLine())!=null) {
			li.add(tmp);
		}
		return v;
	}

/*
	public static double returnBobotGiven(String nilai_nominal) throws Exception 
	{
		double bobot = 0;
		if(nilai_nominal.equalsIgnoreCase("A"))
		{
			bobot = 4;
		}
		else
		{
			if(nilai_nominal.equalsIgnoreCase("B"))
			{
				bobot = 3;
			}
			else
			{
				if(nilai_nominal.equalsIgnoreCase("C"))
				{
					bobot = 2;
				}
				else
				{
					if(nilai_nominal.equalsIgnoreCase("D"))
					{
						bobot = 1;
					}
					else
					{
						if(nilai_nominal.equalsIgnoreCase("E")||nilai_nominal.equalsIgnoreCase("F")||nilai_nominal.equalsIgnoreCase("T"))
						{
							bobot = 0;
						}
					}
				}
			}
		}
		return bobot;
	}
*/
	public static double returnBobotGiven(String nilai_nominal) throws Exception {
		double nlakh_bobot = 0;
		
		if(nilai_nominal.equalsIgnoreCase("A"))
		{
			nlakh_bobot = 4;
		}
		else
		{
			if(nilai_nominal.equalsIgnoreCase("B"))
			{
				nlakh_bobot = 3;
			}
			else
			{
				if(nilai_nominal.equalsIgnoreCase("C"))
				{
					nlakh_bobot = 2;
				}
				else
				{
					if(nilai_nominal.equalsIgnoreCase("D"))
					{
						nlakh_bobot = 1;
					}
					else
					{
						if(nilai_nominal.equalsIgnoreCase("E")||nilai_nominal.equalsIgnoreCase("F")||nilai_nominal.equalsIgnoreCase("T"))
						{
							nlakh_bobot = 0;
						}
					}
				}
			}
		}

		if(nlakh_bobot<1) {
			if(!nilai_nominal.equalsIgnoreCase("E") && !nilai_nominal.equalsIgnoreCase("F") && !nilai_nominal.equalsIgnoreCase("T")) {
				int _nilai_nominal = Integer.valueOf(nilai_nominal).intValue();
				switch(_nilai_nominal) {
					case 99:
						nlakh_bobot = 3.98;
						break;
					case 98:
						nlakh_bobot = 3.96;
						break;
					case 97:
						nlakh_bobot = 3.94;
						break;
					case 96:
						nlakh_bobot = 3.92;
						break;
					case 95:
						nlakh_bobot = 3.90;
						break;
					case 94:
						nlakh_bobot = 3.88;
						break;
					case 93:
						nlakh_bobot = 3.86;
						break;
					case 92:
						nlakh_bobot = 3.84;
						break;
					case 91:
						nlakh_bobot = 3.82;
						break;
					case 90:
						nlakh_bobot = 3.80;
						break;
					case 89:
						nlakh_bobot = 3.78;
						break;
					case 88:
						nlakh_bobot = 3.76;
						break;
					case 87:
						nlakh_bobot = 3.74;
						break;
					case 86:
						nlakh_bobot = 3.72;
						break;
					case 85:
						nlakh_bobot = 3.70;
						break;
					case 84:
						nlakh_bobot = 3.68;
						break;
					case 83:
						nlakh_bobot = 3.66;
						break;
					case 82:
						nlakh_bobot = 3.64;
						break;
					case 81:
						nlakh_bobot = 3.62;
						break;
					case 80:
						nlakh_bobot = 3.60;
						break;
					case 79:
						nlakh_bobot = 3.59;
						break;
					case 78:
						nlakh_bobot = 3.48;
						break;
					case 77:
						nlakh_bobot = 3.42;
						break;
					case 76:
						nlakh_bobot = 3.36;
						break;
					case 75:
						nlakh_bobot = 3.30;
						break;
					case 74:
						nlakh_bobot = 3.24;
						break;
					case 73:
						nlakh_bobot = 3.18;
						break;
					case 72:
						nlakh_bobot = 3.12;
						break;
					case 71:
						nlakh_bobot = 3.06;
						break;
					case 70:
						nlakh_bobot = 3.00;
						break;
					case 69:
						nlakh_bobot = 2.99;
						break;
					case 68:
						nlakh_bobot = 2.90;
						break;
					case 67:
						nlakh_bobot = 2.85;
						break;
					case 66:
						nlakh_bobot = 2.80;
						break;
					case 65:
						nlakh_bobot = 2.75;
						break;
					case 64:
						nlakh_bobot = 2.70;
						break;
					case 63:
						nlakh_bobot = 2.65;
						break;
					case 62:
						nlakh_bobot = 2.60;
						break;
					case 61:
						nlakh_bobot = 2.55;
						break;
					case 60:
						nlakh_bobot = 2.50;
						break;
					case 59:
						nlakh_bobot = 2.49;
						break;
					case 58:
						nlakh_bobot = 2.40;
						break;
					case 57:
						nlakh_bobot = 2.35;
						break;
					case 56:
						nlakh_bobot = 2.30;
						break;
					case 55:
						nlakh_bobot = 2.25;
						break;
					case 54:
						nlakh_bobot = 2.20;
						break;
					case 53:
						nlakh_bobot = 2.15;
						break;
					case 52:
						nlakh_bobot = 2.10;
						break;
					case 51:
						nlakh_bobot = 2.05;
						break;
					case 50:
						nlakh_bobot = 2.00;
						break;
		      default:
						nlakh_bobot = 0;
						break;
				}
			}
		}
		return nlakh_bobot;
	}

	public static String createRandomString(int pjg)throws Exception
	{
		String uuid = UUID.randomUUID().toString();
		Random gen = new Random();
		
		String acak = "";
		for(int i=0;i<pjg;i++)
		{
			int urut = gen.nextInt(35);
			acak = acak+uuid.charAt(urut);
		}

		String new_acak = "";
		for(int i=0;i<pjg;i++)
		{
			String tmp = ""+acak.charAt(i);
			if(tmp.equalsIgnoreCase("-"))
			{
				int urut = gen.nextInt(9);
				new_acak = new_acak+urut;
			}
			else
			{
				new_acak = new_acak+tmp;
			}
		}

//		System.out.println("acak = "+acak);
//		System.out.println("new_acak = "+new_acak);
		return new_acak;
	}

	public static double return2digit(String value)throws Exception 
	{
		value = value.replaceAll(",",".");;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		value = ""+nf.format(Double.valueOf(value).doubleValue());
		value = value.replaceAll(",",".");
		return Double.valueOf(value).doubleValue();
	}

	public static Vector hapusDuplicateRecordFromVector(Vector v)throws Exception 
	{
		Vector v1 = new Vector(v);
		ListIterator li = v.listIterator();
		while(li.hasNext())
		{
			String baris = (String)li.next();
//			System.out.println("v "+baris);
		}	
	

		v1 = new Vector(new LinkedHashSet(v));
		ListIterator li1 = v1.listIterator();
		while(li1.hasNext())
		{
			String baris = (String)li1.next();
//			System.out.println("v1 "+baris);
		}	
		return v1;
	}
	
	public static boolean isStringNullOrEmpty(String word) {
    	boolean empty = true;
    	if(word!=null) {
    		StringTokenizer st = new StringTokenizer(word);
    		if(st.countTokens()>0) {
    			String tkn = st.nextToken();
    			if(!tkn.equalsIgnoreCase("null")) {
    				empty = false;
    			}
    		}
    	}
    	//System.out.println("checker word="+word+" "+empty);
    	return empty;
    }
	
	public static String generateNpm(String thsms,String kdpst) {
    	int ins = 0;
    	String npm =null;
    	try {
    		connectToMysql();
    		//cretae NPM auto increment
    		stmt = con.prepareStatement("SELECT * FROM CIVITAS where NPMHSMSMHS like ? order by NPMHSMSMHS desc");
    		if(isStringNullOrEmpty(thsms)) {
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

	public static void main(String[]args)throws Exception {
		System.out.println("yes");
		try {
			connectToMysql();
			stmt = con.prepareStatement("update IJAZAH set EDITABLE=true,CETAKABLE=true");
			System.out.println(stmt.executeUpdate());
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	

}