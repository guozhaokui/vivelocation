
1. android-usb-arduino
2. 定位硬件
3. 定位算法
https://github.com/ashtuchkin/vive-diy-position-sensor
    1. 预先写死Lighthouse的位置和朝向
    2. 用姿态估计(pnp算法)
         perspective-n-point
         https://en.wikipedia.org/wiki/Perspective-n-Point

    一旦计算出lighthouse的位置，是不是就可以用简单的方式来计算了？
    还要结合IMU信息