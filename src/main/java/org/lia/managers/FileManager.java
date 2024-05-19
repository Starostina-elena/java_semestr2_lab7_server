package org.lia.managers;

import org.lia.models.Product;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;


public class FileManager {

    private String filename;
    private final XStream xstream;

    public FileManager(String filename) {
        this.filename = filename;
        xstream = new XStream(new StaxDriver());
    }

    public String getFilename() {
        return filename;
    }

    public boolean checkFileExists() {
        try (InputStreamReader collectionReader = new InputStreamReader(new FileInputStream(filename))) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void writeCollection(ArrayDeque<Product> collection) {
        if (!filename.isEmpty()) {
            try (FileOutputStream collectionFileWriter = new FileOutputStream(filename)) { // TODO: почему именно такая запись? answer: autoclosable file

                String xml = xstream.toXML(new ArrayList<>(collection));
                collectionFileWriter.write(xml.getBytes());
                collectionFileWriter.flush();
                System.out.println("Collection was successfully saved to the file!");
            } catch (FileNotFoundException exception) {
                System.out.println("File doesn't exist");
            } catch (IOException exception) {
                System.out.println("IOE exception");
            }
        } else System.out.println("Error: bad filename");
    }

    public CollectionManager readCollection() {
        CollectionManager collection = new CollectionManager();
        if (!filename.isEmpty()) {
            try (InputStreamReader collectionReader = new InputStreamReader(new FileInputStream(filename))) {
                xstream.setMode(XStream.NO_REFERENCES);
                xstream.addPermission(NoTypePermission.NONE);
                xstream.addPermission(NullPermission.NULL);
                xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
                xstream.allowTypeHierarchy(List.class);
                xstream.allowTypeHierarchy(String.class);
                xstream.ignoreUnknownElements();
                xstream.allowTypes(new Class[] {ArrayList.class, Product.class});
                StringBuilder xml = new StringBuilder();
                int curSymb = collectionReader.read();
                while (curSymb != -1) {
                    xml.append((char)curSymb);
                    curSymb = collectionReader.read();
                }

                // TODO: data validation

                ArrayList<Product> data = (ArrayList<Product>)xstream.fromXML(xml.toString());
                for (Product item : data) {
                    collection.addToCollection(item);
                }
                collection.changeIdIndex();
                System.out.println("Collection was successfully downloaded from the file!");
                collectionReader.close();
                return collection;
            } catch (FileNotFoundException exception) {
                System.out.println("File doesn't exist");
            } catch (IOException exception) {
                System.out.println("IOE exception");
            }
        } else System.out.println("Error: bad filename");
        return collection;
    }

}
