package setgame;

import java.awt.Color;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * @author Noah Newdorf
 */
public class SetGame extends javax.swing.JFrame {

    private int setsMade = 0;
    private Stack<Card> deck = null;

    //colors have changed, names maynot represent colors
    private final Color nutrualDarkBlue = new Color(0, 0, 153);
    private final Color selectGreen = new Color(51, 204, 0);
    private final Color darkGreen = new Color(0, 153, 51);
    private final Color lightGreen = new Color(0, 180, 51);
    private final ArrayList<Integer> selected;
    private final JLabel lables[];
    private final JPanel cardSpotJPanels[];
    private final Card cardsOnBoard[] = new Card[12];
    private boolean canHint = true;
    private long gameStartTime; // when pressing the start button capture that time and make it the gameStartTime
    private long setStartTime; // after each set is made capture that time here and see if it is the fastest/slowest
    private long fastestSetTime;
    private long slowestSetTime;
    private int hintsUsed;
    private int badSets;

    public SetGame() {
        initComponents();

        deck = new Stack<>();
        selected = new ArrayList<>();
        this.cardSpotJPanels = new JPanel[]{cardSpotJPanel1, cardSpotJPanel2, cardSpotJPanel3, cardSpotJPanel4, cardSpotJPanel5, cardSpotJPanel6, cardSpotJPanel7, cardSpotJPanel8, cardSpotJPanel9, cardSpotJPanel10, cardSpotJPanel11, cardSpotJPanel12};
        this.lables = new JLabel[]{jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jLabel9, jLabel10, jLabel11, jLabel12};

        this.setVisible(true);
    }

    // checks the cards on the board to ensure player wont get stuck without playable set
    private boolean isSetOnBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = i + 1; j < 11; j++) {
                for (int k = j + 1; k < 12; k++) {
                    if (this.checkSet(cardsOnBoard[i], cardsOnBoard[j], cardsOnBoard[k])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // returns t/f for valid set with no output
    public static boolean checkSet(Card c1, Card c2, Card c3) {
        boolean colorOk = false, shapeOk = false, fillOk = false, numberOk = false;
        if ((c1.color.equals(c2.color) && c2.color.equals(c3.color) && c1.color.equals(c3.color))) {
            colorOk = true;
        } else if (!c1.color.equals(c2.color) && !c2.color.equals(c3.color) && !c1.color.equals(c3.color)) {
            colorOk = true;
        }
        if ((c1.fill.equals(c2.fill) && c2.fill.equals(c3.fill) && c1.fill.equals(c3.fill))) {
            fillOk = true;
        } else if (!c1.fill.equals(c2.fill) && !c2.fill.equals(c3.fill) && !c1.fill.equals(c3.fill)) {
            fillOk = true;
        }
        if ((c1.number.equals(c2.number) && c2.number.equals(c3.number) && c1.number.equals(c3.number))) {
            numberOk = true;
        } else if (!c1.number.equals(c2.number) && !c2.number.equals(c3.number) && !c1.number.equals(c3.number)) {
            numberOk = true;
        }
        if ((c1.type.equals(c2.type) && c2.type.equals(c3.type) && c1.type.equals(c3.type))) {
            shapeOk = true;
        } else if (!c1.type.equals(c2.type) && !c2.type.equals(c3.type) && !c1.type.equals(c3.type)) {
            shapeOk = true;
        }
        return colorOk && shapeOk && fillOk && numberOk;
    }

    // used internally to check validity of a set and output results to feedbackJPanel
    private boolean isSet(Card c1, Card c2, Card c3) {
        boolean colorOk = false, shapeOk = false, fillOk = false, numberOk = false;
        if ((c1.color.equals(c2.color) && c2.color.equals(c3.color) && c1.color.equals(c3.color))) {
            colorOk = true;
        } else if (!c1.color.equals(c2.color) && !c2.color.equals(c3.color) && !c1.color.equals(c3.color)) {
            colorOk = true;
        }
        if ((c1.fill.equals(c2.fill) && c2.fill.equals(c3.fill) && c1.fill.equals(c3.fill))) {
            fillOk = true;
        } else if (!c1.fill.equals(c2.fill) && !c2.fill.equals(c3.fill) && !c1.fill.equals(c3.fill)) {
            fillOk = true;
        }
        if ((c1.number.equals(c2.number) && c2.number.equals(c3.number) && c1.number.equals(c3.number))) {
            numberOk = true;
        } else if (!c1.number.equals(c2.number) && !c2.number.equals(c3.number) && !c1.number.equals(c3.number)) {
            numberOk = true;
        }
        if ((c1.type.equals(c2.type) && c2.type.equals(c3.type) && c1.type.equals(c3.type))) {
            shapeOk = true;
        } else if (!c1.type.equals(c2.type) && !c2.type.equals(c3.type) && !c1.type.equals(c3.type)) {
            shapeOk = true;
        }
        String text = "   Color\tFill\tNumber\tShape"
                + "\n1) " + c1.color + "\t" + c1.fill + "\t" + c1.number + "\t" + c1.type
                + "\n2) " + c2.color + "\t" + c2.fill + "\t" + c2.number + "\t" + c2.type
                + "\n3) " + c3.color + "\t" + c3.fill + "\t" + c3.number + "\t" + c3.type
                + "\n    " + (colorOk ? "good" : "bad") + "\t" + (fillOk ? "good" : "bad") + "\t" + (numberOk ? "good" : "bad") + "\t" + (shapeOk ? "good" : "bad")
                + (colorOk && fillOk && numberOk && shapeOk ? "\n\t- - - Good Set - - -" : "\n\t- - - Bad Set - - -");
        feedbackJTextArea.setText(text);
        return colorOk && shapeOk && fillOk && numberOk;
    }

    // makes a deck of one of each card
    private void initDeck() {
        hintsUsed = 0;
        badSets = 0;
        ArrayList<Card> deck = new ArrayList<>(81);
        ShapeColor colors[] = {ShapeColor.GREEN, ShapeColor.PURPLE, ShapeColor.RED};
        ShapeFill fills[] = {ShapeFill.CLEAR, ShapeFill.SOLID, ShapeFill.STRIPED};
        ShapeNumber numbers[] = {ShapeNumber.ONE, ShapeNumber.TWO, ShapeNumber.THREE};
        ShapeType types[] = {ShapeType.DIAMOND, ShapeType.OVAL, ShapeType.WAVE};
        for (ShapeColor color : colors) {
            for (ShapeFill fill : fills) {
                for (ShapeNumber number : numbers) {
                    for (ShapeType type : types) {
                        deck.add(new Card(color, fill, number, type));
                    }
                }
            }
        }
        Collections.shuffle(deck);
        deck.stream().forEach((c) -> {
            this.deck.push(c);
        });
    }

    // gets the corret html string for making a card face
    private String pictureHtml(Card card) {
        String html = "<html>";
        for (int i = 0; i < card.number.ordinal() + 1; i++) {
            html += "<img src=\"" + SetGame.class.getResource(card.resourceName) + "\" alt=\"" + SetGame.class.getResource(card.resourceName) + "\"height=\"50\" width=\"90\"/><br>";
        }
        html += "</html>";
        return html;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backgroundJPanel = new javax.swing.JPanel();
        cardSpotJPanel = new javax.swing.JPanel();
        cardSpotJPanel1 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cardSpotJPanel2 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cardSpotJPanel3 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cardSpotJPanel4 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cardSpotJPanel5 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cardSpotJPanel6 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cardSpotJPanel7 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cardSpotJPanel8 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cardSpotJPanel9 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cardSpotJPanel10 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cardSpotJPanel11 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        cardSpotJPanel12 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        startJButton = new javax.swing.JButton();
        makeSetJButton = new javax.swing.JButton();
        setsMadeJLabel = new javax.swing.JLabel();
        cardsInDeckJLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        feedbackJTextArea = new javax.swing.JTextArea();
        deselectJButton = new javax.swing.JButton();
        readRulesJButton = new javax.swing.JButton();
        hintJButton = new javax.swing.JButton();
        enableHintJCheckBox = new javax.swing.JCheckBox();
        quickestSetJLabel = new javax.swing.JLabel();
        slowestSetJLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        backgroundJPanel.setBackground(new java.awt.Color(190, 190, 190));

        cardSpotJPanel.setBackground(new java.awt.Color(215, 215, 225));

        cardSpotJPanel1.setBackground(new java.awt.Color(0, 0, 153));
        cardSpotJPanel1.setPreferredSize(new java.awt.Dimension(125, 225));
        cardSpotJPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardSpotJPanel1MouseClicked(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(250, 250, 250));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout cardSpotJPanel1Layout = new javax.swing.GroupLayout(cardSpotJPanel1);
        cardSpotJPanel1.setLayout(cardSpotJPanel1Layout);
        cardSpotJPanel1Layout.setHorizontalGroup(
            cardSpotJPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        cardSpotJPanel1Layout.setVerticalGroup(
            cardSpotJPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        cardSpotJPanel2.setBackground(new java.awt.Color(0, 0, 153));
        cardSpotJPanel2.setPreferredSize(new java.awt.Dimension(125, 225));
        cardSpotJPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardSpotJPanel2MouseClicked(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(250, 250, 250));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout cardSpotJPanel2Layout = new javax.swing.GroupLayout(cardSpotJPanel2);
        cardSpotJPanel2.setLayout(cardSpotJPanel2Layout);
        cardSpotJPanel2Layout.setHorizontalGroup(
            cardSpotJPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        cardSpotJPanel2Layout.setVerticalGroup(
            cardSpotJPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        cardSpotJPanel3.setBackground(new java.awt.Color(0, 0, 153));
        cardSpotJPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardSpotJPanel3MouseClicked(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(250, 250, 250));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout cardSpotJPanel3Layout = new javax.swing.GroupLayout(cardSpotJPanel3);
        cardSpotJPanel3.setLayout(cardSpotJPanel3Layout);
        cardSpotJPanel3Layout.setHorizontalGroup(
            cardSpotJPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        cardSpotJPanel3Layout.setVerticalGroup(
            cardSpotJPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        cardSpotJPanel4.setBackground(new java.awt.Color(0, 0, 153));
        cardSpotJPanel4.setPreferredSize(new java.awt.Dimension(125, 225));
        cardSpotJPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardSpotJPanel4MouseClicked(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(250, 250, 250));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout cardSpotJPanel4Layout = new javax.swing.GroupLayout(cardSpotJPanel4);
        cardSpotJPanel4.setLayout(cardSpotJPanel4Layout);
        cardSpotJPanel4Layout.setHorizontalGroup(
            cardSpotJPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        cardSpotJPanel4Layout.setVerticalGroup(
            cardSpotJPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        cardSpotJPanel5.setBackground(new java.awt.Color(0, 0, 153));
        cardSpotJPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardSpotJPanel5MouseClicked(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(250, 250, 250));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout cardSpotJPanel5Layout = new javax.swing.GroupLayout(cardSpotJPanel5);
        cardSpotJPanel5.setLayout(cardSpotJPanel5Layout);
        cardSpotJPanel5Layout.setHorizontalGroup(
            cardSpotJPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardSpotJPanel5Layout.setVerticalGroup(
            cardSpotJPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        cardSpotJPanel6.setBackground(new java.awt.Color(0, 0, 153));
        cardSpotJPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardSpotJPanel6MouseClicked(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(250, 250, 250));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout cardSpotJPanel6Layout = new javax.swing.GroupLayout(cardSpotJPanel6);
        cardSpotJPanel6.setLayout(cardSpotJPanel6Layout);
        cardSpotJPanel6Layout.setHorizontalGroup(
            cardSpotJPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardSpotJPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        cardSpotJPanel6Layout.setVerticalGroup(
            cardSpotJPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        cardSpotJPanel7.setBackground(new java.awt.Color(0, 0, 153));
        cardSpotJPanel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardSpotJPanel7MouseClicked(evt);
            }
        });

        jLabel7.setBackground(new java.awt.Color(250, 250, 250));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout cardSpotJPanel7Layout = new javax.swing.GroupLayout(cardSpotJPanel7);
        cardSpotJPanel7.setLayout(cardSpotJPanel7Layout);
        cardSpotJPanel7Layout.setHorizontalGroup(
            cardSpotJPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardSpotJPanel7Layout.setVerticalGroup(
            cardSpotJPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        cardSpotJPanel8.setBackground(new java.awt.Color(0, 0, 153));
        cardSpotJPanel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardSpotJPanel8MouseClicked(evt);
            }
        });

        jLabel8.setBackground(new java.awt.Color(250, 250, 250));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout cardSpotJPanel8Layout = new javax.swing.GroupLayout(cardSpotJPanel8);
        cardSpotJPanel8.setLayout(cardSpotJPanel8Layout);
        cardSpotJPanel8Layout.setHorizontalGroup(
            cardSpotJPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardSpotJPanel8Layout.setVerticalGroup(
            cardSpotJPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        cardSpotJPanel9.setBackground(new java.awt.Color(0, 0, 153));
        cardSpotJPanel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardSpotJPanel9MouseClicked(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(250, 250, 250));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout cardSpotJPanel9Layout = new javax.swing.GroupLayout(cardSpotJPanel9);
        cardSpotJPanel9.setLayout(cardSpotJPanel9Layout);
        cardSpotJPanel9Layout.setHorizontalGroup(
            cardSpotJPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        cardSpotJPanel9Layout.setVerticalGroup(
            cardSpotJPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        cardSpotJPanel10.setBackground(new java.awt.Color(0, 0, 153));
        cardSpotJPanel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardSpotJPanel10MouseClicked(evt);
            }
        });

        jLabel10.setBackground(new java.awt.Color(250, 250, 250));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout cardSpotJPanel10Layout = new javax.swing.GroupLayout(cardSpotJPanel10);
        cardSpotJPanel10.setLayout(cardSpotJPanel10Layout);
        cardSpotJPanel10Layout.setHorizontalGroup(
            cardSpotJPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardSpotJPanel10Layout.setVerticalGroup(
            cardSpotJPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        cardSpotJPanel11.setBackground(new java.awt.Color(0, 0, 153));
        cardSpotJPanel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardSpotJPanel11MouseClicked(evt);
            }
        });

        jLabel11.setBackground(new java.awt.Color(250, 250, 250));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout cardSpotJPanel11Layout = new javax.swing.GroupLayout(cardSpotJPanel11);
        cardSpotJPanel11.setLayout(cardSpotJPanel11Layout);
        cardSpotJPanel11Layout.setHorizontalGroup(
            cardSpotJPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardSpotJPanel11Layout.setVerticalGroup(
            cardSpotJPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        cardSpotJPanel12.setBackground(new java.awt.Color(0, 0, 153));
        cardSpotJPanel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardSpotJPanel12MouseClicked(evt);
            }
        });

        jLabel12.setBackground(new java.awt.Color(250, 250, 250));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout cardSpotJPanel12Layout = new javax.swing.GroupLayout(cardSpotJPanel12);
        cardSpotJPanel12.setLayout(cardSpotJPanel12Layout);
        cardSpotJPanel12Layout.setHorizontalGroup(
            cardSpotJPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardSpotJPanel12Layout.setVerticalGroup(
            cardSpotJPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout cardSpotJPanelLayout = new javax.swing.GroupLayout(cardSpotJPanel);
        cardSpotJPanel.setLayout(cardSpotJPanelLayout);
        cardSpotJPanelLayout.setHorizontalGroup(
            cardSpotJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardSpotJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cardSpotJPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(cardSpotJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cardSpotJPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(cardSpotJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cardSpotJPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(cardSpotJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cardSpotJPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardSpotJPanelLayout.setVerticalGroup(
            cardSpotJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSpotJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardSpotJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cardSpotJPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cardSpotJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cardSpotJPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cardSpotJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cardSpotJPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardSpotJPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        startJButton.setBackground(new java.awt.Color(0, 153, 51));
        startJButton.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        startJButton.setText("Start");
        startJButton.setFocusable(false);
        startJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startJButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startJButtonMouseExited(evt);
            }
        });

        makeSetJButton.setBackground(java.awt.Color.gray);
        makeSetJButton.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        makeSetJButton.setText("Make a set!");
        makeSetJButton.setEnabled(false);
        makeSetJButton.setFocusable(false);
        makeSetJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                makeSetJButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                makeSetJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                makeSetJButtonMouseExited(evt);
            }
        });

        setsMadeJLabel.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        setsMadeJLabel.setText("Sets made: 0");

        cardsInDeckJLabel.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        cardsInDeckJLabel.setText("Cards in deck: 81");

        feedbackJTextArea.setEditable(false);
        feedbackJTextArea.setBackground(new java.awt.Color(255, 255, 183));
        feedbackJTextArea.setColumns(20);
        feedbackJTextArea.setRows(5);
        feedbackJTextArea.setTabSize(9);
        feedbackJTextArea.setText(" - - - - - - - - - - - - - - - - Rules of the game - - - - - - - - - - - - - - - -\nThe object of the game is to make a SET of 3. A group of  3 can be a SET\nonly if they all either share one attribute or none of them  share that \nattribute. For example if one of the cards is GREEN then the other two \ncards must either both be GREEN or one must be RED and the other one\nPURPLE, likewise for the other attributes.\nExample of a valid set:\n1) GREEN\tFILLED\tONE\tDIAMOND\n2) RED\tFILLED\tTWO\tDIAMOND\n3) PURPLE\tFILLED\tThree\tDIAMOND\n\n");
        feedbackJTextArea.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        feedbackJTextArea.setFocusable(false);
        jScrollPane1.setViewportView(feedbackJTextArea);

        deselectJButton.setBackground(new java.awt.Color(102, 0, 0));
        deselectJButton.setForeground(new java.awt.Color(255, 255, 255));
        deselectJButton.setText("Deselect");
        deselectJButton.setFocusable(false);
        deselectJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deselectJButtonMouseClicked(evt);
            }
        });

        readRulesJButton.setBackground(new java.awt.Color(102, 0, 0));
        readRulesJButton.setForeground(new java.awt.Color(255, 255, 255));
        readRulesJButton.setText("Post rules");
        readRulesJButton.setFocusable(false);
        readRulesJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                readRulesJButtonMouseClicked(evt);
            }
        });

        hintJButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        hintJButton.setText("Hint");
        hintJButton.setDefaultCapable(false);
        hintJButton.setEnabled(false);
        hintJButton.setFocusable(false);
        hintJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hintJButtonMouseClicked(evt);
            }
        });

        enableHintJCheckBox.setText("Enable Hint");
        enableHintJCheckBox.setFocusable(false);
        enableHintJCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                enableHintJCheckBoxItemStateChanged(evt);
            }
        });

        quickestSetJLabel.setText("Quickest Set: ---");

        slowestSetJLabel.setText("Slowest Set: ---");

        javax.swing.GroupLayout backgroundJPanelLayout = new javax.swing.GroupLayout(backgroundJPanel);
        backgroundJPanel.setLayout(backgroundJPanelLayout);
        backgroundJPanelLayout.setHorizontalGroup(
            backgroundJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cardSpotJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(backgroundJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(backgroundJPanelLayout.createSequentialGroup()
                        .addGroup(backgroundJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deselectJButton)
                            .addComponent(readRulesJButton)
                            .addGroup(backgroundJPanelLayout.createSequentialGroup()
                                .addGroup(backgroundJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(startJButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(makeSetJButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(backgroundJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(backgroundJPanelLayout.createSequentialGroup()
                                        .addGroup(backgroundJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cardsInDeckJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(hintJButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(enableHintJCheckBox))
                                    .addGroup(backgroundJPanelLayout.createSequentialGroup()
                                        .addComponent(setsMadeJLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(backgroundJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(slowestSetJLabel)
                                            .addComponent(quickestSetJLabel))))))
                        .addGap(0, 116, Short.MAX_VALUE)))
                .addContainerGap())
        );
        backgroundJPanelLayout.setVerticalGroup(
            backgroundJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundJPanelLayout.createSequentialGroup()
                .addGroup(backgroundJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(backgroundJPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cardSpotJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(backgroundJPanelLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(readRulesJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deselectJButton)
                        .addGap(18, 18, 18)
                        .addGroup(backgroundJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(makeSetJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(backgroundJPanelLayout.createSequentialGroup()
                                .addComponent(cardsInDeckJLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(backgroundJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(setsMadeJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(quickestSetJLabel))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(slowestSetJLabel)
                        .addGap(18, 18, 18)
                        .addGroup(backgroundJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(startJButton, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                            .addComponent(hintJButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(enableHintJCheckBox))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // <editor-fold defaultstate="collapsed" desc="--cardSpotJpanelMouseClicked Methods--">


    private void cardSpotJPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardSpotJPanel1MouseClicked
        if (startJButton.isEnabled()) {
            return;
        }
        if (!cardSpotJPanel1.getBackground().equals(selectGreen) && selected.size() < 3) {
            cardSpotJPanel1.setBackground(selectGreen);
            selected.add(0);
        } else {
            cardSpotJPanel1.setBackground(nutrualDarkBlue);
            selected.remove(new Integer(0));
        }
    }//GEN-LAST:event_cardSpotJPanel1MouseClicked

    private void cardSpotJPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardSpotJPanel2MouseClicked
        if (startJButton.isEnabled()) {
            return;
        }
        if (!cardSpotJPanel2.getBackground().equals(selectGreen) && selected.size() < 3) {
            cardSpotJPanel2.setBackground(selectGreen);
            selected.add(1);
        } else {
            cardSpotJPanel2.setBackground(nutrualDarkBlue);
            selected.remove(new Integer(1));
        }
    }//GEN-LAST:event_cardSpotJPanel2MouseClicked

    private void cardSpotJPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardSpotJPanel3MouseClicked
        if (startJButton.isEnabled()) {
            return;
        }
        if (!cardSpotJPanel3.getBackground().equals(selectGreen) && selected.size() < 3) {
            cardSpotJPanel3.setBackground(selectGreen);
            selected.add(2);
        } else {
            cardSpotJPanel3.setBackground(nutrualDarkBlue);
            selected.remove(new Integer(2));
        }
    }//GEN-LAST:event_cardSpotJPanel3MouseClicked

    private void cardSpotJPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardSpotJPanel4MouseClicked
        if (startJButton.isEnabled()) {
            return;
        }
        if (!cardSpotJPanel4.getBackground().equals(selectGreen) && selected.size() < 3) {
            cardSpotJPanel4.setBackground(selectGreen);
            selected.add(3);
        } else {
            cardSpotJPanel4.setBackground(nutrualDarkBlue);
            selected.remove(new Integer(3));
        }
    }//GEN-LAST:event_cardSpotJPanel4MouseClicked

    private void cardSpotJPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardSpotJPanel5MouseClicked
        if (startJButton.isEnabled()) {
            return;
        }
        if (!cardSpotJPanel5.getBackground().equals(selectGreen) && selected.size() < 3) {
            cardSpotJPanel5.setBackground(selectGreen);
            selected.add(4);
        } else {
            cardSpotJPanel5.setBackground(nutrualDarkBlue);
            selected.remove(new Integer(4));
        }
    }//GEN-LAST:event_cardSpotJPanel5MouseClicked

    private void cardSpotJPanel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardSpotJPanel6MouseClicked
        if (startJButton.isEnabled()) {
            return;
        }
        if (!cardSpotJPanel6.getBackground().equals(selectGreen) && selected.size() < 3) {
            cardSpotJPanel6.setBackground(selectGreen);
            selected.add(5);
        } else {
            cardSpotJPanel6.setBackground(nutrualDarkBlue);
            selected.remove(new Integer(5));
        }
    }//GEN-LAST:event_cardSpotJPanel6MouseClicked

    private void cardSpotJPanel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardSpotJPanel7MouseClicked
        if (startJButton.isEnabled()) {
            return;
        }
        if (!cardSpotJPanel7.getBackground().equals(selectGreen) && selected.size() < 3) {
            cardSpotJPanel7.setBackground(selectGreen);
            selected.add(6);
        } else {
            cardSpotJPanel7.setBackground(nutrualDarkBlue);
            selected.remove(new Integer(6));
        }
    }//GEN-LAST:event_cardSpotJPanel7MouseClicked

    private void cardSpotJPanel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardSpotJPanel8MouseClicked
        if (startJButton.isEnabled()) {
            return;
        }
        if (!cardSpotJPanel8.getBackground().equals(selectGreen) && selected.size() < 3) {
            cardSpotJPanel8.setBackground(selectGreen);
            selected.add(7);
        } else {
            cardSpotJPanel8.setBackground(nutrualDarkBlue);
            selected.remove(new Integer(7));
        }
    }//GEN-LAST:event_cardSpotJPanel8MouseClicked

    private void cardSpotJPanel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardSpotJPanel9MouseClicked
        if (startJButton.isEnabled()) {
            return;
        }
        if (!cardSpotJPanel9.getBackground().equals(selectGreen) && selected.size() < 3) {
            cardSpotJPanel9.setBackground(selectGreen);
            selected.add(8);
        } else {
            cardSpotJPanel9.setBackground(nutrualDarkBlue);
            selected.remove(new Integer(8));
        }
    }//GEN-LAST:event_cardSpotJPanel9MouseClicked

    private void cardSpotJPanel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardSpotJPanel10MouseClicked
        if (startJButton.isEnabled()) {
            return;
        }
        if (!cardSpotJPanel10.getBackground().equals(selectGreen) && selected.size() < 3) {
            cardSpotJPanel10.setBackground(selectGreen);
            selected.add(9);
        } else {
            cardSpotJPanel10.setBackground(nutrualDarkBlue);
            selected.remove(new Integer(9));
        }
    }//GEN-LAST:event_cardSpotJPanel10MouseClicked

    private void cardSpotJPanel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardSpotJPanel11MouseClicked
        if (startJButton.isEnabled()) {
            return;
        }
        if (!cardSpotJPanel11.getBackground().equals(selectGreen) && selected.size() < 3) {
            cardSpotJPanel11.setBackground(selectGreen);
            selected.add(10);
        } else {
            cardSpotJPanel11.setBackground(nutrualDarkBlue);
            selected.remove(new Integer(10));
        }
    }//GEN-LAST:event_cardSpotJPanel11MouseClicked

    private void cardSpotJPanel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardSpotJPanel12MouseClicked
        if (startJButton.isEnabled()) {
            return;
        }
        if (!cardSpotJPanel12.getBackground().equals(selectGreen) && selected.size() < 3) {
            cardSpotJPanel12.setBackground(selectGreen);
            selected.add(11);
        } else {
            cardSpotJPanel12.setBackground(nutrualDarkBlue);
            selected.remove(new Integer(11));
        }
    }//GEN-LAST:event_cardSpotJPanel12MouseClicked
// </editor-fold>
    private void startJButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startJButtonMouseClicked
        if (!startJButton.isEnabled()) {
            return;
        }
        startJButton.setBackground(Color.gray);
        startJButton.setEnabled(false);
        makeSetJButton.setEnabled(true);
        makeSetJButton.setBackground(darkGreen);
        this.initDeck();
        for (int i = 0; i < cardsOnBoard.length; i++) {
            cardsOnBoard[i] = (deck.pop());
            lables[i].setText(this.pictureHtml((cardsOnBoard[i])));
        }
        // if after the cards are delt there are no sets to be made take all the cards back and re-deal thems
        if (!this.isSetOnBoard()) {
            while (!this.isSetOnBoard()) {
                for (int i = 0; i < cardsOnBoard.length; i++) {
                    deck.push(cardsOnBoard[i]);
                }
                Collections.shuffle(deck);
                for (int i = 0; i < cardsOnBoard.length; i++) {
                    cardsOnBoard[i] = deck.pop();
                    lables[i].setText(this.pictureHtml((cardsOnBoard[i])));
                }
            }
        }
        setStartTime = System.currentTimeMillis();
        gameStartTime = System.currentTimeMillis();
        fastestSetTime = Long.MAX_VALUE;
        slowestSetTime = 0;
        cardsInDeckJLabel.setText("Cards in deck: " + deck.size());
//        this.isSetOnBoard(); // not sure why this is here, might be vital but commenting out for now
    }//GEN-LAST:event_startJButtonMouseClicked

    private void startJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startJButtonMouseEntered
        if (!startJButton.isEnabled()) {
            return;
        }
        startJButton.setBackground(lightGreen);
    }//GEN-LAST:event_startJButtonMouseEntered

    private void startJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startJButtonMouseExited
        if (!startJButton.isEnabled()) {
            return;
        }
        startJButton.setBackground(darkGreen);
    }//GEN-LAST:event_startJButtonMouseExited

    private void playSound(String soundfile) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(soundfile + ".wav");
            AudioStream audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetCardSpots() {
        // clear and reset selected cardSpotJPanels and replace used cards from deck
        for (Integer i : selected) {
            cardSpotJPanels[i].setBackground(nutrualDarkBlue);
            cardsOnBoard[i] = deck.pop();
            lables[i].setText(this.pictureHtml((cardsOnBoard[i])));
        }
    }

    private void flipSetEnabledComponents(JComponent... comps) {
        for (JComponent comp : comps) {
            comp.setEnabled(!comp.isEnabled());
        }
    }

    private void reinitGame() {
        long gameLengthTime = System.currentTimeMillis() - gameStartTime;
        setsMade = 0;
        setsMadeJLabel.setText("Sets made: " + setsMade);
        for (int i = 0; i < cardSpotJPanels.length; i++) {
            cardSpotJPanels[i].setBackground(nutrualDarkBlue);
            lables[i].setText("");
        }

        startJButton.setEnabled(true);
        startJButton.setBackground(darkGreen);

        hintJButton.setEnabled(false);

        makeSetJButton.setEnabled(false);
        makeSetJButton.setBackground(Color.gray);

        quickestSetJLabel.setText("Quickest Set: ---");
        slowestSetJLabel.setText("Slowest Set: ---");
        enableHintJCheckBox.setSelected(false);
        int min = (int) (gameLengthTime / 60 / 1000);
        int sec = (int) gameLengthTime / 1000 % 60;
        this.feedbackJTextArea.setText("You finished the game in " + min + ":" + sec
                + ".\nThe fastest set made took: " + fastestSetTime / 1000.0 + " seconds."
                + "\nThe slowest set took " + slowestSetTime / 1000.0 + " seconds."
                + "\n" + hintsUsed + " hints were used.\n"
                + badSets + " bad sets were made.");

    }
    private void makeSetJButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_makeSetJButtonMouseClicked
        if (!makeSetJButton.isEnabled()) {
            return;
        }
        if (selected.size() != 3) { // if not 3 cards were selected
            badSets++;
            playSound("badsound");
            feedbackJTextArea.setText("You need 3 cards selected to make a set.\nTry Again.");
            return;
        } else {
            selected.sort(null); // lets the cards be shown in the order they appear on the board on the feedback area
            if (this.isSet(cardsOnBoard[selected.get(0)], cardsOnBoard[selected.get(1)], cardsOnBoard[selected.get(2)])) {
                playSound("goodsound");
                long setTime = System.currentTimeMillis() - setStartTime;
                if (fastestSetTime > setTime) {
                    fastestSetTime = setTime;
                    quickestSetJLabel.setText("Quickest Set: " + fastestSetTime / 1000.0);
                }
                if (slowestSetTime < setTime) {
                    slowestSetTime = setTime;
                    slowestSetJLabel.setText("Slowest Set: " + slowestSetTime / 1000.0);
                }
                setStartTime = System.currentTimeMillis();

                setsMadeJLabel.setText("Sets made: " + ++setsMade);

                this.resetCardSpots();
                // if the deck is empty re-init the game
                if (deck.isEmpty()) { //CONTINUE FROM HRE
                    this.reinitGame();
                }
                this.ensureSetOnBoard();
                cardsInDeckJLabel.setText("Cards in deck: " + deck.size());
                selected.clear();
            } else { // else not a valid set
                badSets++;
                playSound("badsound");
            }
        }
    }//GEN-LAST:event_makeSetJButtonMouseClicked

    private void ensureSetOnBoard() {
        if (!this.isSetOnBoard()) {
            while (!this.isSetOnBoard()) {
                for (Integer i : selected) {
                    deck.push(cardsOnBoard[i]);
                }
                Collections.shuffle(deck);
                for (Integer i : selected) {
                    cardsOnBoard[i] = deck.pop();
                    lables[i].setText(this.pictureHtml((cardsOnBoard[i])));
                }
            }
        }
    }

    private void makeSetJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_makeSetJButtonMouseEntered
        if (startJButton.isEnabled()) {
            return;
        }
        makeSetJButton.setBackground(lightGreen);
    }//GEN-LAST:event_makeSetJButtonMouseEntered

    private void makeSetJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_makeSetJButtonMouseExited
        if (startJButton.isEnabled()) {
            return;
        }
        makeSetJButton.setBackground(darkGreen);
    }//GEN-LAST:event_makeSetJButtonMouseExited

    private void deselectJButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deselectJButtonMouseClicked
        for (Integer i : selected) {
            cardSpotJPanels[i].setBackground(nutrualDarkBlue);
        }
        selected.clear();
    }//GEN-LAST:event_deselectJButtonMouseClicked

    private void readRulesJButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_readRulesJButtonMouseClicked
        feedbackJTextArea.setText(" - - - - - - - - - - - - - - - - Rules of the game - - - - - - - - - - - - - - - -\n"
                + "The object of the game is to make a SET of 3. A group of  3 can be a SET\n"
                + "only if they all either share one attribute or none of them  share that \n"
                + "attribute. For example if one of the cards is GREEN then the other two \n"
                + "cards must either both be GREEN or one must be RED and the other one\n"
                + "PURPLE, likewise for the other attributes.\n"
                + "Example of a valid set:\n"
                + "1) GREEN	FILLED	ONE	DIAMOND\n"
                + "2) RED	FILLED	TWO	DIAMOND\n"
                + "3) PURPLE	FILLED	Three	DIAMOND\n"
                + "\n"
                + "");
    }//GEN-LAST:event_readRulesJButtonMouseClicked

    private void runHintThread() {
        new Thread() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = getClass().getResourceAsStream("fingersnap.wav");
                    InputStream inputStream2 = getClass().getResourceAsStream("fingersnap.wav");
                    AudioStream audioStream = new AudioStream(inputStream);
                    AudioStream audioStream2 = new AudioStream(inputStream2);
                    ArrayList<int[]> sets = new ArrayList<>();
                    for (int i = 0; i < 9; i++) {
                        for (int j = i + 1; j < 11; j++) {
                            for (int k = j + 1; k < 12; k++) {
                                if (SetGame.checkSet(cardsOnBoard[i], cardsOnBoard[j], cardsOnBoard[k])) {
                                    int setNums[] = {i, j, k};
                                    sets.add(setNums);
                                }
                            }
                        }
                    }
                    Collections.shuffle(sets);
                    int hintSet[] = sets.get(0);
                    Collections.shuffle(Arrays.asList(hintSet));

                    int x = hintSet[0];
                    int y = hintSet[1];
                    Color originalColor1 = cardSpotJPanels[x].getBackground();
                    Color originalColor2 = cardSpotJPanels[y].getBackground();
                    Thread.sleep(50);
                    cardSpotJPanels[x].setBackground(Color.yellow);
                    cardSpotJPanels[y].setBackground(Color.yellow);
                    AudioPlayer.player.start(audioStream);
                    Thread.sleep(110);
                    cardSpotJPanels[x].setBackground(originalColor1);
                    cardSpotJPanels[y].setBackground(originalColor2);
                    Thread.sleep(110);
                    cardSpotJPanels[x].setBackground(Color.yellow);
                    cardSpotJPanels[y].setBackground(Color.yellow);
                    AudioPlayer.player.start(audioStream2);
                    Thread.sleep(110);
                    cardSpotJPanels[x].setBackground(originalColor1);
                    cardSpotJPanels[y].setBackground(originalColor2);
                    Thread.sleep(250);
                } catch (InterruptedException | IOException ex) {
                    Logger.getLogger(SetGame.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    canHint = true;
                }
            }
        }.start();
    }
    private void hintJButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hintJButtonMouseClicked
        if (!canHint || hintJButton.isEnabled() == false) {
            return;
        }
        canHint = false;//prevents spamming of hint button
        hintsUsed++;
        this.runHintThread();
        // after the hint thread runs boolean canHint will be true

    }//GEN-LAST:event_hintJButtonMouseClicked

    private void enableHintJCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_enableHintJCheckBoxItemStateChanged
        if (startJButton.isEnabled()) {
            enableHintJCheckBox.setSelected(false);
            return;
        }
        hintJButton.setEnabled(!hintJButton.isEnabled());
    }//GEN-LAST:event_enableHintJCheckBoxItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SetGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SetGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SetGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SetGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SetGame().setVisible(true);
            }
        });
    }
    // <editor-fold defaultstate="collapsed" desc="--GUI Made variables--">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroundJPanel;
    private javax.swing.JPanel cardSpotJPanel;
    private javax.swing.JPanel cardSpotJPanel1;
    private javax.swing.JPanel cardSpotJPanel10;
    private javax.swing.JPanel cardSpotJPanel11;
    private javax.swing.JPanel cardSpotJPanel12;
    private javax.swing.JPanel cardSpotJPanel2;
    private javax.swing.JPanel cardSpotJPanel3;
    private javax.swing.JPanel cardSpotJPanel4;
    private javax.swing.JPanel cardSpotJPanel5;
    private javax.swing.JPanel cardSpotJPanel6;
    private javax.swing.JPanel cardSpotJPanel7;
    private javax.swing.JPanel cardSpotJPanel8;
    private javax.swing.JPanel cardSpotJPanel9;
    private javax.swing.JLabel cardsInDeckJLabel;
    private javax.swing.JButton deselectJButton;
    private javax.swing.JCheckBox enableHintJCheckBox;
    private javax.swing.JTextArea feedbackJTextArea;
    private javax.swing.JButton hintJButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton makeSetJButton;
    private javax.swing.JLabel quickestSetJLabel;
    private javax.swing.JButton readRulesJButton;
    private javax.swing.JLabel setsMadeJLabel;
    private javax.swing.JLabel slowestSetJLabel;
    private javax.swing.JButton startJButton;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
}
