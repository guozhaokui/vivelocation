/*
sid
clk
u8glib
搜 0.96 oled
应该是 SSD1306
D0 clk
D1 data
RES 复位
DC 控制输入数据/命令（高电平（1）为数据，低电平（0）为命令）
support：
    IIC
    3SPI
    4SPI
    根据实际的焊接，缺省的是4SPI


u8g 
    A0 address line 0    
*/

#include <U8glib.h>

#define OLED_MOSI 12
#define OLED_CLK 13
#define OLED_DC 8
#define OLED_CS 11
#define OLED_RESET 9 

U8GLIB_SSD1306_128X64 u8g(OLED_CLK, OLED_MOSI, OLED_CS, OLED_DC, OLED_RESET);//SCK 13,mosi 12 CS 11

int sensorPin = A0;    // select the input pin for the potentiometer
int sensorValue = 0;  // variable to store the value coming from the sensor
char strBuf[64];

void setup(){

}

void draw(){
    sensorValue = analogRead(sensorPin);
    String vv="V: ";
    vv = vv + sensorValue*5.0/1024.0;
    vv.toCharArray(strBuf,64);
    u8g.drawLine(0,0,128,64);
    u8g.setFont(u8g_font_unifont);
    u8g.drawStr(0,50,strBuf);
}

void loop(){
    u8g.firstPage();//clear
    do draw();
    while( u8g.nextPage());
}