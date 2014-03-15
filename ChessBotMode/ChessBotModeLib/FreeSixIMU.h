/*
FreeSixIMU.h - A libre and easy to use orientation sensing library for Arduino
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

#define FIMU_ACC_ADDR ADXL345_ADDR_ALT_LOW // SDO connected to GND
#include <FIMU_ITG3200.h>

#ifndef FreeSixIMU_h
#define FreeSixIMU_h

#define FIMU_BMA180_DEF_ADDR BMA180_ADDRESS_SDO_LOW
#define FIMU_ITG3200_DEF_ADDR ITG3200_ADDR_AD0_LOW // AD0 connected to GND
// HMC5843 address is fixed so don't bother to define it

#ifndef cbi
#define cbi(sfr, bit) (_SFR_BYTE(sfr) &= ~_BV(bit))
#endif

class FreeSixIMU
{
  public:
    FreeSixIMU();
    void init();
    void reinit();
    void init(int gyro_addr, bool fastmode);
    void getEuler(float * angles);
    
    ITG3200 gyro;
    
  private:
    volatile float gx, gy, gz;
    volatile float angleX, angleY, angleZ;
    unsigned long lastUpdate, now; // sample period expressed in milliseconds
    float sampleFreq; // sample period expressed in seconds
};


#endif // FreeSixIMU_h
