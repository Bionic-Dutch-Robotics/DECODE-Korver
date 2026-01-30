package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.geometry.Pose;

public class Settings {

    /**
     * HardwareMap names for all connected devices.
     */
    public static class HardwareNames {
        public static class Transfer {
            public static final String TRANSFER = "push";
        }
        public static class Shooter {
            public static final String SHOOTER = "shooter";
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
            public static final double RELOAD_POS = 1;
            public static final double FEED_SHOOTER = 0.875;
        }
        public static class Shooter {
            //  Velocities are in degrees per second.
            public static final double FAR_VELOCITY = 330;
            public static final double MIDFIELD_VELOCITY = 300;

            public static final PIDFCoefficients shooterCoefficients = new PIDFCoefficients (
                    0.1,
                    0,
                    0,
                    0
            );
        }

        public static class Drivetrain {
            public static class Red {
                public static final Pose FAR_AUTO_START = new Pose(80, 8, Math.toRadians(90));
                public static final Pose CLOSE_AUTO_START = new Pose(124, 124, Math.toRadians(45));

                public static final Pose FAR_SHOOT = new Pose(80, 15, Math.toRadians(230));
                public static final Pose MIDFIELD_SHOOT = new Pose(70, 72, Math.toRadians(135));
            }
            public static class Blue {
                public static final Pose FAR_AUTO_START = new Pose(64, 8, Math.toRadians(90));
                public static final Pose CLOSE_AUTO_START = new Pose(24, 124, Math.toRadians(-45));
                public static final Pose FAR_SHOOT = new Pose(64, 15, Math.toRadians(-230));
            }

            public static Pose      TELEOP_START = new Pose();
        }

        public static class Intake {
            public static final double INTAKE_SPEED = 1;
            public static final double EJECT_SPEED = -1;
        }
    }
}
