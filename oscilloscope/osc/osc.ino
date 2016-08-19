#include <EEPROM.h>
#include <U8glib.h>
//display
#define OLED_MOSI 12
#define OLED_CLK 13
#define OLED_DC 8
#define OLED_CS 11
#define OLED_RESET 9 

#define MAXFREQ 120
#define DEFFREQ 60
#define SAMPLES 100
#define WAVEDISPWIDTH 100  //100pixel
#define WAVEDISPHEIGHT 60 //60pixel
#define VPERPIX (5.0f/2.0f/WAVEDISPHEIGHT)
int nBeamPos=WAVEDISPHEIGHT/2;
int nFreq=100;
int nFreqAddr=0; //address in eeprom

float sx=1.0f;
float vValue[SAMPLES];
int nSensorPin = A0;

U8GLIB_SSD1306_128X64 u8g(OLED_CLK, OLED_MOSI, OLED_CS, OLED_DC, OLED_RESET);//SCK 13,mosi 12 CS 11

void setup() {
  Serial.begin(9600);
  nFreq = EEPROM.read(nFreqAddr);
  if(nFreq<0 || nFreq>MAXFREQ){
    nFreq = DEFFREQ;
    EEPROM.write(nFreqAddr,nFreq);   
  }
  u8g.firstPage();//clear
}
void onFreeze(){
  
}

void onAdjBeam(){
  
}

void onAdjFreq(){
  
}

void sample(){
  int x=0;
  int kPixelsH =5.0/1024.0/VPERPIX; 
  for( ;x<SAMPLES; x++){
    float sensorV = analogRead(nSensorPin);
    int v = (int)(sensorV*kPixelsH);
    //test
    v = 30*sin(x*0.12f);
    //test end    
    vValue[x]=-v+nBeamPos;
  }
}

void draw(){
  int x=0;
  u8g.drawHLine(0,nBeamPos,WAVEDISPHEIGHT);
  for(;x<SAMPLES; x++){
    u8g.drawPixel(x,vValue[x]);
  }
}

void loop() {
    //u8g.firstPage();//clear
    do draw();
    while( u8g.nextPage());
}
