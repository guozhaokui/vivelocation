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
*/

#include <U8glib.h>

U8GLIB_SSD1306_128X64 u8g(13,12,11);//SCK 13,mosi 12 CS 11

void setup(){

}

void draw(){
    u8g.drawLine(0,0,128,64);
}

void loop(){
    u8g.firstPage();//clear
    do draw();
    while( u8g.nextPage());
}