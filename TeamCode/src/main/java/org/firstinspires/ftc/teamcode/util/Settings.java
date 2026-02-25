package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.geometry.Pose;

public class Settings {

    /**
     * HardwareMap names for all connected devices.
     */
    public static class HardwareNames {
        public static class Transfer {
            public static final String[] KICKERS = {"kicker1", "kicker2", "kicker3"};
        }
        public static class Sorter {
            public static final String[] COLOR_SENSORS = {"color1", "color2", "color3"};
        }
        public static class Shooter {
            public static final String SHOOTER = "shooter";
            public static final String SHOOTER_TWO = "shooter2";
            public static final String TILT_SERVO = "tilt";
            public static final String TURRET = "turret";
        }

        public static class Drivetrain {
            public static final String FRONT_LEFT_DRIVE = "frontLeft";
            public static final String BACK_LEFT_DRIVE = "backLeft";
            public static final String FRONT_RIGHT_DRIVE = "frontRight";
            public static final String BACK_RIGHT_DRIVE = "backRight";
            public static final String PINPOINT_NAME = "pinpoint";
        }

        public static class Intake {
            public static final String INTAKE = "intake";
        }
    }

    /**
     * Target positions and velocities for all subsystems.
     */
    public static class Positions {
        public static class Transfer {
            public static final double[] upPos = {0.19, 0.53, 0.59};
            public static final double[] downPos = {0.63, 0.95, 0.1051};
            public static final double RUN_TO_POS_TIME = 0.18;
        }
        public static class Shooter {
            //  Velocities are in degrees per second.
            public static final double FAR_VELOCITY = 175;
            public static final double MIDFIELD_VELOCITY = 125;

            public static final PIDFCoefficients SHOOTER_COEFFICIENTS = new PIDFCoefficients (
                    0.073230, 0,0.0000045,0.00
            );  //TODO: Tune the kF value
        }

        public static class Drivetrain {
            public static class Blue {
                public static final Pose FAR_AUTO_START = new Pose(57, 8.5, Math.PI);
                public static final Pose FAR_SHOOT = new Pose(55, 15, Math.PI);
                public static final Pose CLOSE_SHOOT = new Pose(72, 72, Math.PI);
                public static final Pose CLOSE_AUTO_START = new Pose(30.9436, 123.8405, -2.27); //Needs to be updated
                public static final Pose PARK = new Pose(100,30, Math.PI);
            }
            public static class Red {

                public static final Pose FAR_AUTO_START = new Pose(87, 8.5, Math.toRadians(180)); //Needs to be updated
                public static final Pose CLOSE_AUTO_START = new Pose(121.6233, 131.271, 2.2);   //Needs to be updated
                public static final Pose FAR_SHOOT = new Pose(79, 15, Math.PI);
                public static final Pose CLOSE_SHOOT = new Pose(72, 72, Math.PI);
                public static final Pose PARK = new Pose(35,30, Math.PI);
            }
        }

        public static class Intake {
            public static final double INTAKE_SPEED = 1.0;
            public static final double EJECT_SPEED = -0.65;
        }
    }
}
