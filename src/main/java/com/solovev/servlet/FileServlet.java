package com.solovev.servlet;

import com.solovev.service.UserIdAndItsFileItems;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

@WebServlet("/files")
public class FileServlet extends HttpServlet {

    private static final File pathToContentDirectory = new File("D:\\Git\\Practice_Projects\\JavaEE\\FileServlet\\files\\filesStored") ;


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();

        if (ServletFileUpload.isMultipartContent(req)) {
            try {
                List<FileItem> fileItems = createServletFileUploadHandler().parseRequest(req);
                if (Objects.nonNull(fileItems)) {
                    UserIdAndItsFileItems userIdAndItsFileItems = new UserIdAndItsFileItems(fileItems);
                    String message  = userIdAndItsFileItems.saveAllUserFiles(pathToContentDirectory);
                    writer.println(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                writer.println(e.getMessage());
                resp.setStatus(400);
            }
        } else {
            writer.print("Not multipart!");
        }
    }

    private ServletFileUpload createServletFileUploadHandler() {
        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        return new ServletFileUpload(factory);
    }

}
