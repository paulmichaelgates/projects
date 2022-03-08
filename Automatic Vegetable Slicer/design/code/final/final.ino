#include<Stepper.h>
// AUTHOR: Paul Gates
// FSE100 03/27/2021

//global constants
const int MOTOR_DELAY = 1500; //delay from blade stopping to switching to next container when full.
//global variables

// **** BLADE / ON SWITCH  ****

//blade pins
const int onOffSwitchPin = 35;
const int bladeMotorSpeedPin = 5;
const int bladeMotorDirectionPin_1 = 10;
const int bladeMotorDirectionPin_2 = 11;

//blade constants
const int BLADE_MOTOR_SPEED = 155; // *** TOOD-> change this value 
int ON_OFF_INTERVAL = 500; //300ms

//blade variables
bool bladeIsOn = false;
float onOffPrev = 0;


// **** PLUNGER ****

//plunger pins
const int plungerMotorSpeedPin = 3;
const int plungerMotorDirectionPin_1 = 21;
const int plungerMotorDirectionPin_2 = 20; 
const int hallSensorPin = 33;

//plunger constants
const int PLUNGER_MOTOR_SPEED = 255;
const int MAX_PLUNGER_DELAY = 23300;
enum PlungerState{
  plunge,
  retract,
  rest
};

//plunger variables
PlungerState plungerState = rest;
int hallSensorValue = 0;
float delayStartedAt = 0;
float plungerDelay = 0;

// *** FULL SENSOR ****

//full sensor pins
const int fullSensorTriggerPin = 13;
const int fullSensorEchoPin = 12;
const int container1LEDPin = 48;
const int container2LEDPin = 49;
const int container3LEDPin = 50;
const int OnIndicatorLEDPin = 53;
const int OffIndicatorLEDPin = 52;

//full sensor constants
const int ALLOWED_INTERVALS_OF_FULL_DETECTION = 4;
const int FULL_INTERVAL = 300; // *** TODO: Change this value 
const int FULL_THRESHOLD_HIGH = 50; // *** TODO: Change this value 
const int FULL_THRESHOLD_LOW = 4; // *** TODO: Change this value 
const int NUM_CONTAINERS = 3;
const int DELAY_AMT = 100;

//full sensor variables
bool containerIsFull = false; 
int fullCounter = 0;
float fullPrev = 0;
int numContainerFull = 0;
bool terminated = false;
bool disableOnOff = false;

// *** How does this work? *** 
// to get amount of time for detection: 
// ALLOWED_INTERVALS_OF_FULL_DETECTTION X interval = 2000MS = 2 Seconds
//
// this means that...
//      If an object is within (FULL_TRESHOLD)cm, for 2 Seconds, then the 
//      container will be considered full

// *** SPIN TRAY ****

//spin tray pins
const int trayMotorController_IN_1 = 9;
const int trayMotorController_IN_2 = 8;
const int trayMotorController_IN_3 = 7;
const int trayMotorController_IN_4 = 6;
const int trayMotorControllerSpeedPin = 3; // analog 
const int nextContainerButtonPin = 34;
int alignmentHallEffectSensorPin = 32; 
int full[3];

//spin tray constants
const  int STEPS_PER_ROTATION = 200; // don't change this as it is the value recommended for NEMA17 motor 
const int INITIAL_SPIN_AMT = 60; 
const int INCREMENT_SPIN_AMT = 2; 
const int TRAY_MOTOR_SPEED = 20; 
const int SPIN_TRAY_HALL_EFFECT_SENSOR_DETECTION_INTERVAL = 2;

//spin tray variables
bool trayIsSpinning = false;
float spinPrev = 0;
Stepper rotationTrayStepperMotor(STEPS_PER_ROTATION, trayMotorController_IN_1,
                                                       trayMotorController_IN_2, 
                                                       trayMotorController_IN_3,
                                                        trayMotorController_IN_4);
//SETUP FUNCTIONS
void setUpBladeMotorController(){

  // set up output pins 
  pinMode(bladeMotorSpeedPin, OUTPUT);     
  pinMode(bladeMotorDirectionPin_1, OUTPUT);
  pinMode(bladeMotorDirectionPin_2, OUTPUT);

  //setup on/off button
  pinMode(onOffSwitchPin, INPUT);

  //on/off LEDs
  pinMode(OffIndicatorLEDPin, OUTPUT);
  pinMode(OnIndicatorLEDPin, OUTPUT);
}

void setUpPlunger(){

  // set up output pins
  pinMode(plungerMotorSpeedPin, OUTPUT);
  pinMode(plungerMotorDirectionPin_1, OUTPUT);
  pinMode(plungerMotorDirectionPin_2, OUTPUT);

  //setup sensor
  pinMode(hallSensorPin, INPUT);
}

void setUpFullSensor(){
  
  // set up pins 
  pinMode(fullSensorTriggerPin, OUTPUT);
  pinMode(fullSensorEchoPin, INPUT);
  pinMode(container1LEDPin, OUTPUT);
  pinMode(container2LEDPin, OUTPUT);
  pinMode(container3LEDPin, OUTPUT);
  full[0] = 0;
  full[1] = 0;
  full[2] = 0;
}

void setUpSpinTray(){

   // set up pins for stepper motor 
   pinMode(trayMotorController_IN_1, OUTPUT);
   pinMode(trayMotorController_IN_2, OUTPUT);
   pinMode(trayMotorController_IN_3, OUTPUT);
   pinMode(trayMotorController_IN_4, OUTPUT);
   pinMode(trayMotorControllerSpeedPin, OUTPUT);

   //set speed for stepper motor
   rotationTrayStepperMotor.setSpeed(TRAY_MOTOR_SPEED);

   //set up pins for next button
   pinMode(nextContainerButtonPin, INPUT);

   //set up pin for spin tray hall sensor
   pinMode(alignmentHallEffectSensorPin, INPUT);
}

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  
  // call all of the set up functions
  setUpBladeMotorController();
  setUpPlunger();
  setUpSpinTray();
  setUpFullSensor();
  
  spinTrayToNextContainer();
  digitalWrite(OnIndicatorLEDPin, LOW);
  digitalWrite(OffIndicatorLEDPin, HIGH);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(!terminated){
     if(plungerState == plunge){
          checkFullSensor();
          checkForPlungerMagnet();
          checkOnOffSwitch();
     }else if(plungerState == retract){
          checkRetractTime();
          checkSpinTray();     
     }else{
          checkOnOffSwitch();
          checkSpinTray();  
     }
  }
}

//Full sensor
void initialFullCheck(){
  
  // turn trigger pin to low 
  digitalWrite(fullSensorTriggerPin, LOW);
  delayMicroseconds(2);  
  
  
  // turn the trigger pin to high 
  digitalWrite(fullSensorTriggerPin, HIGH);
  delayMicroseconds(10);
  
  
  // now that we have value turn back off 
  digitalWrite(fullSensorTriggerPin, LOW);
  
  
  // send a high pulse to the echo pin 
  float distance = pulseIn(fullSensorEchoPin, HIGH) / 58.2;
  
  //IF(container is sensed to be full when not running)
  if(distance<FULL_THRESHOLD_LOW || distance > FULL_THRESHOLD_HIGH){

    Serial.print("distance:");
    Serial.println(distance);

    //increment initiallyFull
    numContainerFull++;

    //IF(all containers are full)
    if(numContainerFull == NUM_CONTAINERS){

      //terminate program
      //terminated = true;
      Serial.println("terminated");

      //flash full light
      //flashFullLight();

    //ELSE
    } else{

      //flash full light
      //flashFullLight();

      setFull();
      
      //spin tray to next container
      spinTrayToNextContainer();
      
    }//END IF
    
  }//END IF
}

void checkFullSensor(){    
     
  // get the time passed since start of program 
  int currentMillis = millis();

   //IF(interval between full sensor readings has passed)
  if(currentMillis - fullPrev > FULL_INTERVAL){
  
      // set previous to the current time for next time
      fullPrev = currentMillis; 
       
      // turn trigger pin to low 
      digitalWrite(fullSensorTriggerPin, LOW);
      delayMicroseconds(2);  
    
      // turn the trigger pin to high 
      digitalWrite(fullSensorTriggerPin, HIGH);
      delayMicroseconds(10);
     
      // now that we have value turn back off 
      digitalWrite(fullSensorTriggerPin, LOW);
        
      // send a high pulse to the echo pin 
      float distance = pulseIn(fullSensorEchoPin, HIGH) / 58.2;
      
      //IF(container is sensed to be full)
      if(distance<FULL_THRESHOLD_LOW || distance > FULL_THRESHOLD_HIGH){

        //increment full counter
        fullCounter++;

        //IF(too many intervals have been full in a row)
        if(fullCounter > ALLOWED_INTERVALS_OF_FULL_DETECTION){
            // make the container full and disable the on switch 
              disableOnOff = true;
              containerIsFull = true;  
            
              //change state to full
              setFull();
            
              //turn off blade
              turnOffBladeMotor();
            
              //spin to next container
              spinTrayToNextContainer();
            
              //retract the plunger
              retractPlunger();

              //set current output state to full
              setFull();
              
              //increment num containers full
              numContainerFull++;
              
        }//END IF

      //ELSE container not full
      }else{
        
        // reset the full counter 
        fullCounter = 0;
        
      }//END IF
      
   }//END IF
}

//Plunger
void checkForPlungerMagnet(){

  //IF(plunger at bottom of feed tube)
  if(!digitalRead(hallSensorPin) == HIGH){

    Serial.println("tube is at bottom");
    
    //turn off blade
    turnOffBladeMotor();
    
    //retract plunger
    retractPlunger();
    
  }//END IF
}

void checkRetractTime(){

  //IF(that time is more than the plunger delay)
  if(millis() - delayStartedAt > plungerDelay){

      //stop retracting
      restPlunger();
      
  }//END IF
}

void retractPlunger(){

  float currTime = millis();

  Serial.print("curr time: ");
  Serial.println(currTime);
  Serial.print("delayStarted: ");
  Serial.println(delayStartedAt);

  //SET retract delay period = time plunger was plunging
  plungerDelay = currTime - delayStartedAt;

  //SET delay started = current time
  delayStartedAt = currTime;

  Serial.print("plungerDelay: ");
  Serial.println(plungerDelay);
    
  //set plunger direction to retract
  digitalWrite(plungerMotorDirectionPin_1, HIGH);
  digitalWrite(plungerMotorDirectionPin_2, LOW);

  // turn the plunger motor on
  analogWrite(plungerMotorSpeedPin, PLUNGER_MOTOR_SPEED);

  // change state to retract
  plungerState = retract;
}

void restPlunger(){

  // remove voltage from both direction pin s
  digitalWrite(plungerMotorDirectionPin_1, LOW);
  digitalWrite(plungerMotorDirectionPin_2, LOW);

  // turn the plunger motor speed to 0 
  analogWrite(plungerMotorSpeedPin, 0);

  // change state to rest
  plungerState = rest;

  delayStartedAt = 0;
}

void startPlunger(){

  //SET delay started = current time
  delayStartedAt = millis();

  //set plunger direction to go down
  digitalWrite(plungerMotorDirectionPin_1, LOW);
  digitalWrite(plungerMotorDirectionPin_2, HIGH); 

 // turn the plunger motor on
  analogWrite(plungerMotorSpeedPin, PLUNGER_MOTOR_SPEED);
  
  // change state to plunge
  plungerState = plunge;
}

//Blade
void checkOnOffSwitch(){

    //get ON/OFF switch value
    int switchValue = digitalRead(onOffSwitchPin);

    //IF(on/off button pushed and more than 300ms since last button push)
    if(digitalRead(onOffSwitchPin) == HIGH && millis() - onOffPrev > ON_OFF_INTERVAL){

        //SET this event as most recent button push
        onOffPrev = millis();
     
        //IF(things aren't moving)
        if(plungerState==rest && !bladeIsOn){
            digitalWrite(OffIndicatorLEDPin, LOW);
            digitalWrite(OnIndicatorLEDPin, HIGH);
            turnOnBladeMotor();
            startPlunger();
        }else{
            digitalWrite(OnIndicatorLEDPin, LOW);
            digitalWrite(OffIndicatorLEDPin, HIGH);
            turnOffBladeMotor();
            retractPlunger();
        }
    }
}

void turnOnBladeMotor(){

  bladeIsOn = true;

  // set direction 
  digitalWrite(bladeMotorDirectionPin_1, HIGH);
  digitalWrite(bladeMotorDirectionPin_2, LOW);

  // start the balde 
  analogWrite(bladeMotorSpeedPin, BLADE_MOTOR_SPEED);
  delay(1500);
  analogWrite(bladeMotorSpeedPin, BLADE_MOTOR_SPEED+100);
  

  Serial.println("blade on");
}

void turnOffBladeMotor(){
  
  bladeIsOn = false;

  // eliminate direction 
  digitalWrite(bladeMotorDirectionPin_1, LOW);
  digitalWrite(bladeMotorDirectionPin_2, LOW);

  // turn off motor 
  analogWrite(bladeMotorSpeedPin, 0);
  
  Serial.println("blade off");
}


//Spin Tray
void checkSpinTray(){

  // get the value of the spin to next container button pin
  int buttonValue = digitalRead(nextContainerButtonPin);

  // determine if it has been pressed -or- 
  // if the tray needs to do its small step routine
  if(buttonValue==HIGH || trayIsSpinning){

    // spin the tray
    spinTrayToNextContainer();
  }

  // do nothing... 
}

void spinTrayToNextContainer(){

    Serial.println("spinTrayToNextContainer");

    // call the step function
    // https://lastminuteengineers.com/stepper-motor-l293d-arduino-tutorial/
    // TRAY_MOTOR_SPEED is how far it will turn 
  
  
    //IF(tray is not currently moving)
    if (!trayIsSpinning){
  
      //spin bigger amount
      step(INITIAL_SPIN_AMT);

      //record time step happened
      spinPrev = millis();   
       
      //prevent the large spin from happening again 
      trayIsSpinning = true;
      
    }//END IF
  
    // get the value of the hall effect sensor
    int alignmentHallEffectSensorValue = !digitalRead(alignmentHallEffectSensorPin);
  
    // IF(the magnet is not within 1/2 inch)
    if(alignmentHallEffectSensorValue == LOW){
  
      //IF(interval has passed since last step
      if(millis() - spinPrev > SPIN_TRAY_HALL_EFFECT_SENSOR_DETECTION_INTERVAL){

        Serial.println("step increment");
  
        // spin a little bit
        step(INCREMENT_SPIN_AMT);
        spinPrev = millis();
        
      }//END IF 

    //ELSE(you're in proper position for a container)
    }else{
        Serial.println("proper position");
  
        // reset the spin tray bool so now next time 
        // it will spin the large amount
        trayIsSpinning = false;
        containerIsFull = false;
        disableOnOff = false;

        //swap position of full indicators
        swapPositions();

        initialFullCheck();

    }//END IF
}

void step(int amt){
    analogWrite(trayMotorControllerSpeedPin, 255);
    rotationTrayStepperMotor.step(amt);
    analogWrite(trayMotorControllerSpeedPin, 0);
}

void swapPositions(){
  int temp;
  temp = full[0];
  full[0] = full[2];
  full[2] = full[1];
  full[1] = temp;

  digitalWrite(container1LEDPin, full[0]);
  digitalWrite(container2LEDPin, full[1]);
  digitalWrite(container3LEDPin, full[2]);
}

void setFull(){
  full[0] = 1;
  digitalWrite(container1LEDPin, full[0]);
}
