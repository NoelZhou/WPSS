package com.hy.bean;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
/**
 *
 * @author Administrator
 *
 */
public class AESUtils {
 
	
	/**
	 * 加密
	 * @param sSrc 十六进制的字符串，如：50 c3 12 30 53 6f 6c 61 72 49
	 * @param sKey 加密秘钥
	 * @return 返回加密后数据， 如：93 96 3B 38 A8 62 58 E4 B8 21 0A
	 * @throws Exception
	 */
	public static String encrypt(String sSrc, String sKey) {
        if (sKey == null) {
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            return null;
        }
        sSrc = sSrc.replaceAll(" ", "");
        sSrc = addSpaceIntoStr(sSrc);
		try {
			byte[] raw = sKey.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");//"算法/模式/补码方式"
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	        //十六进制转为byte数组
	        byte[] content_byte =hexStr2ByteArr(sSrc," ");
	        byte[] original = cipher.doFinal(content_byte);
	        String str = bytesToHexString(original, " ");
	        return str;
		} catch (Exception e) {
		  System.out.print("加密出错了。。。。。。");
		} 
        return null;
    }
	
    // 加密
    public static byte[] Encrypt(byte[] sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        //16的倍数
        byte [] ss=sSrc;
  	    int ct= (ss.length)%16;
  	     int ct2= (ss.length)/16;
  	     int all=0;
  	     if(ct>0){
  	    	  all=(ct2+1)*16;
  	      }
        byte [] newstr=new byte[all];
        System.arraycopy(ss, 0, newstr, 0, ss.length); 
        //不足的补位
     	  for(int i=ss.length;i<all;i++){
     		 newstr[i]=-1;
  	     }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/noPadding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(newstr);
        return encrypted;
    }
 
	// 解密
	public static byte[] Decrypt(byte[] contenxt, String sKey) throws Exception {
		try {
			SecretKeySpec key = new SecretKeySpec(sKey.getBytes(), "AES");
			//Cipher cipher = Cipher.getInstance("AES/ECB/noPadding");PKCS5Padding
			Cipher cipher = Cipher.getInstance("AES/ECB/noPadding");
			// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(contenxt);
			return result; // 解密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

    // 解密
    public static byte[] Decryptshort(byte[] contenxt, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
           SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
           Cipher cipher = Cipher.getInstance("AES/ECB/noPadding");
           cipher.init(Cipher.DECRYPT_MODE, skeySpec);
           short []tmpshort=new short[contenxt.length];
           byte[] original = new byte[contenxt.length];
           original =cipher.doFinal(contenxt, 0, contenxt.length);
           return original;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
            return null;
        }
    }
    public static String removeSpace(String data) {
        return data.replaceAll(" ", "");
    }
   public static String addSpaceIntoStr(String data) {
       char[] dataChar = data.toCharArray();
       StringBuffer sb = new StringBuffer();
       for (int i = 0; i < dataChar.length; i++) {
           char c = dataChar[i];
           sb.append(c);
           if (i % 2 != 0) {
               sb.append(" ");
           }
       }
       return sb.toString().trim();
   }
    /**
     * 解密
     * @param sSrc 十六进制字符串
     * @param sKey 秘钥
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String sKey) {
    	sSrc =removeSpace(sSrc);
    	sSrc = addSpaceIntoStr(sSrc);
        try {
            // 判断Key是否正确
            if (sKey == null) {
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            try {
                //十六进制转为byte数组
                byte[] content_byte = hexStr2ByteArr(sSrc," ");
                byte[] original = cipher.doFinal(content_byte);
                String str = bytesToHexString(original, " ");
                return str;
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }
    
    /**
     * 将byte字节数组转化为16进制的数字
     *
     * @param bArray
     * @param delimiter 分隔符
     * @return
     */
    public static String bytesToHexString(byte[] bArray, String delimiter) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase() + delimiter);
        }
        if (sb.indexOf(delimiter) > 0) {
            sb = new StringBuffer(sb.substring(0, sb.lastIndexOf(delimiter)));
        }
        return sb.toString();
    }
    /**
     * 将16进制的4个字节的字符串转化为byte数组
     *
     * @param str
     * @param delimiter 分隔符
     * @return
     */
    public static byte[] hexStr2ByteArr(String str, String delimiter) {
        String[] s = str.split(delimiter);
        byte[] b = new byte[s.length];
        for (int i = 0; i < s.length; i++) {
            b[i] = (byte) Integer.parseInt(s[i], 16);
        }
        return b;
    }

    
 /*   // 解密
    public static String Decryptone(String contenxt, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
           // byte[] iv=
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec,new IvParameterSpec(raw));
            byte[] encrypted1 = new Base64().decode(contenxt);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
//                String originalString = new String(original,"utf-8");
                return original;
            } catch (Exception e) {
            	e.printStackTrace();
                return null;
            }
            
            byte[] ret=decode(Base64.decodeBase64(contenxt),sKey);
           String ss=new String(ret,"YTF-8");
            System.out.println(ss);
        } catch (Exception ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
            return null;
        }
    }*/
    /**
	 * 解密
	 * @param byteSrc byte字节数组
	 * @param sKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] byteSrc, String password) throws Exception {
		try {
			// 判断Key是否正确
			if (password == null) {
				System.out.print("Key为空null");
				return null;
			}
			byte[] raw = password.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			try {
				byte[] original = cipher.doFinal(byteSrc);
				return original;
			} catch (Exception e) {
				//logger.error("解密出错："+e.getMessage());
				e.printStackTrace();
				return null;
			}
		} catch (Exception ex) {
		//	logger.error("解密出错："+ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
    public static void main(String[] args) throws Exception {
    	String text1="53 4e 41 31 34 30 35 30 36 39 38 37 2f 50 61 73 73 77 6f 72 64 5f 32 30 31 35 30 39 31 38 31 31 35 30 34 37 33 36 32";
        String text="31 53 4e 41 31 34 30 35 30 36 39 38 37 2f 50 61 73 73 77 6f 72 64 5f 32 30 31 35 30 39 31 39 31 30 30 31 32 36 35 33 30";
    	/*    System.out.println(text.split(" ").length);
    	String ss=encrypt(text, "sungrow-2016102");
    	System.out.println(ss);*/
    	
        String[] cdhdata=text.split(" ");
        String[] cdhdata1=text1.split(" ");
		byte[] cdhbyte=new byte[cdhdata.length];
		for(int i=0;i<cdhbyte.length;i++){
			cdhbyte[i]=(byte) Integer.parseInt(cdhdata[i],16);
		}
		String cdhdatastr = new String(cdhbyte);
		System.out.println(cdhdatastr);
		
		
		byte[] cdhbyte1=new byte[cdhdata1.length];
		for(int i=0;i<cdhbyte1.length;i++){
			cdhbyte1[i]=(byte) Integer.parseInt(cdhdata1[i],16);
		}
		String cdhdatastr1 = new String(cdhbyte1);
		System.out.println(cdhdatastr1);
    /*	String[] textstr=text.split(" ");
        byte[] BYTESTR=new byte[textstr.length];
        for(int i=0;i<textstr.length;i++){
        	BYTESTR[i]=(byte)Integer.parseInt(textstr[i],16);
        }
        
        byte[] tmpbyte= decrypt(BYTESTR,"sungrow-20151216");
        String texttmp="";
        for(int k=0;k<tmpbyte.length;k++){
        	texttmp+=Integer.toHexString(tmpbyte[k])+" ";
         }
        System.out.println(texttmp);*/
        }
    	/* byte[] decoded = text.getBytes("utf-8");
    	 byte[] decodednew = new Base64().decode(decoded);
    	  short[] tmp1=new short[decodednew.length];
    	byte[] plainText = decodednew;
    	 for(int i=0;i<plainText.length;i++){
 			if(plainText[i]<0){
 				tmp1[i]=(short)(plainText[i]&0x00ff);
 			}else{
 				tmp1[i]=plainText[i];
 			}
     }
    	String sKey="sdkjsdkj12987391";
        byte[] raw = sKey.getBytes("utf-8");
        IvParameterSpec ss=new IvParameterSpec(raw);
        KeyGenerator kgen=KeyGenerator.getInstance("AES");
        kgen.init(128,new SecureRandom(raw));
        SecretKey skey=kgen.generateKey();
        byte [] encode=skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(encode, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/noPadding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] original = new byte[plainText.length];
        original =cipher.doFinal(plainText);
        String str=new String (original);
        short[] tmp=new short[original.length];
        for(int i=0;i<original.length;i++){
			if(original[i]<0){
				tmp[i]=(short)(original[i]&0x00ff);
			}else{
				tmp[i]=original[i];
			}
    }
}*/}