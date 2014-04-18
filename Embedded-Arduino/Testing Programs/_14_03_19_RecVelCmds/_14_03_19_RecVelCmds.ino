#include <Arduino.h>
#include <Wire.h>
#include <digitalWriteFast.h>

/*
When loaded onto a robot's arduino, this sketch tracks velocity commands using an 
onboard controller. When it receives a new velocity command through serial communication
it updates the reference velocities for both wheels.

The velocity commands should be formatted as a series of 6 bytes. The sketch will
interpret an incoming command as follows:
First byte: Robot ID
Second byte: Reference forward velocity for left wheel
Third byte: Reference backward velocity for left wheel
Fourth byte: Reference forward velocity for right wheel
Fifth byte: Reference backward velocity for right wheel
Sixth byte: Terminating character (0xFF)

The robot will only alter the reference velocities if the robot id for the command
matches its own id and the terminating character is correct. Additionally, a valid
command can only include a single nonzero value for the second and third bytes as 
well as the fourth and fifth bytes.


*/

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

//#####OTHER#########
const byte ROBOT_ID = 1;
const int MAX_MOTOR_VALUE = 255;
const int BUFFER_SIZE = 6;
const int ROBOT_ID_CMD = 0;
const int FORWARD_LEFT_CMD = 1;
const int REVERSE_LEFT_CMD = 2;
const int FORWARD_RIGHT_CMD = 3;
const int REVERSE_RIGHT_CMD = 4;
const int CHECKSUM_CMD = 5;
const int EXPECTED_CHECKSUM_VALUE = 255;
const int TIME_INTERVAL = 20;
const int LEFT = 0;
const int RIGHT = 1;
byte buffer[BUFFER_SIZE];

int referenceVelocities[2];
int activePins[2];
double accumulators[2];
int feedbackVelocities[2];
int errors[2];
int motorInputs[2];
boolean saturatedInput[2];

const double PROPORTIONAL_GAIN = 3.29;
const double INTEGRAL_GAIN = 0.0357;


/*
 *
 */
void setup() {
  Serial.begin(9600);
  Serial.setTimeout(100);
  initializePins();
  initializeControllerValues();
  Serial.println("Ready"); //testing
}

/*
 * Ensures that all pins and interrupts are initialized correctly for the operation
 * of the robot.
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

void initializeControllerValues() {
  for(int i = 0; i < 2; i++) {
    referenceVelocities[i] = 0;
    accumulators[i] = 0;
    feedbackVelocities[i] = 0;
    errors[i] = 0;
    motorInputs[i] = 0;
    saturatedInput[i] = false;
  }
  
  activePins[LEFT] = FORWARD_LEFT;
  activePins[RIGHT] = FORWARD_RIGHT;
}

/*
 * Checks to see if any new commands are available in the serial buffer.
 * If so, calls checkForNewCommand function which reads the commands and
 * updates the references if a valid command has been addressed to the robot.
 */
void loop() {
  checkForNewCommand();
  updateControllerValues(LEFT);
  updateControllerValues(RIGHT);
  measureVelocities();
}

/*
 * Checks to see if a new command has been received through the XBEE. For
 * each new command that is available, calls the processCommand function
 * which updates the reference velocities if the command is valid and 
 * correctly addressed.
 */
void checkForNewCommand() {  
  int elementsInBuffer = 0;
  
  while(Serial.available() > 0) {
    buffer[elementsInBuffer++] = Serial.read();
    delay(3);
    
    if(elementsInBuffer == BUFFER_SIZE) {
      processCommand();
      elementsInBuffer = 0;
    }
  }
}

/*
 * Executed after a new set of velocity commands is received. Determines
 * if a command in the buffer is a valid command that is addressed to it.
 * If so, updates the reference velocities.
 */
void processCommand() {
  const int COMMAND_ADDRESS = buffer[ROBOT_ID_CMD];
  const int COMMAND_CHECKSUM = buffer[CHECKSUM_CMD];
  
  if(COMMAND_ADDRESS == ROBOT_ID && COMMAND_CHECKSUM == EXPECTED_CHECKSUM_VALUE) {  
    if(buffer[FORWARD_LEFT_CMD] == 1 && buffer[FORWARD_RIGHT_CMD] == 1 
        && buffer[REVERSE_LEFT_CMD] == 1 && buffer[REVERSE_RIGHT_CMD] == 1) {
      kill();
    } else {  
      if(buffer[FORWARD_LEFT_CMD] == 1) { //left command must be a reverse command
        activePins[LEFT] = REVERSE_LEFT;
        referenceVelocities[LEFT] = buffer[REVERSE_LEFT_CMD];
        digitalWrite(FORWARD_LEFT, LOW);
      } else if (buffer[REVERSE_LEFT_CMD] == 1) { //left command must be a forward command
        activePins[LEFT] = FORWARD_LEFT;
        referenceVelocities[LEFT] = buffer[FORWARD_LEFT_CMD];
        digitalWrite(REVERSE_LEFT, LOW);
      }
      
      if(buffer[FORWARD_RIGHT_CMD] == 1) { //right command must be a reverse command
        activePins[RIGHT] = REVERSE_RIGHT;
        referenceVelocities[RIGHT] = buffer[REVERSE_RIGHT_CMD];
        digitalWrite(FORWARD_RIGHT, LOW);
      } else if (buffer[REVERSE_RIGHT_CMD] == 1) { //right command must be a forward command
        activePins[RIGHT] = FORWARD_RIGHT;
        referenceVelocities[RIGHT] = buffer[FORWARD_RIGHT_CMD];
        digitalWrite(REVERSE_RIGHT, LOW);
      }
    }
  }
}

void kill() {
  digitalWrite(REVERSE_LEFT, LOW);
  digitalWrite(REVERSE_RIGHT, LOW);
  digitalWrite(FORWARD_LEFT, LOW);
  digitalWrite(FORWARD_RIGHT, LOW);
  initializeControllerValues();
}

/*
 * Updates the input to the controller, the input to the motor (controller output),
 * and the value of the accumulator. Sets flags to indicate if the input is 
 * saturated.
 */
void updateControllerValues(const int LEFT_OR_RIGHT) {
  if(!saturatedInput[LEFT_OR_RIGHT]) {
    accumulators[LEFT_OR_RIGHT] += errors[LEFT_OR_RIGHT] * TIME_INTERVAL;
  }
  
  errors[LEFT_OR_RIGHT] = referenceVelocities[LEFT_OR_RIGHT] 
                                    - feedbackVelocities[LEFT_OR_RIGHT];
  motorInputs[LEFT_OR_RIGHT] = PROPORTIONAL_GAIN * errors[LEFT_OR_RIGHT]
                              + INTEGRAL_GAIN * accumulators[LEFT_OR_RIGHT];
  saturatedInput[LEFT_OR_RIGHT] = motorInputs[LEFT_OR_RIGHT] > MAX_MOTOR_VALUE;
  
  motorInputs[LEFT_OR_RIGHT] = min(motorInputs[LEFT_OR_RIGHT], MAX_MOTOR_VALUE);
  motorInputs[LEFT_OR_RIGHT] = max(motorInputs[LEFT_OR_RIGHT], 1);
  
  if(motorInputs[LEFT_OR_RIGHT] < 4) {
    digitalWrite(activePins[LEFT_OR_RIGHT], LOW);
  } else {
    analogWrite(activePins[LEFT_OR_RIGHT], motorInputs[LEFT_OR_RIGHT]);
  }
}

void measureVelocities() {
  Prev_LeftEncoderTicks = _LeftEncoderTicks;
  Prev_RightEncoderTicks = _RightEncoderTicks;
  delay(TIME_INTERVAL);
  feedbackVelocities[LEFT] = abs(_LeftEncoderTicks - Prev_LeftEncoderTicks);
  feedbackVelocities[RIGHT] = abs(_RightEncoderTicks - Prev_RightEncoderTicks);
  Serial.print(feedbackVelocities[LEFT]);
  delay(5);
  Serial.print("   ");
  delay(5);
  Serial.println(feedbackVelocities[RIGHT]);
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

