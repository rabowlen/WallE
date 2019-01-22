/* Copyright (c) 2018 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

/**
 * This 2018-2019 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the gold and silver minerals.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "DepotAuto", group = "Concept")

public class DepotAuto extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =   "AQsvu/X/////AAADmZ2+bLYDyEwvo4napGAaVU2GVRdiApNTAWxeYlOxYQ2s1/zuMOm0x5QjHI5KoJtrMugyZFnpdue8xQvjC5pEMxmra/P1r+uDsf8G990OEvI3vkv9KNZt2FbPdIZFcruplfyiLGZM61hzLTXhBQ5EiSQI00ZdQw52l4EJhlJ3tLvmp9QYxhYHjKu2lfiqBDcTKzdOIFfEmCR2TkLqvLKs+g4/xi2G+cRXx9GlQT3bKkkR3QGbLeSNJn3dJ2QuMHwCJZFAomEh3hyiaxp4umc8zDTUpzNUQBWIVqH6jL/4T9+MgWz9FQzTMinE/2b+twF/e6ujZvRrU6GssxLN2nl1+wF+h+FcfxJqc7sNDJL85j/3";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        
        //Declare the robot methods
        AutoMethods auto = new AutoMethods();
  
        //Robot is initialized
        waitForStart();
        
        //Create timer Object
        ElapsedTime timer = new ElapsedTime();

        if (opModeIsActive()) {
            /** Activate Tensor Flow Object Detection. */
            if (tfod != null) {
                tfod.activate();
            }
            
            //Initialize hardware
            auto.motorLeft  = hardwareMap.get(DcMotor.class, "motorLeft");
            auto.motorRight = hardwareMap.get(DcMotor.class, "motorRight");
            auto.motorArm  = hardwareMap.get(DcMotor.class, "motorArm");
            auto.servoMarker = hardwareMap.get(Servo.class, "servoMarker");

            while (opModeIsActive()) {
              
              boolean left = false;
              boolean center = false;
              boolean right = false;
              
                
               
                
              while(timer.time() < 6.55) {
                auto.armUp();
                auto.walleUp();
              }
              
              while(timer.time() > 6.55 && (timer.time() < 7)) {
                auto.armStop();
              }
              
              while((timer.time() > 7.5) && (timer.time() < 8.25)) {
                auto.left();
              }
              
              while((timer.time() > 8.4)&&(timer.time() < 8.5)) {
                auto.brake();
              }
              
              while((timer.time() > 9)&&(timer.time() < 11)) {
                auto.armDown();
              }
              
              while((timer.time() > 11.5) &&(timer.time() < 12)) {
                auto.armStop();
              }
              
              while((timer.time() > 12.5)&&(timer.time() < 13)) {
                auto.right();
              }
              
              while((timer.time() > 13.25)&&(timer.time() < 13.5)) {
                auto.brake();
              }
              
              while((timer.time() > 14)&&(timer.time() < 14.10)) {
                auto.forward();
              }
              
              while((timer.time() > 14.95)&&(timer.time() < 15.10)) {
                auto.brake();
              }
              
              while((timer.time() > 15.25)&&(timer.time() < 17)) {
                
              if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                      telemetry.addData("# Object Detected", updatedRecognitions.size());
                      if (updatedRecognitions.size() == 1) {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;
                        for (Recognition recognition : updatedRecognitions) {
                          if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                          auto.forward();
                          if (!recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            while((timer.time()>15.5) && (timer.time()<15.8)){
                              auto.right();
                              
                            }
                          }
                      }
                      telemetry.update();
                    }
                }  
              }
            /*  
              
              while((timer.time() > 14)&&(timer.time() < 19)) {
                auto.forward();
              }
              while((timer.time() > 19.2)&&(timer.time() < 19.35)) {
                auto.brake();
              }
              
              while((timer.time() > 19.55)&&(timer.time() < 19.85)) {
                auto.walleDown();
              }
              
              while((timer.time() > 20)&&(timer.time() < 20.25)) {
                auto.walleUp();
              }
              while ((timer.time() > 20.5) && (timer.time() < 21)) {
                auto.reverse();
              }
              
              while ((timer.time() > 22.50) && (timer.time() < 23.25)) {
                auto.brake();
              }
              
              while ((timer.time() > 23.5)&& (timer.time()<25.5)) {
                auto.left();
              }
              while ((timer.time() > 26) && (timer.time()<27.3)) {
                auto.superForward();
              }
              while ((timer.time() > 28.75) && (timer.time()<30)) {
                auto.brake();
              }
             */ 
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
            }
        }
    }
    

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.FRONT;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
        
        //set the minimum confidence to a higher percentage
        tfodParameters.minimumConfidence = 1;
    }
}
