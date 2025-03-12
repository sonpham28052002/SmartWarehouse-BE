package vn.edu.iuh.fit.smartwarehousebe.exceptions;

import org.apache.tomcat.util.http.fileupload.FileUploadException;

public class FileTypeException extends FileUploadException {
    public FileTypeException() {
        super("File type is not supported");
    }
}
