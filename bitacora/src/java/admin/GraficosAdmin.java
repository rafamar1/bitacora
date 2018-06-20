package admin;

import datos.dao.ViajeJpaController;
import datos.entidades.Viaje;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean
public class GraficosAdmin implements Serializable {

    private PieChartModel pieUsuariosPublican;
    private PieChartModel pieModel2;
    private LineChartModel dateModel;

    @PostConstruct
    public void init() {
        createPieModels();
        createDateModel();
    }

    public PieChartModel getPieUsuariosPublican() {
        return pieUsuariosPublican;
    }

    public PieChartModel getPieModel2() {
        return pieModel2;
    }

    public LineChartModel getDateModel() {
        return dateModel;
    }

    private void createDateModel() {
        dateModel = new LineChartModel();
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Fecha Creacion Viajes");

        HashMap<String, Integer> mapaUsuarios_CantidadViajes = dameNumeroViajesPublicadosPorFecha();

        for(Map.Entry<String, Integer> entry : mapaUsuarios_CantidadViajes.entrySet()) {
            series1.set(entry.getKey(), entry.getValue());
        }
        dateModel.addSeries(series1);

        dateModel.setTitle("Cantidad de Viajes Publicados por Fecha");
        dateModel.setZoom(true);
        dateModel.getAxis(AxisType.Y).setLabel("Numero de Viajes");
        DateAxis axis = new DateAxis("Fecha de Publicacion");
        axis.setTickAngle(-50);
        axis.setMax("2018-07-01");
        axis.setMin("2018-03-01");
        axis.setTickFormat("%b %#d, %y");

        dateModel.getAxes().put(AxisType.X, axis);
    }

    private void createPieModels() {
        //createPieUsuariosPublican();
        createPieModel2();
    }
    
    private void createPieModel2() {
        pieModel2 = new PieChartModel();

        HashMap<String, Integer> mapaUsuarios_CantidadViajes = dameNumeroViajesPublicadosPorUsuario();

        for (Map.Entry<String, Integer> entry : mapaUsuarios_CantidadViajes.entrySet()) {
            pieModel2.set(entry.getKey(), entry.getValue());
        }

        pieModel2.setTitle("Cantidad de Viajes Publicados por Usuarios");
        pieModel2.setLegendPosition("e");
        /*pieModel2.setFill(false);*/
        pieModel2.setShowDataLabels(true);
        pieModel2.setDiameter(150);
    }

    private HashMap<String, Integer> dameNumeroViajesPublicadosPorUsuario() {
        HashMap<String, Integer> mapaUsuariosCantidadViajes = new HashMap();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bitacoraPU");
        ViajeJpaController viajeController = new ViajeJpaController(emf);
        List<Viaje> listaViajes = viajeController.findViajeEntities();
        for (Viaje viaje : listaViajes) {
            if (mapaUsuariosCantidadViajes.containsKey(viaje.getUsuario().getNombreUsuario())) {
                mapaUsuariosCantidadViajes.put(viaje.getUsuario().getNombreUsuario(),
                        mapaUsuariosCantidadViajes.get(viaje.getUsuario().getNombreUsuario()) + 1);
            } else {
                mapaUsuariosCantidadViajes.put(viaje.getUsuario().getNombreUsuario(), 1);
            }
        }
        return mapaUsuariosCantidadViajes;
    }

    private HashMap<String, Integer> dameNumeroViajesPublicadosPorFecha() {
        HashMap<String, Integer> mapaUsuariosCantidadViajes = new HashMap();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bitacoraPU");
        ViajeJpaController viajeController = new ViajeJpaController(emf);
        List<Viaje> listaViajes = viajeController.findViajeEntities();
        String fechaFormateada = "";
        for (Viaje viaje : listaViajes) {
//            fechaFormateada = formateaFecha(viaje.getFechaCreacion());;
            fechaFormateada = formateaFecha(viaje.getFechaCreacion());
            if (mapaUsuariosCantidadViajes.containsKey(fechaFormateada)) {
                mapaUsuariosCantidadViajes.put(formateaFecha(viaje.getFechaCreacion()),
                        mapaUsuariosCantidadViajes.get(fechaFormateada) + 1);
            } else {
                mapaUsuariosCantidadViajes.put(fechaFormateada, 1);
            }
        }
        return mapaUsuariosCantidadViajes;
    }

    private String formateaFecha(Date fecha) {
       Instant instant = fecha.toInstant();
       OffsetDateTime odt = instant.atOffset( ZoneOffset.UTC );
       
       DateTimeFormatter formateo = DateTimeFormatter.ofPattern( "uuuu-MM-dd" );
       return odt.format(formateo);
    }
}
