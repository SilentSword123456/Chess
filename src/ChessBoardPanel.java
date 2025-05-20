import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ChessBoardPanel extends JPanel {
    private final Image[] tiles = new Image[64];

    public ChessBoardPanel(String[] pieces,DisplayManager displayManager) {
        updatePieces(pieces);

        setLayout(null);
        // 3) add one button per cell
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                final int r = row, c = col;
                JButton btn = new JButton();
                btn.setBounds(col * 80, row * 80, 80, 80);
                // make it invisible
                btn.setOpaque(false);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                // handle clicks
                btn.addActionListener(e -> {
                    System.out.println("Clicked cell: " + r + ", " + c);
                    displayManager.clickedSquare(r + 1,c + 1);
                });
                add(btn);
            }
        }
    }

    public void updatePieces(String[] pieces) {
        for (int i = 0; i < 64; i++) {
            String imgPath = getPieceImgPath(pieces[i], i);
            tiles[i] = new ImageIcon(Objects.requireNonNull(getClass().getResource(imgPath))).getImage();
        }
    }

    private String getPieceImgPath(String piece, int i){
        int row = i / 8;
        int col = i % 8;
        boolean darkSquare = (row + col) % 2 == 0;

        String imgPath;
        switch (piece) {
            case "WP": imgPath = "/img/"  + (darkSquare ? "B" : "W") + "WP.jpg"; break;
            case "WH": imgPath = "/img/WH.jpg"; break;
            case "WB": imgPath = "/img/WB.jpg"; break;
            case "WR": imgPath = "/img/WR.jpg"; break;
            case "WQ": imgPath = "/img/WQ.jpg"; break;
            case "WK": imgPath = "/img/WK.jpg"; break;
            case "BP": imgPath = "/img/"  + (darkSquare ? "B" : "W") + "BP.jpg"; break;
            case "BH": imgPath = "/img/WH.jpg"; break;
            case "BB": imgPath = "/img/WB.jpg"; break;
            case "BR": imgPath = "/img/WR.jpg"; break;
            case "BQ": imgPath = "/img/WQ.jpg"; break;
            case "BK": imgPath = "/img/WK.jpg"; break;
            default:   imgPath = "/img/W.jpg";
        }
        return imgPath;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640, 640);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw each of the 64 tiles
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int idx = row * 8 + col;
                g.drawImage(
                        tiles[idx],
                        col * 80,
                        row * 80,
                        80, 80,
                        this
                );
            }
        }
    }
}
