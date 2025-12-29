package org.firstinspires.ftc.teamcode.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class ArtifactOrder {
    public final Artifact[] MOTIF;
    public Integer[] indexer;   //MOTIF, Indexer
    public Artifact[] storedColors;

    public ArtifactOrder(Artifact[] motif) {
        MOTIF = new Artifact[3];
        indexer = new Integer[3];
        storedColors = new Artifact[3];

        int index = 0;
        for (Artifact pattern : motif) {
            MOTIF[index] = pattern;
        }

        Arrays.fill(indexer, 0);
    }
    public void addPair(int motifIndex, int artifactIndex, Artifact color) {
        indexer[motifIndex] = artifactIndex;
        storedColors[artifactIndex] = color;
    }

    public void fireDefaults() {
        for (int i = 0; i < 3; i++) {
            indexer[i] = i;
        }
    }

    public Integer[] get() {
        return indexer;
    }

    public Artifact[] getMotif() {
        return MOTIF;
    }

    public void search() {
        int motifIndex = 0;

        this.fireDefaults();

        for (Artifact target : MOTIF) { //For every color in the motif:
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