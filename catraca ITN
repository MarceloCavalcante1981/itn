#ifdef ESP8266
#include <ESP8266WiFi.h>
#else	//ESP32
#include <WiFi.h>
#endif
#include <ModbusIP_ESP8266.h>

//pinos
//#define pinStatusWIFI  2
#define pinLedFail  4
//#define pinLedPass  5
#define pinPortaIN  27
#define pinPortaOUT  25
//exemplo desenvolvikento sem led, usar saida 2 
#define pinStatusWIFI 13 
#define pinLedPass 33

//constantes
//const char* ssid = "ITN - TECNOLOGIA";
//const char* password =  "1tTn@1907";
//config de redes conhecidas
//const char* ssid = "ITN-T";
//const char* password =  "1tTn@1907";
//const char* ssid = "Cnecteqq";
//const char* password =  "12345678";

const char* ssid = "gatewitfi";
const char* password =  "1q2w3e4r";

#define LEN 10
#define STB 0
#define PASS 1
#define FAIL 10
#define CLOSE LOW
#define OPEN HIGH
const int REGISTRADOR_IN = 4;
const int REGISTRADOR_OUT = 3;
uint16_t TOFF = 1000;

//variaveis
char identificadorOUT = STB;
char identificadorIN = STB;
unsigned long int now = 0;
unsigned long int previous = 0;

//ModbusIP object
ModbusIP mb;

}

void setup() {
  pinMode(pinStatusWIFI, OUTPUT);
  pinMode(pinLedFail, OUTPUT);
  pinMode(pinLedPass, OUTPUT);
  pinMode(pinPortaIN, OUTPUT);
  pinMode(pinPortaOUT, OUTPUT);

  digitalWrite(pinLedPass, OPEN);
  digitalWrite(pinLedFail, OPEN);
  digitalWrite(pinPortaIN, OPEN);
  digitalWrite(pinPortaOUT, OPEN);
  digitalWrite(pinStatusWIFI, LOW);

  blinkLed();
#ifdef ESP8266
  Serial.begin(74880);
#else
  Serial.begin(115200);
#endif

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {

    delay(500);
    Serial.print(".");
    digitalWrite(pinStatusWIFI, LOW);
  }
  digitalWrite(pinStatusWIFI, HIGH);

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  Serial.println(mb.Hreg(REGISTRADOR_IN));
  mb.onConnect(cbConn);   // Add callback on connection event
  mb.slave();

  if (!mb.addHreg(0, 0xF0F0, LEN)) Serial.println("Error"); // Add Hregs
  mb.onGetHreg(0, cbRead, LEN); // Add callback on Coils value get
  mb.onSetHreg(0, cbWrite, LEN);

  Serial.println("--");

  mb.task();

  Serial.println(mb.Hreg(REGISTRADOR_IN));
  delay(2000);
  Serial.println("--");

}

void loop() {
  checkWIFI();
  unsigned long int now = millis();
  mb.task();
  if (mb.Hreg(REGISTRADOR_IN) == 1) {
    identificadorIN = PASS;
    previous = millis();
  }
  if (mb.Hreg(REGISTRADOR_IN) == 2) {
    identificadorIN = FAIL;
    previous = millis();
  }
  if (mb.Hreg(REGISTRADOR_OUT) == 1) {
    identificadorOUT = PASS;
    previous = millis();
  }
  if (mb.Hreg(REGISTRADOR_OUT) == 2) {
    identificadorOUT = FAIL;
    previous = millis();
  }

  if (previous + TOFF > now) {
    if (identificadorIN == PASS) {
      digitalWrite(pinLedPass, CLOSE);
      digitalWrite(pinPortaIN, CLOSE);
      digitalWrite(pinPortaOUT, OPEN);
    }
    if (identificadorIN == FAIL)
      digitalWrite(pinLedFail, CLOSE);
    identificadorIN = STB;
  }

  if (previous + TOFF > now) {
    if (identificadorOUT == PASS) {
      digitalWrite(pinLedPass, CLOSE);
      digitalWrite(pinPortaOUT, CLOSE);
      digitalWrite(pinPortaIN, OPEN);
    }
    if (identificadorOUT == FAIL)
      digitalWrite(pinLedFail, CLOSE);
    identificadorOUT = STB;
  }

  else {
    digitalWrite(pinLedPass, OPEN);
    digitalWrite(pinLedFail, OPEN);
    digitalWrite(pinPortaIN, OPEN);
    digitalWrite(pinPortaOUT, OPEN);
  }
  delay(10);
}


// Callback function to read corresponding DI
uint16_t cbRead(TRegister* reg, uint16_t val) {
  Serial.print("Ler. Reg RAW#: ");
  Serial.print(reg->address.address);
  Serial.print(" velho: ");
  Serial.print(reg->value);
  Serial.print(" novo: ");
  Serial.println(val);
  return val;
}
// Callback function to write-protect DI
uint16_t cbWrite(TRegister* reg, uint16_t val) {
  Serial.print("Write. Reg RAW#: ");
  Serial.print(reg->address.address);
  Serial.print(" Old: ");
  Serial.print(reg->value);
  Serial.print(" New: ");
  Serial.println(val);
  return val;
}

// Callback function for client connect. Returns true to alCLOSE connection.
bool cbConn(IPAddress ip) {
  Serial.println(ip);
  return true;




void checkWIFI() {
  while (WiFi.status() != WL_CONNECTED) {
    WiFi.begin(ssid, password);
    delay(500);
    Serial.print(".");
    digitalWrite(pinStatusWIFI, LOW);
  }
  digitalWrite(pinStatusWIFI, HIGH);
}

void blinkLed() {
  digitalWrite(pinLedFail, OPEN);
  delay(100);
  digitalWrite(pinLedFail, CLOSE);
}
