package cl.gob.sna.gtime.processor.repo;

import cl.gob.sna.gtime.api.vo.Item;
import cl.gob.sna.gtime.api.vo.Proditem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocTranItemProductoRepository {
    @Autowired
    DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(DocTranItemProductoRepository.class);

    public boolean persisteItemProducto(Long idDocBase, Item item) {
        this.jdbcTemplate = new JdbcTemplate(getDataSource());

        List<Proditem> prodItemList = new ArrayList<Proditem>();

        try {
            if (item != null && item.getProdItem() != null
                    && item.getProdItem().getProditem() != null) {

                prodItemList = item.getProdItem().getProditem();

                int pos = 1;

                for (Proditem prodItem : prodItemList) {
                    long id = obtieneSecuenciaDocItemProductoTransporte(idDocBase,
                            Integer.valueOf(item.getNumeroItem())) + pos;

                    BigDecimal cantidad = BigDecimal.valueOf(Double.valueOf(prodItem.getCantidad()));
                    BigDecimal valorDeclarado = BigDecimal.
                            valueOf(Double.valueOf(prodItem.getValorDeclarado()));

                    int itemTransporte = Integer.valueOf(item.getNumeroItem());

                    int op = jdbcTemplate.update(
                            "INSERT INTO DOCTRANSPORTE.DOCTRANITEMPRODUCTO (" +
                                    "      DOCUMENTOTRANSPORTE\n," +
                                    "      ITEMTRANSPORTE\n, " +
                                    "      ID,\n" +
                                    "      IDPARTIDAARANCELARIA,\n" +
                                    "      IDPRODUCTO,\n" +
                                    "      ACTIVO,\n" +
                                    "      FECHAACTIVA,\n" +
                                    "      CANTIDAD,\n" +
                                    "      UNIDADMEDIDA,\n" +
                                    "      PESOBRUTO,\n" +
                                    "      VALORDECLARADO,\n" +
                                    "      MONEDA,\n" +
                                    "      DESCRIPCION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                            idDocBase, itemTransporte,id, 0, 0, "S", new Date(),cantidad,prodItem.getUnidadMedida(),
                            0,valorDeclarado, prodItem.getMoneda().trim(), prodItem.getDescripcion());
                    if (op < 1) return false;

                    pos++;
                }
                return true;
            }
           return false;
        }catch (Exception de){
            log.info(" exception {}", de.getMessage());
            de.getStackTrace();
            return false;
        }
    }

    public long obtieneSecuenciaDocItemProductoTransporte(Long idDocBase, long idItem){
        this.jdbcTemplate = new JdbcTemplate(getDataSource());

        String sql = "SELECT NVL(MAX(ID), 0) as v_MAX_ID " +
                " FROM DOCTRANSPORTE.DOCTRANITEMPRODUCTO " +
                " WHERE DOCUMENTOTRANSPORTE = ? " +
                " AND ITEMTRANSPORTE = ?  ";


        final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, idDocBase, idItem);
        sqlRowSet.next();
        long id = sqlRowSet.getLong(1);
        return id;
    }

}
