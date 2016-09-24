package setgame;

/**
 * @author Noah Newdorf
 */
public class Card {

    public final ShapeColor color;
    public final ShapeFill fill;
    public final ShapeNumber number;
    public final ShapeType type;
    public final String resourceName;

    public Card(ShapeColor color, ShapeFill fill, ShapeNumber number, ShapeType type) {
        this.color = color;
        this.fill = fill;
        this.number = number;
        this.type = type;
        resourceName = color + "_" + fill + "_" + type + ".png"; 
        
    }

    /**
     * Card.toString() is used for 
     * @return 
     */
    @Override
    public String toString() {
        return "<html>" + color + "<br>" + fill + "<br>" + number + "<br>" + type + "</html>";
    }
}
