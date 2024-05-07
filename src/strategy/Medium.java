package strategy;

public class Medium implements DiffStrat {

    @Override
    public int speedOfFalling(){
        return 3;
    }
    @Override
    public int speedOfControl(){
        return 40;
        //sor3et el clown
    }

    @Override
    public int numberOfBombs() {
         return 4;
    }
    
}
