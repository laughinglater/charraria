public interface AI {
    Direction Walker();

}

class SlimeAI implements AI{
    @Override
    public Direction Walker() {
        return Direction.randomDir();
    }
}

