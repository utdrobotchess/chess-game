## This piece of code is used for collecting the output data of the System_ID
## tests. It is currently undergoing debugging (the text it reads from the 
## the serial port is scrambled. Note that using a terminal like PuTTY to
## observe the output of the System_ID tests would yield unscrambled text,
## and so the problem is most likely either within this piece of code,
## or within the pyserial libraries downloaded by the author

# Author: Omar Hasan

## import the serial library
import serial

## Boolean variable that will represent 
## whether or not the arduino is connected
connected = False

## establish connection to the serial port that your arduino 
## is connected to.

locations=['/dev/tty.usbserial-A100SCN0','/dev/cu.usbserial-A100SCN0']

for device in locations:
    try:
        print "Trying...",device
        ser = serial.Serial(device, 9600)
        break
    except:
        print "Failed to connect on",device

## loop until the arduino tells us it is ready
while not connected:
    serin = ser.read()
    connected = True

## open text file to store the current 
##gps co-ordinates received from the rover    
text_file = open("ImpulseFunctionData.txt", 'w')
## read serial data from arduino and 
## write it to the text file 'ImpulseFunctionData.txt'
while "." not in ser.read():
    if ser.inWaiting():
        x=ser.read()
        print(x);
        text_file.write(x)
        if x=="\n":
            text_file.seek(0)
            text_file.truncate()
        text_file.flush()

## close the serial connection and text file
text_file.close()
ser.close()
