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
  Bot.CheckSquareState();
  Serial.print(Bot.backRightLight);
  Serial.print(".");
  Serial.print(Bot.backLeftLight);
  Serial.print(".");
  Serial.print(Bot.frontRightLight);
  Serial.print(".");
  Serial.print(Bot.frontLeftLight);
  Serial.print("    ");
  Serial.println(Bot.squareState);
  delay(500);
}

void GlobalHandleRightMotorInterruptA(){
    Bot.HandleRightMotorInterruptA();
}


void GlobalHandleLeftMotorInterruptA(){
     Bot.HandleLeftMotorInterruptA();
}
