package com.fyp.helper;

import org.opencv.core.Mat;

import static org.opencv.core.Core.ROTATE_180;
import static org.opencv.core.Core.flip;
import static org.opencv.core.Core.rotate;

public class FrameFlipHelper {
    public static Mat FlipRotateFrameHorizental(Mat inputFrame){

        //Intermediate mat
        Mat rotatedFrame = new Mat();
        Mat flippedFrame = new Mat();

        //Rotate the frame by 180
        rotate(inputFrame, rotatedFrame, ROTATE_180);

        //Flip the frame
        flip(rotatedFrame, flippedFrame, 0);

        return flippedFrame;
    }
}
