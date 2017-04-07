package com.zhta.data;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.Uninterruptibles;
import com.zhta.pojo.Device;

import static com.google.common.util.concurrent.Service.State.NEW;

public class SungrowService extends AbstractService implements Service {
	private State state = NEW;
	private Device dev;
	private static boolean isrun = true;
	SungrowService(Device device) {
		this.dev = device;
	}
	public State stopAndWait() {
		return state;
	}

	@Override
	protected void doStart() {
		new Thread() {
			@Override
			public void run() {
				notifyStarted();
				isrun = true;
				while (isrun) {
					Map<Integer, Device> devicelist = TextThd.startDataAcq();
					int acq_real_time = DataAcqAction.getacq_real_time();//获取实时数据的采集频率
					Uninterruptibles.sleepUninterruptibly(acq_real_time * 1000, TimeUnit.MILLISECONDS);
					boolean iscz = false;
					for (int num = 0; num < devicelist.size(); num++) {
						Device devone = devicelist.get(num);
						if (devone.getIp().equals(dev.getIp()) && devone.getDevice_id() == (dev.getDevice_id())) {
							iscz = true;
							break;
						}
					}
					if (iscz == true) {
						DataAcqAction.startthreadother(dev);//开始设备数据采集
					} else {
						System.out.println(dev.getDevice_id() + "-" + dev.getIp() + ":已经被删除，不采集");
						DataAcqAction.updatedevicestate(dev.getDevice_id(), "设备删除");
						isrun = false;
					}

				}
			}
		}.start();
	}

	@Override
	protected void doStop() {
		notifyStopped();
		isrun=false;
		System.out.println("已经停止：" + dev.getDevice_id());
	}
}