package Reportes;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import Modelo.Conexion;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
public class Excel {
    
    // Método para generar un reporte en Excel
    public static void reporte() {
 
        // Creación del libro de trabajo y la hoja
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Productos");
 
        try {
            // Carga de la imagen del log
            InputStream is = Excel.class.getResourceAsStream("Img/logo_faind.png");
            byte[] bytes = IOUtils.toByteArray(is);
            int imgIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close();
 
            // Creación de un objeto Drawing para insertar la imagen
            CreationHelper help = book.getCreationHelper();
            Drawing draw = sheet.createDrawingPatriarch();
 
            ClientAnchor anchor = help.createClientAnchor();
            anchor.setCol1(0);
            anchor.setRow1(1);
            Picture pict = draw.createPicture(anchor, imgIndex);
            pict.resize(1, 3);
 
            // Estilo para el título
            CellStyle tituloEstilo = book.createCellStyle();
            tituloEstilo.setAlignment(HorizontalAlignment.CENTER);
            tituloEstilo.setVerticalAlignment(VerticalAlignment.CENTER);
            Font fuenteTitulo = book.createFont();
            fuenteTitulo.setFontName("Arial");
            fuenteTitulo.setBold(true);
            fuenteTitulo.setFontHeightInPoints((short) 14);
            tituloEstilo.setFont(fuenteTitulo);
 
            // Creación de la fila y celda para el título
            Row filaTitulo = sheet.createRow(1);
            Cell celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("Reporte de Productos");
 
            // Fusión de celdas para el título
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3));
 
            // Cabecera del reporte
            String[] cabecera = new String[]{"Código", "Nombre", "Precio", "Existencia"};
 
            // Estilo para la cabecera
            CellStyle headerStyle = book.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
 
            Font font = book.createFont();
            font.setFontName("Arial");
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setFontHeightInPoints((short) 12);
            headerStyle.setFont(font);
 
            // Creación de la fila para la cabecera
            Row filaEncabezados = sheet.createRow(4);
 
            // Llenado de la cabecera
            for (int i = 0; i < cabecera.length; i++) {
                Cell celdaEnzabezado = filaEncabezados.createCell(i);
                celdaEnzabezado.setCellStyle(headerStyle);
                celdaEnzabezado.setCellValue(cabecera[i]);
            }
 
            // Conexión a la base de datos
            Conexion con = new Conexion();
            PreparedStatement ps;
            ResultSet rs;
            Connection conn = con.getConnection();
 
            // Variables para controlar la posición de los datos en la hoja
            int numFilaDatos = 5;
 
            // Estilo para los datos
            CellStyle datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.THIN);
            datosEstilo.setBorderLeft(BorderStyle.THIN);
            datosEstilo.setBorderRight(BorderStyle.THIN);
            datosEstilo.setBorderBottom(BorderStyle.THIN);
 
            // Consulta a la base de datos para obtener los datos
            ps = conn.prepareStatement("SELECT codigo, nombre, precio, stock FROM productos");
            rs = ps.executeQuery();
 
            // Número de columnas en el resultado de la consulta
            int numCol = rs.getMetaData().getColumnCount();
 
            // Llenado de la hoja con los datos
            while (rs.next()) {
                Row filaDatos = sheet.createRow(numFilaDatos);
 
                for (int a = 0; a < numCol; a++) {
 
                    Cell CeldaDatos = filaDatos.createCell(a);
                    CeldaDatos.setCellStyle(datosEstilo);
                    CeldaDatos.setCellValue(rs.getString(a + 1));
                }
 
 
                numFilaDatos++;
            }
            // Ajuste automático del ancho de las columnas
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            
            // Establecer un zoom del 150%
            sheet.setZoom(150);
            
            // Nombre del archivo y ruta de guardado
            String fileName = "Reporte de productos";
            //String home = System.getProperty("user.home");            
            //File file = new File(home + "/Downloads/" + fileName + ".xlsx");  Ruta absoluta
            // Ruta relativa al directorio de trabajo actual
            File file = new File(fileName + ".xlsx");
            
            // Guardar el libro en un archivo
            FileOutputStream fileOut = new FileOutputStream(file);
            book.write(fileOut);
            fileOut.close();
            
            // Abrir el archivo con la aplicación predeterminada
            Desktop.getDesktop().open(file);
            
            // Mostrar mensaje de reporte generado
            JOptionPane.showMessageDialog(null, "Reporte Generado");
         
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
}