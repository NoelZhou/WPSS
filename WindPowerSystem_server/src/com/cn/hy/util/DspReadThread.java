package com.cn.hy.util;

public class DspReadThread implements Runnable{
	
	/**
	 * 通信端口
	 */
	String ip;
	
	/**
	 * 端口
	 */
	int port;
	
	/**
	 * DSP反馈字节长度
	 */
	int length;
	
	/**
	 * 波形集合
	 */
	String[] waveformCodes;
	
	public DspReadThread(String ip, int port, int length, String[] waveformCodes){
		this.ip = ip;
		this.port = port;
		this.length = length;
		this.waveformCodes = waveformCodes;
	}
	
	@Override
	public void run() {
		DSPTcpUtils.getData(ip, port, length, "A8", "00", waveformCodes);
	}
	
	public static void start(String[] ip, int[] port, int[] length, String[] waveformCodes){
		
		DspReadThread dsp1 = new DspReadThread(ip[0], port[0], length[0], waveformCodes[0].split(","));
		Thread dspT1= new Thread(dsp1);
		dspT1.start();
		
		DspReadThread dsp2 = new DspReadThread(ip[1], port[1], length[1], waveformCodes[1].split(","));
		Thread dspT2= new Thread(dsp2);
		dspT2.start();
	}
	
	public static void main(String[] args) {
		String[] waveformCodes = new String[10];
		String waveformCode1 = "A8,00,01,02,03,04,05,06,07,08";
		String waveformCode2 = "A8,00,01,02,03,04,05,06,07,08";
		waveformCodes[0] = waveformCode1;
		waveformCodes[1] = waveformCode2;
		
		DspReadThread.start(new String[]{"192.168.68.236", "192.168.68.237"}, new int[]{503, 503}, new int[]{66, 66}, waveformCodes);
	}
}
