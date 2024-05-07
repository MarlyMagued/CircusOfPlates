package strategy;

public class Easy implements DiffStrat {

    @Override
    public int speedOfFalling() {
        return 2;
    }

    @Override
    public int speedOfControl() {
        return 60;
        //sor3et el clown
    }

    @Override
    public int numberOfBombs() {
        return 3;
    }
}
