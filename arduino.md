## linux
1. 官网下载
2. 由于不是com，所以修改 ~/.arduinoxx/下面的配置文件，具体设备要看dmesg, 例如 .... attach to ttyUSB0

## 中断
attachInterrupt(interrupt, function, mode)
int interrupt 几号中断，0或者1
void(void) function
mode: LOW,CHANGE,RISING,FALLING

一般只有两个中断引脚 0：D2, 1:D3

### 禁止中断 noInterrupts(), 例如关键代码不允许中断 
启用中断 interrupts();

### 删除中断
detachInterrupt(interrupt_number)

## 寄存器


## 字符串
String s;
s+=char(Searial.read());
Serial.println(s);
s="";
s[0];

## 串口
    while (Serial.available() > 0)  
    {
        comdata += char(Serial.read());
        delay(2);
    }

    注意delay(2)


## 初始化
void setup(){
   Serial.begin(9600);
   pinMode(ledPin, OUTPUT);      // 设置数字端口13为输出模式
   pinMode(KEY, INPUT);       // 设置数字端口2为输入模式
}

## ADC
A0~A5 A6A7
A4 A5 A6 A7 还没接上
默认以输入电压作为基准源，这个可能不太稳定。
如果要换成内部1.1v的话：
analogReference(INTERNAL);
DEFAULT 5v(或3.3v)
EXTERNAL ARFE引脚

analogRead 返回的是一个计数，实际电压为
n*refv/1024.0
5v的最小分辨率是 4.9mv，1.1v的分辨率是1mv