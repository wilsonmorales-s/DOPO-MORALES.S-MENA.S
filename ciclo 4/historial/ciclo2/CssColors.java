import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class CssColors{

    private static final Map<String, Color> TABLE = buildTable();

    
    private CssColors(){
    }

    /** Revisa si el color esta dentro de los colores permitidos. */
    public static boolean isValid(String name){
        return name != null && TABLE.containsKey(name.toLowerCase());
    }

    /** Convierte el nombre del color al formato Color de Java. */
    public static Color toAwtColor(String name){
        Color color = (name == null) ? null : TABLE.get(name.toLowerCase());
        return (color != null) ? color : Color.black;
    }

    // Tabla con los colores que podemos usar en el proyecto.
    private static Map<String, Color> buildTable(){
        Map<String, Color> t = new HashMap<String, Color>();
        t.put("black", new Color(0, 0, 0));
        t.put("white", new Color(255, 255, 255));
        t.put("red", new Color(255, 0, 0));
        t.put("green", new Color(0, 128, 0));
        t.put("lime", new Color(0, 255, 0));
        t.put("blue", new Color(0, 0, 255));
        t.put("yellow", new Color(255, 255, 0));
        t.put("cyan", new Color(0, 255, 255));
        t.put("aqua", new Color(0, 255, 255));
        t.put("magenta", new Color(255, 0, 255));
        t.put("fuchsia", new Color(255, 0, 255));
        t.put("gray", new Color(128, 128, 128));
        t.put("grey", new Color(128, 128, 128));
        t.put("silver", new Color(192, 192, 192));
        t.put("maroon", new Color(128, 0, 0));
        t.put("olive", new Color(128, 128, 0));
        t.put("navy", new Color(0, 0, 128));
        t.put("teal", new Color(0, 128, 128));
        t.put("purple", new Color(128, 0, 128));
        t.put("orange", new Color(255, 165, 0));
        t.put("pink", new Color(255, 192, 203));
        t.put("brown", new Color(165, 42, 42));
        t.put("gold", new Color(255, 215, 0));
        t.put("coral", new Color(255, 127, 80));
        t.put("salmon", new Color(250, 128, 114));
        t.put("tomato", new Color(255, 99, 71));
        t.put("orangered", new Color(255, 69, 0));
        t.put("crimson", new Color(220, 20, 60));
        t.put("hotpink", new Color(255, 105, 180));
        t.put("deeppink", new Color(255, 20, 147));
        t.put("indigo", new Color(75, 0, 130));
        t.put("violet", new Color(238, 130, 238));
        t.put("orchid", new Color(218, 112, 214));
        t.put("plum", new Color(221, 160, 221));
        t.put("turquoise", new Color(64, 224, 208));
        t.put("khaki", new Color(240, 230, 140));
        t.put("chocolate", new Color(210, 105, 30));
        t.put("tan", new Color(210, 180, 140));
        t.put("beige", new Color(245, 245, 220));
        t.put("ivory", new Color(255, 255, 240));
        t.put("lavender", new Color(230, 230, 250));
        t.put("azure", new Color(240, 255, 255));
        t.put("chartreuse", new Color(127, 255, 0));
        t.put("springgreen", new Color(0, 255, 127));
        t.put("seagreen", new Color(46, 139, 87));
        t.put("forestgreen", new Color(34, 139, 34));
        t.put("darkgreen", new Color(0, 100, 0));
        t.put("lightgreen", new Color(144, 238, 144));
        t.put("yellowgreen", new Color(154, 205, 50));
        t.put("skyblue", new Color(135, 206, 235));
        t.put("steelblue", new Color(70, 130, 180));
        t.put("royalblue", new Color(65, 105, 225));
        t.put("dodgerblue", new Color(30, 144, 255));
        t.put("slateblue", new Color(106, 90, 205));
        t.put("midnightblue", new Color(25, 25, 112));
        t.put("darkblue", new Color(0, 0, 139));
        t.put("firebrick", new Color(178, 34, 34));
        t.put("darkred", new Color(139, 0, 0));
        t.put("darkviolet", new Color(148, 0, 211));
        t.put("mediumpurple", new Color(147, 112, 219));
        t.put("wheat", new Color(245, 222, 179));
        t.put("peru", new Color(205, 133, 63));
        t.put("goldenrod", new Color(218, 165, 32));
        t.put("darkgray", new Color(169, 169, 169));
        t.put("darkgrey", new Color(169, 169, 169));
        t.put("lightgray", new Color(211, 211, 211));
        t.put("lightgrey", new Color(211, 211, 211));
        t.put("dimgray", new Color(105, 105, 105));
        t.put("dimgrey", new Color(105, 105, 105));
        t.put("gainsboro", new Color(220, 220, 220));
        t.put("whitesmoke", new Color(245, 245, 245));
        t.put("snow", new Color(255, 250, 250));
        t.put("mistyrose", new Color(255, 228, 225));
        t.put("seashell", new Color(255, 245, 238));
        t.put("cornsilk", new Color(255, 248, 220));
        return t;
    }
}
