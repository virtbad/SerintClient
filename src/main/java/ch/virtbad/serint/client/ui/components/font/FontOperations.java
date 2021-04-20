package ch.virtbad.serint.client.ui.components.font;

import java.text.Normalizer;

/**
 * This class handles operations to be needed for displaying text.
 * The text is displayed by using primitive bitmap fonts. Oriented by the ascii table.
 */
public class FontOperations {

    /**
     * Generates the vertices for one line of text
     * @param s text
     * @return vertices
     */
    public static float[] generateOneLineVertices(String s){
        float[] vertices = new float[s.length() * 4 * 4]; // four values (x, y, u, v) and four vertices (quad)
        char[] characters = s.toCharArray();

        int x = 0;
        int y = 0;
        int index = 0;

        for (char character : characters) {
            int c = toAscii(character);

            int uvX = getLetterUvX(c);
            int uvY = getLetterUvY(c);

            // Bottom Left
            vertices[index++] = x;
            vertices[index++] = y;
            vertices[index++] = uvX;
            vertices[index++] = uvY + 1;

            // Bottom Right
            vertices[index++] = x + 1;
            vertices[index++] = y;
            vertices[index++] = uvX + 1;
            vertices[index++] = uvY + 1;

            // Top Left
            vertices[index++] = x;
            vertices[index++] = y + 1;
            vertices[index++] = uvX;
            vertices[index++] = uvY;

            // Top Right
            vertices[index++] = x + 1;
            vertices[index++] = y + 1;
            vertices[index++] = uvX + 1;
            vertices[index++] = uvY;

            x++;
        }

        return vertices;
    }

    /**
     * Generates the indices for one line of text
     * @param s text
     * @return indices
     */
    public static int[] generateOneLineIndices(String s){
        int[] indices = new int[s.length() * 2 * 3]; // three corners for two triangles

        int index = 0;
        for (int i = 0; i < s.length(); i++) {

            // First triangle
            indices[index++] = i * 4;
            indices[index++] = i * 4 + 3;
            indices[index++] = i * 4 + 2;

            // Second triangle
            indices[index++] = i * 4;
            indices[index++] = i * 4 + 1;
            indices[index++] = i * 4 + 3;
        }

        return indices;
    }

    /**
     * Returns the height of a rendered string. (Although it is currently always one)
     * @param s string to get height for
     * @return height of string
     */
    public static float getOneLineHeight(String s){
        return 1;
    }

    /**
     * Returns the width of a rendered string.
     * @param s string to get width for
     * @return width of string
     */
    public static float getOneLineWidth(String s){
        return s.length();
    }

    /**
     * Returns the uv coordinate of a letter
     * @param letter letter to get coordinate for
     * @return uv coordinate
     */
    private static int getLetterUvX(int letter){
        return letter % 16;
    }

    /**
     * Returns the uv coordinates of a letter
     * @param letter letter to get coordinate for
     * @return uv coordinate
     */
    private static int getLetterUvY(int letter){
        return letter / 16;
    }

    /**
     * This method converts some characters to ascii with the extended table
     * @param c character
     * @return ascii int (not byte because java bytes are automatically signed)
     */
    private static int toAscii(char c){
        if (c < 128) return (byte) c;

        if (c == 0x00FC) return 129; // ue
        if (c == 0x00E4) return 132; // ae
        if (c == 0x00C4) return 142; // AE
        if (c == 0x00F6) return 148; // oe
        if (c == 0x00D6) return 153; // OE
        if (c == 0x00DC) return 154; // UE

        return 63; // ?
    }

}
