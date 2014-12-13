import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Snake extends LinkedList<Point> {

    private int speed;
    private int score;
    private Direction direction;

    public Snake() {
    }

    public Snake(Collection<? extends Point> collection) {
        super(collection);
    }

    public int getSpeed() {
        return speed;
    }

    private void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getScore() {
        return score;
    }

    private void setScore(int score) {
        this.score = score;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void addSpeed(int speed) {
        this.speed += speed;

        if (this.speed < 80) {
            this.speed = 80;
        }
    }

    public void addScore(int score) {
        this.score += score;
    }

    public Snake create() {
        this.setScore(0);
        this.setSpeed(200);
        this.setDirection(Direction.NoDirection);
        this.clear();

        for (int y = 2; y >= 0; y--) {
            this.add(new Point(0, y));
        }

        return this;
    }
    
    public Snake recreate() {
        try {
            this.sendData();
        } catch (Throwable t) {
        }
        
        return this.create();
    }

    public Point move() {
        Point newHead = this.createHead();
        
        if (newHead != null) {
            if (this.contains(newHead)) {
                this.recreate();
            } else if (newHead.x < 0 || newHead.x > (Configuration.GRID_WIDTH - 1)) {
                this.recreate();
            } else if (newHead.y < 0 || newHead.y > (Configuration.GRID_HEIGHT - 1)) {
                this.recreate();
            } else {
                this.removeLast();
                this.push(newHead);
            }
        }

        return newHead;
    }

    public void grow() {
        Point tail = this.createTail();
        this.add(tail);
    }

    public void hitFruit(Fruit fruit) {
        this.addScore(fruit.getAssociatedScore());
        this.addSpeed(fruit.getAssociatedSpeed());
    }

    private Point createHead() {
        Point oldHead;
        Point newHead;

        oldHead = this.peekFirst();

        switch (this.getDirection()) {
            case North:
                newHead = new Point(oldHead.x, oldHead.y - 1);
                break;
            case South:
                newHead = new Point(oldHead.x, oldHead.y + 1);
                break;
            case West:
                newHead = new Point(oldHead.x - 1, oldHead.y);
                break;
            case East:
                newHead = new Point(oldHead.x + 1, oldHead.y);
                break;
            default:
                return null;
        }

        return new Point(newHead);
    }

    private Point createTail() {
        Point oldTail;
        Point newTail;

        oldTail = this.peekLast();

        switch (this.getDirection()) {
            case North:
                newTail = new Point(oldTail.x, oldTail.y + 1);
                break;
            case South:
                newTail = new Point(oldTail.x, oldTail.y - 1);
                break;
            case West:
                newTail = new Point(oldTail.x + 1, oldTail.y);
                break;
            case East:
                newTail = new Point(oldTail.x - 1, oldTail.y);
                break;
            default:
                return new Point(oldTail);
        }

        return new Point(newTail);
    }

    public void sendData() throws IOException {
        JFrame frame = new JFrame("GreenSnake");
        String player = JOptionPane.showInputDialog(frame, "Digite o seu nome", "Salvando sua pontuação", JOptionPane.INFORMATION_MESSAGE);
        
        //Colocar o ip do computador rodando o servidor logo abaixo
        Socket connection = new Socket("127.0.0.1", 2048);

        //Conectar e escrever|receber dados
        DataOutputStream sender = new DataOutputStream(connection.getOutputStream());
        DataInputStream receiver = new DataInputStream(connection.getInputStream());

        sender.writeInt(this.score);
        sender.writeUTF(player);
        
        JOptionPane.showConfirmDialog(frame, "Você é o " + receiver.readInt() + "º colocado entre os jogadores", "Parabéns", JOptionPane.INFORMATION_MESSAGE);
    }
}
