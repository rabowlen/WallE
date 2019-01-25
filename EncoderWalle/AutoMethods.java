/*
Copyright 2017 FIRST Tech Challenge Team 12978

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Remove a @Disabled the on the next line or two (if present) to add this opmode to the Driver Station OpMode list,
 * or add a @Disabled annotation to prevent this OpMode from being added to the Driver Station
 */

public class AutoMethods {
    //getting key
    private static final String VUFORIA_KEY = "AQsvu/X/////AAADmZ2+bLYDyEwvo4napGAaVU2GVRdiApNTAWxeYlOxYQ2s1/zuMOm0x5QjHI5KoJtrMugyZFnpdue8xQvjC5pEMxmra/P1r+uDsf8G990OEvI3vkv9KNZt2FbPdIZFcruplfyiLGZM61hzLTXhBQ5EiSQI00ZdQw52l4EJhlJ3tLvmp9QYxhYHjKu2lfiqBDcTKzdOIFfEmCR2TkLqvLKs+g4/xi2G+cRXx9GlQT3bKkkR3QGbLeSNJn3dJ2QuMHwCJZFAomEh3hyiaxp4umc8zDTUpzNUQBWIVqH6jL/4T9+MgWz9FQzTMinE/2b+twF/e6ujZvRrU6GssxLN2nl1+wF+h+FcfxJqc7sNDJL85j/3";
    //creating future variables for vuforia
    private TFObjectDetector tfod;
    private VuforiaLocalizer vuforia;
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    public DcMotor motorLeft;
    public DcMotor motorRight;
    public DcMotor motorArm;
    public Servo servoMarker;
    
    //create the hardware map for tfod
    walle.motorLeft  = hardwareMap.get(DcMotor.class, "motorLeft");
    walle.motorRight = hardwareMap.get(DcMotor.class, "motorRight");
    walle.motorArm  = hardwareMap.get(DcMotor.class, "motorArm");
    walle.servoMarker = hardwareMap.get(Servo.class, "servoMarker");
    
    
    public void walleDown(){
        servoMarker.setPosition(.6);
    }
    public void walleUp(){
        servoMarker.setPosition(.1);
    }
        
    public void brake() {
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }
    
    public void forward(double revs) {
        motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int revolution = 1440;
          
        motorLeft.setTargetPosition((int)(revolution * revs));    
        motorRight.setTargetPosition((int)(-revolution * revs));
        motorLeft.setPower(1);
        motorRight.setPower(1);
        motorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        while(motorLeft.isBusy() && motorRight.isBusy()) {}   
        
        motorLeft.setPower(0);
        motorRight.setPower(0);
        
    }
    
    public void reverse(double revs) {
        motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int revolution = 1440;
          
        motorLeft.setTargetPosition((int)(-revolution * revs));    
        motorRight.setTargetPosition((int)(revolution * revs));
        motorLeft.setPower(1);
        motorRight.setPower(1);
        motorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        while(motorLeft.isBusy() && motorRight.isBusy()) {}   
        
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }
    
    public void rightQuarterTurn(double revs) {
        motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int revolution = 1440;
          
        motorLeft.setTargetPosition((int)(-revolution * revs));    
        motorRight.setTargetPosition((int)(-revolution * revs));
        motorLeft.setPower(1);
        motorRight.setPower(1);
        motorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        while(motorLeft.isBusy() && motorRight.isBusy()) {}   
        
        motorLeft.setPower(0);
        motorRight.setPower(0);
        
    }
    
    public void leftQuarterTurn() {
        motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int revolution = 1440;
          
        motorLeft.setTargetPosition(revolution);    
        motorRight.setTargetPosition(revolution);
        motorLeft.setPower(1);
        motorRight.setPower(1);
        motorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        while(motorLeft.isBusy() && motorRight.isBusy()) {}   
        
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }
    
    public void armUp(double revs) {
        motorArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int revolution = 1440;
            
        motorArm.setTargetPosition((int)(-revolution * revs));
        motorArm.setPower(1);
        motorArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        while(motorArm.isBusy()) {}   
        
        motorArm.setPower(0);
        
    }
    
    public void armDown(double revs) {
        motorArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int revolution = 1440;
            
        motorArm.setTargetPosition((int)(revolution * revs));
        motorArm.setPower(1);
        motorArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        while(motorArm.isBusy()) {}   
        
        motorArm.setPower(0);
        
    }
    
    public void checkMineral() {
        
         initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        }
        tfod.activate();
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        
        for (Recognition recognition : updatedRecognitions){
        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)){
        motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int revolution = 1440;
          
        motorLeft.setTargetPosition((int)(revolution * revs));    
        motorRight.setTargetPosition((int)(-revolution * revs));
        motorLeft.setPower(1);
        motorRight.setPower(1);
        motorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        while(motorLeft.isBusy() && motorRight.isBusy()) {}   
        
        motorLeft.setPower(0);
        motorRight.setPower(0);
        }
    }
    }
        private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }
        private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
    
}
