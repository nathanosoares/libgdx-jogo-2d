package dev.game.test.api.util;

public enum EnumDirection {

    NORTH(),
    NORTH_EAST(),
    EAST(),
    SOUTH_EAST(),
    SOUTH(),
    SOUTH_WEST(),
    WEST(),
    NORTH_WEST();

    public static EnumDirection fromDegrees(double degrees) {
        int index = (int) (Math.round(((Math.abs(degrees) % 180) / 45)) % 8);

        if (Math.signum(degrees) > 0) {
            return values()[index];
        } else {
            return values()[index > 0 ? 8 - index : index];
        }
    }
}
