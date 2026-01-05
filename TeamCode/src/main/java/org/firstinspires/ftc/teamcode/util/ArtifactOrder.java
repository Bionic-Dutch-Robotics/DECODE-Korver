package org.firstinspires.ftc.teamcode.util;

public class ArtifactOrder {
    public Artifact[] motif;
    public Integer[] indexer;   //motif, Indexer
    public Artifact[] storedColors;
    public int replaceIndex;

    public ArtifactOrder(Artifact[] motif) {
        this.motif = new Artifact[3];
        indexer = new Integer[3];
        storedColors = new Artifact[3];
        this.motif = motif;
        storeDefaultOrder();
    }
    public void addPair(int artifactIndex, Artifact color) {
        storedColors[artifactIndex] = color;
    }

    public void storeDefaultOrder() {
        for (int i = 0; i < 3; i++) {
            indexer[i] = i;
        }
    }

    public Integer[] getOrder() {
        return indexer;
    }

    public Artifact[] getMotif() {
        return motif;
    }

    public void search() {
        this.storeDefaultOrder(); // Reset to [0, 1, 2]

        int storedGreenAt = findStoredGreen();
        int motifGreenAt = findGreenMotif();

        if (storedGreenAt != -1 && motifGreenAt != -1 && storedGreenAt != motifGreenAt) {
            // We need the value at motifGreenAt to move to storedGreenAt
            // and the value currently at storedGreenAt to move to motifGreenAt
            int temp = indexer[motifGreenAt];
            indexer[motifGreenAt] = indexer[storedGreenAt];
            indexer[storedGreenAt] = temp;
        }
    }

    public int findStoredGreen() {
        for (int i = 0; i < 3; i++) {
            if (storedColors[i] == Artifact.GREEN) {
                return i;
            }
        }
        return -1;
    }
    public int findGreenMotif() {
        for (int i = 0; i < 3; i++) {
            if (motif[i] == Artifact.GREEN) {
                return i;
            }
        }
        return -1;
    }

    public void setMotif(Artifact[] motif) {
        this.motif = motif;
    }
}