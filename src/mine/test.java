package mine;
import java.util.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.Blob;
import java.util.UUID;
import java.security.Key;
import java.sql.SQLException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

//import com.otaku.db.OtakuSQL;


public class test extends Maintenance {
	   
	public static void updateTbbnl() {
		try {
			connectToMysql();
			
			stmt =  con.prepareStatement("select * from TRNLP where KDPSTTRNLP=? and NPMHSTRNLP=? and TRANSFERRED=?");
			stmt.setString(1, "20201");
			stmt.setString(2, "2020100000078");
			stmt.setBoolean(3, true);
			rs = stmt.executeQuery();
			//variable initial
			Vector vTrnlp = new Vector();
			ListIterator liTrnlp = vTrnlp.listIterator();
			int sksta = 0;
			double ipsta=0,bbtXnlakhSta=0;;
			//untuk trnlp nlakh dan bobot hrs terisi dan tidak ada nilai tunda
			while(rs.next()) {
				String thsms = "00000"; //thsms=00000 utk menandakan krs trnlp
				String kdkmk = rs.getString("KDKMKTRNLP");
				//String nakmk = rs.getString("NAKMKMAKUL");
				String nlakh = rs.getString("NLAKHTRNLP");
				String bobot = ""+rs.getDouble("BOBOTTRNLP");
				String sksmk = ""+rs.getInt("SKSMKTRNLP");
				String kelas = "00";//trnlp ngga ada kodekelas;
				//liTrnlp.add(thsms+","+kdkmk+","+nakmk+","+nlakh+","+bobot+","+sksmk+","+kelas);
				liTrnlp.add(thsms+"#&"+kdkmk+"#&"+nlakh+"#&"+bobot+"#&"+sksmk+"#&"+kelas);
				sksta = sksta+Integer.valueOf(sksmk).intValue();
				ipsta = ipsta + (Double.valueOf(bobot).doubleValue()*Integer.valueOf(sksmk).intValue());
			}
			if(sksta>0) {
				bbtXnlakhSta = ipsta; //ipsta adalah komulatif bobot * sks
				ipsta = ipsta/sksta;
			}
			//System.out.println("ipsta = "+ipsta+"-"+sksta);
			stmt = con.prepareStatement("select * from MAKUL where KDPSTMAKUL=? and KDKMKMAKUL=?");
			liTrnlp = vTrnlp.listIterator();
			while(liTrnlp.hasNext()) {
				String brs =(String)liTrnlp.next();
				StringTokenizer st = new StringTokenizer(brs,"#&");
				String thsms = st.nextToken();
				System.out.println("thsms="+thsms);
				String kdkmk = st.nextToken();
				System.out.println("kdkmk="+kdkmk);
				String nlakh = st.nextToken();
				System.out.println("nlakh="+nlakh);
				String bobot = st.nextToken();
				System.out.println("bobot="+bobot);
				String sksmk = st.nextToken();
				System.out.println("sksmk="+sksmk);
				String kelas = st.nextToken();
				System.out.println("kelas="+kelas);
				stmt.setString(1,"20201");
				stmt.setString(2,kdkmk);
				System.out.println("kdkmk="+kdkmk);
				rs = stmt.executeQuery();
				String nakmk = "TIDAK TERDAFTAR";
				if(rs.next()) {
					nakmk = rs.getString("NAKMKMAKUL");
				}
				liTrnlp.set(thsms+","+kdkmk+","+nakmk+","+nlakh+","+bobot+","+sksmk+","+kelas);
				System.out.println(thsms+","+kdkmk+","+nakmk+","+nlakh+","+bobot+","+sksmk+","+kelas);
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void updateKdjen() {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		try {
			connectToMysql();
			
			stmt =  con.prepareStatement("select * from CIVITAS");
			rs = stmt.executeQuery();
			while(rs.next()) {
				String npmhs = rs.getString("NPMHSMSMHS");
				String kdpst = rs.getString("KDPSTMSMHS");
				li.add(npmhs+"#&"+kdpst);
			}
			
			stmt =  con.prepareStatement("select * from MSPST where KDPSTMSPST=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs,"#&");
				String npmhs = st.nextToken();
				String kdpst = st.nextToken();
				
				stmt.setString(1, kdpst);
				rs = stmt.executeQuery();
				rs.next();
				String kdjen = rs.getString("KDJENMSPST");
				li.set(npmhs+" "+kdpst+" "+kdjen);
			}
			
			stmt =  con.prepareStatement("update CIVITAS set KDJENMSMHS=? where KDPSTMSMHS=? and NPMHSMSMHS=?");
			li = v.listIterator();
			while(li.hasNext()) {
				String brs = (String)li.next();
				StringTokenizer st = new StringTokenizer(brs);
				String npmhs = st.nextToken();
				String kdpst = st.nextToken();
				String kdjen = st.nextToken();
				kdjen = kdjen.toUpperCase();
				stmt.setString(1, kdjen);
				stmt.setString(2, kdpst);
				stmt.setString(3,npmhs);
				int i=0;
				i = stmt.executeUpdate();

				System.out.println(npmhs+","+kdpst+","+kdjen+" = "+i);

			}
			System.out.println("done");
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}	

	public static void updateShiftTrnlm() {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		try {
			connectToMysql();
			
			stmt =  con.prepareStatement("update TRNLM set SHIFTTRNLM='N/A'");
			System.out.println(stmt.executeUpdate());
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}	
	
	public static void bayaran(String kdpst) {
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		try {
			connectToMysql();
			stmt =  con.prepareStatement("select distinct NPMHSPYMNT from PYMNT where KDPSTPYMNT=?");
			stmt.setString(1, kdpst);
			rs = stmt.executeQuery();
			while(rs.next()) {
				String npmhs = rs.getString("NPMHSPYMNT");
				li.add(npmhs);
			}	
			Vector v1 = new Vector();
			ListIterator li1 = v1.listIterator();
			stmt =  con.prepareStatement("select * from PYMNT where KDPSTPYMNT=? and NPMHSPYMNT=? and VOIDDPYMNT=? order by TGTRSPYMNT limit 1");
			li = v.listIterator();
			while(li.hasNext()) {
				String npmhs = (String)li.next();
				stmt.setString(1,kdpst);
				stmt.setString(2,npmhs);
				stmt.setBoolean(3,false);
				rs = stmt.executeQuery();
				if(rs.next()) {
					String tgtrs = ""+rs.getDate("TGTRSPYMNT");
					String amont = ""+rs.getDouble("AMONTPYMNT");
					NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
			    	amont =  ""+nf.format(Double.valueOf(amont).doubleValue());
			    	if(tgtrs.startsWith("2012")||tgtrs.startsWith("2013")) {
			    		//System.out.println(npmhs+" "+tgtrs+" "+amont);
			    		li1.add(npmhs+" "+tgtrs+" "+amont);
			    	}	
			    	else {
			    		//li.remove();
			    	}
				}	
			}
			stmt = con.prepareStatement("select * from CIVITAS where NPMHSMSMHS=?");
			li1 = v1.listIterator();
			int i = 0;
			while(li1.hasNext()) {
				i++;
				String brs = (String)li1.next();
				//System.out.println(brs);
				StringTokenizer st = new StringTokenizer(brs);
				String npmhs = st.nextToken();
				String tgtrs = st.nextToken();
				String amont = st.nextToken();
				stmt.setString(1,npmhs);
				rs = stmt.executeQuery();
				rs.next();
				String nmmhs = ""+rs.getString("NMMHSMSMHS");
				li1.set(npmhs+","+nmmhs+" "+tgtrs+" "+amont);
				System.out.println(npmhs+","+nmmhs+" "+tgtrs+" "+amont);
				//System.out.println(i+"."+npmhs+","+nmmhs+" "+tgtrs+" "+amont);
				
			}
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void createRandomUsrPwd() {
		
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replace("-","");
		System.out.println(uuid);
		Vector v = new Vector();
		ListIterator li = v.listIterator();
		for(int i=0;i<uuid.length();i++) {
			int j = i+1;
			if(j<uuid.length()) {
				li.add(uuid.substring(i, j));
			}
		}
		String nuid="";
		Collections.shuffle(v);
		li = v.listIterator();
		while(li.hasNext()) {
			nuid=nuid+(String)li.next();
		}
		String usrPed = nuid.substring(0,8)+" "+nuid.substring(10,18);
		System.out.println("uuid = " + nuid);
		System.out.println(usrPed);
	}
	
	public static void encrypt_decript(String str) {
		try {
			String text = str;
			String key = "Bar12345Bar12345"; // 128 bit key
			// Create key and cipher
			Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			// encrypt the text
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encrypted = cipher.doFinal(text.getBytes());
			String tmp = new String(encrypted);
			System.out.println("tmp="+tmp);
			byte[] encrypted2 = cipher.doFinal(tmp.getBytes());
			System.err.println(new String(encrypted));
			System.err.println(new String(encrypted2));
			
			// decrypt the text
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			
			String decrypted = new String(cipher.doFinal(encrypted));
			System.err.println(decrypted);
			String decrypted2 = new String(cipher.doFinal(encrypted2));
			
			System.err.println(decrypted2);
		}	catch(Exception e) {
			e.printStackTrace();
		}
    }
	
	public static String encrypt(String str) {
		String tmp = null;
		try {
			//String text = "Hello World";
			String key = "Bar12345Bar12345"; // 128 bit key
			// Create key and cipher
			Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			// encrypt the text
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encrypted = cipher.doFinal(str.getBytes());
			//System.err.println(new String(encrypted));
			tmp = new String(encrypted, "ISO-8859-1");
			//tmp = new String();
			// decrypt the text
			//cipher.init(Cipher.DECRYPT_MODE, aesKey);
			//String decrypted = new String(cipher.doFinal(encrypted));
			//System.err.println(decrypted);
		}	catch(Exception e) {
			e.printStackTrace();
		}
		return tmp;
    }
	
	public static String decrypt(String encrypted_str) {
		String decrypted = null;
		try {
			//String text = "Hello World";
			String key = "Bar12345Bar12345"; // 128 bit key
			// Create key and cipher
			Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			// encrypt the text
			//cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			//byte[] encrypted = cipher.doFinal(encrypted_str.getBytes());
			//byte[] encrypted = encrypted_str.getBytes();
			//System.err.println(new String(encrypted));
			// decrypt the text
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			//decrypted = new String(cipher.doFinal(encrypted_str.getBytes("ISO-8859-1")));
			decrypted = new String(cipher.doFinal(encrypted_str.getBytes("ISO-8859-1")));
			//System.err.println(decrypted);
		}	catch(Exception e) {
			e.printStackTrace();
		}
		return decrypted;
    }

	public static String decrypt2(Blob bl, String key) {
		String decrypted = null;
		try {
			//String text = "Hello World";
			//String key = "Bar12345Bar12345"; // 128 bit key
			// Create key and cipher
			//Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			// encrypt the text
			//cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			//byte[] encrypted = cipher.doFinal(encrypted_str.getBytes());
			//byte[] encrypted = encrypted_str.getBytes();
			//System.err.println(new String(encrypted));
			// decrypt the text
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			//decrypted = new String(cipher.doFinal(encrypted_str.getBytes("ISO-8859-1")));
			decrypted = new String(cipher.doFinal(bl.getBytes(1, (int) bl.length())));
			//System.err.println(decrypted);
		}	catch(Exception e) {
			e.printStackTrace();
		}
		return decrypted;
    }

	
	public static void insUsrPwd(String key, String usr, String pwd) {
	
		try {
			connectToQapla();
			
			stmt =  con.prepareStatement("INSERT into ENIGMA (EMAIL,USR, PWD) VALUES (?,AES_ENCRYPT(?,?),AES_ENCRYPT(?,?));");
			stmt.setString(1, "me@me");
			stmt.setString(2, usr);
			stmt.setString(3, key);
			stmt.setString(4, pwd);
			stmt.setString(5, key);
			stmt.executeUpdate();
			
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}	
	
	public static String  getPwd(String key, String pwd) {
		String tmp = null;
		try {
			connectToQapla();
			//System.out.println("tmp0="+tmp);
			//stmt =  con.prepareStatement("select USR,PWD from ENIGMA where  PWD=AES_ENCRYPT('dinggo4','Bar12345Bar12345')");
			stmt =  con.prepareStatement("select USR,PWD from ENIGMA where  PWD=AES_ENCRYPT(?,?)");
			stmt.setString(1, pwd);
			stmt.setString(2, key);
			rs = stmt.executeQuery();
			if(rs.next()) {
				Blob blob = rs.getBlob("PWD");
				
				System.out.println("tmp1="+decrypt2(blob, key));
				//System.out.println(decrypt(rs.getString(2)));
				System.out.println("tmp2="+tmp);
				//tmp = decrypt(tmp);
				System.out.println("tmp3="+tmp);
			}
			
			//stmt.executeUpdate();
			
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return tmp;
	}	
	
	
	public static void insertBlobPwd() throws Exception {
		try {
			Vector v = new Vector();
			ListIterator li = v.listIterator();
			connectToQapla();
			/*
			 * stmt = con.prepareStatement("select ID,USR_PWD from USR_DAT");
			 
			rs = stmt.executeQuery();
			while(rs.next()) {
				li.add(rs.getLong("ID")+";"+rs.getString("USR_PWD"));
			}
			li = v.listIterator();
																			   
			*/
			//while(li.hasNext()) {
			//	String tmp = (String)li.next();
			//	StringTokenizer st = new StringTokenizer(tmp,";");
			//	String id = st.nextToken();
			//	String pwd = st.nextToken();
				stmt = con.prepareStatement("update ENIGMA set PWD=AES_ENCRYPT(?,?) where EMAIL=?");
				stmt.setString(1,"berhasil2");
				stmt.setString(2,"Arrahm4nNirrah1m");
				stmt.setString(3,"oke");
				System.out.println("update="+stmt.executeUpdate());
		//	}
			//stmt = con.prepareStatement("INSERT INTO ENIGMA (EMAIL,USR,PWD) VALUES ('fsetiaw@gmail.com',AES_ENCRYPT('dinggo3','Bar12345Bar12345'),AES_ENCRYPT('dinggo4','Bar12345Bar12345'))");
			//stmt.executeUpdate();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			if (rs!=null) try  { rs.close(); } catch (Exception ignore){}
		    if (stmt!=null) try  { stmt.close(); } catch (Exception ignore){}
		    if (con!=null) try { con.close();} catch (Exception ignore){}
		}
	}
	
	
	public static void insertBlobPwdUsg() throws Exception {
		String key = "Arrahm4nNirrah1m";
		try {
			Vector v = new Vector();
			ListIterator li = v.listIterator();
			connectToMysql();
			
			stmt = con.prepareStatement("select ID,USR_PWD from USR_DAT");
			 
			rs = stmt.executeQuery();
			while(rs.next()) {
				li.add(rs.getLong("ID")+";"+rs.getString("USR_PWD"));
			}
			li = v.listIterator();
																			   
			stmt = con.prepareStatement("update USR_DAT set PWD=AES_ENCRYPT(?,?) where ID=?");
			while(li.hasNext()) {
				String tmp = (String)li.next();
				StringTokenizer st = new StringTokenizer(tmp,";");
				String id = st.nextToken();
				String pwd = st.nextToken();
			
				stmt.setString(1,pwd);
				stmt.setString(2,key);
				stmt.setLong(3,Long.parseLong(id));
				System.out.println(pwd+" update="+stmt.executeUpdate());
			}
			//stmt = con.prepareStatement("INSERT INTO ENIGMA (EMAIL,USR,PWD) VALUES ('fsetiaw@gmail.com',AES_ENCRYPT('dinggo3','Bar12345Bar12345'),AES_ENCRYPT('dinggo4','Bar12345Bar12345'))");
			//stmt.executeUpdate();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			if (rs!=null) try  { rs.close(); } catch (Exception ignore){}
		    if (stmt!=null) try  { stmt.close(); } catch (Exception ignore){}
		    if (con!=null) try { con.close();} catch (Exception ignore){}
		}
	}

	
	public static String  getPwdUsg(String pwd) {
		String key = "Arrahm4nNirrah1m";
		String tmp = null;
		try {
			connectToMysql();
			//System.out.println("tmp0="+tmp);
			//stmt =  con.prepareStatement("select USR,PWD from ENIGMA where  PWD=AES_ENCRYPT('dinggo4','Bar12345Bar12345')");
			stmt =  con.prepareStatement("select PWD from USR_DAT where  USR_PWD=?");
			stmt.setString(1, pwd);
			//stmt.setString(2, key);
			rs = stmt.executeQuery();
			if(rs.next()) {
				Blob blob = rs.getBlob("PWD");
				
				System.out.println("tmp1="+decrypt2(blob, key));
				//System.out.println(decrypt(rs.getString(2)));
				//System.out.println("tmp2="+tmp);
				//tmp = decrypt(tmp);
				//System.out.println("tmp3="+tmp);
			}
			
			//stmt.executeUpdate();
			
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return tmp;
	}
	public static void main(String[]args) throws Exception {
	//	System.out.println(12 % 15);
	//	System.out.println(13 % 15);
	//	System.out.println(14 % 15);
	//	System.out.println(15 % 15);
	//	System.out.println(16 % 15);
	//	System.out.println(17 % 15);
		//encrypt_decript("hello1");
		
		//String test = encrypt("dinggo");
		//System.out.println("encrypted="+test);
		//test = decrypt(test);
		//System.out.println("decrypted="+test);
		//insUsrPwd("Bar12345Bar12345", "binggo", "binggo");
		//System.out.println(getPwd("Bar12345Bar12345", "dinggo3"));
		//insUsrPwd("donkey", "namaku", "cleo");
		//insertBlobPwd();
		//insertBlobPwdUsg();
		//getPwd("Bar12345Bar12345","dinggo4");
		//getPwdUsg("usgbersatu");
		String test = "811,KDK 1142,PERALATAN DAPUR, RESTORAN & BAR,2,0,0,2,";
		System.out.println(test.replaceAll(",", "tandaKoma"));
		java.sql.Time bt = java.sql.Time.valueOf("0:0:0");
		System.out.println(bt.toString());
		
		String thsms1 = "20121";
		String thsms2 = "null";
		String thsms3 = "20131";
		System.out.println(thsms1.compareToIgnoreCase(thsms2));
		System.out.println(thsms2.compareToIgnoreCase(thsms3));
		System.out.println(thsms3.compareToIgnoreCase(thsms1));
		//getPwd("donkey","cleo");
	}

}
