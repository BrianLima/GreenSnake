
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;

public final class SnakeCanvas extends Canvas implements Runnable, KeyListener {
    
    private Graphics globalGraphics;
    private Thread gameThread;
    
    private final Snake snake;
    private Fruit fruit;
    
    public SnakeCanvas() {
        this.snake = new Snake().create();
        this.createFruit();
    }
    
    public void createFruit() {
        Random random = new Random();
        
        Point randomPoint;
        int randomX;
        int randomY;

        do {
            randomX = random.nextInt(Configuration.GRID_WIDTH);
            randomY = random.nextInt(Configuration.GRID_HEIGHT);

            randomPoint = new Point(randomX, randomY);
        } while (snake.contains(randomPoint));

        this.fruit = new Fruit(randomPoint);
    }

    public void moveSnake() {
        Point head = this.snake.move();

        if (head != null) {
            if (head.equals(this.fruit)) {
                this.snake.hitFruit(this.fruit);
                this.snake.grow();
                createFruit();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            this.moveSnake();
            this.draw(this.globalGraphics);

            try {
                Thread.currentThread();
                Thread.sleep(this.snake.getSpeed());
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        this.globalGraphics = g.create();
        this.addKeyListener(this);

        if (this.gameThread == null) {
            this.gameThread = new Thread(this);
            this.gameThread.start();
        }
    }

    public void draw(Graphics g) {
        g.clearRect(0, 0, Configuration.BOX_WIDTH * Configuration.GRID_WIDTH + 10, Configuration.BOX_HEIGHT * Configuration.GRID_HEIGHT + 20);

        BufferedImage buffer = new BufferedImage(Configuration.BOX_WIDTH * Configuration.GRID_WIDTH + 10, Configuration.BOX_HEIGHT * Configuration.GRID_HEIGHT + 20, BufferedImage.TYPE_INT_ARGB);

        Graphics bufferGraphics = buffer.getGraphics();

        drawGrid(bufferGraphics);
        drawScore(bufferGraphics);
        drawSnake(bufferGraphics);
        drawFruit(bufferGraphics);

        g.drawImage(buffer, 0, 0, Configuration.BOX_WIDTH * Configuration.GRID_WIDTH + 10, Configuration.BOX_HEIGHT * Configuration.GRID_HEIGHT + 20, this);
    }

    public void drawGrid(Graphics g) {
        // Cor das linhas
        g.setColor(Color.LIGHT_GRAY);

        // Moldura
        g.drawRect(0, 0, Configuration.GRID_WIDTH * Configuration.BOX_WIDTH, Configuration.GRID_HEIGHT * Configuration.BOX_HEIGHT);

        // Linhas verticais
        for (int x = Configuration.BOX_WIDTH; x < Configuration.GRID_WIDTH * Configuration.BOX_WIDTH; x += Configuration.BOX_WIDTH)
            g.drawLine(x, 0, x, Configuration.BOX_HEIGHT * Configuration.GRID_HEIGHT);

        // Linhas horizontais
        for (int y = Configuration.BOX_HEIGHT; y < Configuration.GRID_HEIGHT * Configuration.BOX_HEIGHT; y += Configuration.BOX_HEIGHT)
            g.drawLine(0, y, Configuration.GRID_WIDTH * Configuration.BOX_WIDTH, y);

        g.setColor(Color.BLACK);
    }
    
    public void drawScore(Graphics g) {
        g.drawString("Pontuação: " + this.snake.getScore(), 0, Configuration.BOX_HEIGHT * Configuration.GRID_HEIGHT + 10);
    }

    public void drawSnake(Graphics g) {
        g.setColor(Color.GREEN);

        for (Point p : this.snake)
            g.fillRect(p.x * Configuration.BOX_WIDTH, p.y * Configuration.BOX_HEIGHT, Configuration.BOX_WIDTH, Configuration.BOX_HEIGHT);

        g.setColor(Color.BLACK);
    }

    public void drawFruit(Graphics g) {
        switch (this.fruit.getFruitType()) {
            case Red:
                g.setColor(Color.RED);
                break;
            case Blue:
                g.setColor(Color.BLUE);
                break;
            case Black:
                g.setColor(Color.BLACK);
                break;
            default:
                g.setColor(Color.YELLOW);
        }

        g.fillOval(this.fruit.x * Configuration.BOX_WIDTH, this.fruit.y * Configuration.BOX_HEIGHT, Configuration.BOX_WIDTH, Configuration.BOX_HEIGHT);
        g.setColor(Color.BLACK);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (this.snake.getDirection() != Direction.South)
                    this.snake.setDirection(Direction.North);
                break;
            case KeyEvent.VK_DOWN:
                if (this.snake.getDirection() != Direction.North)
                    this.snake.setDirection(Direction.South);
                break;
            case KeyEvent.VK_RIGHT:
                if (this.snake.getDirection() != Direction.West)
                    this.snake.setDirection(Direction.East);
                break;
            case KeyEvent.VK_LEFT:
                if (this.snake.getDirection() != Direction.East)
                    this.snake.setDirection(Direction.West);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }
}
