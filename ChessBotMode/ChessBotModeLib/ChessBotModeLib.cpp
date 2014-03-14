/*
 ChessBotModeLib.cpp
 
 This file contains definitions of methods within ChessBotModeLib.h. Ensure that this and the .h file are both
 within the same directory under the '~/Arduino/libraries' directory.
*/

#include <ChessBotModeLib.h>


byte errorFlag = 0; //For error detection. Can be 0 (no error), 1 (not fully within a square when asked to 
                    //center or cross squares)

int backRightLight; //Used by CheckSquareState() to hold 
int backLeftLight;  //photodiode value for determining position 
int frontLeftLight; //on chessboard. Can be 0 to 1256. 
int frontRightLight;

bool squareStateArray[4];   //Used by CheckSquareState() to hold bool value that corresponds to whether a  
String squareState;         //photodiode is over a black (1) or white (0) square. String squareState is the 
                            //hex form for the 4-digit binary number that squareStateArray represents.

volatile bool _LeftEncoderBSet;         //Used by HandleLeftMotorInterruptA() and HandleRightMotorInterruptA() respectively
volatile long _LeftEncoderTicks = 0;    //as well as centerAgainstEdgeBlack() and centerAgainstEdgeWhite(). Used to count 
volatile bool _RightEncoderBSet;        //encoder ticks.
volatile long _RightEncoderTicks = 0; 



int velocityState=0;                            //Used by nearly all methods listed below. Variable velocityState holds a 
int currentVelocityR, currentVelocityL;         //value between -255 and 255 that corrresponds to the velocity of the ChessBot
int proportionalOffSetR, proportionalOffSetL;   //center of gravity. Likewise, currentVelocityR and currentVelocityL hold such
float integralOffsetR=0, integralOffsetL=0;     //values for the right and left wheels respectively. Each of the offsets are
float derivativeOffsetR=0, derivativeOffsetL=0; //used in SetWheelVelocities() to achieve a desired heading angle. They can
                                                //be any real value within the limits of int and float definitions. 

float prevAngle = 0;                //Used by several methods to hold computed heading angles. The FreeSixIMU object my3IMU
float currentAngles[3];             //is defined by the FreeSixIMU.h library, which contains drivers for the gyroscope. 
int angleState=0;                   //Using my3IMU.getEuler(currentAngles), for example, will use the gyroscope MEMS device
FreeSixIMU my3IMU = FreeSixIMU();   //to compute the current angles of the ChessBot and return pitch, yaw, and roll
                                    //angles between -180 and 180 (counterclockwise positive). The methods below are only
                                    //concerned with the angle of rotation about the axis perpendicular to the ground,
                                    //which corresponds to currentAngles[0]. 

/*
 
*/
void Center() {
    
    CheckSquareState();
    if(squareState == "f")
    {
        RotateBaseTo(90); 
        centerAgainstEdgeBlack();
        RotateBaseTo(-90);
        centerAgainstEdgeBlack();
    }
    else if(squareState == "0")
    {
        RotateBaseTo(90);  
        centerAgainstEdgeWhite();
        RotateBaseTo(-90);
        centerAgainstEdgeWhite(); 
    }
    else
        errorFlag = 1;
    
    
}//done

void CrossSquares(int numOfSquares){
    byte lookForCrossingSwitch = 0;
    String startingSquare;
    int numOfCrossings = 0;
    int crossingSpeed = 150;
    float adjustAngle = 0;
    
    CheckSquareState();
    if((squareState == "f") || (squareState == "0"))
    {
        startingSquare = squareState;
        
        if(abs(angleState) == 45 || abs(angleState) == 135 || abs(angleState) == 225 || abs(angleState) == 315)
        {
            AccelTo(crossingSpeed);
            while(numOfCrossings < numOfSquares)
            {
                
                CheckSquareState();
                switch (lookForCrossingSwitch)
                {
                    case 0:
                        if((squareState == "e") || (squareState == "1"))
                        {
                            adjustAngle += -0.1;
                            delay(5);
                        }
                        if((squareState == "d") || (squareState == "2"))
                        {
                            adjustAngle += 0.1;
                            delay(5);
                        }
                        
                        if((squareState == "c") || (squareState == "3"))
                            lookForCrossingSwitch = 1;
                        break;
                    case 1:
                        if(startingSquare == squareState)
                        {
                            numOfCrossings++;
                            lookForCrossingSwitch = 0;
                        }
                        break; 
                }
                
                SetWheelVelocities(0);
                RotateWheels(currentVelocityL, currentVelocityR);
                
            }
        }
        else
        {
            AccelTo(crossingSpeed);
            while(numOfCrossings < numOfSquares)
            {
                CheckSquareState();
                switch (lookForCrossingSwitch)
                {
                    case 0:
                        if((squareState == "e") || (squareState == "1"))
                        {
                            adjustAngle += -0.1;
                            delay(5);
                        }
                        if((squareState == "d") || (squareState == "2"))
                        {
                            adjustAngle += 0.1;
                            delay(5);
                        }
                        
                        if((squareState == "c") || (squareState == "3"))
                            lookForCrossingSwitch = 1;
                        break;
                    case 1:
                        if((startingSquare == "f" && squareState == "0") || (startingSquare == "0" && squareState == "f"))
                        {
                            numOfCrossings++;
                            lookForCrossingSwitch = 0;
                            startingSquare = squareState;
                        }
                        break; 
                }
                
                SetWheelVelocities(adjustAngle);
                RotateWheels(currentVelocityL, currentVelocityR);
                
            }
        }
    }
    else
        errorFlag = 1;
    
    delay(100);
    HardStop();
} //done (might need to add delay between SetWheelVelocities() updates. Also test the adjustAngle feature)

void RotateBaseTo(float endAngle){
    float fineTuneBeginTime, fineTuneElapsedTime, fineTuneEndTime = 500;
    byte fineTuneTimingSwitch = 0; 
    
    integralOffsetR = 0;
    integralOffsetL = 0;
    velocityState = 0;
    prevAngle = 0;
    
    
    do
    {
        my3IMU.reinit();
        my3IMU.getEuler(currentAngles);
    } while(abs(currentAngles[0]) > 2);
    
    
        while(fineTuneElapsedTime < fineTuneEndTime)
        {
            
            SetWheelVelocities(endAngle);
            RotateWheels(currentVelocityL,currentVelocityR);
            delay(10);
            
            if(abs(currentAngles[0]-endAngle)<1)
            {
                switch(fineTuneTimingSwitch)
                {
                    case 0:
                        fineTuneBeginTime = millis();
                        fineTuneTimingSwitch = 1;
                        break;
                    case 1:
                        fineTuneElapsedTime = millis() - fineTuneBeginTime;
                        break;
                }
            }
            
        }
    
    HardStop();
    angleState += endAngle;
    if(abs(angleState) == 360)
        angleState = 0;
}//done (might need to adjust delay between SetWheelVelocities() updates)

void CheckSquareState(){
    backRightLight = analogRead(backRightLightPin);
    backLeftLight = analogRead(backLeftLightPin);
    frontLeftLight = analogRead(frontLeftLightPin);
    frontRightLight = analogRead(frontRightLightPin);
    
    squareStateArray[0] = (backRightLight > 800);
    squareStateArray[1] = (backLeftLight > 800);
    squareStateArray[2] = (frontLeftLight > 800);
    squareStateArray[3] = (frontRightLight > 800);
    
    squareState = String(squareStateArray[0]*8 + squareStateArray[1]*4 + squareStateArray[2]*2 + squareStateArray[3],HEX);
}//done

void AccelTo(int endspeed){
    int accelBy = (endspeed - velocityState)/abs(endspeed - velocityState);
    integralOffsetR = 0;
    integralOffsetL = 0;
    prevAngle = 0;
    
    
    if(endspeed < -70 && velocityState > -70)
        velocityState = -70;
    
    if(endspeed > 70 && velocityState < 70)
        velocityState = 70;
    
    
    do
    {
        my3IMU.reinit();
        my3IMU.getEuler(currentAngles);
    } while(abs(currentAngles[0]) > 2);
    
    
    
    while(abs(endspeed - velocityState) > 1)
    {
        velocityState += accelBy; 
        
        SetWheelVelocities(0);
        RotateWheels(currentVelocityL, currentVelocityR);
        delay(10);
    }
}//done

void HardStop(){
    
    RotateWheels(-1*currentVelocityL,-1*currentVelocityR);
    delay(20);
    RotateWheels(0, 0);
    currentVelocityL = 0;
    currentVelocityR = 0;
    velocityState = 0;
}//done

void SetWheelVelocities(float endAngle){
    my3IMU.getEuler(currentAngles); 
    
    if(endAngle == 180)
    {
        if(currentAngles[0]>=0)
        {
            integralOffsetR += abs(endAngle - currentAngles[0])/50;
            integralOffsetL += abs(endAngle - currentAngles[0])/-50;
            
            proportionalOffSetR = 3*abs(endAngle - currentAngles[0]);
            proportionalOffSetL = -3*abs(endAngle - currentAngles[0]);
        }
        else
        {
            integralOffsetR += abs(endAngle - currentAngles[0])/-50;
            integralOffsetL += abs(endAngle - currentAngles[0])/50;
            
            proportionalOffSetR = -3*abs(endAngle - currentAngles[0]);
            proportionalOffSetL = 3*abs(endAngle - currentAngles[0]);
            
        }
    }
    
    else
    {
        integralOffsetR += (endAngle - currentAngles[0])/50;
        integralOffsetL += (endAngle - currentAngles[0])/-50;
        
        proportionalOffSetR = 3*(endAngle - currentAngles[0]);
        proportionalOffSetL = -3*(endAngle - currentAngles[0]);
        
    }
    
    derivativeOffsetR = -10*(currentAngles[0] - prevAngle);
    derivativeOffsetL = 10*(currentAngles[0] - prevAngle);
    
    prevAngle = currentAngles[0];
    
    currentVelocityR = velocityState + integralOffsetR + proportionalOffSetR + derivativeOffsetR; 
    currentVelocityL = velocityState + integralOffsetL + proportionalOffSetL + derivativeOffsetL;
    
        
} //need to check if gains are set correctly

/*
 void RotateWheels()
 
 Description:           Takes input wheel speeds and ensures that they do not exceed abs(255). Then writes the corrresponding
                        values to the motor pins. 
 
 Methods Called by:     RotateBaseTo()
                        CrossSquares()
                        AccelTo()
                        HardStop()
                        
 Methods Called:        None
 Global Vars effected:  None
 */
void RotateWheels(int angularSpeedL, int angularSpeedR){
    
    if(angularSpeedL > 255)
        angularSpeedL = 255;
    else if(angularSpeedL < -255)
        angularSpeedL = -255;
    
    if(angularSpeedR > 255)
        angularSpeedR = 255;
    else if(angularSpeedR < -255)
        angularSpeedR = -255;
    
    if(angularSpeedR >= 0) //forward
    {
        analogWrite(motor2Pin, angularSpeedR); 
        digitalWrite(motor1Pin, LOW);
    }
    else                   //reverse
    {
        analogWrite(motor1Pin, abs(angularSpeedR));
        digitalWrite(motor2Pin, LOW);
    }
    
    
    if(angularSpeedL >= 0) //forward
    {
        analogWrite(motor4Pin, angularSpeedL);
        digitalWrite(motor3Pin, LOW);
    }
    else                   //reverse
    {
        analogWrite(motor3Pin, abs(angularSpeedL));
        digitalWrite(motor4Pin, LOW);
    }
}

/*
 void Setup()
 
 Description:           Sets encoder pins as inputs and turns on pullup resistors to allow for normal function of the encoder 
                        circuits. Attaches interrupts to rising edge of the encoder output. Begins Serial communication at
                        9600 BAUD. Begins I2C communication and initializes the gyroscope. Assigns motorpins as outputs. This
                        method should only be called once in the setup() method of an arduino sketch and should not be used 
                        anywhere else. 
 
 
 Methods Called by:     None
 Methods Called:        HandleRightMotorInterruptA
                        HandleLeftMotorInterruptA
 
 Global Vars effected:  None
*/
void Setup(){
    
    pinMode(c_LeftEncoderPinA, INPUT);       
    digitalWrite(c_LeftEncoderPinA, LOW);  // turn on pullup resistors 
    pinMode(c_LeftEncoderPinB, INPUT);
    digitalWrite(c_LeftEncoderPinB, LOW);  // turn on pullup resistors
    attachInterrupt(c_LeftEncoderInterrupt, HandleLeftMotorInterruptA, RISING); 
    
    pinMode(c_RightEncoderPinA, INPUT); 
    digitalWrite(c_RightEncoderPinA, LOW);  // turn on pullup resistors 
    pinMode(c_RightEncoderPinB, INPUT);
    digitalWrite(c_RightEncoderPinB, LOW);  // turn on pullup resistors 
    attachInterrupt(c_RightEncoderInterrupt, HandleRightMotorInterruptA, RISING); 
    
    Serial.begin(9600); 
    Wire.begin();
    delay(5);
    my3IMU.init();
    delay(5);
    
    pinMode(motor1Pin, OUTPUT); 
    pinMode(motor2Pin, OUTPUT);
    pinMode(motor3Pin, OUTPUT); 
    pinMode(motor4Pin, OUTPUT);    
    
    pinMode(12, OUTPUT);
    digitalWrite(12, HIGH); 
}

void centerAgainstEdgeBlack(){
    AccelTo(70);
    while(squareState == "f")
    {
        CheckSquareState();
        SetWheelVelocities(0);
        RotateWheels(currentVelocityL, currentVelocityR);
    }
    HardStop();
    while(squareState != "c")
    {
        CheckSquareState();
        if(squareState == "d")
        {
            RotateWheels(0, 90);
            currentVelocityL = 0;
            currentVelocityR = 90;
        }
        else if(squareState == "e")
        {
            RotateWheels(90, 0);
            currentVelocityL = 90;
            currentVelocityR = 0;
        }
    }
    HardStop();
    _LeftEncoderTicks = 0;
    _RightEncoderTicks = 0;
    AccelTo(-70);
    while((abs(_LeftEncoderTicks) < 1400) && (abs(_RightEncoderTicks) < 1400))
    {
        SetWheelVelocities(0);
        RotateWheels(currentVelocityL, currentVelocityR);
    }
    HardStop();
    CheckSquareState();
}//done (might need to add delay between SetWheelVelocities() updates)

void centerAgainstEdgeWhite(){
    AccelTo(70);
    while(squareState == "0")
    {
        CheckSquareState();
        SetWheelVelocities(0);
        RotateWheels(currentVelocityL, currentVelocityR);
    }
    HardStop();
    while(squareState != "3")
    {
        CheckSquareState();
        if(squareState == "2")
        {
            RotateWheels(0, 90);
            currentVelocityL = 0;
            currentVelocityR = 90;
        }
        else if(squareState == "1")
        {
            RotateWheels(90, 0);
            currentVelocityL = 90;
            currentVelocityR = 0;
        }
    }
    HardStop();
    _LeftEncoderTicks = 0;
    _RightEncoderTicks = 0;
    AccelTo(-70);
    while((abs(_LeftEncoderTicks) < 1400) && (abs(_RightEncoderTicks) < 1400))
    {
        SetWheelVelocities(0);
        RotateWheels(currentVelocityL, currentVelocityR);
    }
    HardStop();
    CheckSquareState();
}//done (might need to add delay between SetWheelVelocities() updates)

/*
 void HandleLeftMotorInterruptA()
 
 Description:           Called when interrupt pin defined in Setup() is rising. Checks to see if wheel is going forwards or
                        backwards based on whether c_leftEncoderPinB is high or low when interrupt is called. Then increments
                        encoder ticks if there is a forward movement, or decrements if there is a backwards movement.  
 
 
 Methods Called by:     Setup()
 Methods Called:        None
 
 Global Vars effected:  _LeftEncoderTicks
                        _LeftEncoderBSet
 */
void HandleLeftMotorInterruptA(){
    // Test transition; since the interrupt will only fire on 'rising' we don't need to read pin A 
    _LeftEncoderBSet = digitalReadFast(c_LeftEncoderPinB);   // read the input pin 
    
    // and adjust counter + if A leads B 
#ifdef LeftEncoderIsReversed 
    _LeftEncoderTicks -= _LeftEncoderBSet ? -1 : +1; 
#else 
    _LeftEncoderTicks += _LeftEncoderBSet ? -1 : +1; 
#endif 
}

/*
 void HandleRightMotorInterruptA()
 
 Description:           Called when interrupt pin defined in Setup() is rising. Checks to see if wheel is going forwards or
                        backwards based on whether c_rightEncoderPinB is high or low when interrupt is called. Then increments
                        encoder ticks if there is a forward movement, or decrements if there is a backwards movement.  
 
 
 Methods Called by:     Setup()
 Methods Called:        None
 
 Global Vars effected:  _RightEncoderTicks
                        _RightEncoderBSet
 */
void HandleRightMotorInterruptA(){
    // Test transition; since the interrupt will only fire on 'rising' we don't need to read pin A 
    _RightEncoderBSet = digitalReadFast(c_RightEncoderPinB);   // read the input pin 
    
    // and adjust counter + if A leads B 
#ifdef RightEncoderIsReversed 
    _RightEncoderTicks -= _RightEncoderBSet ? -1 : +1; 
#else 
    _RightEncoderTicks += _RightEncoderBSet ? -1 : +1; 
#endif
}

/*
 void checkForError()
 
 Description:           Unfinished. We plan to use it to communicate to the main chess program that the piece cannot make
                        the desired move and must be manually repositioned before continuing the game. 
 
 
 Methods Called by:     
 Methods Called:        
 
 Global Vars effected:  
 */
void checkForError(){
    if(errorFlag != 0)
    {
    }
}
