package com.example.laboras.webControllers;

import com.example.laboras.control.DbUtils;
import com.example.laboras.ds.Course;
import com.example.laboras.ds.File;
import com.example.laboras.ds.Folder;
import com.example.laboras.serializers.FileGSONSerializer;
import com.example.laboras.serializers.FilesGSONSerializer;
import com.example.laboras.serializers.FolderGSONSerializer;
import com.example.laboras.serializers.FoldersGSONSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;

@Controller
public class WebFolderController {
    @RequestMapping(value = "folder/createFolder", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createFolder(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String title = data.getProperty("title");
        String courseId = data.getProperty("courseId");
        DbUtils.addFolder(title, Integer.parseInt(courseId));
        return "Folder created";
    }

    @RequestMapping(value = "folder/updateFolder", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateFolder(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String title = data.getProperty("title");
        String id = data.getProperty("id");
        DbUtils.updateTableString("folders","title", title, Integer.parseInt(id));
        return "Folder updated";
    }

    @RequestMapping(value = "folder/deleteFolder", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteFolder(@RequestParam("id") String id) {
        DbUtils.deleteFolderFromDb(Integer.parseInt(id));
        return "Folder deleted";
    }

    @RequestMapping(value = "folder/getFolders", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getFoldersByCourse(@RequestParam("courseId") String courseId) {
        List<Folder> folders = DbUtils.getFoldersByCourseId(Integer.parseInt(courseId));
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Folder.class, new FolderGSONSerializer());
        Gson parser = gson.create();
        parser.toJson(folders.get(0));

        Type libraryList = new TypeToken<List<Folder>>() {
        }.getType();
        gson.registerTypeAdapter(libraryList, new FoldersGSONSerializer());
        parser = gson.create();

        return parser.toJson(folders);
    }
}
