package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.smartwarehousebe.utils.helpers.MultipartFileUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @description
 * @author: vie
 * @date: 16/3/25
 */
@Service
public class PdfGenerationService {

  private final Configuration freemarkerConfig;

  public PdfGenerationService(Configuration freemarkerConfig) {
    this.freemarkerConfig = freemarkerConfig;
  }

  /**
   * Generates a PDF document from an HTML template
   *
   * @param templateName The name of the template file (without extension)
   * @param model        The data model to be used for template processing
   * @return A byte array containing the generated PDF
   */
  public MultipartFile generatePdfFromHtmlTemplate(String templateName, Map<String, Object> model) {
    try {
      // Process the HTML template with the provided data model
      Template template = freemarkerConfig.getTemplate(templateName + ".html");
      String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

      // Convert the processed HTML to PDF
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ConverterProperties converterProperties = new ConverterProperties();

      // Convert HTML to PDF
      HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);

      return MultipartFileUtil.createMultipartFile(
          outputStream,
          templateName + ".pdf",
          "application/pdf"
      );
    } catch (IOException | TemplateException e) {
      throw new RuntimeException("Error generating PDF from HTML template", e);
    }
  }
}