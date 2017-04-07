package com.cn.hy.controller.firmwareUpgrade;

public class Crc32tmp {
    private static long[] crc32Table = new long[256];
 
    static {
        long crcValue;
        for (int i = 0; i < 256; i++) {
            crcValue = i;
            for (int j = 0; j < 8; j++) {
                if ((crcValue & 1) == 1) {
                    crcValue = crcValue >> 1;
                    crcValue = 0x00000000edb88320L ^ crcValue;
                } else {
                    crcValue = crcValue >> 1;
 
                }
            }
            crc32Table[i] = crcValue;
        }
 
    }
 
    public static long getCrc32(byte[] bytes) {
        long resultCrcValue = 0x00000000ffffffffL;
        for (int i = 0; i < bytes.length; i++) {
            int index = (int) ((resultCrcValue ^ bytes[i]) & 0xff);
            resultCrcValue = crc32Table[index] ^ (resultCrcValue >> 8);
        }
        resultCrcValue = resultCrcValue ^ 0x00000000ffffffffL;
        return resultCrcValue;
    }
    public static long unsigned32ByBytes(byte[] bytes) {
        long value = 0;
        int byteLen = bytes.length;
        // 由高位到低位
        for (int i = 0; i < byteLen; i++) {
            int shift = (byteLen - 1 - i) * 8;
            value += (long) (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }
    public static void main(String[] args) {
      /*  String testStr = "asfkjfkasdjfkldjfhdjfhasdjkfj;asdkljfk;asdjfcnnd";
        java.util.zip.CRC32 jdkCrc32 = new java.util.zip.CRC32();
        jdkCrc32.update(testStr.getBytes());
        System.out.println("jdk  crc32: " + jdkCrc32.getValue());
        System.out.println("test crc32: " + getCrc32(testStr.getBytes()));*/
         	long[] InputData={4294967295l}; int len=1;
    		int dwPolynomial = 0x04c11db7;

    		long xbit;
    		long data;
    		long crc_cnt = 0x00000000FFFFFFFFl; // 4294967295
    		for (int i = 0; i < len; i++) {
    			xbit = (long) 0x0000000080000000l;//2147483648
    			data = InputData[i];
    			for (int bits = 0; bits < 32; bits++) {
    				if ((crc_cnt & 0x80000000) > 0) {
    					crc_cnt <<= 1;
    					crc_cnt=crc_cnt&0x00000000FFFFFFFFl;
    					crc_cnt ^= dwPolynomial;//4215202377
    				} else {
    					crc_cnt <<= 1;
    					crc_cnt=crc_cnt&0x00000000FFFFFFFFl;
    				}
    				if ((data & xbit) > 0) {
    					crc_cnt ^= dwPolynomial;//4215202377
    				}
    				xbit >>= 1;//1073741824
    			}
    		}
    	
    	}
    
}