package runner;

import java.io.IOException;

import control.ServerControl;

public class Run {
    public static void main(String[] args) {
        try {
            new ServerControl();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
