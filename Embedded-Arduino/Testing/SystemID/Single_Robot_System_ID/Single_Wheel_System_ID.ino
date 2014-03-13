#include <Arduino.h>
#include <Wire.h>
#include <digitalWriteFast.h>
#include <math.h>

//######ENCODERS######
//Left Encoder
#define c_LeftEncoderInterrupt 0
#define c_LeftEncoderPinA 2
#define c_LeftEncoderPinB 4
#define LeftEncoderIsReversed
volatile bool _LeftEncoderBSet;
volatile long _LeftEncoderTicks = 0;

//Right Encoder
#define c_RightEncoderInterrupt 1
#define c_RightEncoderPinA 3
#define c_RightEncoderPinB 5
volatile bool _RightEncoderBSet;
volatile long _RightEncoderTicks = 0;

long Prev_LeftEncoderTicks;
long Prev_RightEncoderTicks;

//######MOTORS#########
//Left Motor
const int reverseLeftMotorPin = 11;
const int forwardLeftMotorPin = 10;

//Right Motor
const int reverseRightMotorPin = 9;
const int forwardRightMotorPin = 8;

//######SYSTEM ID#######
//Modify these fields
const int totalVolComs = 20; //the total number of voltage commands to be executed by the robot
const int testPin1 = forwardLeftMotorPin;
const int testPin2 = forwardRightMotorPin;

int volComs[totalVolComs]; //the voltage commands to be executed by robot
int volComsIndex = 0; //keeps track of which voltage command the robot is current executing
unsigned long volComTimes[totalVolComs]; //the lengths of time each voltage command is executed

const int totalTime = 20000; //the total amount of time the robot should measure angular velocity of the wheels
const int velsSize = totalTime / 20;
int vels[velsSize]; //the velocities of the wheels, measured every 20 ms
int velsIndex = 0; //keeps track of which measurement is currently being executed
int velsTimes[velsSize];
int velsCommandIndex[velsSize];

unsigned long currentTime;
unsigned long lastMeasureTime;
unsigned long lastCommandTime;
boolean commandNow = false;
boolean measureNow = false;

void setup(){
  Serial.begin(9600);
  
  //Left Encoder
  pinMode(c_LeftEncoderPinA, INPUT);     //sets pin A as input
  digitalWrite(c_LeftEncoderPinA, LOW);  //turns on pullup resistors
  pinMode(c_LeftEncoderPinB, INPUT);     //sets pin B as input
  digitalWrite(c_LeftEncoderPinB, LOW);  //turns on pullup resistors
  attachInterrupt(c_LeftEncoderInterrupt, HandleLeftMotorInterruptA, RISING);
  
  //Right Encoder
  pinMode(c_RightEncoderPinA, INPUT);    //sets pin A as input
  digitalWrite(c_RightEncoderPinA, LOW); //turns on pullup resistors
  pinMode(c_RightEncoderPinB, INPUT);    //sets pin B as input
  digitalWrite(c_RightEncoderPinB, LOW); //turns on pullupresistors
  attachInterrupt(c_RightEncoderInterrupt, HandleRightMotorInterruptA, RISING);
  
  Prev_RightEncoderTicks = _RightEncoderTicks;
  Prev_LeftEncoderTicks = _LeftEncoderTicks;
  
  //Motors
  pinMode(reverseLeftMotorPin, OUTPUT);
  pinMode(forwardLeftMotorPin, OUTPUT);
  pinMode(reverseRightMotorPin, OUTPUT);
  pinMode(forwardRightMotorPin, OUTPUT);
  
  generateCommandArrays();
  
  outputResults();
  
  performTest();
}

void loop(){
}

void generateCommandArrays(){
  randomSeed(analogRead(0));
  
  for(int i = 0; i < totalVolComs; i++){
    volComs[i] = random(256);
    volComTimes[i] = 5*random(1,400);
  }  
}

void performTest(){
  const unsigned long baseTime = millis();
  currentTime = baseTime;
  analogWrite(reverseLeftMotorPin, volComs[volComsIndex]);
  lastCommandTime = currentTime;
  lastMeasureTime = currentTime;
  
  while(currentTime - baseTime < totalTime){
    currentTime = millis();
    commandNow = (currentTime - lastCommandTime >= volComTimes[volComsIndex] && lastCommandTime != currentTime) ? true : false;
    measureNow = (currentTime - lastMeasureTime >= 20 && lastMeasureTime != currentTime) ? true : false;
    
    if(commandNow || measureNow){
      if(commandNow){
        analogWrite(reverseLeftMotorPin, volComs[++volComsIndex]);
        lastCommandTime = currentTime;
      }
      
      if(measureNow){
        vels[velsIndex] = _LeftEncoderTicks - Prev_LeftEncoderTicks;
        lastMeasureTime = currentTime;
        Prev_LeftEncoderTicks = _LeftEncoderTicks;
        velsTimes[velsIndex] = currentTime - baseTime;
        velsCommandIndex[velsIndex++] = volComsIndex;
      }
    }
  }
  
  digitalWrite(reverseLeftMotorPin, LOW);
}

void outputResults(){
  for(int i = 0; i < totalVolComs; i++){
    Serial.print(volComs[i]);
    delay(5);
    Serial.print(" ");
    delay(5);
    Serial.println(volComTimes[i]);
    delay(5);
  }
  
  Serial.println("End Commands\n\nStart Measurements");
  delay(20);
  
  for(int i = 0; i < velsSize; i++){
    Serial.print(velsTimes[i]);
    delay(5);
    Serial.print(" ");
    delay(5);
    Serial.print(velsCommandIndex[i]);
    delay(5);
    Serial.print(" ");
    delay(5);
    Serial.println(vels[i]);
    delay(5);
  }
  
  Serial.println("End Measurements");
}

//        DO NOT MODIFY BELOW THIS POINT
//############ ENCODER INTERRUPTS ################
// Interrupt service routines for the left motor's quadrature encoder 
void HandleLeftMotorInterruptA() {
  // Test transition; since the interrupt will only fire on 'rising' we don't need to read pin A 
  _LeftEncoderBSet = digitalReadFast(c_LeftEncoderPinB);   // read the input pin 

  // and adjust counter + if A leads B 
#ifdef LeftEncoderIsReversed 
  _LeftEncoderTicks -= _LeftEncoderBSet ? -1 : +1; 
#else 
  _LeftEncoderTicks += _LeftEncoderBSet ? -1 : +1; 
#endif 
} 

// Interrupt service routines for the right motor's quadrature encoder 
void HandleRightMotorInterruptA() {
  // Test transition; since the interrupt will only fire on 'rising' we don't need to read pin A 
  _RightEncoderBSet = digitalReadFast(c_RightEncoderPinB);   // read the input pin 

  // and adjust counter + if A leads B 
#ifdef RightEncoderIsReversed 
  _RightEncoderTicks -= _RightEncoderBSet ? -1 : +1; 
#else 
  _RightEncoderTicks += _RightEncoderBSet ? -1 : +1; 
#endif
}

