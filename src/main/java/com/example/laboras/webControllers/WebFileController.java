package com.example.laboras.webControllers;

import com.example.laboras.control.DbUtils;
import com.example.laboras.ds.Course;
import com.example.laboras.ds.File;
import com.example.laboras.serializers.CourseGSONSerializer;
import com.example.laboras.serializers.CoursesGSONSerializer;
import com.example.laboras.serializers.FileGSONSerializer;
import com.example.laboras.serializers.FilesGSONSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@Controller
public class WebFileController {

    @RequestMapping(value = "file/createFile", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createFile(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String title = data.getProperty("title");
        String fileId = data.getProperty("fileId");
        DbUtils.addFile(title, Integer.parseInt(fileId));
        return "File created";
    }

    @RequestMapping(value = "file/updateFile", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateFile(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String title = data.getProperty("title");
        String id = data.getProperty("id");
        DbUtils.updateTableString("files","title", title, Integer.parseInt(id));
        return "File updated";
    }

    @RequestMapping(value = "file/deleteFile", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteFile(@RequestParam("id") String id) {
        DbUtils.deleteFileFromDb(Integer.parseInt(id));
        return "File deleted";
    }

    @RequestMapping(value = "file/getFiles", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getFilesByFolder(@RequestParam("folderId") String folderId) {
        List<File> files = DbUtils.getFilesByFolderId(Integer.parseInt(folderId));

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(File.class, new FileGSONSerializer());
        Gson parser = gson.create();
        parser.toJson(files.get(0));

        Type libraryList = new TypeToken<List<File>>() {
        }.getType();
        gson.registerTypeAdapter(libraryList, new FilesGSONSerializer());
        parser = gson.create();

        return parser.toJson(files);
    }

}
