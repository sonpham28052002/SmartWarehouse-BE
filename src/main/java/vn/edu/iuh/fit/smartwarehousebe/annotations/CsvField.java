package vn.edu.iuh.fit.smartwarehousebe.annotations;
import java.lang.annotation.*;

/**
 * @description
 * @author: vie
 * @date: 16/3/25
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvField {
  String value();
}
