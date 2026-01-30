package org.airrowe.game_player.image_processing.text_parsing;


import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class DetectedText {
    public Point center;
    public Scalar color;   // BGR
    public String text;

    public DetectedText(Point center, Scalar color, String text) {
        this.center = center;
        this.color = color;
        this.text = text;
    }
}
