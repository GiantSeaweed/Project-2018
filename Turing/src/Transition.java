


public class Transition {
    public enum Direction {LEFT, RIGHT, STAY};

    public String curState;
    public String curSymbol;
    public String nextState;
    public String nextSymbol;
    public Direction direction = Direction.RIGHT;
}
