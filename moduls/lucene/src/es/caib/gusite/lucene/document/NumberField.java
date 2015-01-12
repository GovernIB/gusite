package es.caib.gusite.lucene.document;

public class NumberField {

    private static final int RADIX = 36;
    private static final int INT_LEN = Integer.toString(2147483647, 36).length();
    private static final int SHORT_LEN = Integer.toString(32767, 36).length();


    public static String intToString(int number) {
        StringBuffer sb = new StringBuffer(Integer.toString(number, 36));
        int falten = INT_LEN - sb.length();
        for (int i = 0; i < falten; i++)
            sb.insert(0, 0);

        return sb.toString();
    }


    public static int stringToInt(String str) {
        return Integer.parseInt(str, 36);
    }


    public static String shortToString(short number) {
        StringBuffer sb = new StringBuffer(Integer.toString(number, 36));
        int falten = SHORT_LEN - sb.length();
        for (int i = 0; i < falten; i++)
            sb.insert(0, 0);

        return sb.toString();
    }


    public static short stringToShort(String str) {
        return Short.parseShort(str, 36);
    }

}
