#include <Arduino.h>
#include <Wire.h>
#include <digitalWriteFast.h>
#include <math.h>

/*
This sketch can be used to collect data to be used in the system ID of two wheel motors simultaneously.
Essentially it sends a series of independent, random voltage commands to the wheel motors for independent
random durations and then periodically measures the average angular velocity of the wheels over a period.

Modify the parameters as desired below, upload the sketch, and place the robot on a clear area on
the ground. Establish a terminal connection with the communication port of the XBEE and switch
on the robot. The robot will move quite randomly for a period of time. When the test is complete,
the onboard XBEE will transmit the results. Note that all time measurements are in milliseconds 
and the transmitted velocity measurements are in encoder ticks per velocity interval, where the
velocity interval is user-defined. One can easily convert this to radians per second using:

  w = 2 * PI * (MEASURED_TICKS / TOTAL_TICKS) * (1000 / VELOCITY_INTERVAL)

where TOTAL_TICKS is the number of ticks per revolution, which is approximately 3000.

Author: Ryan J. Marcotte
Date: 19 March 2014
*/

//#####SYSTEM ID PARAMETERS#######
//#####MODIFY THESE FIELDS########
#define RIGHT_TEST_PIN FORWARD_RIGHT //the pin of the right motor you are working with
                                     //either FORWARD_RIGHT or REVERSE_RIGHT
#define LEFT_TEST_PIN FORWARD_LEFT   //the pin of the left motor you are working with
                                     //either FORWARD_LEFT or REVERSE_LEFT
const int TOTAL_VOL_COMS = 10; //the total number of voltage commands to be executed by the robot
                               //increasing beyond ~10, memory can become an issue
                               //if the board runs out of memory, it resets itself which can be 
                               //a challenging bug to diagnose
const int MIN_COM_DUR = 5; //the minimum duration a command could be executed for
const int MAX_COM_DUR = 2000; //the maximum duration a command could be executed for
const int COM_DUR_RESOLUTION = 5; //command durations can take on values as multiples of
                                          //this parameter; 5 ms recommended
const int VELOCITY_INTERVAL = 20; //length of interval (in ms) velocity measurements are taken over
                                 //20 ms is probably optimal for most situations

//##########END PARAMETERS###########
//###NO MODIFICATIONS NEEDED BELOW###

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
const int REVERSE_LEFT = 11;
const int FORWARD_LEFT = 10;

//Right Motor
const int REVERSE_RIGHT = 9;
const int FORWARD_RIGHT = 8;

/*
 * Drives the execution of the system ID routine. Initializes the pins to be used. Generates
 * the arrays of voltage commands (magnitude & duration). Determines the total runtime based
 * on the generated command durations. Performs the system ID test. Outputs a summary of the 
 * parameters of the test as well as the velocity results.
 */
void setup(){
  Serial.begin(9600);
  long runtime;
  int velsSize;
  
  initializePins();
  
  Prev_RightEncoderTicks = _RightEncoderTicks;
  Prev_LeftEncoderTicks = _LeftEncoderTicks;
  
  int leftVolComs[TOTAL_VOL_COMS];
  int rightVolComs[TOTAL_VOL_COMS];
  unsigned long volComTimes[TOTAL_VOL_COMS];
  generateCommandArrays(leftVolComs, rightVolComs, volComTimes);
  
  runtime = determineRuntime(volComTimes);  
  velsSize = runtime / VELOCITY_INTERVAL;
  int leftVels[velsSize];
  int rightVels[velsSize];
  int velsTimes[velsSize];
  int leftVelsCommands[velsSize];
  int rightVelsCommands[velsSize];
  
  performTest(runtime, velsSize, leftVels, rightVels, velsTimes, leftVelsCommands,
                        rightVelsCommands, leftVolComs, rightVolComs, volComTimes);
  outputSummary(runtime, leftVolComs, rightVolComs, volComTimes);
  outputResults(velsSize, leftVels, rightVels, velsTimes, leftVelsCommands, rightVelsCommands);
}

/*
 * Intentionally left empty. Robot simply idles upon completion of setup routine.
 */
void loop(){
}

/*
 * Sets up encoder pins and attaches interrupts. Sets all motor pins to output mode.
 */
void initializePins(){
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
  
  //Motors
  pinMode(FORWARD_RIGHT, OUTPUT);
  pinMode(FORWARD_LEFT, OUTPUT);
  pinMode(REVERSE_RIGHT, OUTPUT);
  pinMode(REVERSE_LEFT, OUTPUT);
}

/*
 * Randomly generates the lists of command magnitudes and durations based
 * on the user's desired parameters of the minimum and maximum command times
 * as well as the command time resolution. If identical command lists are 
 * desired repeatedly for test purposes, comment out the fist line that seeds
 * the random number generator.
 */
void generateCommandArrays(int leftVolComs[], int rightVolComs[], unsigned long volComTimes[]){
  randomSeed(analogRead(0));
  unsigned long min;
  unsigned long max;  
  
  for(int i = 0; i < TOTAL_VOL_COMS; i++){
    leftVolComs[i] = random(256);
    rightVolComs[i] = random(256);
    delay(5);
    min = MIN_COM_DUR / COM_DUR_RESOLUTION;
    max = MAX_COM_DUR / COM_DUR_RESOLUTION;
    volComTimes[i] = COM_DUR_RESOLUTION * random(min, max);
  }
}

/*
 * Determines the total runtime of the test, calculated as the sum of 
 * the voltage command durations.
 */
long determineRuntime(unsigned long volComTimes[]){
  unsigned long runtime = 0;
  
  for(int i = 0; i < TOTAL_VOL_COMS; i++) {
    runtime += volComTimes[i];
  }
  
  runtime += (VELOCITY_INTERVAL - runtime % VELOCITY_INTERVAL);
  
  return runtime;
}

/*
 * Measures repeated step responses on the motors being tested. Each time the main loop iterates, the
 * function checks to see if the current time dictates that a measurement should be taken or a new 
 * command should be executed.
 *
 * Note: An unresolved issue remains with utilizing the millis timing. This code is written under the 
 * assumption that the main while loop iterates many times each millisecond. Microsecond timing tests
 * showed that it executed in approximately 50-100 us, which supports this assumption. However, repeated
 * tests of the code also showed that the while loop appears to "skip over" certain millisecond times.
 * For example, if voltage command or velocity measurement is supposed to be sent or taken at a time
 * 80 ms after the base time, the loop skips over that time and does not take the appropriate action.
 * As a result, a command or measurement may be delayed by up to a millisecond. This is reported in the
 * final results that are output to the user, so it is not terribly significant. However, this is an
 * issue to be explored.
 */
void performTest(const unsigned long RUNTIME, const int VELS_SIZE, int leftVels[], int rightVels[], 
                    int velsTimes[], int leftVelsCommands[], int rightVelsCommands[], 
                    const int LEFT_VOL_COMS[], const int RIGHT_VOL_COMS[], const unsigned long VOL_COM_TIMES[]){
  int velsIndex = 0;
  int volComsIndex = 0;
  boolean commandNow = false;
  boolean measureNow = false;
  
  const unsigned long BASE_TIME = millis();
  unsigned long currentTime = BASE_TIME;
  unsigned long lastCommandTime = currentTime;
  unsigned long lastMeasureTime = currentTime;
  
  analogWrite(LEFT_TEST_PIN, LEFT_VOL_COMS[volComsIndex]);
  analogWrite(RIGHT_TEST_PIN, RIGHT_VOL_COMS[volComsIndex]);
  
  while(currentTime - BASE_TIME < RUNTIME){
    currentTime = millis();
    commandNow = (currentTime - lastCommandTime >= VOL_COM_TIMES[volComsIndex] && lastCommandTime != currentTime);
    measureNow = (currentTime - lastMeasureTime >= VELOCITY_INTERVAL && lastMeasureTime != currentTime);
    
    if(commandNow || measureNow){
      if(commandNow){
        analogWrite(LEFT_TEST_PIN, LEFT_VOL_COMS[++volComsIndex]);
        analogWrite(RIGHT_TEST_PIN, RIGHT_VOL_COMS[volComsIndex]);
        lastCommandTime = currentTime;
      }
      
      if(measureNow){
        leftVels[velsIndex] = _LeftEncoderTicks - Prev_LeftEncoderTicks;
        Prev_LeftEncoderTicks = _LeftEncoderTicks;
        
        rightVels[velsIndex] = _RightEncoderTicks - Prev_RightEncoderTicks;
        Prev_RightEncoderTicks = _RightEncoderTicks;
               
        lastMeasureTime = currentTime;
        velsTimes[velsIndex] = currentTime - BASE_TIME;
        leftVelsCommands[velsIndex] = LEFT_VOL_COMS[volComsIndex];
        rightVelsCommands[velsIndex++] = RIGHT_VOL_COMS[volComsIndex];
      }
    }
  }
  
  digitalWrite(LEFT_TEST_PIN, LOW);
  digitalWrite(RIGHT_TEST_PIN, LOW);
}

/*
 * Outputs a summary of the test, including the user-selected parameters and the generated lists 
 * of voltage command magnitudes and durations.
 */
void outputSummary(const long RUNTIME, const int LEFT_VOL_COMS[], const int RIGHT_VOL_COMS[], 
                        const unsigned long VOL_COM_TIMES[]){
  Serial.println("System ID Summary");
  delay(20);
  Serial.print("Left motor tested: ");
  delay(20);
  Serial.println(getLeftPinName());
  delay(20);
  Serial.print("Right motor tested: ");
  delay(20);
  Serial.println(getRightPinName());
  delay(20);
  Serial.print("Total commands executed: ");
  delay(20);
  Serial.println(TOTAL_VOL_COMS);
  delay(20);
  Serial.print("Total run time: ");
  delay(20);
  Serial.println(RUNTIME);
  delay(20);
  Serial.print("Velocity interval: ");
  delay(20);
  Serial.println(VELOCITY_INTERVAL);
  delay(20);
  Serial.println("\n\nVoltage Commands");
  delay(20);
  Serial.println("Magnitude (L)   Magnitude (R)   Duration");
  delay(40);
  
  for(int i = 0; i < TOTAL_VOL_COMS; i++){
    Serial.print(LEFT_VOL_COMS[i]);
    delay(5);
    Serial.print("     ");
    delay(5);
    Serial.print(RIGHT_VOL_COMS[i]);
    delay(5);
    Serial.print("     ");
    delay(5);
    Serial.println(VOL_COM_TIMES[i]);
    delay(5);
  }
  
  Serial.println("End Summary");
}

/*
 * Returns the name of the left pin being tested as a string.
 */
String getLeftPinName() {
  switch(LEFT_TEST_PIN) {
    case FORWARD_LEFT:
      return "Forward Left";
    case REVERSE_LEFT:
      return "Reverse Left";
  }
}

/*
 * Returns the name of the right pin being tested as a string.
 */
String getRightPinName() {
  switch(RIGHT_TEST_PIN) {
    case FORWARD_RIGHT:
      return "Forward Right";
    case REVERSE_RIGHT:
      return "Reverse Right";
  }
}

/*
 * Outputs the results of the measurements, including the time of the measurements and the index of the voltage
 * command being executed during that measurement.
 */
void outputResults(const int VELS_SIZE, const int LEFT_VELS[], const int RIGHT_VELS[], 
                    const int VELS_TIMES[], const int LEFT_VELS_COMMANDS[], const int RIGHT_VELS_COMMANDS[]){  
  Serial.println("\n\nVelocity Measurements");
  delay(20);
  Serial.print("Elapsed Time   ");
  delay(20);
  Serial.print("Command (L)  ");
  delay(20);
  Serial.print("Velocity (L)   ");
  delay(20);
  Serial.print("Command (R)  ");
  delay(20);
  Serial.println("Velocity (R)   ");
  delay(20);
  
  for(int i = 0; i < VELS_SIZE; i++){
    Serial.print(VELS_TIMES[i]);
    delay(5);
    Serial.print("     ");
    delay(5);
    Serial.print(LEFT_VELS_COMMANDS[i]);
    delay(5);
    Serial.print("     ");
    delay(5);
    Serial.print(LEFT_VELS[i]);
    delay(5);
    Serial.print("     ");
    delay(5);
    Serial.print(RIGHT_VELS_COMMANDS[i]);
    delay(5);
    Serial.print("     ");
    delay(5);
    Serial.println(RIGHT_VELS[i]);
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

