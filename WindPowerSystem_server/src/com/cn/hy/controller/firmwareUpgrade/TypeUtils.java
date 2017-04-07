package com.cn.hy.controller.firmwareUpgrade;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class TypeUtils {
	
	/**
	 * int转byte
	 * @param l
	 * @return
	 */
	public static byte[] int2Byte(int l) {
		byte[] b = new byte[4];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Integer(l).byteValue();
			l = l >> 8;
		}
		return b;
	}

	public static long[] byte2Intstr(byte[] b) {
		int ll=b.length/4;
		if(b.length%4>0){
			ll++;
		}
		long[] int32=new long[ll];
		for(int i=0;i<ll;i++){
			byte[] tmp=new byte[4];
			tmp[0]=b[i*4];
			tmp[1]=b[i*4+1];
			tmp[2]=b[i*4+2];
			tmp[3]=b[i*4+3];
			int k=byte2Int(tmp,0);
			int32[i]=k;
			if(k>=0){
				int32[i]=k;
			}else{
		      long lk=byteToLongnew(tmp);
			  int32[i]=lk;
			}
		}
		return int32;
	}
	/**
	 * byte转long
	 * @param b
	 * @return
	 */
	public static long byteToLongnew(byte[] b) {
		long l = 0;
		l |= (((long) 0 & 0xff) << 56);
		l |= (((long)0 & 0xff) << 48);
		l |= (((long)0 & 0xff) << 40);
		l |= (((long)0 & 0xff) << 32);
		l |= (((long) b[3] & 0xff) << 24);
		l |= (((long) b[2] & 0xff) << 16);
		l |= (((long) b[1] & 0xff) << 8);
		l |= ((long) b[0] & 0xff);
		return l;
	}
	/**
	 * byte转int
	 * @param b
	 * @return
	 */
	public static int byte2Intnew(byte[] b, int startIndex) {
		int l = 0;
		l = b[startIndex + 0];
		l &= 0xff;
		l |= ((int) b[startIndex + 1] << 8);
		l &= 0xffff;
		l |= ((int) b[startIndex + 2] << 16);
		l &= 0xffffff;
		l |= ((int) b[startIndex + 3] << 24);
		l &= 0x7fffffff;
		return l;
	}

	/**
	 * byte转int
	 * @param b
	 * @return
	 */
	public static int byte2Int(byte[] b, int startIndex) {
		int l = 0;
		l = b[startIndex + 0];
		l &= 0xff;
		l |= ((int) b[startIndex + 1] << 8);
		l &= 0xffff;
		l |= ((int) b[startIndex + 2] << 16);
		l &= 0xffffff;
		l |= ((int) b[startIndex + 3] << 24);
		l &= 0xffffffff;
		return l;
	}

	/**
	 * long转byte
	 * @param l
	 * @return
	 */
	public static byte[] longToByte(long l) {
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(l).byteValue();
			l = l >> 8;
		}
		return b;
	}

	/**
	 * byte转long
	 * @param b
	 * @return
	 */
	public static long byteToLong(byte[] b) {
		long l = 0;
		l |= (((long) b[7] & 0xff) << 56);
		l |= (((long) b[6] & 0xff) << 48);
		l |= (((long) b[5] & 0xff) << 40);
		l |= (((long) b[4] & 0xff) << 32);
		l |= (((long) b[3] & 0xff) << 24);
		l |= (((long) b[2] & 0xff) << 16);
		l |= (((long) b[1] & 0xff) << 8);
		l |= ((long) b[0] & 0xff);
		return l;
	}

	/**
	 * float转byte
	 * @param f
	 * @return
	 */
	public static byte[] float2Byte(float f) {
		byte[] b = new byte[4];
		int l = Float.floatToIntBits(f);
		for (int i = 0; i < b.length; i++) {
			b[i] = new Integer(l).byteValue();
			l = l >> 8;
		}

		return b;
	}

	/**
	 * byte转float
	 * @param b
	 * @return
	 */
	public static float byte2Float(byte[] b) {
		int l = 0;
		l = b[0];
		l &= 0xff;
		l |= ((int) b[1] << 8);
		l &= 0xffff;
		l |= ((int) b[2] << 16);
		l &= 0xffffff;
		l |= ((int) b[3] << 24);
		l &= 0xffffffffl;
		return Float.intBitsToFloat(l);
	}

	/**
	 * double转byte
	 * @param d
	 * @return
	 */
	public static byte[] doubleToByte(double d) {
		byte[] b = new byte[8];
		long l = Double.doubleToLongBits(d);
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(l).byteValue();
			l = l >> 8;
		}
		return b;
	}

	/**
	 * byte转chars
	 * @param bytes
	 * @param offset
	 * @param count
	 * @return
	 */
	public static char[] bytesToChars(byte[] bytes, int offset, int count) {
		char chars[] = new char[count];
		for (int i = 0; i < count; i++) {
			chars[i] = (char) bytes[i];
		}
		return chars;
	}

	/**
	 * chars转byte
	 * @param chars
	 * @param offset
	 * @param count
	 * @return
	 */
	public static byte[] charsToBytes(char[] chars, int offset, int count) {
		byte bytes[] = new byte[count];
		for (int i = 0; i < count; i++) {
			bytes[i] = (byte) chars[i];
		}
		return bytes;
	}

	/**
	 * float转byte
	 * @param v
	 * @return
	 */
	public static byte[] floatToByte(float v) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		byte[] ret = new byte[4];
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(v);
		bb.get(ret);
		return ret;
	}

	/**
	 * byte转float
	 * @param v
	 * @return
	 */
	public static float byteToFloat(byte[] v) {
		ByteBuffer bb = ByteBuffer.wrap(v);
		FloatBuffer fb = bb.asFloatBuffer();
		return fb.get();
	}
	
	/**
	 * //合并字节位换成一个16位的整形数,short类
	 * 
	 * @param bytes
	 * @param off
	 * @return
	 */
	public static short byte4ToInt(byte[] bytes, int off) {
		int b0 = bytes[off] & 0xFF;
		int b1 = bytes[off + 1] & 0xFF;
		short ii = (short) ((b0 << 8) | b1);
		return ii;
	}
	
	/**
	 * 字符串转16进制byte
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToByte(String hexString) {
		hexString = hexString.replace(" ", "");  
        if ((hexString.length() % 2) != 0){
            hexString += " ";
        }
	    byte[] result = new byte[hexString.length() / 2];
	    for (int i = 0; i < hexString.length() / 2; ++i) {
		        result[i] = (byte)(Integer.parseInt(hexString.substring(i * 2, i * 2 +2), 16) & 0xff);
	    }
	    return result;
	}

	public String gotolonghex4(String intstr1) {
		int i=intstr1.length();
		if(i==0){
			intstr1+="00000000";
		}else if(i==1){
			intstr1="0000000"+intstr1;
		}else if(i==2){
			intstr1="000000"+intstr1;
		}else if(i==3){
			intstr1="00000"+intstr1;
		}else if(i==4){
			intstr1="0000"+intstr1;
		}else if(i==5){
			intstr1="000"+intstr1;
		}else if(i==6){
			intstr1="00"+intstr1;
		}else if(i==7){
			intstr1="0"+intstr1;
		}
		return intstr1;
		
	}
}
