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

class ChessBot
{
    public:
        ChessBot();
        void Center();
        void CrossSquares(int numOfSquares);
        void RotateBaseTo(float endAngle);

        void CheckSquareState();
        void AccelTo(int endspeed);
        void HardStop();
        void SetWheelVelocitiesStraight(float endAngle);
        void SetWheelVelocitiesTurn(float endAngle);
        void RotateWheels(int angularSpeedL, int angularSpeedR);
        void Setup();
        void alignWithEdgeBlack();
        void alignWithEdgeWhite();
        void HandleLeftMotorInterruptA();
        void HandleRightMotorInterruptA();
        void ObtainAddress();
    
        byte errorFlag; //For error detection. Can be 0 (no error), 1 (not fully within a square when asked to 
        //center or cross squares)
    
        byte botNumber;
		
		bool needToCenter; //For detecting whether centering is necessary before the next movement. 
    
        int backRightLight; //Used by CheckSquareState() to hold 
        int backLeftLight;  //photodiode value for determining position 
        int frontLeftLight; //on chessboard. Can be 0 to 1256. 
        int frontRightLight;
        
        bool squareStateArray[4];   //Used by CheckSquareState() to hold bool value that corresponds to whether a  
        String squareState;         //photodiode is over a black (1) or white (0) square. String squareState is the 
                                    //hex form for the 4-digit binary number that squareStateArray represents.
        
        volatile bool _LeftEncoderBSet;         //Used by HandleLeftMotorInterruptA() and HandleRightMotorInterruptA() respectively
        volatile long _LeftEncoderTicks;        //as well as alignWithEdgeBlack() and alignWithEdgeWhite(). Used to count 
        volatile bool _RightEncoderBSet;        //encoder ticks.
        volatile long _RightEncoderTicks; 
        
        
        
        int velocityState;                              //Used by nearly all methods listed below. Variable velocityState holds a 
        int currentVelocityR, currentVelocityL;         //value between -255 and 255 that corrresponds to the velocity of the ChessBot
        int proportionalOffSetR, proportionalOffSetL;   //center of gravity. Likewise, currentVelocityR and currentVelocityL hold such
        float integralOffsetR, integralOffsetL;         //values for the right and left wheels respectively. Each of the offsets are
        float derivativeOffsetR, derivativeOffsetL;     //used in SetWheelVelocities() to achieve a desired heading angle. They can
                                                        //be any real value within the limits of int and float definitions. 
            
        float prevAngle;                    //Used by several methods to hold computed heading angles. The FreeSixIMU object my3IMU
        float currentAngles[3];             //is defined by the FreeSixIMU.h library, which contains drivers for the gyroscope. 
        int angleState;                     //Using my3IMU.getEuler(), for example, will use the gyroscope MEMS device
        FreeSixIMU my3IMU;                  //to compute the current angles of the ChessBot and return pitch, yaw, and roll
                                            //angles between -180 and 180 (counterclockwise positive). The methods below are only
                                            //concerned with the angle of rotation about the axis perpendicular to the ground,
                                            //which corresponds to currentAngles[0]. 
    
};


#endif