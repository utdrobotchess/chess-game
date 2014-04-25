#include <Wire.h>
#include <ChessBotModeLib.h>

ChessBot Bot = ChessBot();
float currentAngles[3];

void setup() 
{ 
  Bot.Setup();
    
    
  attachInterrupt(c_RightEncoderInterrupt, GlobalHandleRightMotorInterruptA, RISING); 
  attachInterrupt(c_LeftEncoderInterrupt, GlobalHandleLeftMotorInterruptA, RISING); 
  
}

void loop() 
{ 
   Bot.CrossSquares(2);
   Bot.RotateBaseTo(90);
   Bot.CrossSquares(2);
   Bot.RotateBaseTo(90);
   Bot.CrossSquares(2);
   Bot.RotateBaseTo(90);
   Bot.CrossSquares(2);
   Bot.RotateBaseTo(90);
   Bot.Center();
}

void GlobalHandleRightMotorInterruptA(){
    Bot.HandleRightMotorInterruptA();
}


void GlobalHandleLeftMotorInterruptA(){
     Bot.HandleLeftMotorInterruptA();
}
