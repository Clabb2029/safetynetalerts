package com.safetynet.safetynetalerts.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.model.DataModel;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.Arrays;

@Configuration
public class DataMapper {

    public void read() {
        try {
            readFile();
        } catch (Exception e) {
            System.out.println("Unable to read the JSON file " + Arrays.toString(e.getStackTrace()));
        }
    }

    public void readFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream filePath = new FileInputStream("src/main/resources/data/data.json");
        DataModel data = new DataModel();
        data = mapper.readValue(filePath, DataModel.class);
    }

}
