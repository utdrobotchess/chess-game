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
    float gx, gy, gz;
    float angleX, angleY, angleZ;
    unsigned long lastUpdate, now; // sample period expressed in milliseconds
    float sampleFreq; // sample period expressed in seconds
};


#endif // FreeSixIMU_h
