package org.firstinspires.ftc.teamcode.util;

import java.util.Arrays;

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

    public Integer[] get() {
        return indexer;
    }

    public Artifact[] getMotif() {
        return motif;
    }

    public void search() {
        /*int motifIndex = 0;

        this.storeDefaultOrder();

        if (!(findStoredGreen() == findGreenMotif())) {
            replaceIndex = findStoredGreen();
            indexer[replaceIndex] = indexer[findGreenMotif()];
            indexer[findGreenMotif()] = findStoredGreen();
        }*/

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

        /*
        for (Artifact target : motif) { //For every color in the motif:
                for (int i = 0; i < 3; i++) {   // For all currently stored Artifacts:
                    if (storedColors[i] == Artifact.GREEN) {    //If they are green:
                        replaceIndex = Arrays.binarySearch(indexer, i);    // Finds index needed of the green artifact
                        indexer[replaceIndex] = indexer[motifIndex];           //Replace the used artifact with the one the following line replaces

                        indexer[motifIndex] = i;   // Put current artifact in motif spot
                    }
            }
            motifIndex += 1;
        }
         */
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
}

/*
Line 41
42: PURPLE
43: i=0
44: true
45: i (0)
46: indexer[0] = indexer[0] NOO!
48: indexer[0] = 0
 */