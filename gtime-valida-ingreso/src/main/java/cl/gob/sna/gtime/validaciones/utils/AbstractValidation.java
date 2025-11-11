package cl.gob.sna.gtime.validaciones.utils;

import cl.gob.sna.gtime.validaciones.ingreso.beans.ValidacionItemsBeans;
import cl.gob.sna.gtime.vo.Validacion;
import org.jboss.logging.Logger;

import java.util.List;

public class AbstractValidation {
    static Logger LOG = Logger.getLogger(AbstractValidation.class);
    public static void showAbstractProcess(List<Validacion> listValidaciones){
        LOG.info("--------------------------------------------");
        LOG.info("-----------ABSTRACT VALIDATION -------------");
        if (listValidaciones == null || listValidaciones.isEmpty()){
            LOG.info(" [*] No se encontraron validaciones");
        }else{
            for (Validacion val : listValidaciones){
                if (val != null && !val.isValido()){
                    LOG.info(" [+] Validacion no cumplida : " + val);
                }else{
                    //LOG.info(" [*] caso raro....");
                }
            }
            LOG.info("-----------------FIN------------------------");
            LOG.info("--------------------------------------------");
        }
    }
}
