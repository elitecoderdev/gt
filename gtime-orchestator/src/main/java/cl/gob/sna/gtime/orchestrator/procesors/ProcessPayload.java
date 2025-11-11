package cl.gob.sna.gtime.orchestrator.procesors;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.api.vo.Root;
import cl.gob.sna.gtime.orchestrator.vo.aws.PayloadVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;
import org.json.JSONObject;
import org.json.XML;


public class ProcessPayload implements Processor {
    Logger log = Logger.getLogger(ProcessPayload.class);
    @Override
    public void process(Exchange exchange) throws JsonProcessingException {
        PayloadVO payLoad = (PayloadVO) exchange.getIn().getHeader("payLoad");


        if (payLoad != null) {
            //log.info(" [+] idPayload recibido : " + payLoad.getId());
            exchange.getIn().setHeader("idPayload", payLoad.getId());

            JSONObject xmlJSONObj = XML.toJSONObject(payLoad.getMensaje());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            Root root = objectMapper.readValue(xmlJSONObj.toString(), Root.class);

            Gtime gtime = root.getDocumento();
            gtime.setStartTime(System.currentTimeMillis());
            gtime.setIdPayload(Long.valueOf(payLoad.getId()));


            exchange.getIn().setBody(gtime, Gtime.class);
        }
    }
}
