package com.common.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputDevice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class DeviceUtils {

	public static String gpu;

	public static final String m_szDevIDShort = "35"
			+ // we make this look like a valid IMEI

			Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length()
			% 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10
			+ Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
			+ Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10; // 13
																								// digits

	public static final String getModel() {
		return Build.MODEL;
	}

	public static final String getManufacturer() {
		try {
			String manufacturer = Build.MANUFACTURER;
			char a = manufacturer.charAt(0);
			if (Character.isUpperCase(a)) {
				return manufacturer;
			} else {
				return Character.toUpperCase(a) + manufacturer.substring(1);
			}
		} catch (Exception e) {
		}
		return null;
	}

	public static final String getSDKVersion() {
		return Build.VERSION.RELEASE;
	}

	public static DisplayMetrics getScreenSize(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	public static String getDeviceInfo(Context context) {
		if ("000000000000000".equals(getDeviceId(context))) {
			return Utils.getAndroidId(context);
		} else {
			return getDeviceId(context);
		}
	}

	public static String getImsi(Context context) {
		try {
			TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String imsi = mTelephonyMgr.getSubscriberId();
			return imsi;
		} catch (Exception e) {
		}
		return "000000000000000";
	}

	public static String getDeviceId(Context context) {
		String imei = null;
		if (context == null) {
			return "000000000000000";
		}
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (tm != null) {
				imei = tm.getDeviceId();
				if (StringUtil.isEmpty(imei)) {
					imei = "000000000000000";
				}
			} else {
				imei = "000000000000000";
			}

		} catch (Exception e) {
			imei = "000000000000000";
		}
		return imei;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static String getUuid(Context context) {
		TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String m_szImei = TelephonyMgr.getDeviceId();

		String m_szAndroidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();

		BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
		m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		String m_szBTMAC = m_BluetoothAdapter.getAddress();

		String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID + m_szWLANMAC + m_szBTMAC;
		// compute md5
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
		// get md5 bytes
		byte p_md5Data[] = m.digest();
		// create a hex string
		String m_szUniqueID = new String();
		for (int i = 0; i < p_md5Data.length; i++) {
			int b = (0xFF & p_md5Data[i]);
			// if it is a single digit, make sure it have 0 in front (proper
			// padding)
			if (b <= 0xF)
				m_szUniqueID += "0";
			// add number to string
			m_szUniqueID += Integer.toHexString(b);
		} // hex string to uppercase
		m_szUniqueID = m_szUniqueID.toUpperCase();

		return m_szUniqueID;
	}

	public static int getScreenWidthPixels(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * get ram size
	 *
	 * @return
	 */
	public static long getTotalMemory() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return initial_memory / (1024 * 1024);
	}

	/**
	 * get cpu frequency
	 *
	 * @return
	 */
	public static String getMaxCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}

	public static int loadImageSize(Activity activity) {
		int widthPixels;

		try {
			widthPixels = getScreenWidthPixels(activity);
		} catch (Exception e) {
			return -1;
		}

		if (widthPixels >= 720) {
			return -1;
		} else {
			int size = ((int) widthPixels / 80) * 80;
			if (widthPixels % 80 != 0) {
				size += 80;
			}
			if (size > 640) {
				return -1;
			} else {
				if (size < 80) {
					size = 80;
				}
				return size;
			}
		}
	}

	public static String getGpu() {
		return gpu;
	}

	public static void setGpu(String gpu) {
		DeviceUtils.gpu = gpu;
	}

	public static String getBrand() {
		return Build.BRAND;
	}

	public static String getHardware() {
		return Build.HARDWARE;
	}

	public static String getImsiForAntiCheating(Context context) {
		String imsi = null;
		if (context == null) {
			return "100000000000001";
		}
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (tm != null) {
				imsi = tm.getSubscriberId();
				if (StringUtil.isEmpty(imsi)) {
					imsi = "100000000000001";
				}
			} else {
				imsi = "100000000000001";
			}

		} catch (Exception e) {
			imsi = "100000000000001";
		}
		return imsi;
	}

	public static String getBoard() {
		return Build.BOARD;
	}

	public static String getCpuInfo() {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader("/proc/cpuinfo");
			br = new BufferedReader(fr);
			String text = br.readLine();
			String[] array = text.split(":\\s+", 2);
			return array[1];
		} catch (Exception e) {
		} finally {
			CloseUtil.closeQuietly(fr);
			CloseUtil.closeQuietly(br);
		}
		return null;
	}

	public static long getMemorySize() {
		FileInputStream fis = null;
		BufferedReader bufferedReader = null;
		try {
			fis = new FileInputStream(new File("/proc/meminfo"));
			bufferedReader = new BufferedReader(new InputStreamReader(fis));
			String memTotal = bufferedReader.readLine();
			StringBuffer sb = new StringBuffer();
			for (char c : memTotal.toCharArray()) {
				if (c >= '0' && c <= '9') {
					sb.append(c);
				}
			}
			long totalMemory = Long.parseLong(sb.toString()) * 1024;
			return totalMemory;
		} catch (Exception e) {
			return 0;
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static List getInputDevice() {
		try {
			List<String> list = new ArrayList<>();
			if (hasMouse()) {
				list.add("mouse");
			}
			if (hasTouchScreen()) {
				list.add("touchscreen");
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static boolean hasMouse() {
		try {
			int[] deviceIds = InputDevice.getDeviceIds();
			for (int deviceId : deviceIds) {
				InputDevice inputDevice = InputDevice.getDevice(deviceId);
				int sources = inputDevice.getSources();
				if (((sources & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE)) {
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	private static boolean hasTouchScreen() {
		try {
			int[] deviceIds = InputDevice.getDeviceIds();
			for (int deviceId : deviceIds) {
				InputDevice inputDevice = InputDevice.getDevice(deviceId);
				int sources = inputDevice.getSources();
				if (((sources & InputDevice.SOURCE_TOUCHSCREEN) == InputDevice.SOURCE_TOUCHSCREEN)) {
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean isRoot() {
		String buildTags = android.os.Build.TAGS;
		if (buildTags != null && buildTags.contains("test-keys")) {
			return true;
		}
		String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su",
				"/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su",
				"/data/local/su", "/su/bin/su"};
		for (String path : paths) {
			if (FileUtils.isFileExists(path)) {
				return true;
			}
		}
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			if (in.readLine() != null)
				return true;
			return false;
		} catch (Throwable t) {
			return false;
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
	}

	public static List getSimulatorFile() {
		String[] paths = {"/dev/socket/qemud", "/dev/qemu_pipe", "/dev/qemu_trace"};
		List<String> list = new ArrayList<>();
		for (String path : paths) {
			if (FileUtils.isFileExists(path)) {
				list.add(path);
			}
		}
		return list;
	}

	public static List<String> getSensorList(Context context) {
		List<String> result = new ArrayList<>();
		try {
			SensorManager sensorManager = (SensorManager) context.getApplicationContext()
					.getSystemService(Context.SENSOR_SERVICE);
			List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
			for (Sensor sensor : sensorList) {
				result.add(sensor.getName());
			}
		} catch (Exception e) {
		}
		return result;
	}

	public static List getNetworkInterface() {
		try {
			List<Map<String, Object>> list = new ArrayList<>();
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
				NetworkInterface intf = en.nextElement();
				list.add(Utils.buildMap("name", intf.getName(), "displayName", intf.getDisplayName(), "isVirtual",
						intf.isVirtual()));
			}
			return list;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 判断当前设备是手机还是平板
	 * @param context context上下文
	 * @return 平板返回 true，手机返回 false
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
}
