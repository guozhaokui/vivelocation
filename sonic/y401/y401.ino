/*
  http://wenku.baidu.com/link?url=w85bsn3edJ1kXH9JZQhgWk19_zqO7j9Knoes8WX-KpLbm5b7pnexkw6sBBu8FbfhBxr2LThpkxym8dkyZWnIYK4HctwO_jmtpzA6X-Ab8ma
  参数:2cm~4.5m
  今天功耗:<2mA
  串口:9600bps
  后面的跳线：插上串口模式，拔下gpio模式
  GPIO模式
    Trig上10us以上的高电平，就会通过Echo输出一个持续一定时间的高电平，以反应距离
      距离=高电平时间*340/2
      340是声速
  串口模式
    通过Trig/TX管脚输入0X55（波特率9600），US-100便会通过Echo/RX管脚输出两字节的距离值，第一个字节是距离的高8位（HDate），
    第二个字节为距离的低8位（LData），单位为毫米。即距离值为 （HData*256 +LData）mm

*/

unsigned int EchoPin = 2;
unsigned int TrigPin = 3;
unsigned long Time_Echo_us = 0;
unsigned long Len_mm = 0; //距离

void setup() {
  Serial.begin(9600);
  pinMode(EchoPin, INPUT);
  pinMode(TrigPin, OUTPUT);
}

void loop() {
  digitalWrite(TrigPin, HIGH);
  delayMicroseconds(50);
  digitalWrite(TrigPin, LOW);   //结束脉冲

  //pulseIn，等到pin上满足第二个参数，然后计时，直到不满足，返回时间
  Time_Echo_us = pulseIn(EchoPin,HIGH);
  if( Time_Echo_us>1 && Time_Echo_us<60000){//有效范围
    Len_mm = Time_Echo_us*340/1000/2;
    Serial.print("Dist:");
    Serial.print(Len_mm,DEC); //10进制
    Serial.println("mm");
  }
  delay(1000);
}
