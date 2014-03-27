#include <Wire.h>
#include <ChessBotModeLib.h>

ChessBot Bot = ChessBot();

void setup() 
{ 
  Bot.Setup();   
  attachInterrupt(c_RightEncoderInterrupt, GlobalHandleRightMotorInterruptA, RISING); 
  attachInterrupt(c_LeftEncoderInterrupt, GlobalHandleLeftMotorInterruptA, RISING); 
}

void loop() 
{ 
}

void GlobalHandleRightMotorInterruptA(){
    Bot.HandleRightMotorInterruptA();
}


void GlobalHandleLeftMotorInterruptA(){
     Bot.HandleLeftMotorInterruptA();
}
