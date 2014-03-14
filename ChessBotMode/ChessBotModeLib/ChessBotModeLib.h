/*
 ChessBotModeLib.h
 
 This library is used for UTD ChessBot. More information about this project can be accessed here:
 
 (https://github.com/ryanjmarcotte/UTD-Robot-Chess)
 
 ChessBotModeLib.h contains all methods commonly used by the ChessBot to maneuver about the chessboard. 
 It is written specifically for the Arduino Mega 2560 and thus will not work for any other microcontroller 
 without pin and syntax adjustments. To use this library, make sure that it and the following libaries
 are all within the same directory inside of the '~/Arduino/libraries':
 
 Wire.h
 Arduino.h
 digitalWriteFast.h
 FreeSixIMU.h
 
 Additionally, Wire.h must be included before ChessBotModeLib.h within the arduino sketch that uses
 this library. Otherwise, there will be a compiler error where the compiler does not recognize the
 definition of the Wire object. Example code can be found at the link above. 
*/


#ifndef ChessMode_h
#define ChessMode_h

#include <Wire.h>
#include <Arduino.h>
#include <digitalWriteFast.h> 
#include <FreeSixIMU.h>

/*
 The following are definitions that assign pin names to descriptive variables. These pin names correspond 
 to how the arduino is connected to some of the sensors and actuators used by the ChessBot. These are:
 4 Photodiodes
 2 Encoders
 2 Motors
 
 Circuit diagrams available at link in description.
*/  

// Photodiode Pins
#define backRightLightPin A12
#define backLeftLightPin A13
#define frontLeftLightPin A14
#define frontRightLightPin A15

// Encoder Pins
#define c_LeftEncoderInterrupt 0
#define c_LeftEncoderPinA 2 
#define c_LeftEncoderPinB 4 
#define LeftEncoderIsReversed false

#define c_RightEncoderInterrupt 1 
#define c_RightEncoderPinA 3 
#define c_RightEncoderPinB 5 
#define RightEncoderIsReversed false

//Right Motor Pins
#define motor1Pin 9    // H-bridge leg 1 (pin 2, 1A)
#define motor2Pin 8  // H-bridge leg 2 (pin 7, 2A)
//Left Motor Pins
#define motor3Pin 11
#define motor4Pin 10
 

/*
 The following are methods used by the ChessBot to maneuver around the chessboard in various ways. The methods Center(), 
 CrossSquares(), and RotateBaseTo() are written to be at the highest level of abstraction. Those three methods contain 
 the instructions for the three types of movements that a ChessBot should make on the chessboard, which are centering within
 a square, moving across a set number of squares, and rotating in place. The other methods are to ensure that these three
 are performed reliably.
*/

void Center();
void CrossSquares(int numOfSquares);
void RotateBaseTo(float endAngle);

void CheckSquareState();
void AccelTo(int endspeed);
void HardStop();
void SetWheelVelocities(int endAngle);
void RotateWheels(int angularSpeedL, int angularSpeedR);
void Setup();
void alignWithEdgeBlack();
void alignWithEdgeWhite();
void HandleLeftMotorInterruptA();
void HandleRightMotorInterruptA();

#endif