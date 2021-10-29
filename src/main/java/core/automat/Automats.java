package core.automat;

public class Automats {

    public static LeftLookingAutomat newLeftLooking() {
        return new LeftLookingAutomat();
    }

    public static ExtendedRightLookingAutomat newExtendedRightLooking() {
        return new ExtendedRightLookingAutomat();
    }
}
