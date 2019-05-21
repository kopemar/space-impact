package cz.cvut.fel.util;

import java.util.Random;

public enum Images {

    BEAM("beam.png"),
    ENEMY{
        @Override
        public String getResource() {
            return Images.getRandomResource();
        }
    },
    BOSS("final1.png"),
    EMPTY_HT("ht_empty.png"),
    FULL_HT("ht_full.png"),
    PICK_HT("pick_ht.png"),
    PICK_SHIELD("pick_shield.png"),
    PLAYER("player.png"),
    PRO_PLAYER("player_protected.png"),
    STAR("star1.png");

    private static String getRandomResource() {
        int random = new Random().nextInt(3) + 1;
        if (random % 3 == 0) {
            return "enemy1.png";
        }
        else if (random % 3 == 1) {
            return "enemy2.png";
        }
        else return "enemy3.png";
    }

    private String label;

    Images(String label) {
        this.label = label;
    }
    Images() {}

    public String getResource() {
        return label;
    }
}
