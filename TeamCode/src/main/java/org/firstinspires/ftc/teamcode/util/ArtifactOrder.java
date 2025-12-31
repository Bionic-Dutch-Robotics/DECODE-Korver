package org.firstinspires.ftc.teamcode.util;

import java.util.Arrays;

public class ArtifactOrder {
    public Artifact[] motif;
    public Integer[] indexer;   //motif, Indexer
    public Artifact[] storedColors;

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
        int motifIndex = 0;

        this.storeDefaultOrder();

        for (Artifact target : motif) { //For every color in the motif:
            if (target == Artifact.GREEN) { // If the current target is green:
                for (int i = 0; i < 3; i++) {   // For all currently stored Artifacts:
                    if (storedColors[i] == Artifact.GREEN) {    //If they are green:
                         int replaceIndex = Arrays.binarySearch(indexer, i);    // Finds index needed to replace
                         indexer[replaceIndex] = indexer[motifIndex];           //Replace the used artifact with the one the following line replaces
                         indexer[motifIndex] = i;   // Put current artifact in motif spot
                    }
                }
            }
            motifIndex += 1;
        }
    }
}