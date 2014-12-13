
import java.awt.Point;
import java.util.Random;

public class Fruit extends Point {
    
    private FruitType fruitType;
    private int associatedScore;
    private int associatedSpeed;

    public Fruit() {
        this.randomFruitType();
    }

    public Fruit(Point point) {
        super(point);
        this.randomFruitType();
    }

    public Fruit(int x, int y) {
        super(x, y);
        this.randomFruitType();
    }

    public FruitType getFruitType() {
        return fruitType;
    }

    public void setFruitType(FruitType fruitType) {
        this.fruitType = fruitType;
        
        switch (fruitType) {
            case Red:
                associatedScore = 10;
                associatedSpeed = -10;
                break;
            case Blue:
                associatedScore = 10;
                associatedSpeed = 20;
                break;
            case Black:
                associatedScore = 100;
                associatedSpeed = -20;
                break;
            default:
                associatedScore = 0;
                associatedSpeed = 0;
                break;
        }
    }

    public int getAssociatedScore() {
        return associatedScore;
    }

    public int getAssociatedSpeed() {
        return associatedSpeed;
    }
    
    private void randomFruitType() {
        Random random = new Random();
        
        int randomFruit;
        randomFruit = random.nextInt(100);
                
        if (randomFruit <= 80)
            this.setFruitType(FruitType.Red);
        else if (randomFruit <= 95)
            this.setFruitType(FruitType.Blue);
        else
            this.setFruitType(FruitType.Black);
    }

}
