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
            public static final String TILT_SERVO = "tilt";
        }
        public static class  Turret {
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
            public static final double[] upPos = {0.2, 0.6, 0.6};
            public static final double[] downPos = {0.62, 1, 0.15};
            public static final double RUN_TO_POS_TIME = 1.5;
        }
        public static class Shooter {
            //  Velocities are in degrees per second.
            public static final double FAR_VELOCITY = 175;
            public static final double MIDFIELD_VELOCITY = 125;

            public static final PIDFCoefficients SHOOTER_COEFFICIENTS = new PIDFCoefficients (
                    0.00527, 0,0.000013,0
            );
        }

        public static class Drivetrain {
            public static final Pose BLUE_AUTO_START = new Pose(56.875, 8.5, Math.PI);
            public static final Pose BLUE_MIDFIELD_SHOOT = new Pose(68,72, Math.toRadians(-135));
            public static final Pose BLUE_FAR_SHOOT = new Pose(64, 30, -2.7);
        }

        public static class Intake {
            public static final double INTAKE_SPEED = 0.75;
            public static final double EJECT_SPEED = -0.75;
        }
    }
}
