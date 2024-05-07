package strategy;

public class Extreme implements DiffStrat {

    @Override
    public int speedOfFalling() {
        return 4;
    }

    @Override
    public int speedOfControl() {
        return 20;
        //sor3et el clown
    }

    @Override
    public int numberOfBombs() {
        return 6;
    }

}
