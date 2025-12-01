package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.robotcore.external.Const;

public class AllianceColor {
    public enum Selection {
        RED,
        BLUE
    }

    Selection allianceColor;
    public AllianceColor(Selection allianceColor) {
        this.allianceColor = allianceColor;
    }

    public boolean isRed() {
        return allianceColor == Selection.RED;
    }

    public Selection getSelection() {
        return allianceColor;
    }
}
