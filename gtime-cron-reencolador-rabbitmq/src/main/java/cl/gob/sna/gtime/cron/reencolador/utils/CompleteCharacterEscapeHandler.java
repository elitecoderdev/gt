package cl.gob.sna.gtime.cron.reencolador.utils;

import java.io.IOException;
import java.io.Writer;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

public class CompleteCharacterEscapeHandler implements CharacterEscapeHandler {
 
    public CompleteCharacterEscapeHandler() {
        super();
    }
 
    /**
     * @param ch The array of characters.
     * @param start The starting position.
     * @param length The number of characters to use.
     * @param isAttVal true if this is an attribute value literal.
     */
    @Override
    public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
        int limit = start + length;
        for (int i = start; i < limit; i++) {
            char c = ch[i];
            if (c == '&' || c == '<' || c == '>' || (c == '\"' && isAttVal)
                    || (c == '\'' && isAttVal)) {
                if (i != start) {
                    out.write(ch, start, i - start);
                }
                start = i + 1;
                switch (ch[i]) {
                    case '&':
                        out.write("&");
                        break;
 
                    case '<':
                        out.write("<");
                        break;
 
                    case '>':
                        out.write(">");
                        break;
 
//                    case '\"':
//                        out.write(""");
//                        break;
 
                    case '\'':
                        out.write("'");
                        break;
                }
            }
        }
        if (start != limit) {
            out.write(ch, start, limit - start);
        }
    }

	
}