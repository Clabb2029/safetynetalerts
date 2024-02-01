package com.safetynet.safetynetalerts.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.safetynet.safetynetalerts.model.DataModel;
import org.springframework.context.annotation.Configuration;

import java.io.*;

@Configuration
public class DataMapper {

    public void read() {
        try {
            readFile();
        } catch (StreamReadException e) {
            System.out.println(" StreamRead Exception ! ");
            e.printStackTrace();
        } catch (DatabindException e) {
            System.out.println(" Databind Exception ! ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(" IO Exception ! ");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unable to read the JSON file ");
            e.getStackTrace();
        }
    }

    public void readFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(new File("src/main/resources/data/data.json"), DataModel.class);
    }

}
