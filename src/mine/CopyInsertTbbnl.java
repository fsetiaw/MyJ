package mine;
import java.util.*;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;



public class CopyInsertTbbnl extends Maintenance {
	   


	public static Vector getMasterData(String thsms,String kdpst) {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		try {
			 
			connectToMysql();
			stmt =  con.prepareStatement("select * from TBBNL where THSMSTBBNL=? and KDPSTTBBNL=?");
			stmt.setString(1, thsms);
			stmt.setString(2, kdpst);
			rs = stmt.executeQuery();
			while(rs.next()) {
				String nlakh = rs.getString("NLAKHTBBNL");
				String bobot = ""+rs.getDouble("BOBOTTBBNL");
				li.add(nlakh+"#&"+bobot);
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return v;
	}	

	
	public static void insertTbbnl(String thsms,String kdpst) {
		try {
			Vector v = getMasterData("20131",kdpst);
			System.out.println(v.size());
			ListIterator li = v.listIterator();
			connectToMysql();	
			stmt=con.prepareStatement("insert TBBNL (THSMSTBBNL,KDPSTTBBNL,NLAKHTBBNL,BOBOTTBBNL) values (?,?,?,?)");
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"#&");
				String nlakh = st.nextToken();
				String bobot = st.nextToken();
				stmt.setString(1,thsms);
				stmt.setString(2,kdpst);
				stmt.setString(3,nlakh);
				stmt.setDouble(4,Double.valueOf(bobot).doubleValue());
				System.out.println(thsms+","+kdpst+","+nlakh+","+bobot+"="+stmt.executeUpdate());
				//System.out.println(thsms+","+kdpst+","+nlakh+","+bobot+"=");
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void getDistinctMspst() {
		try {
			connectToMysql();
			stmt =  con.prepareStatement("select distinct KDPSTMSPST from MSPST");
			//stmt =  con.prepareStatement("select distinct THSMSTBBNL from TBBNL");
			rs = stmt.executeQuery();
			while(rs.next()) {
				System.out.println(rs.getString("THSMSTBBNL"));
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void rame2(String kdpst) {
		insertTbbnl("1992A",kdpst);
		insertTbbnl("1992B",kdpst);
		insertTbbnl("1993A",kdpst);
		insertTbbnl("1993B",kdpst);
		insertTbbnl("1994A",kdpst);
		insertTbbnl("1994B",kdpst);
		insertTbbnl("1995A",kdpst);
		insertTbbnl("1995B",kdpst);
		insertTbbnl("1996A",kdpst);
		insertTbbnl("1996B",kdpst);
		insertTbbnl("1997A",kdpst);
		insertTbbnl("1997B",kdpst);
		insertTbbnl("1998A",kdpst);
		insertTbbnl("1998B",kdpst);
		insertTbbnl("1999A",kdpst);
		insertTbbnl("1999B",kdpst);
		insertTbbnl("2000A",kdpst);
		insertTbbnl("2000B",kdpst);
		insertTbbnl("2001A",kdpst);
		insertTbbnl("2001B",kdpst);
		insertTbbnl("2002A",kdpst);
		insertTbbnl("2002B",kdpst);
		insertTbbnl("2003A",kdpst);
		insertTbbnl("2003B",kdpst);
		insertTbbnl("2004A",kdpst);
		insertTbbnl("2004B",kdpst);
		insertTbbnl("2005A",kdpst);
		insertTbbnl("2005B",kdpst);
		insertTbbnl("2006A",kdpst);
		insertTbbnl("2006B",kdpst);
		insertTbbnl("2007A",kdpst);
		insertTbbnl("2007B",kdpst);
		insertTbbnl("2008A",kdpst);
		insertTbbnl("2008B",kdpst);
		insertTbbnl("2009A",kdpst);
		insertTbbnl("2009B",kdpst);
		insertTbbnl("2010A",kdpst);
		insertTbbnl("2010B",kdpst);
		insertTbbnl("2011A",kdpst);
		insertTbbnl("2011B",kdpst);
		insertTbbnl("2012A",kdpst);
		insertTbbnl("2012B",kdpst);
		insertTbbnl("2013A",kdpst);
		insertTbbnl("2013B",kdpst);
		insertTbbnl("2014A",kdpst);
		insertTbbnl("2014B",kdpst);
		insertTbbnl("2015A",kdpst);
		insertTbbnl("2015B",kdpst);
		insertTbbnl("2016A",kdpst);
		insertTbbnl("2016B",kdpst);
		insertTbbnl("2017A",kdpst);
		insertTbbnl("2017B",kdpst);

	}
	
	public static void main(String[]args) throws Exception {
		
		//String kdpst = "88888";
		//String kdpst = "54201";
		//String kdpst = "54211";
		//String kdpst = "61201";
		String kdpst = "62201";
		rame2(kdpst);
		kdpst = "93402";
		rame2(kdpst);
		kdpst = "74201";
		rame2(kdpst);
		kdpst = "64201";
		rame2(kdpst);
		kdpst = "65201";
		rame2(kdpst);
		kdpst = "20201";
		rame2(kdpst);
		kdpst = "26201";
		rame2(kdpst);
		kdpst = "55201";
		rame2(kdpst);
		kdpst = "22201";
		rame2(kdpst);
		kdpst = "23201";
		rame2(kdpst);
		kdpst = "61101";
		rame2(kdpst);
		kdpst = "65001";
		rame2(kdpst);
		kdpst = "65101";
		rame2(kdpst);
		kdpst = "57301";
		rame2(kdpst);
		kdpst = "57302";
		rame2(kdpst);
		
		
				System.out.println("done");
	//	ListIterator li = v.listIterator();
	//	while(li.hasNext()) {
	//		String brs = (String)li.next();
	//		System.out.println(brs);
	//	}
	}

}
