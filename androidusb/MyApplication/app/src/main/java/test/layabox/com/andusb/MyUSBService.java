package test.layabox.com.andusb;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.hardware.usb.*;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;

public class MyUSBService {//extends Service {
    public UsbManager myUsbManager;
    protected  UsbDevice myUsbDevice;
    protected  String TAG = new String("");
    protected UsbDeviceConnection myDeviceConnection;
    protected UsbInterface Interface1,Interface2;
    protected UsbEndpoint epBulkIn, epBulkOut,epControl,epIntEndpointOut,epIntEndpointIn;
    public MyUSBService() {
    }
/*
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        myUsbManager = (UsbManager)getSystemService(Context.USB_SERVICE);
        return null; //TODO
    }
*/
    // 枚举设备函数
    public void enumerateDevice(UsbManager mUsbManager,Context context) {
        System.out.println("开始进行枚举设备!");
        if (mUsbManager == null) {
            System.out.println("创建UsbManager失败，请重新启动应用！");
            //info.setText("创建UsbManager失败，请重新启动应用！");
            return;
        } else {
            HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
            if (!(deviceList.isEmpty())) {
                // deviceList不为空
                System.out.println("deviceList is not null!");
                Iterator<UsbDevice> deviceIterator = deviceList.values()
                        .iterator();
                while (deviceIterator.hasNext()) {
                    UsbDevice device = deviceIterator.next();
                    // 保存设备VID和PID
                    int VendorID = device.getVendorId();
                    int ProductID = device.getProductId();
                    // 输出设备信息
                    Log.i(TAG, "DeviceInfo: " + VendorID + " , "
                            + ProductID);
                    // 保存匹配到的设备
                    if (VendorID == 0x067b && ProductID == 0x2303) {
                        myUsbDevice = device; // 获取USBDevice
                        System.out.println("发现待匹配设备:" + VendorID
                                + "," + ProductID);
                        // = getApplicationContext();
                        Toast.makeText(context, "发现待匹配设备", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            } else {
                //info.setText("请连接USB设备至PAD！");
                //Context context = getApplicationContext();
                Toast.makeText(context, "请连接USB设备至PAD！", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    // 寻找设备接口
    public void getDeviceInterface() {
        if (myUsbDevice != null) {
            int icnt = myUsbDevice.getInterfaceCount();
            Log.d(TAG, "interfaceCounts : " + icnt);
            for (int i = 0; i < icnt; i++) {
                UsbInterface intf = myUsbDevice.getInterface(i);

                if (i == 0) {
                    Interface1 = intf; // 保存设备接口
                    System.out.println("成功获得设备接口:" + Interface1.getId());
                }
                if (i == 1) {
                    Interface2 = intf;
                    System.out.println("成功获得设备接口:" + Interface2.getId());
                }
            }
        } else {
            System.out.println("设备为空！");
        }

    }

    // 分配端点，IN | OUT，即输入输出；可以通过判断
    public UsbEndpoint assignEndpoint(UsbInterface mInterface) {
        Log.i(TAG,"Endpoint num:"+mInterface.getEndpointCount());
        for (int i = 0; i < mInterface.getEndpointCount(); i++) {
            UsbEndpoint ep = mInterface.getEndpoint(i);
            // look for bulk endpoint
            if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                    epBulkOut = ep;
                    System.out.println("Find the BulkEndpointOut," + "index:"
                            + i + "," + "使用端点号："
                            + epBulkOut.getEndpointNumber());
                } else {
                    epBulkIn = ep;
                    System.out
                            .println("Find the BulkEndpointIn:" + "index:" + i
                                    + "," + "使用端点号："
                                    + epBulkIn.getEndpointNumber());
                }
            }
            // look for contorl endpoint
            if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_CONTROL) {
                epControl = ep;
                System.out.println("find the ControlEndPoint:" + "index:" + i
                        + "," + epControl.getEndpointNumber());
            }
            // look for interrupte endpoint
            if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_INT) {
                if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                    epIntEndpointOut = ep;
                    System.out.println("find the InterruptEndpointOut:"
                            + "index:" + i + ","
                            + epIntEndpointOut.getEndpointNumber());
                }
                if (ep.getDirection() == UsbConstants.USB_DIR_IN) {
                    epIntEndpointIn = ep;
                    System.out.println("find the InterruptEndpointIn:"
                            + "index:" + i + ","
                            + epIntEndpointIn.getEndpointNumber());
                }
            }
        }
        if (epBulkOut == null && epBulkIn == null && epControl == null
                && epIntEndpointOut == null && epIntEndpointIn == null) {
            throw new IllegalArgumentException("not endpoint is founded!");
        }
        return epIntEndpointIn;
    }

    // 打开设备
    public void openDevice(UsbInterface mInterface) {
        if (mInterface != null) {
            UsbDeviceConnection conn = null;
            // 在open前判断是否有连接权限；对于连接权限可以静态分配，也可以动态分配权限
            if (myUsbManager.hasPermission(myUsbDevice)) {
                conn = myUsbManager.openDevice(myUsbDevice);
            }

            if (conn == null) {
                return;
            }

            if (conn.claimInterface(mInterface, true)) {
                myDeviceConnection = conn;
                if (myDeviceConnection != null)// 到此你的android设备已经连上zigbee设备
                    System.out.println("open设备成功！");
                final String mySerial = myDeviceConnection.getSerial();
                System.out.println("设备serial number：" + mySerial);
            } else {
                System.out.println("无法打开连接通道。");
                conn.close();
            }
        }
    }

    // 发送数据
    public void sendMessageToPoint(byte[] buffer) {
        // bulkOut传输
        if (myDeviceConnection
                .bulkTransfer(epBulkOut, buffer, buffer.length, 0) < 0)
            System.out.println("bulkOut返回输出为  负数");
        else {
            System.out.println("Send Message Succese！");
        }
    }

    // 从设备接收数据bulkIn
    public byte[] receiveMessageFromPoint() {
        byte[] buffer = new byte[15];
        if (myDeviceConnection.bulkTransfer(epBulkIn, buffer, buffer.length,
                2000) < 0)
            System.out.println("bulkIn返回输出为  负数");
        else {
            System.out.println("Receive Message Succese！"
                    // + "数据返回"
                    // + myDeviceConnection.bulkTransfer(epBulkIn, buffer,
                    // buffer.length, 3000)
            );
        }
        return buffer;
    }

    public void onStart(/*Intent intent, int startId,*/Context context) {
        // TODO Auto-generated method stub
        //super.onStart(intent, startId); // 每次startService（intent）时都回调该方法
        System.out.println("进入service的onStart函数");
        //myUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE); // 获取UsbManager
        myUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

        //Context context = getApplicationContext();
        // 枚举设备
        enumerateDevice(myUsbManager, context);
        // 查找设备接口
        getDeviceInterface();
        // 获取设备endpoint
        assignEndpoint(Interface2);
        // 打开conn连接通道
        openDevice(Interface2);
    }
}
