package core.automat;

public class Automats {

    public static LeftHandedAutomat newLeftHanded() {
        return new LeftHandedAutomat();
    }

    public static RightHandedExtendedAutomat newRightHandedExtended() {
        return new RightHandedExtendedAutomat();
    }
}
