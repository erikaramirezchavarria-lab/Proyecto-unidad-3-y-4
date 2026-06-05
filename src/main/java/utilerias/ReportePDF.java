package utilerias;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportePDF {
    private static final Font TITLE = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static final Font SUBTITLE = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
    private static final Font HEADER = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
    private static final Font BODY = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);

    public static void generar(String ruta, String titulo, String descripcion, List<String> columnas, List<List<String>> filas) throws Exception {
        Document document = new Document(PageSize.LETTER.rotate(), 36, 36, 54, 54);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(ruta));
        writer.setPageEvent(new PageNumberEvent());
        document.open();

        Paragraph sistema = new Paragraph("KING HOTEL", TITLE);
        sistema.setAlignment(Element.ALIGN_CENTER);
        document.add(sistema);

        Paragraph nombreReporte = new Paragraph(titulo, SUBTITLE);
        nombreReporte.setAlignment(Element.ALIGN_CENTER);
        document.add(nombreReporte);

        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        document.add(new Paragraph("Fecha y hora: " + fecha, SUBTITLE));
        document.add(new Paragraph("Usuario: " + (Sesion.usuarioActivo == null ? "sin sesion" : Sesion.usuarioActivo), SUBTITLE));
        if (descripcion != null && !descripcion.isBlank()) {
            document.add(new Paragraph(descripcion, SUBTITLE));
        }
        document.add(new Paragraph(" "));

        if (columnas.isEmpty()) {
            document.add(new Paragraph("Sin columnas para mostrar.", BODY));
        } else {
            PdfPTable table = new PdfPTable(columnas.size());
            table.setWidthPercentage(100);
            for (String columna : columnas) {
                PdfPCell cell = new PdfPCell(new Phrase(columna, HEADER));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            for (List<String> fila : filas) {
                for (int i = 0; i < columnas.size(); i++) {
                    String valor = i < fila.size() && fila.get(i) != null ? fila.get(i) : "";
                    table.addCell(new Phrase(valor, BODY));
                }
            }
            document.add(table);
        }

        document.close();
    }

    private static class PageNumberEvent extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            Phrase footer = new Phrase("Pagina " + writer.getPageNumber(), BODY);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, footer,
                    (document.right() + document.left()) / 2, 24, 0);
        }
    }
}
