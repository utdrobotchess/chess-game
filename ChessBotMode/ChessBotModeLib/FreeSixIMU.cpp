#include <inttypes.h>
#include "FreeSixIMU.h"


FreeSixIMU::FreeSixIMU() {
	gyro = ITG3200();
    gx = 0;
    gy = 0;
    gz = 0;
    angleX = 0;
    angleY = 0;
    angleZ = 0;
    lastUpdate = 0;
    now = 0;
}

void FreeSixIMU::reinit() {
    gx = 0;
    gy = 0;
    gz = 0;
    angleX = 0;
    angleY = 0;
    angleZ = 0;
    gyro.zeroCalibrate(200,5);
    lastUpdate = micros();
}

void FreeSixIMU::init() {
  init(FIMU_ITG3200_DEF_ADDR, false);
}

void FreeSixIMU::init(int gyro_addr, bool fastmode) {
  delay(5);
  
  // disable internal pullups of the ATMEGA which Wire enable by default
  #if defined(__AVR_ATmega168__) || defined(__AVR_ATmega8__) || defined(__AVR_ATmega328P__)
    // deactivate internal pull-ups for twi
    // as per note from atmega8 manual pg167
    cbi(PORTC, 4);
    cbi(PORTC, 5);
  #else
    // deactivate internal pull-ups for twi
    // as per note from atmega128 manual pg204
    cbi(PORTD, 0);
    cbi(PORTD, 1);
  #endif
  
  if(fastmode) { // switch to 400KHz I2C - eheheh
    TWBR = ((16000000L / 400000L) - 16) / 2; // see twi_init in Wire/utility/twi.c
    // TODO: make the above usable also for 8MHz arduinos..
  }
  
  // init ITG3200
  gyro.init(gyro_addr);
  //delay(500);
  // calibrate the ITG3200
  gyro.zeroCalibrate(200,5);
  
}

void FreeSixIMU::getEuler(float * angles) {
    float val[3];
    
    now = micros();
    if(now > lastUpdate)
    {
        gyro.readGyro(&val[0]);
        
        gx = val[0];
        gy = val[1];
        gz = val[2];
        
        sampleFreq = 1.0 / ((now - lastUpdate) / 1000000.0);
        lastUpdate = now;
        
        gx *= (1.0f / sampleFreq);  
        gy *= (1.0f / sampleFreq);
        gz *= (1.0f / sampleFreq);
        
        angleX += gx;
        angleY += gy;
        angleZ += gz;

        angles[2] = angleX;
        angles[1] = angleY;
        angles[0] = angleZ;
    }
    else
        lastUpdate = now;
}
