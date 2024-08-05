package Reportes;

import Modelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class Grafico {

    public static void Graficar(String fecha) {
        Connection con;
        Conexion cn = new Conexion();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = "SELECT total FROM ventas WHERE fecha = ?";
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fecha);
            rs = ps.executeQuery();

            // Verificar si no hay ventas registradas
            if (!rs.next()) {
                // Mostrar mensaje y salir del método
                JOptionPane.showMessageDialog(null, "No se registraron ventas para la fecha seleccionada.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Crear el dataset para el gráfico
            DefaultPieDataset dataset = new DefaultPieDataset();
            while (rs.next()) {
                dataset.setValue(rs.getString("total"), rs.getDouble("total"));
            }
            JFreeChart jf = ChartFactory.createPieChart("Reporte de Ventas", dataset);
            ChartFrame frame = new ChartFrame("Total de Ventas por día", jf);
            frame.setSize(1000, 500);
            frame.setLocationRelativeTo(null);

            String rutaIcono = "Img/report.png";
            ImageIcon icon = new ImageIcon(rutaIcono);
            frame.setIconImage(icon.getImage());

            frame.setVisible(true);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
}
