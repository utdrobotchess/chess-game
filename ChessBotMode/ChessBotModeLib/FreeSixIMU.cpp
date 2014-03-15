/*
FreeSixIMU.cpp - A libre and easy to use orientation sensing library for Arduino
Copyright (C) 2011 Fabio Varesano <fabio at varesano dot net>

Development of this code has been supported by the Department of Computer Science,
Universita' degli Studi di Torino, Italy within the Piemonte Project
http://www.piemonte.di.unito.it/


This program is free software: you can redistribute it and/or modify
it under the terms of the version 3 GNU General Public License as
published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

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
    lastUpdate = 0;
    now = 0;
    gyro.zeroCalibrate(128,5);
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
  delay(1000);
  // calibrate the ITG3200
  gyro.zeroCalibrate(128,5);
  
}

void FreeSixIMU::getEuler(float * angles) {
    float val[3];
    now = micros();
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

    angles[0] = angleX;
    angles[1] = angleY;
    angles[2] = angleZ;
}
