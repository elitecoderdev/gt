package cl.gob.sna.gtime.processor.util;

import cl.gob.sna.gtime.api.vo.Fecha;
import cl.gob.sna.gtime.api.vo.Participacion;
import cl.gob.sna.gtime.api.vo.Referencia;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public class HelperUtil {
    public static java.sql.Date obtieneValorFecha(String tipoFecha, List<Fecha> fechas) {
        try{
            for (Fecha fecha : fechas) {
                if (fecha.getNombre() != null && fecha.getNombre().equals(tipoFecha)) {
                    return FechaUtil.parseFecha(fecha.getValor(), FechaUtil.FORMATO_GTIME_FECHA);
                }
            }
        }catch (ParseException pe){
            pe.getStackTrace();
        }catch (Exception e){
            e.getStackTrace();
        }
        return null;
    }

    public static Participacion obtieneParticipantePorTipo(String tipoParticipacion, List<Participacion> participaciones) {
        for (Participacion participacion : participaciones) {
            if (participacion.getNombre() != null && participacion.getNombre().equals(tipoParticipacion)) {
                return participacion;
            }
        }
        return null;
    }

    public static Referencia obtenerReferencia(String tipoReferencia, List<Referencia> referencias) {
        for (Referencia referencia : referencias) {
            if (referencia.getTipoReferencia() != null && referencia.getTipoReferencia().equals(tipoReferencia)) {
                return referencia;
            }
        }
        return null;
    }

    /**
     *
     * @param xml
     */
    public static String removeElementXML(String xml, List<String> tags){
        try{
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(is);

            for (String tag : tags){
                Element element = (Element) doc.getElementsByTagName(tag).item(0);

                Node prevElem = element.getPreviousSibling();
                if (prevElem != null && prevElem.getNodeType() == Node.TEXT_NODE &&
                        prevElem.getNodeValue().trim().length() == 0) {
                    element.getParentNode().removeChild(prevElem);
                }

                Node parent = element.getParentNode();
                parent.removeChild(element);
                parent.normalize();
            }

            return HelperUtil.domToString(doc);
        }catch(Exception pce){
            return xml;
        }

    }

    public static String domToString(Document newDoc) throws TransformerException {
            DOMSource domSource = new DOMSource(newDoc);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.transform(domSource, sr);

            return sw.toString();
    }

}
